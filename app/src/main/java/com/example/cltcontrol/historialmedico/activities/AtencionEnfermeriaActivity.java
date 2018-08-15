package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.DiagnosticoEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.MotivoAtencionEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.fragments.PlanCuidadosFragment;
import com.example.cltcontrol.historialmedico.fragments.SignosVitalesEnfermeriaFragment;
import com.example.cltcontrol.historialmedico.interfaces.IComunicadorMenu;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.orm.util.NamingHelper;
import java.util.ArrayList;

public class AtencionEnfermeriaActivity extends FragmentActivity implements IComunicadorMenu {
    private TextView tvGuardarAtencion, tvNombresEmpleado, tvOcupacion;
    private Fragment[] misFragmentos;
    private String idAtencion, idEmpleado, precedencia, cargo;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_atencion_enfermeria);
        tvGuardarAtencion = findViewById(R.id.tvGuardarAtencion);
        tvOcupacion = findViewById(R.id.tvOcupacion);

        //Recibe el id de atencion desde el HistorialAtencionEnfermeria
        idAtencion = getIntent().getStringExtra("ID_ATENCION");
        idEmpleado = getIntent().getStringExtra("ID_EMPLEADO");
        precedencia = getIntent().getStringExtra("PRECEDENCIA");
        cargo = getIntent().getStringExtra("CARGO");

        if(cargo.equalsIgnoreCase("Doctor")){
            tvGuardarAtencion.setVisibility(View.GONE);
        }
        //Coloca los datos del empleado en el fragment de informacion
        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        final Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());
        tvOcupacion.setText(empleado.getOcupacion());

        tvGuardarAtencion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tvGuardarAtencion.getRight() - tvGuardarAtencion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        AtencionEnfermeria atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Integer.parseInt(idAtencion));
                        if(atencionEnfermeria.getFecha_atencion()==null){
                            atencionEnfermeria.delete();
                        }
                        ArrayList<AtencionEnfermeria> atencionEnfermerias = (ArrayList<AtencionEnfermeria>) AtencionEnfermeria.find(AtencionEnfermeria.class,
                                "empleado = ?", String.valueOf(empleado.getId()));
                        HistorialAtencionEnfermeria.adapterItemAtencionEnfermeria.actualizarAtencionEnfermeriaList(atencionEnfermerias);

                        AtencionEnfermeriaActivity.super.onBackPressed();
                    }
                }
                return true;
            }
        });

        misFragmentos = new Fragment[4];
        misFragmentos[0] = new SignosVitalesEnfermeriaFragment();
        misFragmentos[1] = new MotivoAtencionEnfermeriaFragment();
        misFragmentos[2] = new DiagnosticoEnfermeriaFragment();
        misFragmentos[3] = new PlanCuidadosFragment();

        menuPulsado(0);
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorAtencionEnfermeria,misFragmentos[opcionMenu]).commit();
    }

    /*
     * si presiona el botón de ir atrás valida si desea salir sin guardar datos o permanecer
     * */
    @Override
    public void onBackPressed(){
        if(cargo.equals("Enfermera")) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            if (precedencia.equals("crear")) {
                //seleccionamos la cadena a mostrar
                alertbox.setMessage("¿Está seguro que desea salir?");
                //elegimos un positivo SI
                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Elimina la consulta vacía
                        AtencionEnfermeria atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class, Long.parseLong(idAtencion));

                        if(atencionEnfermeria.getEmpleado()==null || atencionEnfermeria.getFecha_atencion()==null){
                            atencionEnfermeria.delete();
                        }

                        //Reset el autoincrement
                        AtencionEnfermeria.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NamingHelper.toSQLName(AtencionEnfermeria.class) + "'");

                        AtencionEnfermeriaActivity.super.onBackPressed();
                    }
                });
                //Si editó una consulta
            } else {
                //seleccionamos la cadena a mostrar
                alertbox.setMessage("¿Está seguro que desea salir?");
                //elegimos un positivo SI
                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        AtencionEnfermeriaActivity.super.onBackPressed();
                    }
                });
            }
            //elegimos un positivo NO
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                //Funcion llamada cuando se pulsa el boton No
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            //mostramos el alertbox
            alertbox.show();
        } else {
            AtencionEnfermeriaActivity.super.onBackPressed();
        }
    }
}