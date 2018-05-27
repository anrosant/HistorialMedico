package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;

import java.util.ArrayList;

public class AdapterItemsAtencionEnfermeria extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<AtencionEnfermeria> items;

    public AdapterItemsAtencionEnfermeria(Activity activity, ArrayList<AtencionEnfermeria> atencionEnfermeriaArrayList) {
        this.activity = activity;
        items = atencionEnfermeriaArrayList;
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
            v = inf.inflate(R.layout.item_atencion_enfermeria, null);
        }

        AtencionEnfermeria atencionEnfermeria = items.get(position);

        TextView fechaAtencion = (TextView) v.findViewById(R.id.tvFechaAtencion);
        TextView motivoAtencion = (TextView) v.findViewById(R.id.tvMotivo);

        fechaAtencion.setText(atencionEnfermeria.getFecha_atencion().toString());
        motivoAtencion.setText(atencionEnfermeria.getMotivoAtencion());

        return v;
    }
}
