package com.example.cltcontrol.historialmedico.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.PatologiasPersonalesFragment;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;

import java.util.List;

public class AdapterPatologiasPersonales extends ArrayAdapter<PatologiasPersonales> {
    private final Context context;
    private List<PatologiasPersonales> patologiasPersonalesList;
    private PatologiasPersonalesFragment activity;

    public AdapterPatologiasPersonales(Context context, List<PatologiasPersonales> patologiasPersonalesList) {
        super(context, 0, patologiasPersonalesList);
        this.context = context;
        this.patologiasPersonalesList = patologiasPersonalesList;
    }

    @Override
    public int getCount() {
        return patologiasPersonalesList.size();
    }

    @Override
    public PatologiasPersonales getItem(int position) {
        return patologiasPersonalesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_patologias_personales, parent, false);
        }

        PatologiasPersonales patologiasPersonales = patologiasPersonalesList.get(position);

        TextView tvLugar = v.findViewById(R.id.tvLugar);
        TextView tvDetalle = v.findViewById(R.id.tvDetalle);

        //Muestra los valores
        tvLugar.setText(patologiasPersonales.getLugar());
        tvDetalle.setText(patologiasPersonales.getDetalle());

        return v;
    }

    public void actualizarPatologiasPersonalesList(List<PatologiasPersonales> patologiasPersonalesListNuevo) {
        this.patologiasPersonalesList.clear();
        this.patologiasPersonalesList.addAll(patologiasPersonalesListNuevo);
        notifyDataSetChanged();
    }
}
