package com.example.cltcontrol.historialmedico.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.R;
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
    EditText etPSistolica, etPDistolica, etTemperatura, etPulso;
    String id_consulta_medica, id_empleado;
    ConsultaMedica consultaMedica;
    Button btn_guardar;
    ListView lvSignosVitales;
    AdapterSignosVitales adapterSignosVitales;
    Empleado empleado;
    public SignosVitalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        etPSistolica = view.findViewById(R.id.etPresionSistolica);
        etPDistolica = view.findViewById(R.id.etPresionDistolica);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        etPulso = view.findViewById(R.id.etPulso);
        lvSignosVitales = view.findViewById(R.id.lvSignosVitales);


        btn_guardar=view.findViewById(R.id.btnGuardar);

        //
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica
        if (extras != null) {
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

            id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

            //Historial de Signos vitales
            //Obtiene los signos vitales de un empleado
            List<SignosVitales> signosVitalesList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(consultaMedica.getId()));

            //Crea un adapter de dicha lista y la muestra en un listview
            adapterSignosVitales = new AdapterSignosVitales(getContext(), (ArrayList<SignosVitales>) signosVitalesList);
            lvSignosVitales.setAdapter(adapterSignosVitales);

        }


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSignosVitales();
            }
        });
        return view;




    }

    public void guardarSignosVitales(){
        //Guarda el id del empleado en la consulta y la fecha de consulta
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(new Date());
        consultaMedica.save();

        //Recibe los datos de signos vitales
        String presionSistolicaText = etPSistolica.getText().toString();
        String presionDistolicaText = etPDistolica.getText().toString();
        String temperaturatext = etTemperatura.getText().toString();
        String pulsoText = etPulso.getText().toString();

        int presionSistolica=0, presionDistolica=0, temp=0, pulso=0;

        if(!presionDistolicaText.equals("")) {
            presionSistolica = Integer.parseInt(presionSistolicaText);
        }

        if(!presionSistolicaText.equals("")) {
            presionSistolica = Integer.parseInt(presionSistolicaText);
        }

        if(!temperaturatext.equals("")) {
            temp = Integer.parseInt(temperaturatext);
        }

        if(!pulsoText.equals("")) {
            pulso = Integer.parseInt(pulsoText);
        }

        //Guarda los datos y el id de la consulta medica
        SignosVitales signosVitales = new SignosVitales(presionSistolica,presionDistolica,pulso,temp,consultaMedica);
        signosVitales.save();

        //adapterSignosVitales.notifyDataSetChanged();
        ArrayList<SignosVitales> signosVitalesList = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(consultaMedica.getId()));
        adapterSignosVitales.actualizarSignosVitalesList(signosVitalesList);
        //lvSignosVitales.setAdapter(adapterSignosVitales);
        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();

    }

}
