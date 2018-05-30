package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        lvConsultasMedicas = findViewById(R.id.lvConsultasMedicas);

        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID");

        List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);

        AdapterItemsConsultaMedica adapter = new AdapterItemsConsultaMedica(this, (ArrayList<ConsultaMedica>) consultaMedicaList);
        lvConsultasMedicas.setAdapter(adapter);
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());
    }



    @Override
    public void menuPulsado(int opcionMenu) {
        //Se crea una consulta medica
        Date fecha_actual = new Date();
        //Sacar los tres ultimos valores de la ficha medica (WEB)
        ConsultaMedica consultaMedica = new ConsultaMedica(empleado, fecha_actual,
                "Problema act", "Rev aparatos", "Preescripcion");
        consultaMedica.save();

        Intent inMenu = new Intent(getApplicationContext(),ConsultaMedicaNuevoActivity.class);
        inMenu.putExtra("BOTONPULSADO",opcionMenu);
        inMenu.putExtra("ID",idEmpleado);
        inMenu.putExtra("ID_CONSULTA_MEDICA",consultaMedica.getId());


        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        startActivity(inMenu);
    }


    public void aperturaConsultaMedica(View v){
        menuPulsado(0);
    }

}