package com.example.cltcontrol.historialmedico.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoFragment;
import com.example.cltcontrol.historialmedico.fragments.ExamenFisicoFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionFragment;
import com.example.cltcontrol.historialmedico.fragments.PatologiasFamiliaresFragment;
import com.example.cltcontrol.historialmedico.fragments.PatologiasPersonalesFragment;
import com.example.cltcontrol.historialmedico.fragments.PermisosMedicosFragment;
import com.example.cltcontrol.historialmedico.fragments.PrescripcionFragment;
import com.example.cltcontrol.historialmedico.fragments.ProblemaActualFragment;
import com.example.cltcontrol.historialmedico.fragments.RevisionMedicaFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.Objects;

public class ConsultaMedicaNuevoActivity extends FragmentActivity implements ComunicadorMenu{

    private Fragment[] misFragmentos;
    private Empleado empleado;
    String idConsultaMedica, idEmpleado, presedencia;

    Button btn_ok;

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
        misFragmentos[8] = new PrescripcionFragment();
        misFragmentos[9] = new PermisosMedicosFragment();

        //Permite regresar al historial
        btn_ok = findViewById(R.id.btn_ok);

        Bundle extras = this.getIntent().getExtras();

        menuPulsado(Objects.requireNonNull(extras).getInt("BOTONPULSADO"));

        //Recibe el id del empleado desde el HistorialConsultaMedica
        final String id_empleado = extras.getString("ID_EMPLEADO");
        idConsultaMedica = extras.getString("ID_CONSULTA_MEDICA");
        presedencia = extras.getString("PRESEDENCIA");

        //Coloca los datos del empleado en el fragment de informacion
        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        empleado = Empleado.findById(Empleado.class, Long.parseLong(id_empleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ConsultaMedica> consultaMedicas = (ArrayList<ConsultaMedica>) ConsultaMedica.find(ConsultaMedica.class,
                        "empleado = ?", String.valueOf(empleado.getId()));
                HistorialConsultaMedica.adapterItemsConsultaMedica.actualizarConsultaMedicaList(consultaMedicas);

                ConsultaMedicaNuevoActivity.super.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        //seleccionamos la cadena a mostrar
        alertbox.setMessage("No se guardara la consulta. Desea salir?");
        //elegimos un positivo SI
        alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            //Funcion llamada cuando se pulsa el boton Si
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(),"Pulsaste SI",Toast.LENGTH_LONG).show();
                //Elimina la consulta vacía
                if(presedencia.equals("crear")){
                    ConsultaMedica consultaMedica= ConsultaMedica.findById(ConsultaMedica.class,Long.parseLong(idConsultaMedica));
                    consultaMedica.delete();
                }

                ConsultaMedicaNuevoActivity.super.onBackPressed();
            }
        });

        //elegimos un positivo NO
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            //Funcion llamada cuando se pulsa el boton No
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(),"Pulsaste NO",Toast.LENGTH_LONG).show();
            }
        });

        //mostramos el alertbox
        alertbox.show();
    }


    @Override
    public void menuPulsado(int opcionMenu) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConsultaMedica,misFragmentos[opcionMenu]).commit();
    }

}