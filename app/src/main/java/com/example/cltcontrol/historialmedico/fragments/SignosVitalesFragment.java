package com.example.cltcontrol.historialmedico.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cltcontrol.historialmedico.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignosVitalesFragment extends Fragment {

    EditText pSistolica;
    EditText pDistolica;
    EditText temperatura;
    EditText pulso;

    public SignosVitalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        pSistolica = view.findViewById(R.id.etPresionSistolica);
        pDistolica = view.findViewById(R.id.etPresionDistolica);
        temperatura = view.findViewById(R.id.etTemperatura);
        pulso = view.findViewById(R.id.etPulso);

        return view;
    }

    public void grabarSignos(){
    }

}
