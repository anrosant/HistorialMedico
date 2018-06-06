package com.example.cltcontrol.historialmedico.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cltcontrol.historialmedico.R;

import java.util.Objects;

public class PatologiasPersonalesFragment extends Fragment {

    public PatologiasPersonalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patologias_personales, container, false);

        Spinner spPatologias = view.findViewById(R.id.spPatologia);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()).getBaseContext(), R.array.patologias, android.R.layout.simple_spinner_dropdown_item);
        spPatologias.setAdapter(adapter);

        return view;
    }

}
