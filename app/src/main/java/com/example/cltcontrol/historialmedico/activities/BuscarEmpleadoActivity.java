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

import com.example.cltcontrol.historialmedico.Adapter.AdaptadorItemsEmpleados;
import com.example.cltcontrol.historialmedico.Adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import java.util.ArrayList;
import java.util.List;

public class BuscarEmpleadoActivity extends FragmentActivity {

    public static List<Empleado> empleadosList;
    RecyclerView recyclerEmpleados;
    AdaptadorItemsEmpleados adaptadorEmpleados;
    EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_empleados);


        readEmpleadosAll();
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
                    List<Empleado> newList = new ArrayList<>();
                    for(Empleado empleado:empleadosList){
                        String nombre = empleado.getNombre().toLowerCase();
                        String area = empleado.getAreaTrabajo().toLowerCase();
                        if(nombre.contains(newText)){
                            newList.add(empleado);
                        }
                        if(area.contains(newText)){
                            newList.add(empleado);
                        }
                    }

                    adaptadorEmpleados.setFilter((List<Empleado>) newList);
                }else{
                    adaptadorEmpleados.setFilter((List<Empleado>) empleadosList);
                }
            }
        });

        adaptadorEmpleados = new AdaptadorItemsEmpleados(empleadosList);
        recyclerEmpleados.setAdapter(adaptadorEmpleados);
        /*recyclerEmpleados.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
                startActivity(i);
            }
        });*/

        recyclerEmpleados.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerEmpleados ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent i = new Intent(getApplicationContext(), MenuEmpleadoActivity.class);
                        //se cambia ID por ID_EMPLEADO
                        i.putExtra("CEDULA",String.valueOf(empleadosList.get(position).getCedula()));
                        startActivity(i);

                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void readEmpleadosAll(){
        try{
            empleadosList = Empleado.listAll(Empleado.class);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void seleccionarEmpleado(View v){
        Intent inSeleccionEmpleado = new Intent(this, MenuEmpleadoActivity.class);
        startActivity(inSeleccionEmpleado);
    }

}
