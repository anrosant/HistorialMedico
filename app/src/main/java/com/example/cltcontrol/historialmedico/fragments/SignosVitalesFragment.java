package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.cltcontrol.historialmedico.adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignosVitalesFragment extends Fragment {
    private EditText etPSistolica, etPDistolica, etTemperatura, etPulso;
    private String id_consulta_medica, id_empleado, id_atencion_enfermeria, precedencia, cargo;
    private ConsultaMedica consultaMedica;
    private ListView lvSignosVitales;
    private AdapterSignosVitales adapterSignosVitales;
    private Empleado empleado;
    private AtencionEnfermeria atencionEnfermeria;
    private List<SignosVitales> signosVitalesList;
    private Button btn_guardar;
    private ImageButton ib_mostrar_ocultar_contendido;
    LinearLayout ly_signos_vitales;
    private TextView tvTitulo;

    public SignosVitalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        id_atencion_enfermeria = extras.getString("ID_ATENCION");
        precedencia = extras.getString("PRESEDENCIA");
        //Recibe el id del empleado
        id_empleado = extras.getString("ID_EMPLEADO");
        cargo = extras.getString("CARGO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

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
        //Ingresa a atencion enfermeria
        else{
            if(cargo.equals("Doctor")){
                btn_guardar.setVisibility(View.GONE);
                ib_mostrar_ocultar_contendido.setVisibility(View.GONE);
                ly_signos_vitales.setVisibility(View.GONE);
                tvTitulo.setVisibility(View.GONE);
            }

            atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.valueOf(id_atencion_enfermeria));

            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            signosVitalesList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(id_atencion_enfermeria));
        }
        //Crea un adapter de dicha lista y la muestra en un listview
        adapterSignosVitales = new AdapterSignosVitales(getContext(), (ArrayList<SignosVitales>) signosVitalesList);
        lvSignosVitales.setAdapter(adapterSignosVitales);


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSignosVitales();
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

    public void guardarSignosVitales(){
        //Recibe los datos de signos vitales
        String presionSistolicaText = etPSistolica.getText().toString();
        String presionDistolicaText = etPDistolica.getText().toString();
        String temperaturatext = etTemperatura.getText().toString();
        String pulsoText = etPulso.getText().toString();

        SignosVitales signos = new SignosVitales();
        int res = signos.validarSignos(presionSistolicaText, presionDistolicaText, temperaturatext, pulsoText);
        if(res == 0) {
            Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
            return;
        } else if(res == 1) {
            Toast.makeText(getContext(), "Los valores están fuera de rango", Toast.LENGTH_SHORT).show();
            return;
        }

        signos.setPresion_sistolica(Integer.parseInt(presionSistolicaText));
        signos.setPresion_distolica(Integer.parseInt(presionDistolicaText));
        signos.setPulso(Integer.parseInt(pulsoText));
        signos.setTemperatura(Float.parseFloat(temperaturatext));

        //Guarda los datos y el id de la consulta medica o enfermeria
        if(id_consulta_medica!=null){
            //Si es la primera vez que crea la consulta medica
            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta
                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
                consultaMedica.save();
            }

            signos.setConsultaMedica(consultaMedica);
            signos.save();

            ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                    "consultamedica = ?", String.valueOf(consultaMedica.getId()));
            adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
        } else {
            if(atencionEnfermeria.getEmpleado() == null){
                atencionEnfermeria.setEmpleado(empleado);
                atencionEnfermeria.setFecha_atencion(new Date());
                atencionEnfermeria.save();
            }

            signos.setAtencion_enfermeria(atencionEnfermeria);
            signos.save();

            ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class,
                    "atencionenfermeria = ?", String.valueOf(id_atencion_enfermeria));
            adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
        }
        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    private void limpiarCampos(){
        etPSistolica.setText("");
        etPDistolica.setText("");
        etTemperatura.setText("");
        etPulso.setText("");
        etPSistolica.requestFocus();
    }

}