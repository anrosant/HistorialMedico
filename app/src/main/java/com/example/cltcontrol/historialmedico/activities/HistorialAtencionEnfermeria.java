package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;

import java.util.ArrayList;

public class HistorialAtencionEnfermeria extends FragmentActivity implements ComunicadorMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        Intent inEmpleado = getIntent();
        String id = inEmpleado.getStringExtra("ID_EMPLEADO");

        ArrayList<AtencionEnfermeria> historialAtencionEnfermeria = new ArrayList<>();
        historialAtencionEnfermeria = (ArrayList<AtencionEnfermeria>) AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", id);

        ListView lv = (ListView) findViewById(R.id.lvAtencionEnfermeria);

        AdapterItemsAtencionEnfermeria adapter = new AdapterItemsAtencionEnfermeria(this, historialAtencionEnfermeria);
        lv.setAdapter(adapter);
    }


    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(this,AtencionEnfemeriaActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        startActivity(inMenu);
    }

    public void aperturaAtencionMedica(View v){
        menuPulsado(0);
    }

}
