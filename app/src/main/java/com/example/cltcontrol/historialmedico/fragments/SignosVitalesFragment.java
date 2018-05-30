package com.example.cltcontrol.historialmedico.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignosVitalesFragment extends Fragment {
    EditText etPSistolica, etPDistolica, etTemperatura, etPulso;
    Long id_consulta_medica;
    ConsultaMedica consultaMedica;
    Button btn_guardar;
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

        btn_guardar=view.findViewById(R.id.btnGuardar);

        //
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Objects.requireNonNull(extras).getInt("BOTONPULSADO");

        id_consulta_medica = extras.getLong("ID_CONSULTA_MEDICA");
        consultaMedica.findById(ConsultaMedica.class,id_consulta_medica);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSignosVitales();
            }
        });
        return view;

    }

    public void guardarSignosVitales(){
        int presionSistolica = Integer.parseInt(etPSistolica.getText().toString());
        int presionDistolica = Integer.parseInt(etPDistolica.getText().toString());
        int temp = Integer.parseInt(etTemperatura.getText().toString());
        int pulso = Integer.parseInt(etPulso.getText().toString());

        SignosVitales signosVitales = new SignosVitales(presionSistolica,presionDistolica,pulso,temp,consultaMedica);
        signosVitales.save();

        Toast.makeText(getContext(),"Se han guardado los datos", Toast.LENGTH_SHORT).show();

    }

}
