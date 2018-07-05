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
public class PlanCuidadosFragment extends Fragment {


    private String idAtencion=null, presedencia, idEmpleado, cargo;
    private Bundle bun;
    private Button boton;
    private EditText etPlan;
    private Empleado empleado;
    private AtencionEnfermeria atencion;

    public PlanCuidadosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_plan_cuidados, container, false);
        boton = view.findViewById(R.id.btnGuardar);
        etPlan = view.findViewById(R.id.etPlanCuidados);
        bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        idAtencion = bun.getString("ID_ATENCION");
        presedencia = bun.getString("PRESEDENCIA");
        idEmpleado = bun.getString("ID_EMPLEADO");

        cargo = bun.getString("CARGO");
        if(cargo.equals("Doctor")){
            boton.setVisibility(View.GONE);
            etPlan.setEnabled(false);

        }

        atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        //Si va a consultar, muestra los datos
        if(presedencia.equals("consultar")) {
            etPlan.setText(atencion.getPlanCuidados());
            boton.setText("Editar");
        }

        /*
         * Guarda los datos de un plan de cuidados
         * */
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan_cuidados = etPlan.getText().toString();
                int res = atencion.validarCampoTexto(plan_cuidados);
                if(res == 0)
                    Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
                else if(res == 1){
                    Toast.makeText(getContext(),"Ha ingresado solo numeros",Toast.LENGTH_SHORT).show();
                }else {
                    atencion.setPlanCuidados(plan_cuidados);
                    //Si no ha creado la atención enfermería, la crea y agrega los datos
                    if (atencion.getEmpleado() == null) {
                        atencion.setEmpleado(empleado);
                        atencion.setFecha_atencion(new Date());
                    }
                    atencion.save();
                    Toast.makeText(getContext(), "Plan Guardado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
