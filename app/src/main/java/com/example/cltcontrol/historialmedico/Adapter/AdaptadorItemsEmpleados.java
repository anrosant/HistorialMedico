package com.example.cltcontrol.historialmedico.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Usuario;

import java.util.ArrayList;

public class AdaptadorItemsEmpleados extends RecyclerView.Adapter<AdaptadorItemsEmpleados.ViewHolder> {

    ArrayList<Usuario> listaEmpleados;

    public AdaptadorItemsEmpleados(ArrayList<Usuario> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buscarempleados_recyclerview, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvnombresitems.setText(listaEmpleados.get(position).getNombre());
        holder.tvareatrabajoitems.setText(listaEmpleados.get(position).getArea_trabajo());
        holder.ivfotoitems.setImageResource(listaEmpleados.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivfotoitems;
        TextView tvnombresitems, tvareatrabajoitems;
        public ViewHolder(View itemView) {
            super(itemView);
            tvnombresitems = (TextView) itemView.findViewById(R.id.tvnombresitems);
            tvareatrabajoitems = (TextView) itemView.findViewById(R.id.tvareatrabajoitems);
            ivfotoitems = (ImageView) itemView.findViewById(R.id.ivfotoitems);
        }
    }

    public void setFilter(ArrayList<Usuario> newList){
        listaEmpleados = new ArrayList<>();
        listaEmpleados.addAll(newList);
        notifyDataSetChanged();
    }
}
