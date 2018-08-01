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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemaActualFragment extends Fragment {
    private EditText et_problema_actual;
    private Button btn_guardar;
    private String id_consulta_medica, precedencia, id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;
    private String id_empleado_servidor; //1) Declarar id_empelado_servidor y mResultCallback
    private IResult mResultCallback;
    private String problema_actual;

    public ProblemaActualFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problema_actual, container, false);

        et_problema_actual = view.findViewById(R.id.et_problema_actual);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        if (extras != null) {
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            precedencia = extras.getString("PRECEDENCIA");
            id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            id_empleado_servidor = String.valueOf(empleado.getId_serv());
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            cargo = extras.getString("CARGO");
        }

        if(Objects.equals(cargo, "Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            et_problema_actual.setEnabled(false);

        }
        if(precedencia.equals("consultar")){
            et_problema_actual.setText(consultaMedica.getProb_actual());
            btn_guardar.setText("Editar");
        }
        btn_guardar.setOnClickListener(new View.OnClickListener() {
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
        problema_actual = et_problema_actual.getText().toString();
        int res = consultaMedica.validarCampoTexto(problema_actual);
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
                    //HACER PUT
                }

                break;
        }
    }

    //3) Copiar los funciones init y post
    /*
     * Guardar motivo localmento
     * */
    private void guardarConsultaLocal(Date fecha, int status, int id_serv){
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setId_serv(id_serv);
        consultaMedica.setFechaConsulta(fecha);
        consultaMedica.setStatus(status);
        consultaMedica.setProb_actual(problema_actual); //setea lo que quieres
        consultaMedica.save();
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
    private void postConsultaMedica(final Date fechaConsulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback();
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        // 5) PASAR LOS DATOS A LA FUNCIÓN
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(id_empleado_servidor,fechaConsulta, "",problema_actual,"","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
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
                    //Log.d("HERECONSULTA", String.valueOf(response));
                    //Si ha realizado post en ConsultaMedica
                    //PASO 6) FINAL
                    String fechaConsulta = response.getString("fechaConsulta");
                    Date fecha = convertirFecha(fechaConsulta);
                    String pk = response.getString("pk");
                    guardarConsultaLocal(fecha, NAME_SYNCED_WITH_SERVER,Integer.parseInt(pk));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                guardarConsultaLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                guardarConsultaLocal(new Date(),NAME_NOT_SYNCED_WITH_SERVER, 0);
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };

    }



}
