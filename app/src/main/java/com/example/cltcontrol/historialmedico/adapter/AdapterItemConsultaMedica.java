package com.example.cltcontrol.historialmedico.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.util.List;

public class AdapterItemConsultaMedica extends ArrayAdapter<ConsultaMedica> {
    private Activity activity;
    private List<ConsultaMedica> consultaMedicasList;
    private final Context context;

    public AdapterItemConsultaMedica(Context context, List<ConsultaMedica> consultaMedicaList) {
        super(context, 0, consultaMedicaList);
        this.context = context;
        this.consultaMedicasList = consultaMedicaList;
    }

    @Override
    public int getCount() {
        return consultaMedicasList.size();
    }

    @Override
    public ConsultaMedica getItem(int position) {
        return consultaMedicasList.get(position);
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_consulta_medica, parent, false);

            /*LayoutInflater inf = (LayoutInflater) Objects.requireNonNull(activity.getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_signos_vitales, null);*/
        }

        ConsultaMedica consultaMedica = consultaMedicasList.get(position);

        TextView fechaConsulta = v.findViewById(R.id.tvFechaConsulta);


        //Muestra los valores

        fechaConsulta.setText(consultaMedica.getFechaConsulta().toString());
        //Desabilita la edicion
        fechaConsulta.setEnabled(false);

        return v;
    }

    public void actualizarConsultaMedicaList(List<ConsultaMedica> consultaMedicaListNuevo) {
        this.consultaMedicasList.clear();
        this.consultaMedicasList.addAll(consultaMedicaListNuevo);
        notifyDataSetChanged();
    }
    /*public AdapterItemConsultaMedica(Activity activity, ArrayList<ConsultaMedica> listaConsultaMedica) {
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
    }*/
}
