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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosticoEnfermeriaFragment extends Fragment {

    private String idAtencion=null;
    private String idAtencion2=null;
    private String presedencia;
    private Bundle bun;
    private Button boton;
    private EditText etDiagnostico;



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

        if(presedencia.equals("consultar")){//funcionalidad para cargar un signo vital con el id de Atencion
            Toast.makeText(getContext(),"Viniste a consultar",Toast.LENGTH_SHORT).show();
            AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
            etDiagnostico.setText(atencion.getDiagnosticoEnfermeria());

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion));
                    atencion.setDiagnosticoEnfermeria(etDiagnostico.getText().toString());
                    try{
                        atencion.save();
                        Toast.makeText(getContext(),"Diagnostico Guardado",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{//funcionalidad para guardar un signo vital nuevo que fue creado en la ventana contenedora AtencionEnfermeriaActivity
            // al presionar el boton + en historial de Atencion Medica
            Toast.makeText(getContext(),"Viniste a crear",Toast.LENGTH_SHORT).show();
            idAtencion2 = bun.getString("ID_ATENCION2");

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AtencionEnfermeria atencion = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAtencion2));
                    atencion.setDiagnosticoEnfermeria(etDiagnostico.getText().toString());
                    try{
                        atencion.save();
                        Toast.makeText(getContext(),"Diagnostico Guardado",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        return view;
    }

}