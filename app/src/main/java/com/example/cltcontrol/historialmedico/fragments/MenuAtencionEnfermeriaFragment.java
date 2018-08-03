package com.example.cltcontrol.historialmedico.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuAtencionEnfermeriaFragment extends Fragment {

    private final int[] BOTONES_MENU={R.id.btnSignosVitales, R.id.btnMotivoAtencion, R.id.btnDiagnosticoEnfermeria,
            R.id.btnPlanCuidados};

    public MenuAtencionEnfermeriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_atencion_enfermeria, container, false);

        int boton = -1;
        if(getArguments()!= null){
            boton =getArguments().getInt("BOTONPULSADO");
        }

        Button botonMenu;
        for(int i=0; i<BOTONES_MENU.length; i++){
            botonMenu = view.findViewById(BOTONES_MENU[i]);

            final int queBoton = i;
            botonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity estaActividad = getActivity();
                    ((ComunicadorMenu) Objects.requireNonNull(estaActividad)).menuPulsado(queBoton);
                }
            });
        }

        return view;
    }
}
