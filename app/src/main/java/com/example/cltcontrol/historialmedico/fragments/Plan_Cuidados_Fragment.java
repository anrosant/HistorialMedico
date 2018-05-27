package com.example.cltcontrol.historialmedico.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cltcontrol.historialmedico.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Plan_Cuidados_Fragment extends Fragment {


    public Plan_Cuidados_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan__cuidados_, container, false);
    }

}
