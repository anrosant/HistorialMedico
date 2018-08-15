package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.utils.Identifiers;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import java.text.DateFormat;

public class MenuEmpleadoActivity extends AppCompatActivity {

    private TextView tvIdEmpleado;

    private String idEmpleado;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_empleado);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

        //Datos del empleado
        LinearLayout lyDatosPersonales = findViewById(R.id.lyDatosPersonales);
        TextView tvDatosPersonales = findViewById(R.id.tvDatosPersonales);
        LinearLayout lyDatosEmpresa = findViewById(R.id.lyDatosDeEmpresa);
        TextView tvDatosDeEmpresa = findViewById(R.id.tvDatosDeEmpresa);
        TextView tvCi = findViewById(R.id.tvCi);
        TextView tvApellidosNombre = findViewById(R.id.tvApellidosNombre);
        TextView tvSexo = findViewById(R.id.tvSexo);
        TextView tvLugarFechaNacimiento = findViewById(R.id.tvLugarFechaNacimiento);
        TextView tvEdad = findViewById(R.id.tvEdad);
        TextView tvProfesion = findViewById(R.id.tvProfesion);
        TextView tvEstadoCivil = findViewById(R.id.tvEstadoCivil);

        //Datos usados para la empresa
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
        //tvIdEmpleado.setText(empleado.getId().toString());
        String fechaRegisto = DateFormat.getDateInstance().format(empleado.getFechaRegistro());
        tvFechaIngreso.setText(fechaRegisto);
        //tvCargo.setText(empleado.getCargo);
        tvOcupacion.setText(empleado.getOcupacion());

        Identifiers.mostrarOcultarTabsMenu(tvDatosPersonales, lyDatosPersonales);
        Identifiers.mostrarOcultarTabsMenu(tvDatosDeEmpresa, lyDatosEmpresa);
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
    private void cerrarSesion(){
        SessionManager sesion = new SessionManager(getApplicationContext());
        sesion.cerrarSesion(getApplicationContext());
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
    }

    /*
     * Lleva a la ventana de HistorialConsultaMedicaActivity y envia el id del empleado
     * */
    public void aperturaHistorialConsultaMedica(View v) {
        //Envia el id del empleado a HistorialConsultaMedicaActivity
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
        inSignos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inSignos);
    }

    /*
     * Lleva a la ventana de HistorialPermisosMedicosParticulares y envia el id del empleado
     * */
    public void aperturaHistorialPermisosParticulares(View v){
        //Envia el id del empleado a HistorialAtencionEnfermeria
        Intent inPermisosMedicos = new Intent(getApplicationContext(), HistorialPermisoParticularActivity.class);
        inPermisosMedicos.putExtra("ID_EMPLEADO", idEmpleado);
        startActivity(inPermisosMedicos);
    }

}