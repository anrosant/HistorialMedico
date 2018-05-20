package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import java.util.ArrayList;

public class HistorialConsultaMedica extends AppCompatActivity {

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
}
