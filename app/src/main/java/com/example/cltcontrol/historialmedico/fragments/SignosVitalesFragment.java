package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SIGNOS;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignosVitalesFragment extends Fragment {
    private EditText etPSistolica, etPDistolica, etTemperatura, etPulso;
    private String id_consulta_medica, id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private ListView lvSignosVitales;
    private AdapterSignosVitales adapterSignosVitales;
    private Empleado empleado;
    private List<SignosVitales> signosVitalesList;
    private Button btn_guardar;
    private ImageButton ib_mostrar_ocultar_contendido;
    LinearLayout ly_signos_vitales;
    private TextView tvTitulo;

    private SignosVitales signos;

    //POST
    IResult mResultCallback = null;
    RequestService requestService;
    int idEmpleadoServidor;
    String TAGSIGNOS = "tagsignos", TAGCONSULTA="tagconsulta";

    String presionSistolicaText;
    String presionDistolicaText;
    String temperaturatext;
    String pulsoText;
    Date fechaConsulta;

    public SignosVitalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Registar el receiver para sincronizar datos
        //Objects.requireNonNull(getContext()).registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        etPSistolica = view.findViewById(R.id.etPSistolica);
        etPDistolica = view.findViewById(R.id.etPDistolica);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        etPulso = view.findViewById(R.id.etPulso);
        lvSignosVitales = view.findViewById(R.id.lvSignosVitales);
        btn_guardar = view.findViewById(R.id.btnGuardar);
        ib_mostrar_ocultar_contendido =  view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        ly_signos_vitales = view.findViewById(R.id.ly_signos_vitales);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        //
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica

        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        //Recibe el id del empleado
        id_empleado = extras.getString("ID_EMPLEADO");
        cargo = extras.getString("CARGO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        idEmpleadoServidor = empleado.getId_serv();

        //Ingresa a nueva consulta medica
        if(id_consulta_medica!=null) {
            if(cargo.equals("Enfermera")){
                btn_guardar.setVisibility(View.GONE);
                ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
                ly_signos_vitales.setVisibility(View.GONE);
                tvTitulo.setVisibility(View.GONE);
            }
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(id_consulta_medica));

        }

        //Crea un adapter de dicha lista y la muestra en un listview
        adapterSignosVitales = new AdapterSignosVitales(getContext(), (ArrayList<SignosVitales>) signosVitalesList);
        lvSignosVitales.setAdapter(adapterSignosVitales);

        //BOTON GUARDAR SIGNOS VITALES
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibe los datos de signos vitales
                presionSistolicaText = etPSistolica.getText().toString();
                presionDistolicaText = etPDistolica.getText().toString();
                temperaturatext = etTemperatura.getText().toString();
                pulsoText = etPulso.getText().toString();

                signos = new SignosVitales();
                int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                if(res == 0) {
                    Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                } else if(res == 1) {
                    Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                }else{
                    //Si es la primera vez que crea la consulta medica
                    if (consultaMedica.getEmpleado() == null) {
                        fechaConsulta = new Date();
                        postConsultaMedica(fechaConsulta);
                    } else{
                        postSignosVitales(String.valueOf(consultaMedica.getId_serv()));

                    }

                }
                cargarSignosVitales(consultaMedica.getId());
            }
        });

        ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
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
        });

        return view;
    }
    /*
    * Función que carga los signos vitales en la lista
    * */
    public void cargarSignosVitales(Long id){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "consultamedica = ?", String.valueOf(id));
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
        signos.setConsultaMedica(consultaMedica);
        signos.save();

        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        cargarSignosVitales(signos.getConsultaMedica().getId());
    }

    /*
    * Función que guarda una consulta médica localmente
    * */
    public void guardarConsultaMedicaLocal(Date fechaConsulta, int id_servidor, int status){
        consultaMedica.setId_serv(id_servidor);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(fechaConsulta);
        consultaMedica.setStatus(status);
        consultaMedica.save();
    }
    /*
     * Limpia los campos luego de haber ingresado los signos vitales
     * */
    private void limpiarCampos(){
        etPSistolica.setText("");
        etPDistolica.setText("");
        etTemperatura.setText("");
        etPulso.setText("");
        etPSistolica.requestFocus();
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    try {
                        //Si ha realizado post en ConsultaMedica
                        String fechaConsulta = response.getString("fecha");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarConsultaMedicaLocal(fecha,Integer.parseInt(pk), NAME_SYNCED_WITH_SERVER);
                        postSignosVitales(pk);
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
                    guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
                Log.e("ERROR", String.valueOf(error));
                Toast.makeText(getContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
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
        initRequestCallback(TAGCONSULTA);
        requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'empleado': "+String.valueOf(idEmpleadoServidor)+", " +
                    "'fecha': "+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fechaConsulta))+", " +
                    "'motivo': '',"+
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

    /*
     * Envía datos de Signos vitales al servidor
     * */
    public void postSignosVitales(String id_consulta_medica){
        initRequestCallback(TAGSIGNOS);
        requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'consulta_medica': "+id_consulta_medica+", " +
                    "'atencion_enfermeria': '',"+
                    "'empleado': "+String.valueOf(idEmpleadoServidor)+", " +
                    "'presion_sistolica': "+presionSistolicaText+", " +
                    "'presion_distolica': "+presionDistolicaText+", " +
                    "'pulso': "+pulsoText+", " +
                    "'temperatura': " +temperaturatext+
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj);
    }

}