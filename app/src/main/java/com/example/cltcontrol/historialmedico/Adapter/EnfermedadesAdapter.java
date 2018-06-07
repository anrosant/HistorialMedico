package com.example.cltcontrol.historialmedico.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Enfermedad;

import java.util.ArrayList;
import java.util.List;

public class EnfermedadesAdapter extends RecyclerView.Adapter<EnfermedadesAdapter.ViewHolder> {

    private List<Enfermedad> listaEnfermedades;

    public EnfermedadesAdapter(List<Enfermedad> listaEnfermedades) {
        this.listaEnfermedades = listaEnfermedades;
    }

    public List<Enfermedad> getListaEnfermedades() {
        return listaEnfermedades;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enfermedades,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EnfermedadesAdapter.ViewHolder holder, int position) {
        holder.tvNombreCie10items.setText(listaEnfermedades.get(position).getNombre());
        holder.tvCodigoCie10items.setText(listaEnfermedades.get(position).getCodigo());

    }

    @Override
    public int getItemCount() {
        return listaEnfermedades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCie10items, tvCodigoCie10items;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombreCie10items = itemView.findViewById(R.id.tvNombreCie10items);
            tvCodigoCie10items = itemView.findViewById(R.id.tvCodigoCie10items);

        }
    }

    public void setFilter(List<Enfermedad> newList){
        listaEnfermedades = new ArrayList<>();
        listaEnfermedades.addAll(newList);
        notifyDataSetChanged();
    }
}

