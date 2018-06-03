package com.example.cltcontrol.historialmedico.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdapterSignosVitales extends ArrayAdapter<SignosVitales> {
    private final Context context;
    private List<SignosVitales> signosVitalesList;
    private SignosVitalesFragment activity;



    public AdapterSignosVitales(Context context, List<SignosVitales> signosVitalesList) {
            super(context, 0, signosVitalesList);
            this.context = context;
            this.signosVitalesList = signosVitalesList;
    }

    @Override
    public int getCount() {
        return signosVitalesList.size();
    }

    @Override
    public SignosVitales getItem(int position) {
        return signosVitalesList.get(position);
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_signos_vitales, parent, false);

            /*LayoutInflater inf = (LayoutInflater) Objects.requireNonNull(activity.getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_signos_vitales, null);*/
        }

        SignosVitales signosVitales = signosVitalesList.get(position);

        TextView etPresionSistolica = v.findViewById(R.id.etPresionSistolica);
        TextView etPresionDistolica = v.findViewById(R.id.etPresionDistolica);
        TextView etPulso = v.findViewById(R.id.etPulso);
        TextView etTemperatura = v.findViewById(R.id.etTemperatura);

        //Muestra los valores
        etPresionSistolica.setText(String.valueOf(signosVitales.getPresion_sistolica()));
        etPresionDistolica.setText(String.valueOf(signosVitales.getPresion_distolica()));
        etPulso.setText(String.valueOf(signosVitales.getPulso()));
        etTemperatura.setText(String.valueOf(signosVitales.getTemperatura()));

        //Desabilita la edicion
        etPresionSistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etPulso.setEnabled(false);
        etTemperatura.setEnabled(false);

        return v;
    }

    public void actualizarSignosVitalesList(List<SignosVitales> signosVitalesListNuevo) {
        this.signosVitalesList.clear();
        this.signosVitalesList.addAll(signosVitalesListNuevo);
        notifyDataSetChanged();
    }
}

        /*
    private SignosVitalesFragment activity;
    private ArrayList<SignosVitales> items;

    public AdapterSignosVitales(SignosVitalesFragment activity, ArrayList<SignosVitales> signosVitalesArrayList) {
        this.activity = activity;
        items = signosVitalesArrayList;
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
            LayoutInflater inf = (LayoutInflater) Objects.requireNonNull(activity.getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(inf).inflate(R.layout.item_signos_vitales, null);
        }

        SignosVitales signosVitales = items.get(position);

        TextView etPresionSistolica = v.findViewById(R.id.etPresionSistolica);
        TextView etPresionDistolica = v.findViewById(R.id.etPresionDistolica);
        TextView etPulso = v.findViewById(R.id.etPulso);
        TextView etTemperatura = v.findViewById(R.id.etTemperatura);

        //Desabilita la edicion
        etPresionSistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etPresionDistolica.setEnabled(false);
        etTemperatura.setEnabled(false);

        //Muestra los valores
        etPresionSistolica.setText(String.valueOf(signosVitales.getPresion_sistolica()));
        etPresionDistolica.setText(String.valueOf(signosVitales.getPresion_distolica()));
        etPulso.setText(String.valueOf(signosVitales.getPulso()));
        etTemperatura.setText(String.valueOf(signosVitales.getTemperatura()));

        return v;
    }

    public void actualizarSignosVitalesList(ArrayList<SignosVitales> listaNuevaSignosVitales) {
        items.clear();
        items.addAll(listaNuevaSignosVitales);
        Log.d("Here actualizar", String.valueOf(items.size()));
        this.notifyDataSetChanged();
    }
}*/
