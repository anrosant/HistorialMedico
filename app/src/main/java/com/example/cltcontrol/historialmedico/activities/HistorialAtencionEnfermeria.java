package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.Adapter.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class HistorialAtencionEnfermeria extends FragmentActivity {
    private String idEmpleado,cargo, idAtencion;
    private TextView tvNombresEmpleado;
    private FloatingActionButton btnAgregarAtencionEnfermeria;
    private ListView lvAtencionEnf;
    private List<AtencionEnfermeria> atencionEnfermeriaList;
    public static AdapterItemsAtencionEnfermeria adapter;
    private Empleado empleado;
    private SessionManager sesion;
    private Intent inMenuEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        btnAgregarAtencionEnfermeria = findViewById(R.id.btnAgregarAtencionEnfermeria);
        lvAtencionEnf = findViewById(R.id.lvAtencionEnfermeria);

        //Obtener el cargo del usuario que inició sesión
        sesion = new SessionManager(getApplicationContext());
        cargo = sesion.obtenerInfoUsuario().get("cargo");

        //Recibe el id del empleado desde MenuEmpleadoActivity
        inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Obtiene las atenciones de enfermeria de un empleado
        atencionEnfermeriaList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);

        //Crea un adapter de dicha lista y la muestra en un listview
        adapter = new AdapterItemsAtencionEnfermeria(this, atencionEnfermeriaList);
        lvAtencionEnf.setAdapter(adapter);

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());



        if(cargo.equals("Doctor")){
            btnAgregarAtencionEnfermeria.setVisibility(View.GONE);
        }

        lvAtencionEnf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent atencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
                idAtencion = String.valueOf(atencionEnfermeriaList.get(position).getId());

                atencionEnfermeria.putExtra("ID_ATENCION",idAtencion);
                atencionEnfermeria.putExtra("PRESEDENCIA","consultar");
                atencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
                atencionEnfermeria.putExtra("CARGO", cargo);
                startActivity(atencionEnfermeria);
            }
        });
    }

    public void aperturaAtencionMedica(View v){
        //Se crea una atencion enfermeria vacia
        AtencionEnfermeria atencionEnfermeria=new AtencionEnfermeria();
        atencionEnfermeria.save();

        Intent inAtencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
        inAtencionEnfermeria.putExtra("PRESEDENCIA","crear");
        inAtencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
        inAtencionEnfermeria.putExtra("ID_ATENCION", atencionEnfermeria.getId().toString());
        inAtencionEnfermeria.putExtra("CARGO", cargo);
        startActivity(inAtencionEnfermeria);
    }

}
