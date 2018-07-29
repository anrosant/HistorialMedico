package com.example.cltcontrol.historialmedico.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemDiagnostico;
import com.example.cltcontrol.historialmedico.adapter.AdapterEnfermedades;
import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.BuscarTexto;
import com.example.cltcontrol.historialmedico.utils.ListaEnfermedades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class DiagnosticoFragment extends Fragment {

    private RecyclerView recyclerEnfermedades;
    private AdapterItemDiagnostico adapterItemDiagnostico;
    private AdapterEnfermedades adaptadorEnfermedades;
    private EditText etBuscarEnfermedades;
    private Button btn_guardar;
    private RadioGroup rg_tipo_enfermedad;
    private RadioButton radioButton;
    private ListView lvDiagnostico;
    private LinearLayout ly_diagnostico;
    private ImageButton ib_mostrar_ocultar_contendido;
    private TextView tvTitulo;

    private static List<Enfermedad> enfermedadList;
    private String tipo_enfermedad,id_consulta_medica, id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;
    private List<Diagnostico> diagnosticoList;
    private Diagnostico diagnostico;
    private List<Enfermedad> newList;
    private Enfermedad enfermedad;

    private IResult mResultCallback = null;
    private RequestService requestService;
    private int id_empleado_servidor;

    private Date fecha_consulta;
    private final String TAGDIAGNOSTICO = "tagdiagnostico";
    private final String TAGCONSULTA="tagconsulta";

    public DiagnosticoFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_diagnostico, container, false);

        btn_guardar = view.findViewById(R.id.btn_guardar);
        rg_tipo_enfermedad = view.findViewById(R.id.rgTipoEnfer);
        lvDiagnostico = view.findViewById(R.id.lvDiagnostico);
        ib_mostrar_ocultar_contendido = view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        ly_diagnostico = view.findViewById(R.id.ly_diagnostico);
        tvTitulo = view.findViewById(R.id.tv_titulo);

        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        assert extras != null;
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        id_empleado_servidor = empleado.getId_serv();
        cargo = extras.getString("CARGO");

        assert cargo != null;
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

        enfermedadList = ListaEnfermedades.readEnfermedadesAll();

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
                    etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_cancel_grey_24dp,0);
                    newTest = BuscarTexto.quitaDiacriticos(etBuscarEnfermedades.getText().toString().toLowerCase());
                    newList = new ArrayList<>();
                    for (Enfermedad enfermedad:enfermedadList){
                        String nombre = enfermedad.getNombre().toLowerCase();
                        String codigo = enfermedad.getCodigo().toString().toLowerCase();
                        if(nombre.contains(newTest)){
                            newList.add(enfermedad);
                        }
                    }
                    adaptadorEnfermedades.setFilter(newList);
                }else{
                    etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    adaptadorEnfermedades.setFilter(enfermedadList);
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
                        etBuscarEnfermedades.setText(enfermedad.getNombre());
                        Log.d("ENFERMEDAD", String.valueOf(enfermedad.getId()));
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        etBuscarEnfermedades.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        if (motionEvent.getX() >= (etBuscarEnfermedades.getRight() - etBuscarEnfermedades.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            etBuscarEnfermedades.setText("");
                            etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }catch(NullPointerException e){
                        etBuscarEnfermedades.requestFocus();
                        //Llamada al teclado
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etBuscarEnfermedades, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                return true;
            }
        });

        //Escoge el tipo de enfermedad
        rg_tipo_enfermedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = view.findViewById(checkedId);
                tipo_enfermedad = (String) radioButton.getText();
            }
        });

        //Boton guardarDiagnostico
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diagnostico = new Diagnostico();
                if (enfermedad == null || tipo_enfermedad == null) {
                    Toast.makeText(getContext(), "No ha seleccionado todos los datos", Toast.LENGTH_SHORT).show();
                    Diagnostico.delete(diagnostico);
                } else {
                    consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

                    //Si es la primera vez que crea la consulta medica
                    if (consultaMedica.getEmpleado() == null) {
                        fecha_consulta = new Date();
                        postConsultaMedica(fecha_consulta);
                    } else {
                        postDiagnostico(String.valueOf(consultaMedica.getId_serv()));
                    }
                    cargarDiagnosticos(consultaMedica.getId());
                }
            }
        });

        ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ly_diagnostico.isShown()){
                    ly_diagnostico.setVisibility(View.VISIBLE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    ly_diagnostico.setVisibility(View.GONE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });
        return  view;
    }

    private void postDiagnostico(String id_consulta) {
        String id_serv = "";
        if(enfermedad.getId_serv()!=0){
            id_serv = String.valueOf(enfermedad.getId_serv());
        }
        Log.d("IDSERV", id_serv);
        mResultCallback = null;
        initRequestCallback(TAGDIAGNOSTICO);
        requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = Diagnostico.getJSONDiagnostico(id_consulta, tipo_enfermedad, id_serv);
        requestService.postDataRequest("POSTCALL", URL_DIAGNOSTICO, sendObj);
    }

    /*
     * Función que carga los diagnosticos en la lista
     * */
    private void cargarDiagnosticos(Long id){
        ArrayList<Diagnostico> diagnosticoList = (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class,
                "consultamedica = ?", String.valueOf(id));
        adapterItemDiagnostico.actualizarDiagnosticoList(diagnosticoList);
    }

    /*
    * Función que guarda un diagnóstico localmente
    * */
    private void guardarDiagnosticoLocal(int id_serv, int status) {
        diagnostico.setEnfermedad(enfermedad);
        diagnostico.setTipoEnfermedad(tipo_enfermedad);
        diagnostico.setId_serv(id_serv);
        diagnostico.setStatus(status);
        diagnostico.setConsulta_medica(consultaMedica);
        diagnostico.save();

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
        cargarDiagnosticos(consultaMedica.getId());
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

        postDiagnostico(String.valueOf(id_servidor));
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
                        String fechaConsulta = response.getString("fecha");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");
                        guardarConsultaMedicaLocal(fecha,Integer.parseInt(pk), NAME_SYNCED_WITH_SERVER);
                        //postDiagnostico(pk);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        //Si ha realizado post en Diagnóstico
                        String pk = response.getString("pk");
                        guardarDiagnosticoLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);
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
                    guardarDiagnosticoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fecha_consulta, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarDiagnosticoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
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
        initRequestCallback(TAGCONSULTA);
        requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = ConsultaMedica.getJSONConsultaMedica(String.valueOf(id_empleado_servidor), fecha_consulta, "","","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj);
    }

}