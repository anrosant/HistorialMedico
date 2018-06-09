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
    private String presedencia, idEmpleado,cargo;
    private Bundle bun;
    private Button boton;
    private EditText etDiagnostico;
    private Empleado empleado;



    public DiagnosticoEnfermeriaFragment() {

    }

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
        presedencia = bun.getString("PRESEDENCIA");
        idEmpleado = bun.getString("ID_EMPLEADO");
        cargo = bun.getString("CARGO");

        AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        //Si va a consultar, muestra los datos
        if(presedencia.equals("consultar")) {
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
                AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
                if(atencion.getEmpleado()==null){
                    atencion.setEmpleado(empleado);
                    atencion.setFecha_atencion(new Date());
                    atencion.save();
                }
                atencion.setDiagnosticoEnfermeria(etDiagnostico.getText().toString());
                try{
                    atencion.save();
                    Toast.makeText(getContext(),"Diagnostico Guardado",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}