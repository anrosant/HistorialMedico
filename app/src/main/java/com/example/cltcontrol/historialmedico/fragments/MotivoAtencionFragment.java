package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.Date;
import java.util.Objects;

public class MotivoAtencionFragment extends Fragment {
    private EditText etMotivoAtencion;
    private Button btn_guardar;
    private String id_consulta_medica, id_atencion_enfermeria, presedencia, id_empleado;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;
    //private AtencionEnfermeria atencionEnfermeria;

    public MotivoAtencionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motivo_atencion, container, false);

        etMotivoAtencion = view.findViewById(R.id.txt_motivo);
        btn_guardar = view.findViewById(R.id.btnGuardar);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        //Recibe el id de consulta medica desde Historial de consulta medica
        id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        presedencia = extras.getString("PRESEDENCIA");
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        //id_atencion_enfermeria = extras.getString("ID_ATENCION_ENFERMERIA");

        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        if(presedencia.equals("consultar")){
            etMotivoAtencion.setText(consultaMedica.getMotivo());
            btn_guardar.setText("Editar");
        }
        //consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarMotivo();
            }
        });
        return view;
    }

    //Verifica si ha ingresado texto y guarda en consulta medica, caso contrario imprime un mensaje
    private void guardarMotivo() {
        String motivo = etMotivoAtencion.getText().toString();
        if(motivo.equals("")) {
            Toast.makeText(getContext(), "No ha ingresado nada", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta
                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
            }
                consultaMedica.setMotivo(motivo);
                consultaMedica.save();
                Toast.makeText(getContext(),"Se ha guardado con éxito", Toast.LENGTH_SHORT).show();

        }
    }
}