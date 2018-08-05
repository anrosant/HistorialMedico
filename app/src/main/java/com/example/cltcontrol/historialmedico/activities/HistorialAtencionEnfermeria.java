package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;
import java.util.List;

public class HistorialAtencionEnfermeria extends FragmentActivity {
    private String idEmpleado,cargo, idAtencion;
    private List<AtencionEnfermeria> atencionEnfermeriaList;
    @SuppressLint("StaticFieldLeak")
    public static AdapterItemAtencionEnfermeria adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        FloatingActionButton btnAgregarAtencionEnfermeria = findViewById(R.id.btnAgregarAtencionEnfermeria);
        ListView lvAtencionEnf = findViewById(R.id.lvAtencionEnfermeria);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        cargo = sesion.obtenerInfoUsuario().get("cargo");

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Obtiene las atenciones de enfermeria de un empleado
        atencionEnfermeriaList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);

        //Crea un adapter de dicha lista y la muestra en un listview
        adapter = new AdapterItemAtencionEnfermeria(this, atencionEnfermeriaList);
        lvAtencionEnf.setAdapter(adapter);

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());


        if(cargo.equalsIgnoreCase("Doctor")){
            btnAgregarAtencionEnfermeria.setVisibility(View.GONE);
        }

        lvAtencionEnf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent atencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfermeriaActivity.class);
                idAtencion = String.valueOf(atencionEnfermeriaList.get(position).getId());

                atencionEnfermeria.putExtra("ID_ATENCION",idAtencion);
                atencionEnfermeria.putExtra("PRECEDENCIA","consultar");
                atencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
                atencionEnfermeria.putExtra("CARGO", cargo);

                startActivity(atencionEnfermeria);
            }
        });
    }
    /*
     * Lleva al usuario a la ventana de AtencionEnfermeriaActivity y envia datos
     * */
    public void aperturaAtencionMedica(View v){
        //Se crea una atencion enfermeria vacia
        AtencionEnfermeria atencionEnfermeria=new AtencionEnfermeria();
        atencionEnfermeria.save();

        Intent inAtencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfermeriaActivity.class);
        inAtencionEnfermeria.putExtra("PRECEDENCIA","crear");
        inAtencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
        inAtencionEnfermeria.putExtra("ID_ATENCION", atencionEnfermeria.getId().toString());
        inAtencionEnfermeria.putExtra("CARGO", cargo);
        startActivity(inAtencionEnfermeria);
    }

}
