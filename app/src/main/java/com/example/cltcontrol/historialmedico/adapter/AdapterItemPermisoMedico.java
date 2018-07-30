package com.example.cltcontrol.historialmedico.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterItemPermisoMedico extends ArrayAdapter<PermisoMedico>{

    private Activity activity;
    private List<PermisoMedico> permisoMedicoList;
    private final Context context;
    SimpleDateFormat simpleDateFormat;

    public AdapterItemPermisoMedico(Context context, List<PermisoMedico> permisoMedicoList) {
        super(context, 0, permisoMedicoList);
        this.context = context;
        this.permisoMedicoList = permisoMedicoList;
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getCount() {
        return permisoMedicoList.size();
    }

    /*
     * Retorna un PermisoMedico dentro de la lista dada una posición
     * */
    @Override
    public PermisoMedico getItem(int position) {
        return permisoMedicoList.get(position);
    }

    /*
     * Retorna la posicion de una PermisoMedico
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_permiso_medico, parent, false);
        }

        PermisoMedico permisoMedico = permisoMedicoList.get(position);
        TextView tvfechaPermiso = v.findViewById(R.id.tvFechaPermiso);

        simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date fecha_consulta = simpleDateFormat.parse(permisoMedico.getFecha_inicio().toString());
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tvfechaPermiso.setText(simpleDateFormat.format(fecha_consulta));
            tvfechaPermiso.setEnabled(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
    }

    /*
     * Actualiza la lista de Permiso Medico cuando ya ha almacenado una
     * */
    public void actualizarPermisoMedicoList(List<PermisoMedico> permisoMedicoListNuevo) {
        this.permisoMedicoList.clear();
        this.permisoMedicoList.addAll(permisoMedicoListNuevo);
        notifyDataSetChanged();
    }
}