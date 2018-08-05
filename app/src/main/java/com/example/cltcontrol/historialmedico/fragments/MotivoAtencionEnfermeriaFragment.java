package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_ATENCION_ENFERMERIA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class MotivoAtencionEnfermeriaFragment extends Fragment {

    //Interface
    private IResult mResultCallback;
    //Variables de view
    private EditText etMotivoAtencion;
    private Button btnGuardarMotivoAtencion;
    //Variables de Clases
    private AtencionEnfermeria atencionEnfermeria;
    private Empleado empleado;

    private String descripcionMotivoAtencion, idEmpleadoServidor;

    //constructor por defecto
    public MotivoAtencionEnfermeriaFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_motivo_atencion, container, false);

        //referencia de variables de views
        etMotivoAtencion = view.findViewById(R.id.et_motivo_atencion);
        btnGuardarMotivoAtencion = view.findViewById(R.id.btn_guardar_motivo_atencion);

        Bundle bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        if (bun != null) {
            String precedencia = bun.getString("PRECEDENCIA");
            String id_atencion_enfermeria = bun.getString("ID_ATENCION");
            atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(id_atencion_enfermeria));
            String id_empleado = bun.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.parseLong(id_empleado));

            idEmpleadoServidor = String.valueOf(empleado.getId_serv());

            String cargo = bun.getString("CARGO");
            if (cargo != null && cargo.equals("Doctor")) {
                btnGuardarMotivoAtencion.setVisibility(View.GONE);
                etMotivoAtencion.setEnabled(false);
            }
            if (precedencia != null && precedencia.equals("consultar")) {
                ////funcionalidad para cargar un signo vital con el id de Atencion
                etMotivoAtencion.setText(atencionEnfermeria.getMotivoAtencion());
                btnGuardarMotivoAtencion.setText("Editar");
            }
        }

        /*
         * Guarda los datos de un motivo atención
         * */
        btnGuardarMotivoAtencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAtencionEnfermeria();
            }
        });
        return view;
    }

    /*
     * Verifica si ha ingresado texto y guarda en consulta medica, caso contrario imprime un mensaje
     **/
    private void guardarAtencionEnfermeria() {
        //Valida lo que se ingresa  (2 lineas)
        descripcionMotivoAtencion = etMotivoAtencion.getText().toString();
        int res = atencionEnfermeria.validarCampoTexto(descripcionMotivoAtencion);
        switch (res) {
            case 0:
                Toast.makeText(getContext(), "No ha ingresado nada", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(), "Ha ingresado solo numeros", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Si aun no ha creado la consulta, la crea y añade los datos
                if (atencionEnfermeria.getEmpleado() == null) {
                    //4) Comentar las funciones de abajo y hacer post y enviar new Date()
                    postAtencionEnfermeria(new Date());
                } else {
                    putMotivoAtencion();
                }
                break;
        }
    }

    /*
     * Guardar motivo localmento
     * */
    private void guardarAtencionLocal(Date fecha, int status, int id_serv){
        atencionEnfermeria.setEmpleado(empleado);
        atencionEnfermeria.setId_serv(id_serv);
        atencionEnfermeria.setFecha_atencion(fecha);
        atencionEnfermeria.setStatus(status);
        atencionEnfermeria.setMotivoAtencion(descripcionMotivoAtencion);
        atencionEnfermeria.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Actualiza los datos localmente
     * */
    private void actualizarAtencionLocal(int status){
        atencionEnfermeria.setStatus(status);
        atencionEnfermeria.setMotivoAtencion(descripcionMotivoAtencion);
        atencionEnfermeria.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han editado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Envía datos de Atencion enfermeria al servidor
     * */
    private void postAtencionEnfermeria(final Date fechaConsulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("POST");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = AtencionEnfermeria.getHashMapAtencionEnfermeria(idEmpleadoServidor,fechaConsulta, descripcionMotivoAtencion,"","");
        requestService.postDataRequest("POSTCALL", URL_ATENCION_ENFERMERIA, sendObj, token);
    }

    /*
     * Actualiza cambios en Motivo de Atencion Enfermeria
     * */
    private void putMotivoAtencion(){
        String idConsultaServidor= String.valueOf(atencionEnfermeria.getId_serv());
        String idEmpleadoServidor = String.valueOf(atencionEnfermeria.getEmpleado().getId_serv());
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("PUT");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = atencionEnfermeria.getHashMapAtencionEnfermeria(idEmpleadoServidor,
                new Date(), descripcionMotivoAtencion,atencionEnfermeria.getDiagnosticoEnfermeria(), atencionEnfermeria.getPlanCuidados());
        requestService.putDataRequest("PUTCALL", URL_ATENCION_ENFERMERIA+idConsultaServidor+"/", sendObj, token);
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String metodo_request){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                try {
                    if(metodo_request.equalsIgnoreCase("POST")){
                        //Si ha realizado post en Atencion enfermería
                        String fechaConsulta = response.getString("fechaAtencion");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarAtencionLocal(fecha, NAME_SYNCED_WITH_SERVER,Integer.parseInt(pk));
                    }else {
                        actualizarAtencionLocal(NAME_SYNCED_WITH_SERVER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(metodo_request.equalsIgnoreCase("POST"))
                    guardarAtencionLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
                else
                    actualizarAtencionLocal(NAME_NOT_SYNCED_WITH_SERVER);
            }
            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.e("STRING ERROR", String.valueOf(error));
                if(metodo_request.equalsIgnoreCase("POST"))
                    guardarAtencionLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
                else
                    actualizarAtencionLocal(NAME_NOT_SYNCED_WITH_SERVER);
            }
            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };
    }

}
