package com.example.cltcontrol.historialmedico.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.cltcontrol.historialmedico.Adapter.AdaptadorItemsEmpleados;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.R;

import java.util.ArrayList;

public class BuscarEmpleadoActivity extends Activity {

    public static ArrayList<Empleado> listaEmpleados;
    RecyclerView recyclerEmpleados;
    AdaptadorItemsEmpleados adaptadorEmpleados;
    EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscarempleados);

        listaEmpleados = new ArrayList<>();
        llenarEmpleados();
        recyclerEmpleados = (RecyclerView) findViewById(R.id.rvlistaempleados);
        recyclerEmpleados.setLayoutManager(new LinearLayoutManager(this));
        buscar = (EditText) findViewById(R.id.etBusquedaUsuario);

        buscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String newText;
                if(s.length() != 0){
                    newText = buscar.getText().toString().toLowerCase();
                    ArrayList<Empleado> newList = new ArrayList<>();
                    for(Empleado empleado:listaEmpleados){
                        String nombre = empleado.getNombre().toLowerCase();
                        String area = empleado.getAreaTrabajo().toLowerCase();
                        if(nombre.contains(newText)){
                            newList.add(empleado);
                        }
                        if(area.contains(newText)){
                            newList.add(empleado);
                        }
                    }

                    adaptadorEmpleados.setFilter(newList);
                }else{
                    adaptadorEmpleados.setFilter(listaEmpleados);
                }
            }
        });



        adaptadorEmpleados = new AdaptadorItemsEmpleados(listaEmpleados);
        recyclerEmpleados.setAdapter(adaptadorEmpleados);
    }

    public void llenarEmpleados(){
        listaEmpleados.add(new Empleado("Renato","Sistemas",R.drawable.modelo));
        listaEmpleados.add(new Empleado("Anni","Finanzas",R.drawable.modelo));
        listaEmpleados.add(new Empleado("Jorge","Marketing",R.drawable.modelo));
        listaEmpleados.add(new Empleado("Daniel","Ingenieria",R.drawable.modelo));
        //Log.d("LISTA", String.valueOf(listaEmpleados.size()));
    }


    public void seleccionarEmpleado(View v){
        Intent inSeleccionEmpleado = new Intent(this, MenuEmpleadoActivity.class);
        startActivity(inSeleccionEmpleado);
    }

}
