package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class MenuEmpleadoActivity extends FragmentActivity {

    private String cedulaEmpleado, idEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        Intent inBuscarEmpleadoActivity = getIntent();
        cedulaEmpleado = inBuscarEmpleadoActivity.getStringExtra("CEDULA");
        idEmpleado = inBuscarEmpleadoActivity.getStringExtra("ID");

        List<Empleado> empleado = Empleado.find(Empleado.class, "CEDULA = ?", cedulaEmpleado);
        tvNombresEmpleado.setText(empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());
    }

    public void aperturaHistorialConsultaMedica(View v) {
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        inHistorialConsultaMedica.putExtra("CEDULA", cedulaEmpleado);
        inHistorialConsultaMedica.putExtra("ID", idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }

    public void aperturaHistorialAtencionEnfermeria(View v){
        Intent inHistorialAtencionEnfermeria = new Intent(getApplicationContext(), HistorialAtencionEnfermeria.class);
        inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inHistorialAtencionEnfermeria.putExtra("ID", idEmpleado);
        startActivity(inHistorialAtencionEnfermeria);
    }
}
