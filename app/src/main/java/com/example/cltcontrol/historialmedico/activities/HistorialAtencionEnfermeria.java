package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
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
    private Button btnAgregarAtencionEnfermeria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        btnAgregarAtencionEnfermeria = findViewById(R.id.btnAgregarAtencionEnfermeria);

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

        if(cargo.equals("Doctor")){
            btnAgregarAtencionEnfermeria.setVisibility(View.GONE);
        }
    }


    @Override
    public void menuPulsado(int opcionMenu) {
        //Se crea una atencion enfermeria vacia
        AtencionEnfermeria atencionEnfermeria=new AtencionEnfermeria();
        atencionEnfermeria.save();

        //Envia los datos a AtencionEnfermeriaActivity
        Intent inAtencionEnfermeriaActivity = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
        inAtencionEnfermeriaActivity.putExtra("BOTONPULSADO",opcionMenu);
        inAtencionEnfermeriaActivity.putExtra("ID_EMPLEADO",idEmpleado);
        inAtencionEnfermeriaActivity.putExtra("ID_ATENCION_ENFERMERIA",String.valueOf(atencionEnfermeria.getId()));

        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        startActivity(inAtencionEnfermeriaActivity);
    }

    public void aperturaAtencionMedica(View v){
        menuPulsado(0);
    }

}
