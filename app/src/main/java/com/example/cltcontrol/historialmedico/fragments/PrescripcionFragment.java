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
public class PrescripcionFragment extends Fragment {
    private EditText et_prescripcion;
    private Button btn_guardar;
    private String id_consulta_medica, presedencia,id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    public PrescripcionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prescripcion, container, false);
        et_prescripcion = view.findViewById(R.id.et_prescripcion);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Recibe el datos desde Historial de consulta medica
        presedencia = extras.getString("PRESEDENCIA");
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.parseLong(id_consulta_medica));

        cargo = extras.getString("CARGO");
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            et_prescripcion.setEnabled(false);

        }

        //Si va a consultar que muestre los datos
        if(presedencia.equals("consultar")) {
            et_prescripcion.setText(consultaMedica.getPrescripcion());
            btn_guardar.setText("Editar");
        }

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPreescripcion();
            }
        });

        return view;
    }

    private void guardarPreescripcion() {
        String preescripcion = et_prescripcion.getText().toString();
        if(preescripcion.equals("")){
            Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
        }else {
            //consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta

                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
            }
            consultaMedica.setPrescripcion(preescripcion);
            consultaMedica.save();

            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
    }

}
