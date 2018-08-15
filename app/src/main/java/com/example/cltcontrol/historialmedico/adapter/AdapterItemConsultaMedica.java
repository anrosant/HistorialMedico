package com.example.cltcontrol.historialmedico.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterItemConsultaMedica extends ArrayAdapter<ConsultaMedica> {
    private final List<ConsultaMedica> consultaMedicasList;
    private final List<Diagnostico> diagnosticoList;

    public AdapterItemConsultaMedica(Context context, List<ConsultaMedica> consultaMedicaList, List<Diagnostico> diagnosticoList) {
        super(context, 0, consultaMedicaList);
        this.consultaMedicasList = consultaMedicaList;
        this.diagnosticoList = diagnosticoList;
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getCount() {
        return consultaMedicasList.size();
    }

    /*
     * Retorna una ConsultaMedica dentro de la lista dada una posición
     * */
    @Override
    public ConsultaMedica getItem(int position) {
        return consultaMedicasList.get(position);
    }

    /*
     * Retorna la posicion de una ConsultaMedica
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_consulta_medica, parent, false);
        }

        ConsultaMedica consultaMedica = consultaMedicasList.get(position);
        TextView tvfechaConsulta = v.findViewById(R.id.tvFechaConsulta);
        TextView tvEnfermedad = v.findViewById(R.id.tvEnfermedad);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date fechaConsulta = simpleDateFormat.parse(consultaMedica.getFechaConsulta().toString());
            simpleDateFormat = new SimpleDateFormat("yyyy-dd-mm");
            tvfechaConsulta.setText(simpleDateFormat.format(fechaConsulta));
            tvfechaConsulta.setEnabled(false);
            for(int i=0 ; i< diagnosticoList.size() ; i++){
                if(consultaMedica.getId().equals(diagnosticoList.get(i).getConsulta_medica().getId())){
                    tvEnfermedad.setText(diagnosticoList.get(i).getEnfermedad().getNombre());
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
    }
    /*
     * Actualiza la lista de ConsultaMedica cuando ya ha almacenado una
     * */
    public void actualizarConsultaMedicaList(List<ConsultaMedica> consultaMedicaListNuevo) {
        this.consultaMedicasList.clear();
        this.consultaMedicasList.addAll(consultaMedicaListNuevo);
        notifyDataSetChanged();
    }

    public void actualizarDiagnosticoList(List<Diagnostico> diagnosticoListNuevo) {
        this.diagnosticoList.clear();
        this.diagnosticoList.addAll(diagnosticoListNuevo);
        notifyDataSetChanged();
    }
}
