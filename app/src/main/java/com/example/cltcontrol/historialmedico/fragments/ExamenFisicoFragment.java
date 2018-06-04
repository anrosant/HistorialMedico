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
public class ExamenFisicoFragment extends Fragment {

    EditText etExamenFisico;
    Button btn_guardar;
    String id_consulta_medica;
    ConsultaMedica consultaMedica;

    public ExamenFisicoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_examen_fisico, container, false);
        etExamenFisico = view.findViewById(R.id.etExamenFisico);
        btn_guardar = view.findViewById(R.id.btnGuardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        if (extras != null) {
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");

        }
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarExamenFisico();
            }
        });

        return view;
    }
    private void guardarExamenFisico() {
        String examen_fisico = etExamenFisico.getText().toString();
        if(examen_fisico.equals("")){
            Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
        }else {
            consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            consultaMedica.setExamen_fisico(examen_fisico);
            consultaMedica.save();
            limpiarCampos();
            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
    }
    private void limpiarCampos(){
        etExamenFisico.setText("");
    }


}
