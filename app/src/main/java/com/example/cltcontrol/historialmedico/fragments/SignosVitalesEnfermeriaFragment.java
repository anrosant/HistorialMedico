package com.example.cltcontrol.historialmedico.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_ATENCION_ENFERMERIA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SIGNOS;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class SignosVitalesEnfermeriaFragment extends Fragment {

    private String idEmpleado;
    private EditText etPresionSistolica;
    private EditText etPresionDistolica;
    private EditText etPulso;
    private EditText etTemperatura;

    private AdapterSignosVitales adapterSignosVitales;
    private AtencionEnfermeria atencionEnfermeria;
    private SignosVitales signos;
    private Empleado empleado;
    private String idAtencion, presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText, cargo;
    private Date fechaAtencion, fechaSigno;
    private int idEmpleadoServidor;
    //POST
    private IResult mResultCallback = null;
    private RequestService requestService;


    private BroadcastReceiver broadcastReceiver;


    public SignosVitalesEnfermeriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Objects.requireNonNull(getContext()).registerReceiver(new SincronizacionInmediata(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        etPresionSistolica = view.findViewById(R.id.etPSistolica);
        etPresionDistolica = view.findViewById(R.id.etPDistolica);
        etPulso = view.findViewById(R.id.etPulso);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        Button btn_guardar = view.findViewById(R.id.btnGuardar);
        //ib_mostrar_ocultar_contendido =  view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        LinearLayout ly_signos_vitales = view.findViewById(R.id.ly_signos_vitales);
        ListView lvSignosVitales = view.findViewById(R.id.lvSignosVitales);
        TextView tvTitulo = view.findViewById(R.id.tv_titulo);
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        if (extras != null) {
            idAtencion = extras.getString("ID_ATENCION");
            idEmpleado = extras.getString("ID_EMPLEADO");
            cargo = extras.getString("CARGO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(idEmpleado));
            idEmpleadoServidor = empleado.getId_serv();
        }



        //Ingresa a nueva atención enfermería
        List<SignosVitales> signosVitalesList;
        if(idAtencion !=null) {
            if(cargo.equals("Doctor")){
                btn_guardar.setVisibility(View.GONE);
                //ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
                ly_signos_vitales.setVisibility(View.GONE);
                tvTitulo.setVisibility(View.GONE);
            }

            atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.valueOf(idAtencion));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(idAtencion));
        }else{ //Esta es la parte de signos vitales rapidos
            atencionEnfermeria = null;
            signosVitalesList = SignosVitales.find(SignosVitales.class, "empleado = ?", String.valueOf(idEmpleado));
        }

        adapterSignosVitales = new AdapterSignosVitales(getContext(), signosVitalesList);
        lvSignosVitales.setAdapter(adapterSignosVitales);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibe los datos de signos vitales
                presionSistolicaText = etPresionSistolica.getText().toString();
                presionDistolicaText = etPresionDistolica.getText().toString();
                temperaturatext = etTemperatura.getText().toString();
                pulsoText = etPulso.getText().toString();

                signos = new SignosVitales();
                int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, pulsoText , temperaturatext);
                switch (res) {
                    case 0:
                        Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                        SignosVitales.delete(signos);
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                        SignosVitales.delete(signos);
                        break;
                    default:
                        //Si ingresa externamente sin pasar por atención enfermería
                        if (idAtencion == null) {
                            fechaSigno = new Date();
                            postSignosVitales("");
                            cargarSignosVitalesSin();
                        }
                        //Si ingresa desde atención enfermería y es la primera vez que la crea
                        else if (idAtencion != null && atencionEnfermeria.getEmpleado() == null) {
                            fechaAtencion = new Date();
                            Log.d("FECHA", String.valueOf(fechaAtencion));

                            //Guarda el id del empleado en la atención y la fecha de atención
                            postAtencionEnfermeria(fechaAtencion);
                        } else {
                            Log.d("ELSE", "else");
                            fechaSigno = new Date();
                            postSignosVitales(String.valueOf(atencionEnfermeria.getId_serv()));
                        }

                        break;
                }
                //Broadcast receiver to know the sync status
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //Confirmar que se ha guardado
                        if(idAtencion ==null)
                            cargarSignosVitalesSin();
                        else
                            cargarSignosVitales(atencionEnfermeria.getId());
                    }
                };

            }
        });


        return view;
    }
    /*
     * Función que carga los signos vitales en la lista
     * */
    private void cargarSignosVitales(Long id){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "atencionenfermeria = ?", String.valueOf(id));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }
    /*
    * Función que carga los signos vitales sin haber ingresado a atencion enfermería
    * */
    private void cargarSignosVitalesSin(){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "empleado = ?", String.valueOf(idEmpleado));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }

    /*
     * Función que guarda los signos vitales localmente
     * */
    private void guardarSignosVitalesLocal(int id_serv, int status){
        signos.setId_serv(id_serv);
        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));
        signos.setStatus(status);
        signos.setAtencion_enfermeria(atencionEnfermeria);
        signos.setEmpleado(empleado);
        signos.setFecha(new Date());
        signos.save();

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
        limpiarCampos();
        if(atencionEnfermeria!=null){
            cargarSignosVitales(atencionEnfermeria.getId());
        }else{
            cargarSignosVitalesSin();
        }

    }
    
    /*
    * Función que guarda una atencion enfermeria localmente
    * */

    private void guardarAtencionEnfermeriaLocal(Date fecha_Atencion, int id_servidor, int status){
        atencionEnfermeria.setId_serv(id_servidor);
        atencionEnfermeria.setEmpleado(empleado);
        atencionEnfermeria.setFecha_atencion(fecha_Atencion);
        atencionEnfermeria.setStatus(status);
        atencionEnfermeria.save();

        fechaSigno = new Date();
        postSignosVitales(String.valueOf(id_servidor));
    }
    
    /*
    * Limpia los campos luego de haber ingresado los signos vitales
    * */
    private void limpiarCampos(){
        etPresionSistolica.setText("");
        etPresionDistolica.setText("");
        etTemperatura.setText("");
        etPulso.setText("");
        etPresionSistolica.requestFocus();
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                if(TAG.equalsIgnoreCase("tagatencion")){
                    try {
                        //Si ha realizado post en Atención enfermería
                        String fechaConsulta = response.getString("fechaAtencion");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarAtencionEnfermeriaLocal(fecha,Integer.parseInt(pk), NAME_SYNCED_WITH_SERVER);
                        //postSignosVitales(pk);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        //Si ha realizado post en SignosVitales
                        String pk = response.getString("pk");
                        guardarSignosVitalesLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagatencion")){
                    guardarAtencionEnfermeriaLocal(fechaAtencion, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagatencion")){
                    guardarAtencionEnfermeriaLocal(fechaAtencion, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };

    }

    /*
     * Envía datos de Consulta médica al servidor
     * */
    private void postAtencionEnfermeria(final Date fecha_atencion){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGATENCION = "tagatencion";
        initRequestCallback(TAGATENCION);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = AtencionEnfermeria.getHashMapAtencionEnfermeria(String.valueOf(idEmpleadoServidor), fecha_atencion,"","","");
        requestService.postDataRequest("POSTCALL", URL_ATENCION_ENFERMERIA, sendObj, token);
    }

    /*
     * Envía datos de Signos vitales al servidor
     * */
    private void postSignosVitales(String id_atencion_enfermeria){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGSIGNOS = "tagsignos";
        initRequestCallback(TAGSIGNOS);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = SignosVitales.getHashMapSignosVitales(String.valueOf(idEmpleadoServidor),"",id_atencion_enfermeria,presionSistolicaText,presionDistolicaText,pulsoText,temperaturatext, fechaSigno);
        requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj, token);
    }


}
