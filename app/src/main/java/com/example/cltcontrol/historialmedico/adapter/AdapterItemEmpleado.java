package com.example.cltcontrol.historialmedico.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class AdapterItemEmpleado extends RecyclerView.Adapter<AdapterItemEmpleado.ViewHolder> {
    private List<Empleado> listaEmpleados;
    //Constructores
    public AdapterItemEmpleado(){}
    public AdapterItemEmpleado(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buscar_empleados_recyclerview, null, false);
        return new ViewHolder(view);
    }
    /*
     * Retorna la lista de empleados que hay
     * */
    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_nombres_items.setText(listaEmpleados.get(position).getApellido()+" "+listaEmpleados.get(position).getNombre());
        holder.tv_cedula_items.setText(listaEmpleados.get(position).getCedula());
        holder.tv_profesion_items.setText(listaEmpleados.get(position).getProfesion());
        holder.tv_ocupacion_items.setText(listaEmpleados.get(position).getOcupacion());
        holder.ivfotoitems.setImageResource(listaEmpleados.get(position).getFoto());
    }

    /*
     * Retorna el número de empleados en la lista
     * */
    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivfotoitems;
        TextView tv_nombres_items, tv_cedula_items, tv_profesion_items, tv_ocupacion_items;
        ViewHolder(View itemView) {
            super(itemView);
            tv_nombres_items = itemView.findViewById(R.id.tv_nombres_items);
            tv_cedula_items =itemView.findViewById(R.id.tv_cedula_items);
            tv_profesion_items =itemView.findViewById(R.id.tv_profesion_items);
            tv_ocupacion_items = itemView.findViewById(R.id.tv_ocupacion_items);
            ivfotoitems = itemView.findViewById(R.id.ivfotoitems);
        }
    }
    /*
     * Setea la lista de la nueva búsqueda
     * */
    public void setFilter(List<Empleado> newList){
        listaEmpleados = new ArrayList<>();
        listaEmpleados.addAll(newList);
        notifyDataSetChanged();
    }
    /*
     * Valida que los caracteres para realizar la búsque sean válidos
     * */
    public Boolean validarBusqueda(String s){
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(!Character.isLetter(c))
                return false;
        }
        return true;
    }

}