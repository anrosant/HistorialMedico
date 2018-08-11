package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import java.text.DateFormat;
import java.util.Objects;

public class MenuEmpleadoActivity extends AppCompatActivity {

    private TextView tvDatosPersonales;
    private LinearLayout lyDatosPersonales;

    private String idEmpleado;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

        //Datos del empleado
        lyDatosPersonales = findViewById(R.id.lyDatosPersonales);
        tvDatosPersonales = findViewById(R.id.tvDatosPersonales);
        TextView tvCi = findViewById(R.id.tvCi);
        TextView tvApellidosNombre = findViewById(R.id.tvApellidosNombre);
        TextView tvSexo = findViewById(R.id.tvSexo);
        TextView tvLugarFechaNacimiento = findViewById(R.id.tvLugarFechaNacimiento);
        TextView tvEdad = findViewById(R.id.tvEdad);
        TextView tvProfesion = findViewById(R.id.tvProfesion);
        TextView tvEstadoCivil = findViewById(R.id.tvEstadoCivil);

        //Datos usados para la empresa
        TextView tvIdEmpleado = findViewById(R.id.tvIdEmpleado);
        TextView tvFechaIngreso = findViewById(R.id.tvFechaIngreso);
        TextView tvOcupacion = findViewById(R.id.tvOcupacion);

        //Recibe la cedula del empleado desde BuscarEmpleadoActivity
        Intent inBuscarEmpleadoActivity = getIntent();
        idEmpleado = inBuscarEmpleadoActivity.getStringExtra("ID_EMPLEADO");

        //Busca al empleado por su cedula y muestra en un fragment los datos
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvCi.setText(empleado.getCedula());
        tvApellidosNombre.setText(empleado.getApellido()+" "+ empleado.getNombre());
        //tvNombres.setText(empleado.getNombre());
        tvSexo.setText(empleado.getSexo());

        String fechaNacimiento = DateFormat.getDateInstance().format(empleado.getFechaNacimiento());
        tvLugarFechaNacimiento.setText(empleado.getLugarNacimiento()+", "+ fechaNacimiento);

        tvEdad.setText(empleado.getEdad()+" años");
        tvProfesion.setText(empleado.getProfesion());
        tvEstadoCivil.setText(empleado.getEstadoCivil());
        tvIdEmpleado.setText(empleado.getId().toString());
        String fechaRegisto = DateFormat.getDateInstance().format(empleado.getFechaRegistro());
        tvFechaIngreso.setText(fechaRegisto);
        //tvCargo.setText(empleado.getCargo);
        tvOcupacion.setText(empleado.getOcupacion());

        tvDatosPersonales.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (tvDatosPersonales.getRight() - tvDatosPersonales.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!lyDatosPersonales.isShown()){
                            lyDatosPersonales.setVisibility(View.VISIBLE);
                            tvDatosPersonales.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_cyan_24dp,0);
                        }else {
                            lyDatosPersonales.setVisibility(View.GONE);
                            tvDatosPersonales.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_cyan_24dp,0);
                        }
                    }
                }
                return true;
            }
        });

    }

    /*
    * Crea una instancia del menú
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_usuario, menu);

        return true;

    }

    /*
    * Menú de opciones
    * Si selecciona el primer item va a la actividad Acerca sino cierra sesión
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_acerca:

                startActivity(new Intent(this, AcercaActivity.class));

                return true;

            case R.id.item_cerrar_sesion:

                cerrarSesion();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }



    }

    /*
     * Cierra la sesión
     * */
    public void cerrarSesion(){
        SessionManager sesion = new SessionManager(getApplicationContext());
        sesion.cerrarSesion(getApplicationContext());
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
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
