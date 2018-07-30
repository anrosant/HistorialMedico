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

    private String id_atencion;
    private String id_empleado;
    private EditText etPresionSistolica;
    private EditText etPresionDistolica;
    private EditText etPulso;
    private EditText etTemperatura;
    private AdapterSignosVitales adapterSignosVitales;
    private ImageButton ib_mostrar_ocultar_contendido;
    private LinearLayout ly_signos_vitales;
    private AtencionEnfermeria atencionEnfermeria;
    private SignosVitales signos;
    private Empleado empleado;
    private String presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText, cargo;
    private Date fecha_atencion;

    //POST
    private IResult mResultCallback = null;
    private RequestService requestService;
    private int id_empleado_Servidor;
    private String TAGSIGNOS = "tagsignos", TAGATENCION ="tagatencion";

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
        ly_signos_vitales = view.findViewById(R.id.ly_signos_vitales);
        ListView lvSignosVitales = view.findViewById(R.id.lvSignosVitales);
        TextView tvTitulo = view.findViewById(R.id.tv_titulo);
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        if (extras != null) {
            id_atencion = extras.getString("ID_ATENCION");
            id_empleado = extras.getString("ID_EMPLEADO");
            cargo = extras.getString("CARGO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            id_empleado_Servidor = empleado.getId_serv();
        }



        //Ingresa a nueva atención enfermería
        List<SignosVitales> signosVitalesList;
        if(id_atencion!=null) {
            if(cargo.equals("Doctor")){
                btn_guardar.setVisibility(View.GONE);
                //ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
                ly_signos_vitales.setVisibility(View.GONE);
                tvTitulo.setVisibility(View.GONE);
            }

            atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.valueOf(id_atencion));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(id_atencion));
        }else{ //Esta es la parte de signos vitales rapidos
            atencionEnfermeria = null;
            signosVitalesList = SignosVitales.find(SignosVitales.class, "empleado = ?", String.valueOf(id_empleado));
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
                if(res == 0) {
                    Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                } else if(res == 1) {
                    Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                }else{
                    //Si ingresa externamente sin pasar por atención enfermería
                    if(id_atencion==null){
                        postSignosVitales("");
                        cargarSignosVitalesSin();
                    }
                    //Si ingresa desde atención enfermería y es la primera vez que la crea
                    else if (id_atencion!=null && atencionEnfermeria.getEmpleado() == null) {
                        fecha_atencion = new Date();

                        //Guarda el id del empleado en la atención y la fecha de atención
                        postAtencionEnfermeria(fecha_atencion);
                    }else{
                        Log.d("ELSE", "else");
                        postSignosVitales(String.valueOf(atencionEnfermeria.getId_serv()));
                    }

                }
                //cargarSignosVitales(atencionEnfermeria.getId());
                //Broadcast receiver to know the sync status
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        //Confirmar que se ha guardado
                        Toast.makeText(getContext(),"Datos enviados al servidor ",Toast.LENGTH_SHORT).show();
                        //cargarSignosVitales(atencionEnfermeria.getId());
                    }
                };

                //Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
            }
        });

        /*ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ly_signos_vitales.isShown()){
                    ly_signos_vitales.setVisibility(view.VISIBLE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    ly_signos_vitales.setVisibility(view.GONE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });*/

        return view;
    }
    /*
     * Función que carga los signos vitales en la lista
     * */
    public void cargarSignosVitales(Long id){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "atencionenfermeria = ?", String.valueOf(id));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }
    /*
    * Función que carga los signos vitales sin haber ingresado a atencion enfermería
    * */
    public void cargarSignosVitalesSin(){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "empleado = ?", String.valueOf(id_empleado));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }

    /*
     * Función que guarda los signos vitales localmente
     * */
    public void guardarSignosVitalesLocal(int id_serv, int status){
        signos.setId_serv(id_serv);
        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));
        signos.setStatus(status);
        signos.setAtencion_enfermeria(atencionEnfermeria);
        signos.setEmpleado(empleado);
        signos.save();

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
        limpiarCampos();
        long id_atencion= atencionEnfermeria.getId();
        if(id_atencion!=0){
            cargarSignosVitales(id_atencion);
        }else{
            cargarSignosVitalesSin();
        }

    }
    
    /*
    * Función que guarda una atencion enfermeria localmente
    * */

    public void guardarAtencionEnfermeriaLocal(Date fecha_Atencion, int id_servidor, int status){
        atencionEnfermeria.setId_serv(id_servidor);
        atencionEnfermeria.setEmpleado(empleado);
        atencionEnfermeria.setFecha_atencion(fecha_Atencion);
        atencionEnfermeria.setStatus(status);
        atencionEnfermeria.save();

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
    void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                if(TAG.equalsIgnoreCase("tagatencion")){
                    try {
                        //Si ha realizado post en ConsultaMedica
                        String fechaConsulta = response.getString("fecha");
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
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarAtencionEnfermeriaLocal(fecha_atencion, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarAtencionEnfermeriaLocal(fecha_atencion, 0,NAME_NOT_SYNCED_WITH_SERVER);
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
    public void postAtencionEnfermeria(final Date fecha_atencion){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback(TAGATENCION);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = AtencionEnfermeria.getHashMapAtencionEnfermeria(String.valueOf(id_empleado_Servidor), fecha_atencion,"","","");
        requestService.postDataRequest("POSTCALL", URL_ATENCION_ENFERMERIA, sendObj, token);
    }

    /*
     * Envía datos de Signos vitales al servidor
     * */
    public void postSignosVitales(String id_atencion_enfermeria){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback(TAGSIGNOS);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = SignosVitales.getHashMapSignosVitales(String.valueOf(id_empleado_Servidor),"",id_atencion_enfermeria,presionSistolicaText,presionDistolicaText,pulsoText,temperaturatext);
        requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj, token);
    }


}
