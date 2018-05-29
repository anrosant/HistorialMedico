package com.example.cltcontrol.historialmedico.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionFragment;
import com.example.cltcontrol.historialmedico.fragments.Plan_Cuidados_Fragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;

import java.util.Objects;

public class AtencionEnfemeriaActivity extends FragmentActivity implements ComunicadorMenu {

    private Fragment[] misFragmentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_atencion_enfermeria);

        misFragmentos = new Fragment[4];

        misFragmentos[0] = new SignosVitalesFragment();
        misFragmentos[1] = new MotivoAtencionFragment();
        misFragmentos[2] = new DiagnosticoEnfermeriaFragment();
        misFragmentos[3] = new Plan_Cuidados_Fragment();

        Bundle extras = getIntent().getExtras();
        menuPulsado(Objects.requireNonNull(extras).getInt("BOTONPULSADO"));
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        Bundle datos = new Bundle();
        datos.putInt("BOTONPULSADO",opcionMenu);
        misFragmentos[opcionMenu].setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorAtencionEnfermeria,misFragmentos[opcionMenu]).commit();

    }
}
