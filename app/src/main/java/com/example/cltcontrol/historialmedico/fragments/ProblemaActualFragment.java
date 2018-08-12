package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
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
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class ProblemaActualFragment extends Fragment {

    //Interface
    private IResult mResultCallback;
    //Variables de view
    private EditText etProblemaActual;
    //Variables de Clases
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    private String descripcionProblemaActual, idEmpleadoServidor;

    //constructor por defecto
    public ProblemaActualFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problema_actual, container, false);

        //referencia de variables de views
        etProblemaActual = view.findViewById(R.id.etProblemaActual);
        Button btnGuardarProblemaActual = view.findViewById(R.id.btnGuardarProblemaActual);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            String precedencia = extras.getString("PRECEDENCIA");
            //Recibe el id de consulta medica desde Historial de consulta medica
            String id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            String id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

            idEmpleadoServidor = String.valueOf(empleado.getId_serv());

            String cargo = extras.getString("CARGO");
            if(Objects.equals(cargo, "Enfermera")){
                btnGuardarProblemaActual.setVisibility(View.GONE);
                etProblemaActual.setEnabled(false);
            }
            if (precedencia != null && precedencia.equals("consultar")) {
                etProblemaActual.setText(consultaMedica.getProb_actual());
                btnGuardarProblemaActual.setText("Editar");
            }
        }
        btnGuardarProblemaActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarConsulta();
            }
        });
        return view;
    }

    /*
     * Verifica si ha ingresado texto y guarda en consulta medica, caso contrario imprime un mensaje
     **/
    private void guardarConsulta() {
        //Valida lo que se ingresa  (2 lineas)
        descripcionProblemaActual = etProblemaActual.getText().toString();
        int res = consultaMedica.validarCampoTexto(descripcionProblemaActual);
        switch (res) {
            case 0:
                Toast.makeText(getContext(), "No ha ingresado nada", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getContext(), "Ha ingresado solo numeros", Toast.LENGTH_SHORT).show();
                break;
            default:
                //Si aun no ha creado la consulta, la crea y añade los datos
                if (consultaMedica.getEmpleado() == null) {
                    //4) Comentar las funciones de abajo y hacer post y enviar new Date()
                    postConsultaMedica(new Date());
                } else {
                    putProblemaActual();
                }
                break;
        }
    }

    /*
     * Guardar motivo localmento
     * */
    private void guardarConsultaLocal(Date fecha, int status, int id_serv){
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setId_serv(id_serv);
        consultaMedica.setFechaConsulta(fecha);
        consultaMedica.setStatus(status);
        consultaMedica.setProb_actual(descripcionProblemaActual); //setea lo que quieres
        consultaMedica.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Envía datos de Consulta médica al servidor
     * */
    private void postConsultaMedica(final Date fechaConsulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("POST");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(idEmpleadoServidor,fechaConsulta, "", descripcionProblemaActual,"","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
    }

    /*
     * Actualiza cambios en Examen Fisico
     * */
    private void putProblemaActual(){
        String idConsultaServidor= String.valueOf(consultaMedica.getId_serv());
        String idEmpleadoServidor = String.valueOf(consultaMedica.getEmpleado().getId_serv());
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("PUT");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(idEmpleadoServidor,
                new Date(),consultaMedica.getMotivo(), descripcionProblemaActual, consultaMedica.getRevision_medica(),
                consultaMedica.getPrescripcion(), consultaMedica.getExamen_fisico());
        requestService.putDataRequest("PUTCALL", URL_CONSULTA_MEDICA+idConsultaServidor+"/", sendObj, token);
    }

    /*
     * Actualiza los datos localmente
     * */
    private void actualizarConsultaLocal(int status){
        consultaMedica.setStatus(status);
        consultaMedica.setProb_actual(descripcionProblemaActual);
        consultaMedica.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han editado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String metodo_request){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    if (metodo_request.equalsIgnoreCase("POST")) {
                        //Si ha realizado post en ConsultaMedica
                        String fechaConsulta = response.getString("fechaConsulta");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarConsultaLocal(fecha, NAME_SYNCED_WITH_SERVER, Integer.parseInt(pk));
                    } else {
                        actualizarConsultaLocal(NAME_SYNCED_WITH_SERVER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                if (metodo_request.equalsIgnoreCase("POST"))
                    guardarConsultaLocal(new Date(), NAME_NOT_SYNCED_WITH_SERVER, 0);
                else
                    actualizarConsultaLocal(NAME_NOT_SYNCED_WITH_SERVER);
            }
            @Override
            public void notifyMsjError(String requestType, String error) {
                if (metodo_request.equalsIgnoreCase("POST"))
                    guardarConsultaLocal(new Date(), NAME_NOT_SYNCED_WITH_SERVER, 0);
                else
                    actualizarConsultaLocal(NAME_NOT_SYNCED_WITH_SERVER);
            }
            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };

    }


}
