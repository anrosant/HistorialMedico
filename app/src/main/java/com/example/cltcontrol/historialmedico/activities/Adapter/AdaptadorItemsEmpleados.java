package com.example.cltcontrol.historialmedico.activities.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;

public class AdaptadorItemsEmpleados extends RecyclerView.Adapter<AdaptadorItemsEmpleados.ViewHolderEmpleados> {

    ArrayList<Empleado> listaEmpleados;

    public AdaptadorItemsEmpleados(ArrayList<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    @Override
    public ViewHolderEmpleados onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buscarempleados_recyclerview, null, false);
        return new ViewHolderEmpleados(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderEmpleados holder, int position) {
        holder.tvnombresitems.setText(listaEmpleados.get(position).getNombre());
        holder.tvareatrabajoitems.setText(listaEmpleados.get(position).getArea());
        holder.ivfotoitems.setImageResource(listaEmpleados.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderEmpleados extends RecyclerView.ViewHolder {
        ImageView ivfotoitems;
        TextView tvnombresitems, tvareatrabajoitems;
        public ViewHolderEmpleados(View itemView) {
            super(itemView);
            tvnombresitems = (TextView) itemView.findViewById(R.id.tvnombresempleado);
            tvareatrabajoitems = (TextView) itemView.findViewById(R.id.tvareatrabajoitems);
            ivfotoitems = (ImageView) itemView.findViewById(R.id.ivfotoitems);
        }
    }
}
