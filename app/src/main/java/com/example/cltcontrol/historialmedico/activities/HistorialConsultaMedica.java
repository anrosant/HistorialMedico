package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.adapter.AdapterItemConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements IComunicadorMenu {

    @SuppressLint("StaticFieldLeak")
    public static AdapterItemConsultaMedica adapterItemConsultaMedica;

    private String idEmpleado, idConsultaMedica, cargo;
    private List<ConsultaMedica> consultaMedicaList;
    private List<Diagnostico> diagnosticoList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        TextView tvOcupacion = findViewById(R.id.tvOcupacion);
        ListView lvConsultasMedicas = findViewById(R.id.lvConsultasMedicas);
        FloatingActionButton btnAgregarConsultaMedica = findViewById(R.id.btnAgregarConsultaMedica);

        //Obtener el cargo del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());
        cargo = sesion.obtenerInfoUsuario().get("cargo");
        if(cargo.equalsIgnoreCase("Enfermera")){
            btnAgregarConsultaMedica.setVisibility(View.GONE);
        }
        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");
        Log.d("IDEMPLEADO", idEmpleado);

        //Busca las consultas medica de un empleado
        consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);
        for(int i=0; i < consultaMedicaList.size() ; i++ ){
            diagnosticoList = Diagnostico.find(Diagnostico.class,"consultamedica");
        }

        //Muestra los datos de las consultas medica en el listview
        adapterItemConsultaMedica = new AdapterItemConsultaMedica(this, consultaMedicaList,diagnosticoList);
        lvConsultasMedicas.setAdapter(adapterItemConsultaMedica);

        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));

        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());
        Log.d("OCUPACION", empleado.getOcupacion());
        tvOcupacion.setText(empleado.getOcupacion());

        lvConsultasMedicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idConsultaMedica= String.valueOf(consultaMedicaList.get(position).getId());
                Log.d("IDCONSULTA",idConsultaMedica);
                Log.d("IDEMPLEADO",idEmpleado);
                Log.d("IDCARGO",cargo);

                Intent inConsultaMedica = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
                inConsultaMedica.putExtra("ID_CONSULTA_MEDICA",idConsultaMedica);
                inConsultaMedica.putExtra("PRECEDENCIA","consultar");
                inConsultaMedica.putExtra("ID_EMPLEADO",idEmpleado);
                inConsultaMedica.putExtra("CARGO", cargo);
                startActivity(inConsultaMedica);
            }
        });
    }

    /*
     * Lleva a la ventana de ConsultaMedicaNuevo y envia datos
     * */
    @Override
    public void menuPulsado(int opcionMenu) {
        //Se crea una consulta medica vacia
        ConsultaMedica consultaMedica=new ConsultaMedica();
        consultaMedica.save();

        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("ID_EMPLEADO",idEmpleado);
        inMenu.putExtra("ID_CONSULTA_MEDICA", consultaMedica.getId().toString());
        inMenu.putExtra("PRECEDENCIA", "crear");
        inMenu.putExtra("CARGO", cargo);
        startActivity(inMenu);
    }

    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}