package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.adapter.AdapterPatologiasPersonales;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PATOLOGIAS_PERSONALES;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class PatologiasPersonalesFragment extends Fragment {

    private ImageButton ibMostrarOcultarContendido;
    private LinearLayout lyPatologiasPersonales;
    private EditText etDetalleEnfermedad;
    private String idConsultaMedica;
    private String lugar;
    private String detalle;
    private String cargo;
    private ConsultaMedica consultaMedica;
    private AdapterPatologiasPersonales adapterPatologiaPers;
    private List<String> lugarList;
    private Empleado empleado;

    private int idEmpleadoServidor;
    private IResult mResultCallback;
    private RequestService requestService;
    private Date fecha_consulta;

    public PatologiasPersonalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patologias_personales, container, false);

        lyPatologiasPersonales = view.findViewById(R.id.ly_patologias_personales);
        ibMostrarOcultarContendido = view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        etDetalleEnfermedad = view.findViewById(R.id.etDetalleEnfermedad);
        ListView lvPatologiasPersonales = view.findViewById(R.id.lvPatologiasPersonales);
        Button btn_guardar = view.findViewById(R.id.btnGuardar);
        TextView tvTitulo = view.findViewById(R.id.tv_titulo);

        Spinner spPatologias = view.findViewById(R.id.spPatologia);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()).getBaseContext(), R.array.patologias, android.R.layout.simple_spinner_dropdown_item);
        spPatologias.setAdapter(adapter);

        //Obtenemos las patologias en un array list
        lugarList = Arrays.asList(getResources().getStringArray(R.array.patologias));

        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica y de empleado desde Historial de consulta medica
        if (extras != null) {
            idConsultaMedica = extras.getString("ID_CONSULTA_MEDICA");
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(idConsultaMedica));

            String id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            idEmpleadoServidor = empleado.getId_serv();

            cargo = extras.getString("CARGO");
        }

        if(Objects.requireNonNull(cargo).equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            ibMostrarOcultarContendido.setVisibility(View.GONE);
            lyPatologiasPersonales.setVisibility(View.GONE);
            tvTitulo.setVisibility(View.GONE);

        }

        spPatologias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lugar = lugarList.get(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //Obtenemos la lista de patologias personales que existan
        List<PatologiasPersonales> patologiasPersonalesList = PatologiasPersonales.find(PatologiasPersonales.class, "consultamedica = ?", String.valueOf(idConsultaMedica));

        //Crea un adapterItemAtencionEnfermeria de dicha lista y la muestra en un listview
        adapterPatologiaPers = new AdapterPatologiasPersonales(getContext(), patologiasPersonalesList);
        lvPatologiasPersonales.setAdapter(adapterPatologiaPers);

        ibMostrarOcultarContendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!lyPatologiasPersonales.isShown()){
                    lyPatologiasPersonales.setVisibility(View.VISIBLE);
                    ibMostrarOcultarContendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    lyPatologiasPersonales.setVisibility(View.GONE);
                    ibMostrarOcultarContendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detalle = etDetalleEnfermedad.getText().toString();
                if(lugar==null || detalle.equals("")){
                    //guardar lo que hay en detalle
                    Toast.makeText(getContext(),"No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Si es la primera entrada
                    if(consultaMedica.getEmpleado()==null){
                        fecha_consulta = new Date();
                        //Dentro de guardar Consulta Medica, almacena las patologías personales
                        postConsultaMedica(fecha_consulta);
                    }else{
                        postPatologiasPersonales(String.valueOf(consultaMedica.getId_serv()));
                    }

                    actualizarPatologiasPersonales();
                }
            }
        });

        return view;
    }
    /*
    * Función que actualiza la lista de las patologías personales
    * */
    private void actualizarPatologiasPersonales(){
        ArrayList<PatologiasPersonales> patPersoList = (ArrayList<PatologiasPersonales>) PatologiasPersonales.find(PatologiasPersonales.class,
                "consultamedica = ?", String.valueOf(consultaMedica.getId()));
        adapterPatologiaPers.actualizarPatologiasPersonalesList(patPersoList);
    }

    /*
     * Función que guarda una consulta médica localmente
     * */
    private void guardarConsultaMedicaLocal(Date fechaConsulta, int id_servidor, int status){
        consultaMedica.setId_serv(id_servidor);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(fechaConsulta);
        consultaMedica.setStatus(status);
        consultaMedica.save();

        postPatologiasPersonales(String.valueOf(id_servidor));
    }

    /*
     * Guarda los datos de una patologia personal localmente
     * */
    private void guardarPatologiasPersonalesLocal(int id_serv, int status) {
        PatologiasPersonales patologiasPersonales = new PatologiasPersonales(consultaMedica, lugar, detalle);
        patologiasPersonales.setId_serv(id_serv);
        patologiasPersonales.setStatus(status);
        patologiasPersonales.setId_ficha(empleado.getFicha_actual());
        patologiasPersonales.save();

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }

        actualizarPatologiasPersonales();
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    try {
                        //Si ha realizado post en ConsultaMedica
                        String fechaConsulta = response.getString("fechaConsulta");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarConsultaMedicaLocal(fecha,Integer.parseInt(pk), NAME_SYNCED_WITH_SERVER);
                        //postPatologiasPersonales(pk);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        //Si ha realizado post en SignosVitales
                        String pk = response.getString("pk");
                        guardarPatologiasPersonalesLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fecha_consulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarPatologiasPersonalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
                Log.e("ERROR", String.valueOf(error));
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fecha_consulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarPatologiasPersonalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
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
    private void postConsultaMedica(final Date fecha_consulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGCONSULTA = "tagconsulta";
        initRequestCallback(TAGCONSULTA);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(String.valueOf(idEmpleadoServidor), fecha_consulta,"","","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
    }

    /*
     * Envía datos de Signos vitales al servidor
     * */
    private void postPatologiasPersonales(String id_consulta_medica){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGPATOLOGIA = "tagpatologias";
        initRequestCallback(TAGPATOLOGIA);
        requestService = new RequestService(mResultCallback, getActivity());
        String id_ficha_actual = "";
        Log.d("EMPLEADOFICHA",String.valueOf(empleado.getFicha_actual()));
        if(empleado.getFicha_actual()!=0)
            id_ficha_actual = String.valueOf(empleado.getFicha_actual());

        Map<String, String> sendObj = PatologiasPersonales.getHashMapPatologiasPersonales(id_ficha_actual,id_consulta_medica,lugar, detalle);
        requestService.postDataRequest("POSTCALL", URL_PATOLOGIAS_PERSONALES, sendObj, token);
    }
}
