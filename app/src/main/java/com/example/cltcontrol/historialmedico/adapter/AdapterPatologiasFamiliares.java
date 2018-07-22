package com.example.cltcontrol.historialmedico.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.PatologiasFamiliaresFragment;
import com.example.cltcontrol.historialmedico.models.PatologiasFamiliares;

import java.util.List;

public class AdapterPatologiasFamiliares  extends ArrayAdapter<PatologiasFamiliares> {
    private final Context context;
    private List<PatologiasFamiliares> patologiasFamiliaresList;
    private PatologiasFamiliaresFragment activity;

    public AdapterPatologiasFamiliares(Context context, List<PatologiasFamiliares> patologiasFamiliaresList) {
        super(context, 0, patologiasFamiliaresList);
        this.context = context;
        this.patologiasFamiliaresList = patologiasFamiliaresList;
    }

    /*
     * Retorna el número de PatologiasFamiliares en la lista
     * */
    @Override
    public int getCount() {
        return patologiasFamiliaresList.size();
    }

    /*
     * Retorna una PatologiaFamiliar dentro de la lista dada una posición
     * */
    @Override
    public PatologiasFamiliares getItem(int position) {
        return patologiasFamiliaresList.get(position);
    }

    /*
     * Retorna la posicion de una PatologiaFamiliar
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_patologias_familiares, parent, false);
        }

        PatologiasFamiliares patologiasFamiliares = patologiasFamiliaresList.get(position);

        TextView tvEnfermedad = v.findViewById(R.id.tvEnfermedad);
        TextView tvParentesco = v.findViewById(R.id.tvParentesco);

        //Muestra los valores
        tvEnfermedad.setText(patologiasFamiliares.getEnfermedad().getNombre());
        tvParentesco.setText(patologiasFamiliares.getParentesco());

        return v;
    }

    /*
     * Actualiza la lista de PatologiasFamiliares cuando ya ha almacenado una
     * */
    public void actualizarPatologiasFamiliaresList(List<PatologiasFamiliares> patologiasFamiliaresListNuevo) {
        this.patologiasFamiliaresList.clear();
        this.patologiasFamiliaresList.addAll(patologiasFamiliaresListNuevo);
        notifyDataSetChanged();
    }
}
