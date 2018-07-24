package com.example.cltcontrol.historialmedico.fragments;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class MotivoAtencionFragment extends Fragment {
    private EditText etMotivoAtencion;
    private Button btn_guardar;
    private String id_consulta_medica, precedencia, id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;
    private String motivo, id_empleado_servidor; //1) Declarar id_empelado_servidor y las 2 de abajo
    private RequestService requestService;
    private IResult mResultCallback;
    //private AtencionEnfermeria atencionEnfermeria;

    public MotivoAtencionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivo_atencion, container, false);

        etMotivoAtencion = view.findViewById(R.id.txt_motivo);
        btn_guardar = view.findViewById(R.id.btnGuardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        precedencia = extras.getString("PRECEDENCIA");
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        id_empleado_servidor = String.valueOf(empleado.getId_serv()); //2) Obtenemos Id del servidor del empleado
        cargo = extras.getString("CARGO");
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            etMotivoAtencion.setEnabled(false);

        }
        //id_atencion_enfermeria = extras.getString("ID_ATENCION_ENFERMERIA");

        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        if(precedencia.equals("consultar")){
            etMotivoAtencion.setText(consultaMedica.getMotivo());
            btn_guardar.setText("Editar");
        }
        //consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarMotivo();
            }
        });
        return view;
    }

    /*
    * Verifica si ha ingresado texto y guarda en consulta medica, caso contrario imprime un mensaje
    **/
    private void guardarMotivo() {
        motivo = etMotivoAtencion.getText().toString();
        int res = consultaMedica.validarCampoTexto(motivo);
        if(res == 0)
            Toast.makeText(getContext(), "No ha ingresado nada", Toast.LENGTH_SHORT).show();
        else if(res == 1)
            Toast.makeText(getContext(),"Ha ingresado solo numeros",Toast.LENGTH_SHORT).show();
        else{
            //Si aun no ha creado la consulta, la crea y añade los datos
            if (consultaMedica.getEmpleado() == null) {
                //4) Comentar las funciones de abajo y hacer post y enviar new Date()
                postConsultaMedica(new Date());
            }else{
                //HACER PUT
            }
            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();

        }
    }

    //3) Copiar los funciones init y post (Guardar motivo hay que crearla)
    /*
    * Guardar motivo localmento
    * */
    void guardarMotivoLocal(Date fecha, int status, int id_serv){
        consultaMedica.setId_serv(id_serv);
        consultaMedica.setFechaConsulta(fecha);
        consultaMedica.setStatus(status);
        consultaMedica.setMotivo(motivo);
        consultaMedica.save();

    }
    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    void initRequestCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                try {
                    //Log.d("HERECONSULTA", String.valueOf(response));
                    //Si ha realizado post en ConsultaMedica
                    //PASO 6) FINAL
                    String fechaConsulta = response.getString("fecha");
                    Date fecha = convertirFecha(fechaConsulta);
                    String pk = response.getString("pk");
                    guardarMotivoLocal(fecha,Integer.parseInt(pk), NAME_SYNCED_WITH_SERVER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                guardarMotivoLocal(new Date(), 0,NAME_NOT_SYNCED_WITH_SERVER);

                Log.e("ERROR", String.valueOf(error));
                Toast.makeText(getContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                guardarMotivoLocal(new Date(), 0,NAME_NOT_SYNCED_WITH_SERVER);

                Log.e("ERROR", String.valueOf(error));
                Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };

    }

    /*
     * Envía datos de Consulta médica al servidor
     * */
    public void postConsultaMedica(final Date fechaConsulta){
        initRequestCallback();
        requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        // 5) CAMBIAR LOS DATOS DEL JSON
        try {
            sendObj = new JSONObject("{" +
                    "'empleado': "+String.valueOf(id_empleado_servidor)+", " +
                    "'fecha': "+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fechaConsulta))+", " +
                    "'motivo': "+motivo+", "+
                    "'problema_actual': '',"+
                    "'revision': '',"+
                    "'prescripcion': '',"+
                    "'examen_fisico': ''"+
                    "}");
            Log.d("ENDOBJ", String.valueOf(sendObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj);
    }

    //PASO 7 PROBAR

}