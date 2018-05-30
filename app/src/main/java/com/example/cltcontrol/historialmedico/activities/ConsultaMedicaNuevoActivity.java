package com.example.cltcontrol.historialmedico.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoFragment;
import com.example.cltcontrol.historialmedico.fragments.ExamenFisicoFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionFragment;
import com.example.cltcontrol.historialmedico.fragments.PatologiasFamiliaresFragment;
import com.example.cltcontrol.historialmedico.fragments.PatologiasPersonalesFragment;
import com.example.cltcontrol.historialmedico.fragments.PermisosMedicosFragment;
import com.example.cltcontrol.historialmedico.fragments.PreescripcionFragment;
import com.example.cltcontrol.historialmedico.fragments.ProblemaActualFragment;
import com.example.cltcontrol.historialmedico.fragments.RevisionMedicaFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.List;
import java.util.Objects;

public class ConsultaMedicaNuevoActivity extends FragmentActivity implements ComunicadorMenu{

    private Fragment[] misFragmentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_consulta_medica);


        misFragmentos = new Fragment[10];

        misFragmentos[0] = new SignosVitalesFragment();
        misFragmentos[1] = new MotivoAtencionFragment();
        misFragmentos[2] = new PatologiasPersonalesFragment();
        misFragmentos[3] = new PatologiasFamiliaresFragment();
        misFragmentos[4] = new ProblemaActualFragment();
        misFragmentos[5] = new RevisionMedicaFragment();
        misFragmentos[6] = new ExamenFisicoFragment();
        //misFragmentos[8] = new anexa examenes
        misFragmentos[7] = new DiagnosticoFragment();
        misFragmentos[8] = new PreescripcionFragment();
        misFragmentos[9] = new PermisosMedicosFragment();

        Bundle extras = this.getIntent().getExtras();
        menuPulsado(Objects.requireNonNull(extras).getInt("BOTONPULSADO"));
        //String idEmpleado = extras.getString("CEDULA");
        String idEmpleado = extras.getString("ID");
        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());


    }

    @Override
    public void menuPulsado(int opcionMenu) {
        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        misFragmentos[opcionMenu].setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConsultaMedica,misFragmentos[opcionMenu]).commit();
    }
}
