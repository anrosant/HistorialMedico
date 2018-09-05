package com.example.cltcontrol.historialmedico.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
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
    private LinearLayout lySignosVitales;
    private TextView tvTitulo;
    private Button btnGuardar;

    private AdapterSignosVitales adapterSignosVitales;
    private List<SignosVitales> signosVitalesList;
    private ConsultaMedica consultaMedica;
    private SignosVitales signos;
    private Empleado empleado;
    private Date fechaConsulta, fechaSigno;
    private String idConsultaMedica, cargo,presionDistolicaText, presionSistolicaText,
            temperaturatext, pulsoText ;
    private int idEmpleadoServidor;

    //POST
    private IResult mResultCallback = null;
    private RequestService requestService;


    public SignosVitalesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Registar el receiver para sincronizar datos
        //Objects.requireNonNull(getContext()).registerReceiver(new SincronizacionInmediata(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);

        etPSistolica = view.findViewById(R.id.etPSistolica);
        etPDistolica = view.findViewById(R.id.etPDistolica);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        etPulso = view.findViewById(R.id.etPulso);
        ListView lvSignosVitales = view.findViewById(R.id.lvSignosVitales);
        btnGuardar = view.findViewById(R.id.btnGuardarPermiso);
        lySignosVitales = view.findViewById(R.id.lySignosVitales);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        //
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica

        validaPermisos();

        if (extras != null) {
            idConsultaMedica = extras.getString("ID_CONSULTA_MEDICA");
            //Recibe el id del empleado
            String id_empleado = extras.getString("ID_EMPLEADO");
            cargo = extras.getString("CARGO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            idEmpleadoServidor = empleado.getId_serv();
        }


        //Ingresa a nueva consulta medica
        if(idConsultaMedica !=null) {
            if(cargo.equals("Enfermera")){
                btnGuardar.setVisibility(View.GONE);
                lySignosVitales.setVisibility(View.GONE);
                tvTitulo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(idConsultaMedica));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(idConsultaMedica));

        }

        //Crea un adapterItemAtencionEnfermeria de dicha lista y la muestra en un listview
        adapterSignosVitales = new AdapterSignosVitales(getContext(), signosVitalesList);
        lvSignosVitales.setAdapter(adapterSignosVitales);

        //BOTON GUARDAR SIGNOS VITALES
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGuardar.setEnabled(false);
                //Recibe los datos de signos vitales
                presionSistolicaText = etPSistolica.getText().toString();
                presionDistolicaText = etPDistolica.getText().toString();
                temperaturatext = etTemperatura.getText().toString();
                pulsoText = etPulso.getText().toString();

                signos = new SignosVitales();
                int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, pulsoText, temperaturatext);
                switch (res) {
                    case 0:
                        Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                        SignosVitales.delete(signos);
                        btnGuardar.setEnabled(true);
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                        SignosVitales.delete(signos);
                        btnGuardar.setEnabled(true);
                        break;
                    default:
                        //Si es la primera vez que crea la consulta medica
                        if (consultaMedica.getEmpleado() == null) {
                            fechaConsulta = new Date();
                            Log.d("HEREEE", "1");
                            postConsultaMedica(fechaConsulta);
                        } else {
                            fechaSigno = new Date();
                            postSignosVitales(String.valueOf(consultaMedica.getId_serv()));

                        }

                        break;
                }
                cargarSignosVitales(consultaMedica.getId());
            }
        });

        tvTitulo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tvTitulo.getRight() - tvTitulo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!lySignosVitales.isShown()){
                            lySignosVitales.setVisibility(View.VISIBLE);
                            tvTitulo.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_white_24dp,0);
                        }else {
                            lySignosVitales.setVisibility(View.GONE);
                            tvTitulo.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_white_24dp,0);
                        }
                    }
                }
                return true;
            }
        });

        return view;
    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        if((ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar permiso en la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permisos otorgados");
            }
        }
    }


    /*
     * Función que carga los signos vitales en la lista
     * @param id id de la consulta médica en la que se encuentra, tipo long
     * */
    private void cargarSignosVitales(Long id){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "consultamedica = ?", String.valueOf(id));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }

    /*
     * Función que guarda los signos vitales localmente
     * @param idServidor id del signo vital en el servidor, tipo entero
     * @param status si los datos se enviaron al servidor (1) o no (0) tipo entero
     * */
    private void guardarSignosVitalesLocal(int idServidor, int status){
        signos.setId_serv(idServidor);
        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));
        signos.setStatus(status);
        signos.setConsultaMedica(consultaMedica);
        signos.setEmpleado(empleado);
        signos.setFecha(fechaSigno);
        signos.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
        limpiarCampos();
        btnGuardar.setEnabled(true);
        cargarSignosVitales(signos.getConsultaMedica().getId());
    }

    /*
    * Función que guarda una consulta médica localmente
    * @param fechaConsulta fecha en la que se realizó la consulta médica, tipo Date
    * @param idServidor id de la atención en el servidor, tipo entero
    * @param status si los datos se enviaron al servidor (1) o no (0)
    * */
    private void guardarConsultaMedicaLocal(Date fechaConsulta, int idServidor, int status){
        consultaMedica.setId_serv(idServidor);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(fechaConsulta);
        consultaMedica.setStatus(status);
        consultaMedica.save();

        fechaSigno = new Date();
        postSignosVitales(String.valueOf(idServidor));
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
     * @param TAG me indica a qué clase pertenece el request hecho, tipo String
     * */
    private void initRequestCallback(final String TAG){
        Log.d("HEREEE", "3");
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
                    guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarSignosVitalesLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
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
     * Obtiene el token
     * Inicia un requerimiento
     * Obtiene un hashmap con los datos enviados por parámetros
     * Envía datos de Consulta médica al servidor
     * @param fechaConsulta fecha en la que el paciente fue a la consulta médica tipo Date
     * */
    private void postConsultaMedica(final Date fechaConsulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        Log.d("HERE", "2");
        String TAGCONSULTA = "tagconsulta";
        initRequestCallback(TAGCONSULTA);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(String.valueOf(idEmpleadoServidor), fechaConsulta,"","","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
    }

    /*
     * Obtiene el token
     * Inicia un requerimiento
     * Obtiene un hashmap con los datos enviados por parámetros
     * Envía datos de Signos vitales al servidor
     * @param idConsultaMedica id de la consulta médica del servidor en la que se registró
     * el signo vital, tipo String
     * */
    private void postSignosVitales(String idConsultaMedica){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGSIGNOS = "tagsignos";
        initRequestCallback(TAGSIGNOS);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = SignosVitales.getHashMapSignosVitales(String.valueOf(idEmpleadoServidor),idConsultaMedica,"",presionSistolicaText,presionDistolicaText,pulsoText,temperaturatext, fechaSigno);
        requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj, token);
    }

}