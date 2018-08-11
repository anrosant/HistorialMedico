package com.example.cltcontrol.historialmedico.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Diagnostico;

import java.util.List;

public class AdapterItemDiagnostico extends ArrayAdapter<Diagnostico> {
    private final List<Diagnostico> diagnosticoList;


    public AdapterItemDiagnostico(Context context, List<Diagnostico> diagnosticoList) {
        super(context, 0, diagnosticoList);
        this.diagnosticoList = diagnosticoList;
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getCount() {
        return diagnosticoList.size();
    }

    /*
     * Retorna un Diagnostico dentro de la lista dada una posición
     * */
    @Override
    public Diagnostico getItem(int position) {
        return diagnosticoList.get(position);
    }

    /*
     * Retorna la posicion de un Diagnostico
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_diagnostico, parent, false);
        }

        Diagnostico diagnostico = diagnosticoList.get(position);

        TextView codigoEnfermedad = v.findViewById(R.id.tvCodigoEnfermedad);
        TextView nombreEnfermedad = v.findViewById(R.id.tvNombreEnfermedad);
        TextView tipoEnfermedad = v.findViewById(R.id.tvTipoEnfermedad);

        //Muestra los valores
        codigoEnfermedad.setText(String.valueOf(diagnostico.getEnfermedad().getCodigo()));
        nombreEnfermedad.setText(String.valueOf(diagnostico.getEnfermedad().getNombre()));
        tipoEnfermedad.setText(String.valueOf(diagnostico.getTipo_enfermedad()));

        return v;
    }
    /*
     * Actualiza la lista de Diagnostico cuando ya ha almacenado una
     * */
    public void actualizarDiagnosticoList(List<Diagnostico> diagnosticoListNuevo) {
        this.diagnosticoList.clear();
        this.diagnosticoList.addAll(diagnosticoListNuevo);
        notifyDataSetChanged();
    }
}
