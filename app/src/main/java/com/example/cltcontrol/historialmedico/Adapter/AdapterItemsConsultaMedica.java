package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterItemsConsultaMedica extends BaseAdapter{
    private Activity activity;
    private ArrayList<ConsultaMedica> items;

    public AdapterItemsConsultaMedica(Activity activity, ArrayList<ConsultaMedica> listaConsultaMedica) {
        this.activity = activity;
        items = listaConsultaMedica;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_consulta_medica, null);
        }

        ConsultaMedica consultaMedica = items.get(position);

        TextView fechaConsulta = v.findViewById(R.id.tvFechaConsulta);
        fechaConsulta.setText(consultaMedica.getFechaConsulta().toString());

        return v;
    }
}
