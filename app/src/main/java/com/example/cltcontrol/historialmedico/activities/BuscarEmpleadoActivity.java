package com.example.cltcontrol.historialmedico.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.Adapter.AdaptadorItemsEmpleados;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;

public class BuscarEmpleadoActivity extends Activity {

    public static ArrayList<Empleado> listaEmpleados;
    RecyclerView recyclerEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscarempleados);

        listaEmpleados = new ArrayList<>();
        llenarEmpleados();
        recyclerEmpleados = (RecyclerView) findViewById(R.id.rvlistaempleados);
        recyclerEmpleados.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorItemsEmpleados adaptadorEmpleados = new AdaptadorItemsEmpleados(listaEmpleados);
        recyclerEmpleados.setAdapter(adaptadorEmpleados);
    }

    public void llenarEmpleados(){
        listaEmpleados.add(new Empleado("Renato","Sistemas",R.drawable.usuario));
        listaEmpleados.add(new Empleado("Anni","Sistemas",R.drawable.usuario));
        listaEmpleados.add(new Empleado("Jorge","Sistemas",R.drawable.usuario));
        listaEmpleados.add(new Empleado("Daniel","Sistemas",R.drawable.usuario));
        Log.d("LISTA", String.valueOf(listaEmpleados.size()));
    }

    public void seleccionarEmpleado(View v){
        Intent inSeleccionEmpleado = new Intent(this, MenuEmpleadoActivity.class);
        startActivity(inSeleccionEmpleado);
    }

}
