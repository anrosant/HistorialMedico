package com.example.cltcontrol.historialmedico.fragments;


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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemaActualFragment extends Fragment {
    EditText etProblemActual;
    Button btn_guardar;
    String id_consulta_medica;
    ConsultaMedica consultaMedica;

    public ProblemaActualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problema_actual, container, false);

        etProblemActual = view.findViewById(R.id.etProblemActual);
        btn_guardar = view.findViewById(R.id.btnGuardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        if (extras != null) {
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");

        }
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarProblemaActual();
            }
        });

        return view;
    }

    private void guardarProblemaActual() {
        String problema_actual = etProblemActual.getText().toString();
        if(problema_actual.equals("")){
            Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
        }else {
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            consultaMedica.setProbActual(problema_actual);
            consultaMedica.save();
            limpiarCampos();
            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
    }
    private void limpiarCampos(){
        etProblemActual.setText("");
    }

}
