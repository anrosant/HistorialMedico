package com.example.cltcontrol.historialmedico.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class SigVitalRapidoActivity extends FragmentActivity {

    private String idEmpleado, idConsultaMedica, cargo;
    private TextView tvNombresEmpleado;
    private List<SignosVitales> signosVitalesList;
    private List<SignosVitales> signosVitalesTempList;
    List<ConsultaMedica> consultasList;
    List<AtencionEnfermeria> atencionesList;
    private Empleado empleado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signo_rapido);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Busca los signos vitales de un empleado
        signosVitalesList= SignosVitales.find(SignosVitales.class, "empleado = ?", idEmpleado);

        //Se obtienen todos los signos vitales del empleado provenientes de ConsultaMedica
        consultasList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);
        for(ConsultaMedica actual:consultasList){
            signosVitalesTempList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(actual.getId()));
            signosVitalesList.addAll(signosVitalesTempList);//Se añaden todos los signos encontrados
            signosVitalesTempList.clear();//Se vacia la lista temporal
        }

        //Se obtienen todos los signos vitales del empleado provenientes de ConsultaMedica
        atencionesList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);
        for(AtencionEnfermeria actual : atencionesList){
            signosVitalesTempList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(actual.getId()));
            signosVitalesList.addAll(signosVitalesTempList);//Se añaden todos los signos encontrados
            signosVitalesTempList.clear(); //Se vacia la lista temporal
        }

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

    }


}
