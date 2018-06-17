package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemEmpleado;
import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import java.util.ArrayList;
import java.util.List;

public class BuscarEmpleadoActivity extends FragmentActivity {
    private static List<Empleado> empleadosList;
    private AdapterItemEmpleado adaptadorEmpleados;
    private EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_empleados);
        readEmpleadosAll();
        RecyclerView recyclerEmpleados = findViewById(R.id.rvlistaempleados);
        recyclerEmpleados.setLayoutManager(new LinearLayoutManager(this));
        buscar = findViewById(R.id.etBusquedaUsuario);

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = buscar.getText().toString().toLowerCase();
                if(newText.length() != 0 && adaptadorEmpleados.validarBusqueda(newText)){
                    List<Empleado> newList = new ArrayList<>();
                    for(Empleado empleado:empleadosList){
                        String nombre = empleado.getNombre().toLowerCase();
                        String apellido = empleado.getApellido().toLowerCase();
                        if(nombre.contains(newText) || apellido.contains(newText)){
                            newList.add(empleado);
                        }
                    }
                    adaptadorEmpleados.setFilter(newList);
                } else {
                    adaptadorEmpleados.setFilter(empleadosList);
                }
            }
        });

        adaptadorEmpleados = new AdapterItemEmpleado(empleadosList);
        recyclerEmpleados.setAdapter(adaptadorEmpleados);

        recyclerEmpleados.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerEmpleados,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Long id_empleado = adaptadorEmpleados.getListaEmpleados().get(position).getId();
                        //Envia el id del empleado a MenuEmpleadoActivity
                        Intent i = new Intent(getApplicationContext(), MenuEmpleadoActivity.class);
                        i.putExtra("ID_EMPLEADO", String.valueOf(id_empleado));
                        startActivity(i);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void readEmpleadosAll(){
        try{
            empleadosList = Empleado.listAll(Empleado.class);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}