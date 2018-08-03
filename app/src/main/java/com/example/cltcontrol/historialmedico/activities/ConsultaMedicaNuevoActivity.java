package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.fragments.AnexarExamenesFragment;
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
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.orm.util.NamingHelper;
import java.util.ArrayList;
import java.util.Objects;

public class ConsultaMedicaNuevoActivity extends FragmentActivity implements ComunicadorMenu{

    private Fragment[] misFragmentos;
    private Empleado empleado;
    private String idConsultaMedica;
    private String precedencia;
    private String cargo;
    private TextView tv_guardar_consulta;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_consulta_medica);

        tv_guardar_consulta = findViewById(R.id.tv_guardar_consulta);

        misFragmentos = new Fragment[11];
        misFragmentos[0] = new SignosVitalesFragment();
        misFragmentos[1] = new MotivoAtencionFragment();
        misFragmentos[2] = new PatologiasPersonalesFragment();
        misFragmentos[3] = new PatologiasFamiliaresFragment();
        misFragmentos[4] = new ProblemaActualFragment();
        misFragmentos[5] = new RevisionMedicaFragment();
        misFragmentos[6] = new ExamenFisicoFragment();
        misFragmentos[7] = new AnexarExamenesFragment();
        misFragmentos[8] = new DiagnosticoFragment();
        misFragmentos[9] = new PrescripcionFragment();
        misFragmentos[10] = new PermisosMedicosFragment();

        //Permite regresar al historial


        Bundle extras = this.getIntent().getExtras();

        menuPulsado(Objects.requireNonNull(extras).getInt("BOTONPULSADO"));

        //Recibe el id del empleado desde el HistorialConsultaMedica
        final String id_empleado = extras.getString("ID_EMPLEADO");
        idConsultaMedica = extras.getString("ID_CONSULTA_MEDICA");
        precedencia = extras.getString("PRECEDENCIA");
        cargo = extras.getString("CARGO");

        if (cargo != null && cargo.equalsIgnoreCase("Enfermera")) {
            tv_guardar_consulta.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        //Coloca los datos del empleado en el fragment de informacion
        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        empleado = Empleado.findById(Empleado.class, Long.parseLong(id_empleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        tv_guardar_consulta.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tv_guardar_consulta.getRight() - tv_guardar_consulta.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ConsultaMedica consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Integer.parseInt(idConsultaMedica));
                        if(consultaMedica.getFechaConsulta()==null){
                            consultaMedica.delete();
                        }

                        ArrayList<ConsultaMedica> consultaMedicas = (ArrayList<ConsultaMedica>) ConsultaMedica.find(ConsultaMedica.class,
                                "empleado = ?", String.valueOf(empleado.getId()));
                        HistorialConsultaMedica.adapterItemConsultaMedica.actualizarConsultaMedicaList(consultaMedicas);
                        ConsultaMedicaNuevoActivity.super.onBackPressed();
                    }
                }
                return true;
            }
        });
    }

    /*
     * si presiona el botón de ir atrás valida si desea salir sin guardar datos o permanecer
     * */
    @Override
    public void onBackPressed() {
        if(cargo.equals("Doctor")) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            if (precedencia.equals("crear")) {
                //seleccionamos la cadena a mostrar
                alertbox.setMessage("¿Está seguro que desea salir?");
                //elegimos un positivo SI
                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Elimina la consulta vacía
                        ConsultaMedica consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.parseLong(idConsultaMedica));

                        if(consultaMedica.getEmpleado()==null || consultaMedica.getFechaConsulta()==null){
                            consultaMedica.delete();
                        }
                        //Reset el autoincrement
                        ConsultaMedica.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NamingHelper.toSQLName(ConsultaMedica.class) + "'");

                        ConsultaMedicaNuevoActivity.super.onBackPressed();
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
                        ConsultaMedicaNuevoActivity.super.onBackPressed();
                    }
                });
            }
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
        else {
            ConsultaMedicaNuevoActivity.super.onBackPressed();
        }
    }

    @Override
    public void menuPulsado(int opcionMenu) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConsultaMedica,misFragmentos[opcionMenu]).commit();
    }

}