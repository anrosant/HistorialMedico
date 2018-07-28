package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterPatologiasFamiliares;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PatologiasFamiliares;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatologiasFamiliaresFragment extends Fragment {

    private ListView lvPatologiasFamiliares;
    private View view;
    private String id_empleado;
    private List<PatologiasFamiliares> patologiasFamiliaresList;
    private AdapterPatologiasFamiliares adapterPatologiaFamiliares;
    private Empleado empleado;

    public PatologiasFamiliaresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_patologias_familiares, container, false);
        lvPatologiasFamiliares = view.findViewById(R.id.lvPatologiasFamiliares);

        //Obtiene datos desde desde Historial de consulta medica
        final Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) {
            id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            //Obtenemos la lista de patologias personales que existan
            patologiasFamiliaresList = PatologiasFamiliares.find(PatologiasFamiliares.class, "idficha = ?", String.valueOf(empleado.getFicha_actual()));

            //Crea un adapter de dicha lista y la muestra en un listview
            adapterPatologiaFamiliares = new AdapterPatologiasFamiliares(getContext(), patologiasFamiliaresList);
            lvPatologiasFamiliares.setAdapter(adapterPatologiaFamiliares);
        }
        return view;

    }

    /*
     * Función que actualiza la lista de las patologías personales
     * */
    private void actualizarPatologiasPersonales(){
        ArrayList<PatologiasFamiliares> patFamList = (ArrayList<PatologiasFamiliares>) PatologiasFamiliares.find(PatologiasFamiliares.class,
                "idficha = ?", String.valueOf(empleado.getFicha_actual()));
        adapterPatologiaFamiliares.actualizarPatologiasFamiliaresList(patFamList);
    }

}