package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import java.util.List;

public class SigVitalRapidoActivity extends FragmentActivity {


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signo_rapido);

        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        Fragment miFragmento = new SignosVitalesEnfermeriaFragment();

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        String idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());

        //Busca los signos vitales de un empleado
        List<SignosVitales> signosVitalesList = SignosVitales.find(SignosVitales.class, "empleado = ?", idEmpleado);

        //Se obtienen todos los signos vitales del empleado provenientes de ConsultaMedica
        List<ConsultaMedica> consultasList = ConsultaMedica.find(ConsultaMedica.class, "empleado = ?", idEmpleado);
        List<SignosVitales> signosVitalesTempList;
        for(ConsultaMedica actual: consultasList){
            signosVitalesTempList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(actual.getId()));
            signosVitalesList.addAll(signosVitalesTempList);//Se añaden todos los signos encontrados
            signosVitalesTempList.clear();//Se vacia la lista temporal
        }

        //Se obtienen todos los signos vitales del empleado provenientes de ConsultaMedica
        List<AtencionEnfermeria> atencionesList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);
        for(AtencionEnfermeria actual : atencionesList){
            signosVitalesTempList = SignosVitales.find(SignosVitales.class, "atencionenfermeria = ?", String.valueOf(actual.getId()));
            signosVitalesList.addAll(signosVitalesTempList);//Se añaden todos los signos encontrados
            signosVitalesTempList.clear(); //Se vacia la lista temporal
        }

        //Guarda el empleado en los signos vitales que vinieron desde atencion de Enfermeria y consulta medica
        for(SignosVitales actual : signosVitalesList){
            if(actual.getEmpleado()==null){
                actual.setEmpleado(empleado);
                actual.save();
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorSigno, miFragmento).commit();

    }


}
