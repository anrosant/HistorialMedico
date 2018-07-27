package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private ConsultaMedica consultaMedica;
    private String motivo, id_empleado_servidor; //1) Declarar id_empelado_servidor y la de abajo
    private IResult mResultCallback;
    private Empleado empleado;

    public MotivoAtencionFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivo_atencion, container, false);

        etMotivoAtencion = view.findViewById(R.id.txt_motivo);
        Button btn_guardar = view.findViewById(R.id.btnGuardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica
        assert extras != null;
        String id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        String precedencia = extras.getString("PRECEDENCIA");
        String id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        id_empleado_servidor = String.valueOf(empleado.getId_serv()); //2) Obtenemos Id del servidor del empleado
        String cargo = extras.getString("CARGO");
        assert cargo != null;
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            etMotivoAtencion.setEnabled(false);

        }
        //id_atencion_enfermeria = extras.getString("ID_ATENCION_ENFERMERIA");

        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        if(Objects.equals(precedencia, "consultar")){
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

    //3) Copiar los funciones init y post (Guardar motivo hay que crearla)
    /*
    * Guardar motivo localmento
    * */
    private void guardarMotivoLocal(Date fecha, int status, int id_serv){
        consultaMedica.setId_serv(id_serv);
        consultaMedica.setFechaConsulta(fecha);
        consultaMedica.setStatus(status);
        consultaMedica.setMotivo(motivo);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
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
    private void postConsultaMedica(final Date fechaConsulta){
        initRequestCallback();
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        // 5) PASAR LOS DATOS A LA FUNCIÓN
        JSONObject sendObj = ConsultaMedica.getJSONConsultaMedica(id_empleado_servidor,fechaConsulta, motivo,"","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj);
    }

    //PASO 7 PROBAR

}