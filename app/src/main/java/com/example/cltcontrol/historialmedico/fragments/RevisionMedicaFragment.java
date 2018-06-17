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
public class RevisionMedicaFragment extends Fragment {

    private EditText et_revision_medica;
    private Button btn_guardar;
    private String id_consulta_medica, precedencia,id_empleado, cargo;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    public RevisionMedicaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_revision_medica, container, false);
        et_revision_medica = view.findViewById(R.id.et_revision_medica);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        precedencia = extras.getString("PRESEDENCIA");

        //Recibe el id de consulta medica desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

        cargo = extras.getString("CARGO");
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            et_revision_medica.setEnabled(false);
        }

        if(precedencia.equals("consultar")) {
            et_revision_medica.setText(consultaMedica.getRevision_medica());
            btn_guardar.setText("Editar");
        }
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRevisionMedica();
            }
        });

        return view;
    }

    private void guardarRevisionMedica() {
        String revision_medica = et_revision_medica.getText().toString();
        int res = consultaMedica.validarCampoTexto(revision_medica);
        if(res == 0)
            Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
        else if (res == 1)
            Toast.makeText(getContext(),"Ha ingresado solo numeros",Toast.LENGTH_SHORT).show();
        else {
            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta

                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
            }
            consultaMedica.setRevision_medica(revision_medica);
            consultaMedica.save();

            Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
    }

}
