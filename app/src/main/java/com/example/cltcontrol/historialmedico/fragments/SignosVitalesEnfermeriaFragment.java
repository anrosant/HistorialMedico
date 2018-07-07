package com.example.cltcontrol.historialmedico.fragments;

import android.app.ProgressDialog;
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
import com.example.cltcontrol.historialmedico.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SAVE_SIGNOS;

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


    public SignosVitalesEnfermeriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Log.d("ATENCIONENF", String.valueOf(atencionEnfermeria));
            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(id_atencion));
            Log.d("LISTASIGNOS", String.valueOf(signosVitalesList.size()));
        }
        adapterSignosVitales = new AdapterSignosVitales(getContext(), (ArrayList<SignosVitales>) signosVitalesList);
        Log.d("ADAPTERSIGNOSV", String.valueOf(adapterSignosVitales));
        lvSignosVitales.setAdapter(adapterSignosVitales);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardarSignosVitalesLocal();
                //Recibe los datos de signos vitales
                final String presionSistolicaText = etPresionSistolica.getText().toString();
                final String presionDistolicaText = etPresionDistolica.getText().toString();
                final String temperaturatext = etTemperatura.getText().toString();
                final String pulsoText = etPulso.getText().toString();

                signos = new SignosVitales();
                int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                if(res == 0) {
                    Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                } else if(res == 1) {
                    Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
                    SignosVitales.delete(signos);
                }else{
                    guardarSignosVitalesEnServidor(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
                }
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
     * Función que guarda los signos vitales localmente
     * */
    public void guardarSignosVitalesLocal(String presionSistolicaText, String presionDistolicaText,
                                          String temperaturatext, String pulsoText, int status){

        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));
        signos.setStatus(status);
        signos.save();

        if(id_atencion!=null){
            //Si es la primera vez que crea la atención enferemería
            if(atencionEnfermeria.getEmpleado() == null){
                //Guarda el id del empleado en la atención enfermería y la fecha de atención
                atencionEnfermeria.setEmpleado(empleado);
                atencionEnfermeria.setFecha_atencion(new Date());
                atencionEnfermeria.save();
            }

            signos.setAtencion_enfermeria(atencionEnfermeria);
            signos.save();

            ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                    "atencionenfermeria = ?", String.valueOf(id_atencion));
            adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
        }
        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        limpiarCampos();
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Guardando signos vitales...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_SIGNOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                guardarSignosVitalesLocal(presionSistolicaText, presionDistolicaText,
                                        temperaturatext, pulsoText, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                guardarSignosVitalesLocal(presionSistolicaText, presionDistolicaText,
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
                        //on error storing the name to sqlite with status unsynced
                        guardarSignosVitalesLocal(presionSistolicaText, presionDistolicaText,
                                temperaturatext, pulsoText, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("presion_sistolica", presionSistolicaText);
                params.put("presion_distolica", presionDistolicaText);
                params.put("temperatura", temperaturatext);
                params.put("pulso", pulsoText);
                params.put("atencion_enfermeria", String.valueOf(0));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
