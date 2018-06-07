package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdapterItemsAtencionEnfermeria extends ArrayAdapter<AtencionEnfermeria> {

    private Activity activity;
    private List<AtencionEnfermeria> atencionEnfermeriaList;
    private final Context context;

    public AdapterItemsAtencionEnfermeria(Context context, List<AtencionEnfermeria> atencionEnfermeriaList) {
        super(context, 0, atencionEnfermeriaList);
        this.context = context;
        this.atencionEnfermeriaList = atencionEnfermeriaList;
    }

    @Override
    public int getCount() {
        return atencionEnfermeriaList.size();
    }

    @Override
    public AtencionEnfermeria getItem(int position) {
        return atencionEnfermeriaList.get(position);
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_atencion_enfermeria, parent, false);

            /*LayoutInflater inf = (LayoutInflater) Objects.requireNonNull(activity.getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_signos_vitales, null);*/
        }

        AtencionEnfermeria atencionEnfermeria = atencionEnfermeriaList.get(position);

        TextView fechaAtencion = v.findViewById(R.id.tvFechaAtencion);
        TextView motivoAtencion = v.findViewById(R.id.tvMotivo);


        String DateToStr;
        AtencionEnfermeria atenciones = atencionEnfermeriaList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //Se obtiene la fecha, se le da el formato del simpleDateFormat y se lo setea al holder
        DateToStr = format.format(atenciones.getFecha_atencion());

        //Muestra los valores
        fechaAtencion.setText(DateToStr);
        motivoAtencion.setText(atenciones.getMotivoAtencion());

        //Desabilita la edicion
        fechaAtencion.setEnabled(false);
        motivoAtencion.setEnabled(false);

        return v;
    }

    public void actualizarAtencionEnfermeriaList(List<AtencionEnfermeria> atencionEnfermeriaListNuevo) {
        this.atencionEnfermeriaList.clear();
        this.atencionEnfermeriaList.addAll(atencionEnfermeriaListNuevo);
        notifyDataSetChanged();
    }
    /*private Activity activity;
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
        //TextView idAtencion = v.findViewById(R.id.tvIdAtencion);
        TextView motivoAtencion = v.findViewById(R.id.tvMotivo);


        String DateToStr;
        AtencionEnfermeria atenciones = items.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //Se obtiene la fecha, se le da el formato del simpleDateFormat y se lo setea al holder
        DateToStr = format.format(atenciones.getFecha_atencion());


        fechaAtencion.setText(DateToStr);
        //idAtencion.setText(atenciones.getId().toString());
        motivoAtencion.setText(atenciones.getMotivoAtencion());

        return v;
    }*/
}
