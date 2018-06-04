package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.Adapter.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class HistorialAtencionEnfermeria extends FragmentActivity implements ComunicadorMenu {
    private String idEmpleado;
    private TextView tvNombresEmpleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        String cargo = sesion.obtenerInfoUsuario().get("cargo");

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID");
        ListView lv = findViewById(R.id.lvAtencionEnfermeria);

        //Obtiene las atenciones de enfermeria de un empleado
        List<AtencionEnfermeria> atencionEnfermeriaList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);

        //Crea un adapter de dicha lista y la muestra en un listview
        AdapterItemsAtencionEnfermeria adapter = new AdapterItemsAtencionEnfermeria(this, (ArrayList<AtencionEnfermeria>) atencionEnfermeriaList);
        lv.setAdapter(adapter);

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());
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
