package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtenEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionFragment;
import com.example.cltcontrol.historialmedico.fragments.PlanCuidadosFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AtencionEnfemeriaActivity extends FragmentActivity implements ComunicadorMenu {

    private Fragment[] misFragmentos;
    private String idAtencion;
    private String idEmpleado;
    private String presedencia;
    private String idAtencion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_atencion_enfermeria);

        idAtencion = getIntent().getStringExtra("ID_ATENCION");
        idEmpleado = getIntent().getStringExtra("ID_EMPLEADO");
        presedencia= getIntent().getStringExtra("PRESEDENCIA");

        if(presedencia.equals("crear")){
            AtencionEnfermeria nuevaAtencion = new AtencionEnfermeria();
            nuevaAtencion.setFecha_atencion(new Date());
            Empleado empleado = Empleado.findById(Empleado.class,Long.valueOf(idEmpleado));
            nuevaAtencion.setEmpleado(empleado);
            nuevaAtencion.save();
            idAtencion2 = String.valueOf(nuevaAtencion.getId());
        }

        misFragmentos = new Fragment[4];

        misFragmentos[0] = new SignosVitalesEnfermeriaFragment();
        misFragmentos[1] = new MotivoAtenEnfermeriaFragment();
        misFragmentos[2] = new DiagnosticoEnfermeriaFragment();
        misFragmentos[3] = new PlanCuidadosFragment();

        Intent intent = getIntent();
        if(idAtencion!=null){
            intent.putExtra("ID_ATENCION",idAtencion);
        }
        if(idEmpleado!=null){
            intent.putExtra("ID_EMPLEADO",idEmpleado);
        }
        if(idAtencion2!=null){
            intent.putExtra("ID_ATENCION2",idAtencion2);
        }
        intent.putExtra("PRESEDENCIA",presedencia);

        menuPulsado(0);

    }

    @Override
    public void menuPulsado(int opcionMenu) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorAtencionEnfermeria,misFragmentos[opcionMenu]).commit();

    }
}
