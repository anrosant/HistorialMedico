package com.example.cltcontrol.historialmedico.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterItemPermisosParticulares extends ArrayAdapter<PermisoMedico> {

    private final List<PermisoMedico> permisoMedicoList;
    private final List<Diagnostico> diagnosticoList;

    public AdapterItemPermisosParticulares(Context context, List<PermisoMedico> permisoMedicoList, List<Diagnostico> diagnosticoList) {
        super(context, 0, permisoMedicoList);
        this.permisoMedicoList = permisoMedicoList;
        this.diagnosticoList = diagnosticoList;
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getCount() {
        return permisoMedicoList.size();
    }

    /*
     * Retorna una ConsultaMedica dentro de la lista dada una posición
     * */
    @Override
    public PermisoMedico getItem(int position) {
        return permisoMedicoList.get(position);
    }

    /*
     * Retorna la posicion de una ConsultaMedica
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_permiso_particular, parent, false);
        }

        PermisoMedico permisoMedico = permisoMedicoList.get(position);
        TextView tvEnfermedadPermiso = v.findViewById(R.id.tvEnfermedadPermiso);
        TextView tvFechasPermiso = v.findViewById(R.id.tvFechasPermiso);
        TextView tvDiasPermiso = v.findViewById(R.id.tvDiasPermiso);

        Log.i("AAAA",""+diagnosticoList.size());
        for(int i=0 ; i< diagnosticoList.size() ; i++){
            if(permisoMedico.getId().equals(diagnosticoList.get(i).getPermiso_medico().getId())){
                tvEnfermedadPermiso.setText(diagnosticoList.get(i).getEnfermedad().getNombre());
                break;
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date fechaPermisodesde = simpleDateFormat.parse(permisoMedico.getFecha_inicio().toString());
            Date FechaPermisohasta = simpleDateFormat.parse(permisoMedico.getFecha_fin().toString());
            simpleDateFormat = new SimpleDateFormat("yyyy-dd-mm");
            tvFechasPermiso.setText("Desde "+simpleDateFormat.format(fechaPermisodesde) + " hasta " + simpleDateFormat.format(FechaPermisohasta));
        }catch(ParseException e) {
            e.printStackTrace();
        }
        tvDiasPermiso.setText("Dias "+permisoMedico.getDias_permiso());

        return v;
    }
    /*
     * Actualiza la lista de ConsultaMedica cuando ya ha almacenado una
     * */
    public void actualizarPermisoParticularList(List<PermisoMedico> permisoParticularListNuevo) {
        this.permisoMedicoList.clear();
        this.permisoMedicoList.addAll(permisoParticularListNuevo);
        notifyDataSetChanged();
    }

    public void actualizarDiagnosticoList(List<Diagnostico> diagnosticoListNuevo) {
        this.diagnosticoList.clear();
        this.diagnosticoList.addAll(diagnosticoListNuevo);
        notifyDataSetChanged();
    }
}

