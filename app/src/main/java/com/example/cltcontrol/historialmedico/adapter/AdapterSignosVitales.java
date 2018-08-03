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
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import java.text.SimpleDateFormat;
import java.util.List;


public class AdapterSignosVitales extends ArrayAdapter<SignosVitales> {
    private final List<SignosVitales> signosVitalesList;

    public AdapterSignosVitales(Context context, List<SignosVitales> signosVitalesList) {
            super(context, 0, signosVitalesList);
        this.signosVitalesList = signosVitalesList;
    }

    /*
     * Retorna el número de SignosVitales en la lista
     * */
    @Override
    public int getCount() {
        return signosVitalesList.size();
    }

    /*
     * Retorna un SignoVital dentro de la lista dada una posición
     * */
    @Override
    public SignosVitales getItem(int position) {
        return signosVitalesList.get(position);
    }

    /*
     * Retorna la posicion de un SignoVital
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_row_signos_vitales, parent, false);

            /*LayoutInflater inf = (LayoutInflater) Objects.requireNonNull(activity.getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_signos_vitales, null);*/
        }

        SignosVitales signosVitales = signosVitalesList.get(position);

        TextView etPresionSistolica = v.findViewById(R.id.etPresionSistolica);
        TextView etPresionDistolica = v.findViewById(R.id.etPresionDistolica);
        TextView etPulso = v.findViewById(R.id.etPulso);
        TextView etTemperatura = v.findViewById(R.id.etTemperatura);
        TextView etFecha = v.findViewById(R.id.etFecha);

        //Muestra los valores
        etPresionSistolica.setText(String.valueOf(signosVitales.getPresion_sistolica()));
        etPresionDistolica.setText(String.valueOf(signosVitales.getPresion_distolica()));
        etPulso.setText(String.valueOf(signosVitales.getPulso()));
        etTemperatura.setText(String.valueOf(signosVitales.getTemperatura()));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDate =  new SimpleDateFormat("EEEE dd MMMM, yyyy");
        try{
            String strFecha = simpleDate.format(signosVitales.getFecha());
            etFecha.setText(String.valueOf(strFecha));
        }catch (Exception e){
            e.getMessage();
        }





        //Desabilita la edicion
        etPresionSistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etPulso.setEnabled(false);
        etTemperatura.setEnabled(false);

        return v;
    }

    /*
     * Actualiza la lista de SignosVitales cuando ya ha almacenado una
     * */
    public void actualizarSignosVitalesList(List<SignosVitales> signosVitalesListNuevo) {
        this.signosVitalesList.clear();
        this.signosVitalesList.addAll(signosVitalesListNuevo);
        notifyDataSetChanged();
    }
}

