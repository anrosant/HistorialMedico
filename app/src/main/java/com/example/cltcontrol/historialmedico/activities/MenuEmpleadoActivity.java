package com.example.cltcontrol.historialmedico.activities;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class MenuEmpleadoActivity extends Activity {

    TextView tvNombresEmpleado;
    String idEmpleado,nombresEmpleado;
    /*
    TextView tvId;
    String idEmpleado;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        tvNombresEmpleado = findViewById(R.id.tvnombresempleado);

        Intent inCedula = getIntent();
        idEmpleado= inCedula.getStringExtra("ID");
        List<Empleado> empleado = Empleado.find(Empleado.class, "ID = ?", idEmpleado);
        tvNombresEmpleado.setText(""+empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());

    }

    public void aperturaHistorialConsultaMedica(View v) {
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        inHistorialConsultaMedica.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }

    public void aperturaHistorialAtencionEnfermeria(View v){
        Intent inHistorialAtencionEnfermeria = new Intent(getApplicationContext(), HistorialAtencionEnfermeria.class);
        inHistorialAtencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
        startActivity(inHistorialAtencionEnfermeria);
    }
}
