package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import static com.example.cltcontrol.historialmedico.activities.HistorialConsultaMedica.adapterItemConsultaMedica;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemPermisoMedico;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.R;

import java.util.List;

public class HistorialPermisoMedicoParticular extends FragmentActivity {

    public static AdapterItemPermisoMedico adapterItemPermisoMedico;
    private FloatingActionButton btnAgregarPermisoMedico;
    private ListView lvPermisosMedicosParticulares;
    private TextView tvNombresEmpleado;

    private List<PermisoMedico> permisoMedicoList;
    private Empleado empleado;
    private String idEmpleado, idPermiso, cargo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_permiso_medico_particular);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        lvPermisosMedicosParticulares = findViewById(R.id.lvPermisosMedicosParticulares);
        btnAgregarPermisoMedico = findViewById(R.id.btnAgregarConsultaMedica);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        cargo = sesion.obtenerInfoUsuario().get("cargo");
        if(cargo.equals("Enfermera")){
            btnAgregarPermisoMedico.setVisibility(View.GONE);
        }
        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Busca los permisos medicos de un empleado
        permisoMedicoList = PermisoMedico.find(PermisoMedico.class, "empleado = ?", idEmpleado);
        //Muestra los datos de los permisos medicos en el listview
        adapterItemPermisoMedico = new AdapterItemPermisoMedico(this, permisoMedicoList);
        lvPermisosMedicosParticulares.setAdapter(adapterItemPermisoMedico);

        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        lvPermisosMedicosParticulares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idPermiso= String.valueOf(permisoMedicoList.get(position).getId());

                Intent inPermisoMedico = new Intent(getApplicationContext(),PermisosMedicosActivity.class);
                //inConsultaMedica.putExtra("ID_CONSULTA_MEDICA",idConsultaMedica);
                inPermisoMedico.putExtra("PRECEDENCIA","consultar");
                inPermisoMedico.putExtra("ID_EMPLEADO",idEmpleado);
                inPermisoMedico.putExtra("CARGO", cargo);
                startActivity(inPermisoMedico);
            }
        });
    }

    /*
     * Lleva a la ventana de PermisoMedicosParticulares y envia el id del empleado
     */
    public void aperturaPermisosMedicos(View v){
        //Envia el id del empleado a HistorialAtencionEnfermeria
        Intent inPermisosMedicos = new Intent(getApplicationContext(), PermisosMedicosActivity.class);
        inPermisosMedicos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inPermisosMedicos);
    }

}
