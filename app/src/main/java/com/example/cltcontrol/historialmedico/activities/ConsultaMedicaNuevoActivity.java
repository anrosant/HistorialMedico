package com.example.cltcontrol.historialmedico.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.PreescripcionFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;

public class ConsultaMedicaNuevoActivity extends FragmentActivity implements ComunicadorMenu{

    Fragment[] misFragmentos;
    TextView tvNombresEmpleado;
    String idEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_consulta_medica);

        //Opcion a cargar por defecto SignosVitales
        /*SignosVitalesFragment contenidoSignosVitales = new SignosVitalesFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorConsultaMedica,contenidoSignosVitales).commit();
        */

        misFragmentos = new Fragment[2];

        misFragmentos[0] = new SignosVitalesFragment();
        //misFragmentos[1] = new ();
        //misFragmentos[2] = new ;
        //misFragmentos[3] = new ;
        //misFragmentos[4] = new ;
        //misFragmentos[5] = new ;
        //misFragmentos[6] = new ;
        //misFragmentos[7] = new ;
        misFragmentos[1] = new PreescripcionFragment();
        //misFragmentos[9] = new ;

        Bundle extras = this.getIntent().getExtras();
        menuPulsado(extras.getInt("BOTONPULSADO"));
        idEmpleado = extras.getString("CEDULA");
//        Toast.makeText(getApplicationContext(), ""+idEmpleado,Toast.LENGTH_SHORT).show();

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        List<Empleado> empleado = Empleado.find(Empleado.class, "CEDULA = ?", idEmpleado);
        tvNombresEmpleado.setText(empleado.get(0).getApellido()+" "+empleado.get(0).getNombre());


    }

    @Override
    public void menuPulsado(int opcionMenu) {
        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        misFragmentos[opcionMenu].setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConsultaMedica,misFragmentos[opcionMenu]).commit();
    }
}
