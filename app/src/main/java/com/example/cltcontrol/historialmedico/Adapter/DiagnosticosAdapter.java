package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Diagnostico;

import java.util.ArrayList;

/**
 * Created by jorge on 7/1/2018.
 */

public class DiagnosticosAdapter extends ArrayAdapter<Diagnostico> {

    Context context;
    int layoutResourceId;
    ArrayList<Diagnostico> data=null;

    public DiagnosticosAdapter(Context context, int layaoutResouceId, ArrayList<Diagnostico> data) {
        super(context,layaoutResouceId,data);

        this.context=context;
        this.layoutResourceId=layaoutResouceId;
        this.data=data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row =convertView;
        DiagnosticoHolder holder = null;

        if (row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutResourceId,parent,false);
            holder = new DiagnosticoHolder();
            holder.enfermedad = (TextView)row.findViewById(R.id.tvEnfermedad);
            holder.codigo = (TextView)row.findViewById(R.id.tvCodigo);
            holder.tipoEnfermedad = (TextView)row.findViewById(R.id.tvTipoEnfermedad);
            row.setTag(holder);


        }else{
            holder=(DiagnosticoHolder)row.getTag();
        }

        Diagnostico diagnosticos = data.get(position);
        holder.enfermedad.setText(diagnosticos.getEnfermedad());
        holder.codigo.setText(diagnosticos.getCodigo());
        holder.tipoEnfermedad.setText(diagnosticos.getTipoEnfermedad());

        return row;
    }

    static class DiagnosticoHolder{
        TextView enfermedad;
        TextView codigo;
        TextView tipoEnfermedad;
    }
}
