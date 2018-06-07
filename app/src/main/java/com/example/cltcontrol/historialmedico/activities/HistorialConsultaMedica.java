package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.Adapter.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class HistorialConsultaMedica extends FragmentActivity implements ComunicadorMenu{

    private String idEmpleado, presedencia, idConsultaMedica;
    private TextView tvNombresEmpleado;
    private ListView lvConsultasMedicas;
    private Empleado empleado;
    private FloatingActionButton btnAgregarConsultaMedica;
    private List<ConsultaMedica> consultaMedicaList;
    public static AdapterItemsConsultaMedica adapterItemsConsultaMedica;

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
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Busca las consultas medica de un empleado
        consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);
        Log.d("TAMANIO", String.valueOf(consultaMedicaList.size()));
        //Muestra los datos de las consultas medica en el listview
        adapterItemsConsultaMedica = new AdapterItemsConsultaMedica(this, consultaMedicaList);
        lvConsultasMedicas.setAdapter(adapterItemsConsultaMedica);

        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        lvConsultasMedicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                idConsultaMedica= String.valueOf(consultaMedicaList.get(position).getId());
                /*idConsultaMedica = view.findViewById(R.id.tvIdAtencion);
                String idConsulta = idConsultaMedica.getText().toString();*/
                Intent inConsultaMedica = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
                inConsultaMedica.putExtra("ID_CONSULTA_MEDICA",idConsultaMedica);
                inConsultaMedica.putExtra("PRESEDENCIA","consultar");
                inConsultaMedica.putExtra("ID_EMPLEADO",idEmpleado);
                startActivity(inConsultaMedica);
            }
        });
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        //Se crea una consulta medica vacia
        /*ConsultaMedica consultaMedica = new ConsultaMedica(empleado, fecha_actual,
                "Problema act", "Rev aparatos", "Preescripcion");*/
        ConsultaMedica consultaMedica=new ConsultaMedica();
        consultaMedica.save();

        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("ID_EMPLEADO",idEmpleado);
        inMenu.putExtra("ID_CONSULTA_MEDICA", consultaMedica.getId().toString());
        inMenu.putExtra("PRESEDENCIA", "crear");

        startActivity(inMenu);

        /*Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);*/

    }

    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}