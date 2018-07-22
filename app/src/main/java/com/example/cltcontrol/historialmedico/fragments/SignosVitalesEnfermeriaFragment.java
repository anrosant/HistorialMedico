package com.example.cltcontrol.historialmedico.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.utils.NetworkStateChecker;
import com.example.cltcontrol.historialmedico.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.DATA_SAVED_BROADCAST;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_ATENCION_ENFERMERIA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SIGNOS;

public class SignosVitalesEnfermeriaFragment extends Fragment {

    private String id_atencion, id_empleado, cargo;
    private Bundle extras;
    private EditText etPresionSistolica;
    private EditText etPresionDistolica;
    private ListView lvSignosVitales;
    private EditText etPulso;
    private EditText etTemperatura;
    private Button btn_guardar;
    private AdapterSignosVitales adapterSignosVitales;
    private ImageButton ib_mostrar_ocultar_contendido;
    private LinearLayout ly_signos_vitales;
    private TextView tvTitulo;
    private AtencionEnfermeria atencionEnfermeria;
    private List<SignosVitales> signosVitalesList;
    private SignosVitales signos;
    private Empleado empleado;

    private BroadcastReceiver broadcastReceiver;


    public SignosVitalesEnfermeriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getContext()).registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        etPresionSistolica = view.findViewById(R.id.etPSistolica);
        etPresionDistolica = view.findViewById(R.id.etPDistolica);
        etPulso = view.findViewById(R.id.etPulso);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        btn_guardar = view.findViewById(R.id.btnGuardar);
        ib_mostrar_ocultar_contendido =  view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        ly_signos_vitales = view.findViewById(R.id.ly_signos_vitales);
        lvSignosVitales = view.findViewById(R.id.lvSignosVitales);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        id_atencion = extras.getString("ID_ATENCION");

        id_empleado = extras.getString("ID_EMPLEADO");
        cargo = extras.getString("CARGO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

        //Ingresa a nueva atención enfermería
        if(id_atencion!=null) {
            if(cargo.equals("Doctor")){
                btn_guardar.setVisibility(View.GONE);
                ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
                ly_signos_vitales.setVisibility(View.GONE);
                tvTitulo.setVisibility(View.GONE);
            }

            atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.valueOf(id_atencion));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(id_atencion));
        }else{ //Esta es la parte de signos vitales rapidos
            signosVitalesList = SignosVitales.find(SignosVitales.class, "empleado = ?", String.valueOf(id_empleado));
        }

        adapterSignosVitales = new AdapterSignosVitales(getContext(), (ArrayList<SignosVitales>) signosVitalesList);
        lvSignosVitales.setAdapter(adapterSignosVitales);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recibe los datos de signos vitales
                final String presionSistolicaText = etPresionSistolica.getText().toString();
                final String presionDistolicaText = etPresionDistolica.getText().toString();
                final String temperaturatext = etTemperatura.getText().toString();
                final String pulsoText = etPulso.getText().toString();

                signos = new SignosVitales();
                int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, pulsoText , temperaturatext);
                if(res == 0) {
                    Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                } else if(res == 1) {
                    Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                }else{
                    //Si es la primera vez que crea la atención enfermería
                    if (id_atencion!=null && atencionEnfermeria.getEmpleado() == null) {
                        Date fechaAtencion = new Date();

                        //Guarda el id del empleado en la atención y la fecha de atención
                        guardarAtencionEnfermeriaEnServidor(empleado.getId(), fechaAtencion, presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                        cargarSignosVitales(atencionEnfermeria.getId());
                    }else if(atencionEnfermeria.getEmpleado()!=null){
                        signos.setAtencion_enfermeria(atencionEnfermeria);
                        signos.save();
                        guardarSignosVitalesEnServidor(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                        cargarSignosVitales(atencionEnfermeria.getId());
                    }else{
                        cargarSignosVitalesSin();
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

                Objects.requireNonNull(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
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
                "atencionenfermeria = ?", String.valueOf(id));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }

    public void cargarSignosVitalesSin(){
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                "empleado = ?", String.valueOf(id_empleado));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
    }

    /*
     * Función que guarda los signos vitales localmente
     * */
    public void guardarSignosVitalesLocal(int id_serv, String presionSistolicaText, String presionDistolicaText,
                                          String temperaturatext, String pulsoText,int status){
        signos.setId_serv(id_serv);
        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));
        signos.setStatus(status);
        signos.save();

        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        cargarSignosVitales(signos.getAtencion_enfermeria().getId());
    }
    
    /*
    * Función que guarda una atencion enfermeria localmente
    * */

    public void guardarAtencionEnfermeriaLocal(Date fechaAtencion, int id_servidor, int status,
                                               String presionSistolicaText, String presionDistolicaText,
                                               String temperaturatext, String pulsoText){
        atencionEnfermeria.setId_serv(id_servidor);
        atencionEnfermeria.setEmpleado(empleado);
        atencionEnfermeria.setFecha_atencion(fechaAtencion);
        atencionEnfermeria.setStatus(status);
        atencionEnfermeria.save();

        signos.setAtencion_enfermeria(atencionEnfermeria);
        signos.save();
        guardarSignosVitalesEnServidor(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
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
    * Función que guardar los datos de signos vitales en el servidor
    * */
    private void guardarSignosVitalesEnServidor(final String presionSistolicaText, final String presionDistolicaText,
                                                final String temperaturatext, final String pulsoText) {
        Log.d("IDSERV", String.valueOf(signos.getAtencion_enfermeria().getId_serv()));
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Guardando datos en el servidor...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                int id_serv_signos = Integer.parseInt(String.valueOf(obj.get("pk")));
                                //Si se guarda en el servidor, guardar localmente con status 1
                                guardarSignosVitalesLocal(id_serv_signos, presionSistolicaText, presionDistolicaText,
                                        temperaturatext, pulsoText, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //Si no se guarda en el servidor, guardar localmente con status 0
                                guardarSignosVitalesLocal(0, presionSistolicaText, presionDistolicaText,
                                        temperaturatext, pulsoText, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Si no se guarda en el servidor, guardar localmente con status 0
                        guardarSignosVitalesLocal(0, presionSistolicaText, presionDistolicaText,
                                temperaturatext, pulsoText, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("consulta_medica", "");
                params.put("atencion_enfermeria", String.valueOf(signos.getAtencion_enfermeria().getId_serv()));
                params.put("presion_sistolica", presionSistolicaText);
                params.put("presion_distolica", presionDistolicaText);
                params.put("pulso", pulsoText);
                params.put("temperatura", temperaturatext);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    /*
     * Función que guardar una atención de enfermería en el servidor
     * */
    private void guardarAtencionEnfermeriaEnServidor(final long idEmpleado, final Date fechaAtencion,
                                                     final String presionSistolicaText, final String presionDistolicaText,
                                                     final String temperaturatext, final String pulsoText) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ATENCION_ENFERMERIA,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                int id_serv_atencion = Integer.parseInt(String.valueOf(obj.get("pk")));
                                //Si se guarda en el servidor, guardar localmente con status 1
                                guardarAtencionEnfermeriaLocal(fechaAtencion, id_serv_atencion,NAME_SYNCED_WITH_SERVER,
                                        presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                            } else {
                                //Si no se guarda en el servidor, guardar localmente con status 0
                                guardarAtencionEnfermeriaLocal(fechaAtencion, 0,NAME_NOT_SYNCED_WITH_SERVER,
                                        presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Si no se guarda en el servidor, guardar localmente con status 0
                        Toast.makeText(getContext(),"No tiene conexión.",Toast.LENGTH_SHORT).show();
                        guardarAtencionEnfermeriaLocal(fechaAtencion, 0,NAME_NOT_SYNCED_WITH_SERVER,
                                presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Falta Examen fisico
                Map<String, String> params = new HashMap<>();
                params.put("empleado", "1");
                params.put("fecha", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fechaAtencion)));
                params.put("motivo", "");
                params.put("diagnostico", "");
                params.put("plan_cuidados", "");

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
