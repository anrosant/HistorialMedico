package com.example.cltcontrol.historialmedico.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.EnfermedadesAdapter;
import com.example.cltcontrol.historialmedico.Adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Enfermedad;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticoFragment extends Fragment {

    public static List<Enfermedad> enfermedadList;
    private RecyclerView recyclerEnfermedades;
    private EnfermedadesAdapter adaptadorEnfermedades;
    private EditText etBuscarEnfermedades;

    public DiagnosticoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnostico, container, false);

        readEnfermedadesAll();
        recyclerEnfermedades = view.findViewById(R.id.rvListaEnfermedades);
        recyclerEnfermedades.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        etBuscarEnfermedades = view.findViewById(R.id.etBuscarEnfermedades);

        etBuscarEnfermedades.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int star,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int star,
                                      int count, int after) {

                String newTest;
                if(charSequence.length() != 0){
                    newTest = etBuscarEnfermedades.getText().toString().toLowerCase();
                    List<Enfermedad> newList = new ArrayList<>();
                    for (Enfermedad enfermedad:enfermedadList){
                        String nombre = enfermedad.getNombre().toLowerCase();
                        if(nombre.contains(newTest)){
                            newList.add(enfermedad);
                        }
                    }

                    adaptadorEnfermedades.setFilter((List<Enfermedad>) newList);
                }else{
                    adaptadorEnfermedades.setFilter((List<Enfermedad>) enfermedadList);
                }
            }
        });

        adaptadorEnfermedades = new EnfermedadesAdapter(enfermedadList);
        recyclerEnfermedades.setAdapter(adaptadorEnfermedades);

        recyclerEnfermedades.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerEnfermedades, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), ""+enfermedadList.get(position).getNombre(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        return  view;
    }

    public void readEnfermedadesAll(){
        try{
            enfermedadList = Enfermedad.listAll(Enfermedad.class);
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
