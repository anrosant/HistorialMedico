package com.example.cltcontrol.historialmedico.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemDiagnostico;
import com.example.cltcontrol.historialmedico.adapter.AdapterEnfermedades;
import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
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
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SAVE_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SAVE_DIAGNOSTICO;

public class DiagnosticoFragment extends Fragment {

    public static List<Enfermedad> enfermedadList;
    private RecyclerView recyclerEnfermedades;
    private AdapterEnfermedades adaptadorEnfermedades;
    private EditText etBuscarEnfermedades;
    private Enfermedad enfermedad;
    private String tipo_enfermedad,id_consulta_medica, id_empleado, cargo;
    private AdapterItemDiagnostico adapterItemDiagnostico;
    private Button btn_guardar;
    private ConsultaMedica consultaMedica;
    private RadioGroup rg_tipo_enfermedad;
    private RadioButton radioButton;
    private Empleado empleado;
    private ListView lvDiagnostico;
    private LinearLayout ly_diagnostico;
    private ImageButton ib_mostrar_ocultar_contendido;
    private List<Diagnostico> diagnosticoList;
    private List<Enfermedad> newList;
    private TextView tvTitulo;
    private Diagnostico diagnostico;

    public DiagnosticoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_diagnostico, container, false);

        btn_guardar = view.findViewById(R.id.btn_guardar);
        rg_tipo_enfermedad = view.findViewById(R.id.rgTipoEnfer);
        lvDiagnostico = view.findViewById(R.id.lvDiagnostico);
        ib_mostrar_ocultar_contendido = view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        ly_diagnostico = view.findViewById(R.id.ly_diagnostico);
        tvTitulo = view.findViewById(R.id.tvTitulo);

        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        cargo = extras.getString("CARGO");

        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            ly_diagnostico.setVisibility(View.GONE);
            ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
            tvTitulo.setVisibility(View.GONE);

        }
        //Muestra la lista de diagnosticos
        diagnosticoList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", id_consulta_medica);
        //Crea un adapter de dicha lista y la muestra en un listview
        adapterItemDiagnostico = new AdapterItemDiagnostico(getContext(), diagnosticoList);
        lvDiagnostico.setAdapter(adapterItemDiagnostico);

        readEnfermedadesAll();
        recyclerEnfermedades = view.findViewById(R.id.rvListaEnfermedades);
        recyclerEnfermedades.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        etBuscarEnfermedades = view.findViewById(R.id.etBuscarEnfermedades);

        //Muestra la lsita de enfermedades
        adaptadorEnfermedades = new AdapterEnfermedades(enfermedadList);
        recyclerEnfermedades.setAdapter(adaptadorEnfermedades);

        etBuscarEnfermedades.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int star,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int star,
                                      int count, int after) {

                String newTest;
                if(charSequence.length() != 0){
                    newTest = etBuscarEnfermedades.getText().toString().toLowerCase();
                    newList = new ArrayList<>();
                    for (Enfermedad enfermedad:enfermedadList){
                        String nombre = enfermedad.getNombre().toLowerCase();
                        if(nombre.contains(newTest)){
                            newList.add(enfermedad);
                        }
                    }
                    adaptadorEnfermedades.setFilter((List<Enfermedad>) newList);
                }else{
                    adaptadorEnfermedades.setFilter((List<Enfermedad>) enfermedadList);
                }
            }
        });

        //Al dar click en un item, este se guarda en la variable enfermedad
        recyclerEnfermedades.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerEnfermedades, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), "Se ha escogido " + adaptadorEnfermedades.getListaEnfermedades().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                        enfermedad = adaptadorEnfermedades.getListaEnfermedades().get(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        //Escoge el tipo de enfermedad
        rg_tipo_enfermedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) view.findViewById(checkedId);
                tipo_enfermedad = (String) radioButton.getText();
            }
        });

        //Boton guardarDiagnostico
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enfermedad == null || tipo_enfermedad==null){
                    Toast.makeText(getContext(),"No ha seleccionado todos los datos",Toast.LENGTH_SHORT).show();
                }else {
                    consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
                    Log.d("CONSULTAMED", id_consulta_medica);
                    Log.d("CONSULTAMEDD", String.valueOf(consultaMedica.getId_serv()));

                    //Si es la primera vez que crea la consulta medica
                    if(consultaMedica.getEmpleado()==null){
                        Date fechaConsulta = new Date();

                        //Guarda el id del empleado en la consulta y la fecha de consulta
                        guardarConsultaMedicaEnServidor(empleado.getId(), fechaConsulta);
                    }else{
                        guardarDiagnosticoEnServidor();
                    }

                    actualizarDiagnosticos();
                }
            }
        });

        ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ly_diagnostico.isShown()){
                    ly_diagnostico.setVisibility(view.VISIBLE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    ly_diagnostico.setVisibility(view.GONE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });
        return  view;
    }

    /*
     * Función que actualiza la lista de diagnósticos
     * */
    public void actualizarDiagnosticos(){
        //Actualizar la lista de diagnosticos
        ArrayList<Diagnostico> diagnosticosList = (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class, "consultamedica = ?", String.valueOf(consultaMedica.getId()));
        adapterItemDiagnostico.actualizarDiagnosticoList(diagnosticosList);
    }

    /*
     * Función que guardar una Consulta médica localmente
     * */
    public void guardarConsultaMedicaLocal(Date fechaConsulta, int id_servidor, int status){
        consultaMedica.setId_serv(id_servidor);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(fechaConsulta);
        consultaMedica.setStatus(status);
        consultaMedica.save();

        guardarDiagnosticoEnServidor();
    }

    /*
     * Función que guarda un diagnóstico localmente
     * */
    private void guardarDiagnostico(int id_serv, int status) {
        //Se guarda la consulta medica en diagnostico
        diagnostico = new Diagnostico(consultaMedica,enfermedad, tipo_enfermedad);
        diagnostico.setStatus(status);
        diagnostico.setId_serv(id_serv);
        diagnostico.save();

        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
    }

    /*
     * Guarda las enfermedades que están en la base de datos, en una lista
     * */
    public void readEnfermedadesAll(){
        try{
            enfermedadList = Enfermedad.listAll(Enfermedad.class);
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Función que guardar una Consulta médica en el servidor
     * */
    private void guardarConsultaMedicaEnServidor(Long id, final Date fechaConsulta) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_CONSULTA_MEDICA,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                //Si se guarda en el servidor, guardar localmente con status 1
                                guardarConsultaMedicaLocal(fechaConsulta, Integer.parseInt(String.valueOf(obj.get("pk"))),NAME_SYNCED_WITH_SERVER);
                            } else {
                                //Si no se guarda en el servidor, guardar localmente con status 0
                                guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
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
                        progressDialog.dismiss();
                        guardarConsultaMedicaLocal(fechaConsulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Falta Examen fisico
                Map<String, String> params = new HashMap<>();
                params.put("empleado", "1");
                params.put("fecha", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fechaConsulta)));
                params.put("motivo", "");
                params.put("problema_actual", "");
                params.put("revision", "");
                params.put("prescripcion", "");
                params.put("examen_fisico", "");

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    /*
    * Función que guarda un diágnostico en el servidor
    * */
    private void guardarDiagnosticoEnServidor() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Guardando diagnóstico...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_DIAGNOSTICO,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                //Si se guarda en el servidor, guardar localmente con status 1
                                guardarDiagnostico(Integer.parseInt(String.valueOf(obj.get("pk"))),NAME_SYNCED_WITH_SERVER);
                            } else {
                                //Si no se guarda en el servidor, guardar localmente con status 0
                                guardarDiagnostico(0, NAME_NOT_SYNCED_WITH_SERVER);
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"No tiene conexión.",Toast.LENGTH_SHORT).show();
                        guardarDiagnostico(0, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Falta Examen fisico
                Map<String, String> params = new HashMap<>();
                params.put("consulta_medica", String.valueOf(consultaMedica.getId_serv()));
                params.put("enfermedad", String.valueOf(enfermedad.getId()));
                params.put("tipo", tipo_enfermedad);

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}