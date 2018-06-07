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
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamenFisicoFragment extends Fragment {

    private EditText et_examen_fisico;
    private Button btn_guardar;
    private String id_consulta_medica, presedencia, id_empleado;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    public ExamenFisicoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_examen_fisico, container, false);
        et_examen_fisico = view.findViewById(R.id.et_examen_fisico);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        presedencia = extras.getString("PRESEDENCIA");
        //Recibe el id de consulta medica desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

        if(presedencia.equals("consultar")) {
            et_examen_fisico.setText(consultaMedica.getExamen_fisico());
            btn_guardar.setText("Editar");
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
        String examen_fisico = et_examen_fisico.getText().toString();
        if(examen_fisico.equals("")){
            Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
        }else {
            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta

                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
            }
            consultaMedica.setExamen_fisico(examen_fisico);
            consultaMedica.save();

            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
    }

}
