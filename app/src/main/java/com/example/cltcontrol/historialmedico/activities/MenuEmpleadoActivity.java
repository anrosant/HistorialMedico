package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

public class MenuEmpleadoActivity extends FragmentActivity {

    private String idEmpleado;
    private Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        //Recibe la cedula del empleado desde BuscarEmpleadoActivity
        Intent inBuscarEmpleadoActivity = getIntent();
        idEmpleado = inBuscarEmpleadoActivity.getStringExtra("ID_EMPLEADO");

        //Busca al empleado por su cedula y muestra en un fragment los datos
        empleado = Empleado.findById(Empleado.class,Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());
    }
    /*
     * Lleva a la ventana de HistorialConsultaMedica y envia el id del empleado
     * */
    public void aperturaHistorialConsultaMedica(View v) {

        //Envia el id del empleado a HistorialConsultaMedica
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        //inHistorialConsultaMedica.putExtra("CEDULA", cedulaEmpleado);
        inHistorialConsultaMedica.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }
    /*
     * Lleva a la ventana de HistorialAtencionEnfermeria y envia el id del empleado
     * */
    public void aperturaHistorialAtencionEnfermeria(View v){
        //Envia el id del empleado a HistorialAtencionEnfermeria
        Intent inHistorialAtencionEnfermeria = new Intent(getApplicationContext(), HistorialAtencionEnfermeria.class);
        //inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inHistorialAtencionEnfermeria.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inHistorialAtencionEnfermeria);
    }

    public void aperturaSignos(View v){
        //Envia el id del empleado a Signos Vitales
        Intent inSignos = new Intent(getApplicationContext(), SigVitalRapidoActivity.class);
        //inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inSignos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inSignos);
    }
}
