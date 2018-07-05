package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.cltcontrol.historialmedico.adapter.AdapterPatologiasPersonales;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PatologiasPersonalesFragment extends Fragment {

    private ImageButton ib_mostrar_ocultar_contendido;
    private LinearLayout ly_patologias_personales;
    private EditText etDetalleEnfermedad;
    private ListView lvPatologiasPersonales;
    private String id_consulta_medica, id_empleado, lugar, detalle, cargo;
    private ConsultaMedica consultaMedica;
    private ArrayAdapter<CharSequence> adapter;
    private AdapterPatologiasPersonales adapterPatologiaPers;
    private List<String> lugarList;
    private List<PatologiasPersonales> patologiasPersonalesList;
    private Button btn_guardar;
    private Empleado empleado;
    private TextView tvTitulo;

    public PatologiasPersonalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patologias_personales, container, false);

        ly_patologias_personales = view.findViewById(R.id.ly_patologias_personales);
        ib_mostrar_ocultar_contendido = view.findViewById(R.id.ib_mostrar_ocultar_contendido);
        etDetalleEnfermedad = view.findViewById(R.id.etDetalleEnfermedad);
        lvPatologiasPersonales = view.findViewById(R.id.lvPatologiasPersonales);
        btn_guardar = view.findViewById(R.id.btnGuardar);
        tvTitulo = view.findViewById(R.id.tvTitulo);

        Spinner spPatologias = view.findViewById(R.id.spPatologia);
        adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()).getBaseContext(), R.array.patologias, android.R.layout.simple_spinner_dropdown_item);
        spPatologias.setAdapter(adapter);

        //Obtenemos las patologias en un array list
        lugarList = Arrays.asList(getResources().getStringArray(R.array.patologias));

        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica y de empleado desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

        cargo = extras.getString("CARGO");
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
            ly_patologias_personales.setVisibility(View.GONE);
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
        patologiasPersonalesList = PatologiasPersonales.find(PatologiasPersonales.class, "consultamedica = ?", String.valueOf(id_consulta_medica));

        //Crea un adapter de dicha lista y la muestra en un listview
        adapterPatologiaPers = new AdapterPatologiasPersonales(getContext(), patologiasPersonalesList);
        lvPatologiasPersonales.setAdapter(adapterPatologiaPers);

        ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ly_patologias_personales.isShown()){
                    ly_patologias_personales.setVisibility(view.VISIBLE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    ly_patologias_personales.setVisibility(view.GONE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPatologiasPersonales();
            }
        });

        return view;
    }
    /*
     * Guarda los datos de una patologia personal
     * */
    private void guardarPatologiasPersonales() {
        detalle = etDetalleEnfermedad.getText().toString();
        if(lugar==null || detalle.equals("")){
            //guardar lo que hay en detalle

            Toast.makeText(getContext(),"No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            //Si es la primera entrada
            if(consultaMedica.getEmpleado()==null){
                //Guarda el id del empleado en la consulta y la fecha de consulta
                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
                consultaMedica.save();
            }
            PatologiasPersonales patologiasPersonales = new PatologiasPersonales(consultaMedica, lugar,detalle);
            patologiasPersonales.save();

            Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
            ArrayList<PatologiasPersonales> patPersoList = (ArrayList<PatologiasPersonales>) PatologiasPersonales.find(PatologiasPersonales.class,
                    "consultamedica = ?", String.valueOf(consultaMedica.getId()));

            adapterPatologiaPers.actualizarPatologiasPersonalesList(patPersoList);
        }
    }

}
