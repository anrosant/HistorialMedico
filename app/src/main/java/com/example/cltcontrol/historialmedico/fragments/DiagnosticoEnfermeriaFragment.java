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
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosticoEnfermeriaFragment extends Fragment {

    private String idAtencion=null;
    //private String idAtencion2=null;
    private String precedencia, idEmpleado,cargo;
    private Bundle bun;
    private Button boton;
    private EditText etDiagnostico;
    private Empleado empleado;
    private AtencionEnfermeria atencion;

    public DiagnosticoEnfermeriaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_diagnotico_enfermeria, container, false);
        boton = view.findViewById(R.id.btnGuardar);
        etDiagnostico = view.findViewById(R.id.txt_prescripcion);

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        idAtencion = bun.getString("ID_ATENCION");
        precedencia = bun.getString("PRECEDENCIA");
        idEmpleado = bun.getString("ID_EMPLEADO");
        cargo = bun.getString("CARGO");

        atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        //Si va a consultar, muestra los datos
        if(precedencia.equals("consultar")) {
            etDiagnostico.setText(atencion.getDiagnosticoEnfermeria());
            boton.setText("Editar");
        }
        if(cargo.equals("Doctor")){
            boton.setVisibility(View.GONE);
            etDiagnostico.setEnabled(false);
        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diagnostico = etDiagnostico.getText().toString();
                int res = atencion.validarCampoTexto(diagnostico);
                if(res == 0)
                    Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
                else if(res == 1){
                    Toast.makeText(getContext(),"Ha ingresado solo numeros",Toast.LENGTH_SHORT).show();
                }else {
                    //Si no ha creado la atención enfermería, la crea y agrega los datos
                    if (atencion.getEmpleado() == null) {
                        atencion.setEmpleado(empleado);
                        atencion.setFecha_atencion(new Date());
                    }
                    atencion.setDiagnosticoEnfermeria(diagnostico);
                    atencion.save();
                    Toast.makeText(getContext(), "Diagnostico Guardado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}