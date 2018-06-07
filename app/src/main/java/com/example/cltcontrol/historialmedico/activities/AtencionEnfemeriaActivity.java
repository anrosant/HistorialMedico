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
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtenEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionFragment;
import com.example.cltcontrol.historialmedico.fragments.PlanCuidadosFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesFragment;
import com.example.cltcontrol.historialmedico.interfaces.ComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AtencionEnfemeriaActivity extends FragmentActivity implements ComunicadorMenu {

    private Fragment[] misFragmentos;
    private String idAtencion, idEmpleado, presedencia;
    private Empleado empleado;
    TextView tvNombresEmpleado;
    Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_atencion_enfermeria);

        //Recibe el id de atencion desde el HistorialAtencionEnfermeria
        idAtencion = getIntent().getStringExtra("ID_ATENCION");
        idEmpleado = getIntent().getStringExtra("ID_EMPLEADO");
        presedencia= getIntent().getStringExtra("PRESEDENCIA");

        //Coloca los datos del empleado en el fragment de informacion
        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        btn_ok = findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<AtencionEnfermeria> atencionEnfermerias = (ArrayList<AtencionEnfermeria>) AtencionEnfermeria.find(AtencionEnfermeria.class,
                        "empleado = ?", idEmpleado);
                HistorialAtencionEnfermeria.adapter.actualizarAtencionEnfermeriaList(atencionEnfermerias);

                AtencionEnfemeriaActivity.super.onBackPressed();
            }
        });

        misFragmentos = new Fragment[4];

        misFragmentos[0] = new SignosVitalesFragment();
        misFragmentos[1] = new MotivoAtenEnfermeriaFragment();
        misFragmentos[2] = new DiagnosticoEnfermeriaFragment();
        misFragmentos[3] = new PlanCuidadosFragment();

        menuPulsado(0);
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorAtencionEnfermeria,misFragmentos[opcionMenu]).commit();
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

                //Si se creó una consulta vacía se elimina (Si viene de editar no se elimina)
                if(presedencia.equals("crear")) {
                    AtencionEnfermeria atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.parseLong(idAtencion));
                    atencionEnfermeria.delete();
                }
                AtencionEnfemeriaActivity.super.onBackPressed();
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
}
