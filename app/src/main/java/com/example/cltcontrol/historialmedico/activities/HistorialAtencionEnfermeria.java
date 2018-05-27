package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;

import java.util.ArrayList;
import java.util.List;

public class HistorialAtencionEnfermeria extends FragmentActivity implements ComunicadorMenu {

    String idEmpleado;
    public List<AtencionEnfermeria> atencionEnfermeriaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        Intent inEmpleado = getIntent();
        idEmpleado = inEmpleado.getStringExtra("CEDULA");
        ListView lv = (ListView) findViewById(R.id.lvAtencionEnfermeria);


        atencionEnfermeriaList = AtencionEnfermeria.find(AtencionEnfermeria.class,"cedula_empleado = ?", idEmpleado);

        AdapterItemsAtencionEnfermeria adapter = new AdapterItemsAtencionEnfermeria(this, (ArrayList<AtencionEnfermeria>) atencionEnfermeriaList);
        lv.setAdapter(adapter);
    }


    @Override
    public void menuPulsado(int opcionMenu) {
        Intent inMenu = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        startActivity(inMenu);
    }

    public void aperturaAtencionMedica(View v){
        menuPulsado(0);
    }

}
