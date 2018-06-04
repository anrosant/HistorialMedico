package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.Adapter.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu{

    private String idEmpleado;
    private TextView tvNombresEmpleado;
    private ListView lvConsultasMedicas;
    private Empleado empleado;
    private Button btnAgregarConsultaMedica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        lvConsultasMedicas = findViewById(R.id.lvConsultasMedicas);
        btnAgregarConsultaMedica = findViewById(R.id.btnAgregarConsultaMedica);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        String cargo = sesion.obtenerInfoUsuario().get("cargo");
        if(cargo.equals("Enfermera")){
            btnAgregarConsultaMedica.setVisibility(View.GONE);
        }
        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID");

        //Busca las consultas medica de un empleado
        List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);
        Log.d("size consultamedicalist", String.valueOf(consultaMedicaList.size()));
        //Muestra los datos de las consultas medica en el listview
        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, (ArrayList<ConsultaMedica>) consultaMedicaList);
        lvConsultasMedicas.setAdapter(adapter);

        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        //Se crea una consulta medica vacia
        /*ConsultaMedica consultaMedica = new ConsultaMedica(empleado, fecha_actual,
                "Problema act", "Rev aparatos", "Preescripcion");*/
        ConsultaMedica consultaMedica=new ConsultaMedica();
        consultaMedica.save();

        //Envia el id del empleado y de la nueva consulta medica a ConsultaMedicaNuevoActivity
        Intent inConsultaMedicaNuevoAct = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inConsultaMedicaNuevoAct.putExtra("BOTONPULSADO",opcionMenu);
        inConsultaMedicaNuevoAct.putExtra("ID_EMPLEADO",idEmpleado);
        inConsultaMedicaNuevoAct.putExtra("ID_CONSULTA_MEDICA",String.valueOf(consultaMedica.getId()));

        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        startActivity(inConsultaMedicaNuevoAct);
    }

    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}