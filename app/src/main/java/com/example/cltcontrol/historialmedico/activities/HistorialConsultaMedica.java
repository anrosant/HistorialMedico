package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu{

    private String idEmpleado;
    private TextView tvNombresEmpleado;
    private ListView lvConsultasMedicas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        lvConsultasMedicas = findViewById(R.id.lvConsultasMedicas);

        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID");

        List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);

        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, (ArrayList<ConsultaMedica>) consultaMedicaList);
        lvConsultasMedicas.setAdapter(adapter);
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());
    }



    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("ID",idEmpleado);
        startActivity(inMenu);
    }


    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}