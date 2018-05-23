package com.example.cltcontrol.historialmedico.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuConsultaMedicaFragment extends Fragment {

    private final int[] BOTONES_MENU={R.id.btnsignosvitales, R.id.btnprescripcion};
    private int boton;

    public MenuConsultaMedicaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_consulta_medica, container, false);

        boton = -1;
        if(getArguments()!= null){
            boton=getArguments().getInt("BOTONPULSADO");
        }

        Button botonMenu;
        for(int i=0; i<BOTONES_MENU.length; i++){
            botonMenu = view.findViewById(BOTONES_MENU[i]);

            final int queBoton = i;
            botonMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity estaActividad = getActivity();
                    ((ComunicadorMenu)estaActividad).menuPulsado(queBoton);
                }
            });
        }

        return view;
    }
}
