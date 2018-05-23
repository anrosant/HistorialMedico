package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.util.ArrayList;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        Intent inEmpleado = getIntent();
        String id = inEmpleado.getStringExtra("ID_EMPLEADO");

        ArrayList<ConsultaMedica> historialConsultasMedicas = new ArrayList<>();
        historialConsultasMedicas = (ArrayList<ConsultaMedica>) ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", id);

        ListView lv = (ListView) findViewById(R.id.lvConsultasMedicas);

        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, historialConsultasMedicas);
        lv.setAdapter(adapter);
    }


    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(this,ConsultaMedicaActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        startActivity(inMenu);
    }

    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}
