package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu {

    TextView tvNombresEmpleado;
    String idEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        Intent inEmpleado = getIntent();
        idEmpleado = inEmpleado.getStringExtra("ID");

        //anadido estas 2 lineas
        List<Empleado> empleado = Empleado.find(Empleado.class, "ID = ?", idEmpleado);
        tvNombresEmpleado.setText(empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());

        ArrayList<ConsultaMedica> historialConsultasMedicas = new ArrayList<>();
        historialConsultasMedicas = (ArrayList<ConsultaMedica>) ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);

        ListView lv = (ListView) findViewById(R.id.lvConsultasMedicas);
        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, historialConsultasMedicas);
        lv.setAdapter(adapter);
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("ID",idEmpleado);
        //Toast.makeText(this, ""+idEmpleado,Toast.LENGTH_SHORT).show();
        startActivity(inMenu);
    }

    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}