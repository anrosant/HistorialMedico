package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class AdapterItemsAtencionEnfermeria extends BaseAdapter {

    private Activity activity;
    private ArrayList<AtencionEnfermeria> items;

    public AdapterItemsAtencionEnfermeria(Activity activity, ArrayList<AtencionEnfermeria> data) {
        this.activity = activity;
        items = data;
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
            v = Objects.requireNonNull(inf).inflate(R.layout.item_atencion_enfermeria, null);
        }

        TextView fechaAtencion = v.findViewById(R.id.tvFechaAtencion);
        TextView idAtencion = v.findViewById(R.id.tvIdAtencion);
        TextView motivoAtencion = v.findViewById(R.id.tvMotivo);


        String DateToStr;
        AtencionEnfermeria atenciones = items.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //Se obtiene la fecha, se le da el formato del simpleDateFormat y se lo setea al holder
        DateToStr = format.format(atenciones.getFecha_atencion());


        fechaAtencion.setText(DateToStr);
        idAtencion.setText(atenciones.getId().toString());
        motivoAtencion.setText(atenciones.getMotivoAtencion());

        return v;
    }
}
