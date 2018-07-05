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
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterItemAtencionEnfermeria extends ArrayAdapter<AtencionEnfermeria> {

    private Activity activity;
    private List<AtencionEnfermeria> atencionEnfermeriaList;
    private final Context context;

    public AdapterItemAtencionEnfermeria(Context context, List<AtencionEnfermeria> atencionEnfermeriaList) {
        super(context, 0, atencionEnfermeriaList);
        this.context = context;
        this.atencionEnfermeriaList = atencionEnfermeriaList;
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getCount() {
        return atencionEnfermeriaList.size();
    }
    /*
     * Retorna una AtencionEnfermeria dentro de la lista dada una posición
     * */
    @Override
    public AtencionEnfermeria getItem(int position) {
        return atencionEnfermeriaList.get(position);
    }

    /*
     * Retorna la posicion de una AtencionEnfermeria
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_atencion_enfermeria, parent, false);
        }

        TextView fechaAtencion = v.findViewById(R.id.tvFechaAtencion);
        TextView motivoAtencion = v.findViewById(R.id.tvMotivo);

        AtencionEnfermeria atencion = atencionEnfermeriaList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //Se obtiene la fecha, se le da el formato del simpleDateFormat y se lo setea al holder
        String DateToStr = format.format(atencion.getFecha_atencion());

        //Muestra los valores
        fechaAtencion.setText(DateToStr);
        motivoAtencion.setText(atencion.getMotivoAtencion());

        //Desabilita la edicion
        fechaAtencion.setEnabled(false);
        motivoAtencion.setEnabled(false);

        return v;
    }
    /*
     * Actualiza la lista de AtencionEnfermeria cuando ya ha almacenado una
     * */
    public void actualizarAtencionEnfermeriaList(List<AtencionEnfermeria> atencionEnfermeriaListNuevo) {
        this.atencionEnfermeriaList.clear();
        this.atencionEnfermeriaList.addAll(atencionEnfermeriaListNuevo);
        notifyDataSetChanged();
    }
}
