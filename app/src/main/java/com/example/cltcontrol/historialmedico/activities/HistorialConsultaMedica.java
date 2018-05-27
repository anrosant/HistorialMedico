package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.util.ArrayList;
import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu{

    TextView tvNombresEmpleado;
    String idEmpleado;
    public List<ConsultaMedica> consultaMedicaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        ListView lvConsultasMedicas = (ListView) findViewById(R.id.lvConsultasMedicas);

        Intent inEmpleado = getIntent();
        idEmpleado = inEmpleado.getStringExtra("CEDULA");

        //anadido estas 2 lineas
        //List<Empleado> empleado = Empleado.find(Empleado.class, "CEDULA = ?", idEmpleado);
        //tvNombresEmpleado.setText(empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());

        consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class,"cedula_empleado = ?", idEmpleado);

        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, (ArrayList<ConsultaMedica>) consultaMedicaList);
        lvConsultasMedicas.setAdapter(adapter);
        /*lvConsultasMedicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultaMedica consultaMedicaItem= (ConsultaMedica) parent.getItemAtPosition(position);
            }
        });*/
    }



    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("CEDULA",idEmpleado);
        startActivity(inMenu);
    }


    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}