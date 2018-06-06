package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.cltcontrol.historialmedico.R;

import java.util.Objects;

public class PatologiasPersonalesFragment extends Fragment {

    private ImageButton ib_mostrar_ocultar_contendido;
    private LinearLayout ly_patologias_personales;
    public PatologiasPersonalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patologias_personales, container, false);

        ly_patologias_personales = view.findViewById(R.id.ly_patologias_personales);
        ib_mostrar_ocultar_contendido = view.findViewById(R.id.ib_mostrar_ocultar_contendido);

        Spinner spPatologias = view.findViewById(R.id.spPatologia);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()).getBaseContext(), R.array.patologias, android.R.layout.simple_spinner_dropdown_item);
        spPatologias.setAdapter(adapter);


        ib_mostrar_ocultar_contendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ly_patologias_personales.isShown()){
                    ly_patologias_personales.setVisibility(view.VISIBLE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_arriba);
                }else {
                    ly_patologias_personales.setVisibility(view.GONE);
                    ib_mostrar_ocultar_contendido.setImageResource(R.drawable.flecha_abajo);
                }
            }
        });

        return view;
    }

}
