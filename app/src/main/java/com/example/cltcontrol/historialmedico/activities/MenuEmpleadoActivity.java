package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.text.DateFormat;

public class MenuEmpleadoActivity extends FragmentActivity {

    private ImageView FotoEmpleado;
    private TextView tvCi, tvApellidosNombre, tvSexo, tvLugarFechaNacimiento,
            tvEdad, tvProfesion, tvEstadoCivil, tvIdEmpleado, tvFechaIngreso,
            tvOcupacion, tvDatosPersonales, tvDatosDeEmpresa;
    private LinearLayout lyDatosPersonales,lyDatosDeEmpresa;

    private String idEmpleado;
    private Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        //Datos del empleado
        lyDatosPersonales = findViewById(R.id.lyDatosPersonales);
        tvDatosPersonales = findViewById(R.id.tvDatosPersonales);
        tvCi =  findViewById(R.id.tvCi);
        tvApellidosNombre =  findViewById(R.id.tvApellidosNombre);
        tvSexo =  findViewById(R.id.tvSexo);
        tvLugarFechaNacimiento =  findViewById(R.id.tvLugarFechaNacimiento);
        tvEdad =  findViewById(R.id.tvEdad);
        tvProfesion =  findViewById(R.id.tvProfesion);
        tvEstadoCivil =  findViewById(R.id.tvEstadoCivil);

        //Datos usados para la empresa
        lyDatosDeEmpresa = findViewById(R.id.lyDatosDeEmpresa);
        tvDatosDeEmpresa = findViewById(R.id.tvDatosDeEmpresa);
        tvIdEmpleado = findViewById(R.id.tvIdEmpleado);
        tvFechaIngreso =  findViewById(R.id.tvFechaIngreso);
        tvOcupacion =  findViewById(R.id.tvOcupacion);

        //Recibe la cedula del empleado desde BuscarEmpleadoActivity
        Intent inBuscarEmpleadoActivity = getIntent();
        idEmpleado = inBuscarEmpleadoActivity.getStringExtra("ID_EMPLEADO");

        //Busca al empleado por su cedula y muestra en un fragment los datos
        empleado = Empleado.findById(Empleado.class,Long.parseLong(idEmpleado));
        tvCi.setText(empleado.getCedula());
        tvApellidosNombre.setText(empleado.getApellido()+" "+empleado.getNombre());
        //tvNombres.setText(empleado.getNombre());
        tvSexo.setText(empleado.getSexo());

        String fechaNacimiento = DateFormat.getDateInstance().format(empleado.getFechaNacimiento());
        tvLugarFechaNacimiento.setText(empleado.getLugarNacimiento()+", "+fechaNacimiento.toString());

        tvEdad.setText(empleado.getEdad()+" años");
        tvProfesion.setText(empleado.getProfesion());
        tvEstadoCivil.setText(empleado.getEstadoCivil());
        tvIdEmpleado.setText(empleado.getId().toString());
        String fechaRegisto = DateFormat.getDateInstance().format(empleado.getFechaRegistro());
        tvFechaIngreso.setText(fechaRegisto.toString());
        //tvCargo.setText(empleado.getCargo);
        tvOcupacion.setText(empleado.getOcupacion());

        tvDatosPersonales.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tvDatosPersonales.getRight() - tvDatosPersonales.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!lyDatosPersonales.isShown()){
                            lyDatosPersonales.setVisibility(view.VISIBLE);
                            tvDatosPersonales.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_cyan_24dp,0);
                        }else {
                            lyDatosPersonales.setVisibility(view.GONE);
                            tvDatosPersonales.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_cyan_24dp,0);
                        }
                    }
                }
                return true;
            }
        });

        tvDatosDeEmpresa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tvDatosDeEmpresa.getRight() - tvDatosDeEmpresa.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!lyDatosDeEmpresa.isShown()){
                            lyDatosDeEmpresa.setVisibility(view.VISIBLE);
                            tvDatosDeEmpresa.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_cyan_24dp,0);
                        }else {
                            lyDatosDeEmpresa.setVisibility(view.GONE);
                            tvDatosDeEmpresa.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_cyan_24dp,0);
                        }
                    }
                }
                return true;
            }
        });
    }

    /*
     * Lleva a la ventana de HistorialConsultaMedica y envia el id del empleado
     * */
    public void aperturaHistorialConsultaMedica(View v) {
        //Envia el id del empleado a HistorialConsultaMedica
        Intent inHistorialConsultaMedica = new Intent(getApplicationContext(), HistorialConsultaMedica.class);
        //inHistorialConsultaMedica.putExtra("CEDULA", cedulaEmpleado);
        inHistorialConsultaMedica.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inHistorialConsultaMedica);
    }

    /*
     * Lleva a la ventana de HistorialAtencionEnfermeria y envia el id del empleado
     * */
    public void aperturaHistorialAtencionEnfermeria(View v){
        //Envia el id del empleado a HistorialAtencionEnfermeria
        Intent inHistorialAtencionEnfermeria = new Intent(getApplicationContext(), HistorialAtencionEnfermeria.class);
        //inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inHistorialAtencionEnfermeria.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inHistorialAtencionEnfermeria);
    }

    public void aperturaSignos(View v){
        //Envia el id del empleado a Signos Vitales
        Intent inSignos = new Intent(getApplicationContext(), SigVitalRapidoActivity.class);
        //inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inSignos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inSignos);
    }

    /*
     * Lleva a la ventana de PermisoMedicosParticulares y envia el id del empleado
     */
    public void aperturaPermisosMedicos(View v){
        //Envia el id del empleado a HistorialAtencionEnfermeria
        Intent inPermisosMedicos = new Intent(getApplicationContext(), PermisosMedicosActivity.class);
        //inHistorialAtencionEnfermeria.putExtra("CEDULA", cedulaEmpleado);
        inPermisosMedicos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inPermisosMedicos);
    }
}
