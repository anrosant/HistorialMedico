package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import java.util.ArrayList;

/**
 * Created by jorge on 7/1/2018.
 */

class EnfermedadesAdapter extends ArrayAdapter<Enfermedad> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Enfermedad> data=null;

    public EnfermedadesAdapter(Context context, int layaoutResouceId, ArrayList<Enfermedad> data) {
        super(context,layaoutResouceId,data);

        this.context=context;
        this.layoutResourceId=layaoutResouceId;
        this.data=data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row =convertView;
        EnfermedadHolder holder = null;

        if (row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId,parent,false);
            holder = new EnfermedadHolder();
            holder.nombre = row.findViewById(R.id.tvNombre);
            holder.codigo = row.findViewById(R.id.tvCodigo);
            row.setTag(holder);


        }else{
            holder=(EnfermedadHolder)row.getTag();
        }

        Enfermedad enfermedades = data.get(position);
        holder.nombre.setText(enfermedades.getNombre());
        holder.codigo.setText(enfermedades.getCodigo());

        return row;
    }

    static class EnfermedadHolder{
        TextView nombre;
        TextView codigo;
    }
}
