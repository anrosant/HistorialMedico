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
public class MotivoAtencionEnfermeriaFragment extends Fragment {

    private String idAtencion=null, precedencia, idEmpleado, cargo;
    private Bundle bun;
    private Button boton;
    private EditText etMotivo;
    private Empleado empleado;
    private AtencionEnfermeria atencion;
    public MotivoAtencionEnfermeriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_motivo_atencion, container, false);
        boton = view.findViewById(R.id.btnGuardar);
        etMotivo = view.findViewById(R.id.txt_motivo);
        bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        idAtencion = bun.getString("ID_ATENCION");
        precedencia = bun.getString("PRECEDENCIA");
        idEmpleado = bun.getString("ID_EMPLEADO");

        atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        cargo = bun.getString("CARGO");
        if(cargo.equals("Doctor")){
            boton.setVisibility(View.GONE);
            etMotivo.setEnabled(false);

        }

        if(precedencia.equals("consultar")) {
            ////funcionalidad para cargar un signo vital con el id de Atencion
            etMotivo.setText(atencion.getMotivoAtencion());
            boton.setText("Editar");
        }
        /*
         * Guarda los datos de un motivo atención
         * */
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motivo_atencion = etMotivo.getText().toString();
                int res = atencion.validarCampoTexto(motivo_atencion);
                if(res == 0)
                    Toast.makeText(getContext(),"No ha ingresado nada",Toast.LENGTH_SHORT).show();
                else if(res == 1){
                    Toast.makeText(getContext(),"Ha ingresado solo numeros",Toast.LENGTH_SHORT).show();
                }else {
                    //Si atencion enfermería aun no ha sido creada, se crea y se agregan los datos
                    if (atencion.getEmpleado() == null) {
                        atencion.setEmpleado(empleado);
                        atencion.setFecha_atencion(new Date());
                    }
                    atencion.setMotivoAtencion(motivo_atencion);
                    atencion.save();
                    Toast.makeText(getContext(), "Motivo Guardado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}
