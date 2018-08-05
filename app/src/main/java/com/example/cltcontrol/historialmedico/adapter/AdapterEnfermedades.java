package com.example.cltcontrol.historialmedico.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Enfermedad;

import java.util.ArrayList;
import java.util.List;

public class AdapterEnfermedades extends RecyclerView.Adapter<AdapterEnfermedades.ViewHolder> {

    private List<Enfermedad> listaEnfermedades;

    //Constructor
    public AdapterEnfermedades(List<Enfermedad> listaEnfermedades) {
        this.listaEnfermedades = listaEnfermedades;
    }

    /*
     * Retorna la lista de enfermedades
     * */
    public List<Enfermedad> getListaEnfermedades() {
        return listaEnfermedades;
    }

    /*
     * Rellena la lista con las enfermedades
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_row_enfermedades,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEnfermedades.ViewHolder holder, int position) {
        holder.tvNombreCie10items.setText(listaEnfermedades.get(position).getNombre());
        holder.tvCodigoCie10items.setText(listaEnfermedades.get(position).getCodigo());
    }

    /*
     * Retorna el número de elementos en la lista
     * */
    @Override
    public int getItemCount() {
        return listaEnfermedades.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombreCie10items;
        final TextView tvCodigoCie10items;

        ViewHolder(View itemView) {
            super(itemView);
            tvNombreCie10items = itemView.findViewById(R.id.tvNombreCie10items);
            tvCodigoCie10items = itemView.findViewById(R.id.tvCodigoCie10items);
        }
    }

    /*
     * Setea un nuevo filtro con la lista actual
     * */
    public void setFilter(List<Enfermedad> newList){
        listaEnfermedades = new ArrayList<>();
        listaEnfermedades.addAll(newList);
        notifyDataSetChanged();
    }
}

