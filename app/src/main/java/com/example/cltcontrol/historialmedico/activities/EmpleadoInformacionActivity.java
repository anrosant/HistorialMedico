package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;

public class EmpleadoInformacionActivity extends AppCompatActivity {
    TextView tvId;
    String idEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_informacion);
        tvId = findViewById(R.id.tvIdEmp);

        Intent i = getIntent();
        idEmpleado = i.getStringExtra("ID");

        tvId.setText(idEmpleado);

    }

/*
    public void verHistorialConsultaMedia(View v ){
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        inHistorialConsultaMedica.putExtra("ID_EMPLEADO",idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }*/
}
