package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class MenuEmpleadoActivity extends FragmentActivity {

    TextView tvNombresEmpleado;
    String idEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        Intent inCedula = getIntent();
        idEmpleado= inCedula.getStringExtra("CEDULA");
        //Toast.makeText(this, ""+idEmpleado,Toast.LENGTH_SHORT).show();

        List<Empleado> empleado = Empleado.find(Empleado.class, "CEDULA = ?", idEmpleado);
        tvNombresEmpleado.setText(empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());
    }

    public void aperturaHistorialConsultaMedica(View v) {
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        inHistorialConsultaMedica.putExtra("CEDULA", idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }

    public void aperturaHistorialAtencionEnfermeria(View v){
        Intent inHistorialAtencionEnfermeria = new Intent(getApplicationContext(), HistorialAtencionEnfermeria.class);
        inHistorialAtencionEnfermeria.putExtra("CEDULA",idEmpleado);
        startActivity(inHistorialAtencionEnfermeria);
    }
}
