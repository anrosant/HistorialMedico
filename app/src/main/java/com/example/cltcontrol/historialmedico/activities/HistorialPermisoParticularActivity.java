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

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemPermisosParticulares;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import java.util.List;

public class HistorialPermisoParticularActivity  extends FragmentActivity {

    @SuppressLint("StaticFieldLeak")
    public static AdapterItemPermisosParticulares adapterItemPermisosParticulares;

    private String idEmpleado, idPermisoParticular, cargo;
    private List<PermisoMedico> permisoMedicoParticularList;
    private List<Diagnostico> diagnosticoList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_permiso_particular);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        ListView lvPermisosParticulares = findViewById(R.id.lvPermisosParticulares);
        FloatingActionButton btnAgregarPermisoParticular = findViewById(R.id.btnAgregarPermisoParticular);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        cargo = sesion.obtenerInfoUsuario().get("cargo");
        if(cargo.equalsIgnoreCase("Enfermera")){
            btnAgregarPermisoParticular.setVisibility(View.GONE);
        }
        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Busca los permisos particulares de un empleado
        permisoMedicoParticularList = PermisoMedico.find(PermisoMedico.class, "empleado = ?", idEmpleado);
        for(int i=0; i < permisoMedicoParticularList.size() ; i++ ){
            diagnosticoList = Diagnostico.find(Diagnostico.class,"consultamedica=?","0");
        }

        //Muestra los datos de las permisos particulares en el listview
        adapterItemPermisosParticulares = new AdapterItemPermisosParticulares(this, permisoMedicoParticularList,diagnosticoList);
        lvPermisosParticulares.setAdapter(adapterItemPermisosParticulares);

        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());

        lvPermisosParticulares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                idPermisoParticular= String.valueOf(permisoMedicoParticularList.get(position).getId());
                Intent permisoParticular = new Intent(getApplicationContext(),PermisosMedicosActivity.class);

                permisoParticular.putExtra("ID_PERMISO_PARTICULAR",idPermisoParticular);
                permisoParticular.putExtra("PRECEDENCIA","consultar");
                permisoParticular.putExtra("ID_EMPLEADO",idEmpleado);
                permisoParticular.putExtra("CARGO", cargo);

                startActivity(permisoParticular);
            }
        });
    }

    /*
     * Lleva al usuario a la ventana de PermisoParticular y envia datos
     * */
    public void aperturaPermisoMedicoParticular(View v){
        //Se crea un permiso medico particular vacio
        PermisoMedico permisoMedico = new PermisoMedico();
        permisoMedico.save();

        Intent inPermisoMedico = new Intent(getApplicationContext(),PermisosMedicosActivity.class);
        inPermisoMedico.putExtra("PRECEDENCIA","crear");
        inPermisoMedico.putExtra("ID_EMPLEADO",idEmpleado);
        inPermisoMedico.putExtra("ID_ATENCION", permisoMedico.getId().toString());
        inPermisoMedico.putExtra("CARGO", cargo);
        startActivity(inPermisoMedico);
    }


}
