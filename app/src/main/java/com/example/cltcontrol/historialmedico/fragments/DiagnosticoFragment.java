package com.example.cltcontrol.historialmedico.fragments;

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
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemDiagnostico;
import com.example.cltcontrol.historialmedico.Adapter.EnfermedadesAdapter;
import com.example.cltcontrol.historialmedico.Adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DiagnosticoFragment extends Fragment {

    public static List<Enfermedad> enfermedadList;
    private RecyclerView recyclerEnfermedades;
    private EnfermedadesAdapter adaptadorEnfermedades;
    private EditText etBuscarEnfermedades;
    private Enfermedad enfermedad;
    private String tipo_enfermedad,id_consulta_medica, id_empleado;
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


    public DiagnosticoFragment() {
        // Required empty public constructor
    }

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

        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            //Recibe el id de consulta medica desde Historial de consulta medica
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

            //Muestra la lista de diagnosticos
            diagnosticoList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", id_consulta_medica);
            //Crea un adapter de dicha lista y la muestra en un listview
            adapterItemDiagnostico = new AdapterItemDiagnostico(getContext(), diagnosticoList);
            lvDiagnostico.setAdapter(adapterItemDiagnostico);
        }
        readEnfermedadesAll();
        recyclerEnfermedades = view.findViewById(R.id.rvListaEnfermedades);
        recyclerEnfermedades.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        etBuscarEnfermedades = view.findViewById(R.id.etBuscarEnfermedades);

        //Muestra la lsita de enfermedades
        adaptadorEnfermedades = new EnfermedadesAdapter(enfermedadList);
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
                guardarDiagnostico();
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


    private void guardarDiagnostico() {
        if(enfermedad == null || tipo_enfermedad==null){
            Toast.makeText(getContext(),"No ha seleccionado todos los datos",Toast.LENGTH_SHORT).show();
        }else {
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

            //Si es la primera vez que crea la consulta medica
            if(consultaMedica.getEmpleado()==null){
                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
                consultaMedica.save();
            }

            //Se guarda la consulta medica en diagnostico
            Diagnostico diagnostico = new Diagnostico(consultaMedica,enfermedad, tipo_enfermedad);
            diagnostico.save();

            //Actualizar la lista de diagnosticos
            ArrayList<Diagnostico> diagnosticosList = (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class, "consultamedica = ?", String.valueOf(consultaMedica.getId()));
            adapterItemDiagnostico.actualizarDiagnosticoList(diagnosticosList);
            Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void readEnfermedadesAll(){
        try{
            enfermedadList = Enfermedad.listAll(Enfermedad.class);
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
