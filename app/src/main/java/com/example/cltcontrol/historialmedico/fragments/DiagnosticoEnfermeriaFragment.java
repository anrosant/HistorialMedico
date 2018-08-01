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
import com.example.cltcontrol.historialmedico.models.SignosVitales;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosticoEnfermeriaFragment extends Fragment {

    private String idAtencion=null;
    private String precedencia, idEmpleado,cargo;
    private Bundle bun;
    private Button boton;
    private EditText etDiagnostico;
    private Empleado empleado;
    private AtencionEnfermeria atencion;
    private String id_empleado_servidor; //1) Declarar id_empelado_servidor y mResultCallback
    private IResult mResultCallback;
    private String diagnostico;

    public DiagnosticoEnfermeriaFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_diagnotico_enfermeria, container, false);
        boton = view.findViewById(R.id.btnGuardar);
        etDiagnostico = view.findViewById(R.id.txt_prescripcion);

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (bun != null) {
            idAtencion = bun.getString("ID_ATENCION");
            precedencia = bun.getString("PRECEDENCIA");
            idEmpleado = bun.getString("ID_EMPLEADO");
            cargo = bun.getString("CARGO");
            atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
            empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
            id_empleado_servidor = String.valueOf(empleado.getId_serv());
        }
        //Si va a consultar, muestra los datos
        if(precedencia.equals("consultar")) {
            etDiagnostico.setText(atencion.getDiagnosticoEnfermeria());
            boton.setText("Editar");
        }
        if(cargo.equals("Doctor")){
            boton.setVisibility(View.GONE);
            etDiagnostico.setEnabled(false);
        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {guardarAtencionEnfermeria();
            }
        });
        return view;
    }

    /*
     * Verifica si ha ingresado texto y guarda en consulta medica, caso contrario imprime un mensaje
     **/
    private void guardarAtencionEnfermeria() {
        //Valida lo que se ingresa  (2 lineas)
        diagnostico = etDiagnostico.getText().toString();
        int res = atencion.validarCampoTexto(diagnostico);
        switch (res) {
            case 0:
                Toast.makeText(getContext(), "No ha ingresado nada", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(), "Ha ingresado solo numeros", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Si aun no ha creado la consulta, la crea y añade los datos
                if (atencion.getEmpleado() == null) {
                    //4) Comentar las funciones de abajo y hacer post y enviar new Date()
                    postAtencionEnfermeria(new Date());
                } else {
                    //HACER PUT
                }

                break;
        }
    }

    //3) Copiar los funciones init y post
    /*
     * Guardar motivo localmento
     * */
    private void guardarAtencionLocal(Date fecha, int status, int id_serv){
        atencion.setEmpleado(empleado);
        atencion.setId_serv(id_serv);
        atencion.setFecha_atencion(fecha);
        atencion.setStatus(status);
        atencion.setDiagnosticoEnfermeria(diagnostico); //setea lo que quieres
        atencion.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Envía datos de Consulta médica al servidor
     * */
    private void postAtencionEnfermeria(final Date fechaConsulta){
        initRequestCallback();
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");

        RequestService requestService = new RequestService(mResultCallback, getActivity());
        // 5) PASAR LOS DATOS A LA FUNCIÓN
        Map<String, String> sendObj = AtencionEnfermeria.getHashMapAtencionEnfermeria(id_empleado_servidor,fechaConsulta, "",diagnostico,"");
        requestService.postDataRequest("POSTCALL", URL_ATENCION_ENFERMERIA, sendObj, token);
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                try {
                    //Si ha realizado post en Atencion enfermería
                    //PASO 6) FINAL
                    String fechaConsulta = response.getString("fechaAtencion");
                    Date fecha = convertirFecha(fechaConsulta);
                    String pk = response.getString("pk");
                    guardarAtencionLocal(fecha, NAME_SYNCED_WITH_SERVER,Integer.parseInt(pk));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                guardarAtencionLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                guardarAtencionLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        };

    }

}