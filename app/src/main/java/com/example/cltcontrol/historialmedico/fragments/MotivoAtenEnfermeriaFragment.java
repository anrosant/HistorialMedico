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
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MotivoAtenEnfermeriaFragment extends Fragment {

    private String idAtencion=null, presedencia, idEmpleado;
    private Bundle bun;
    private Button boton;
    private EditText etMotivo;
    private Empleado empleado;
    public MotivoAtenEnfermeriaFragment() {
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
        presedencia = bun.getString("PRESEDENCIA");
        idEmpleado = bun.getString("ID_EMPLEADO");

        AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        if(presedencia.equals("consultar")) {
            ////funcionalidad para cargar un signo vital con el id de Atencion
            //Toast.makeText(getContext(),"Viniste a consultar",Toast.LENGTH_SHORT).show();
            etMotivo.setText(atencion.getMotivoAtencion());
            boton.setText("Editar");
        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
                atencion.setMotivoAtencion(etMotivo.getText().toString());
                if(atencion.getEmpleado()==null){
                    atencion.setEmpleado(empleado);
                    atencion.setFecha_atencion(new Date());
                    atencion.save();
                }
                try{
                    atencion.save();
                    Toast.makeText(getContext(),"Motivo Guardado",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //}
        /*else{//funcionalidad para guardar un signo vital nuevo que fue creado en la ventana contenedora AtencionEnfermeriaActivity
            // al presionar el boton + en historial de Atencion Medica
            //Toast.makeText(getContext(),"Viniste a crear",Toast.LENGTH_SHORT).show();
            //idAtencion2 = bun.getString("ID_ATENCION2");

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
                    atencion.setMotivoAtencion(etMotivo.getText().toString());
                    try{
                        atencion.save();
                        Toast.makeText(getContext(),"Motivo Guardado",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }*/

        return view;
    }

}
