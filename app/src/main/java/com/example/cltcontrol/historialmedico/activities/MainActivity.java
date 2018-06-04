package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;
//import com.facebook.stetho.Stetho;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);

        /*Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());*/

        //Almacena datos temporales
        inicializarVariablesTemp();

    }
    /*
    * Verifica si el usuario y contrasenia son correctos
    * si lo son, va a la funcion aperturarBusqeuda
    * Caso contrario, imprime un mensaje de error*/
    public void ingresoSistema(View v){
        String nombreUsuario = etUsuario.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        //Usuario usuario;

        List<Usuario> usuarios = Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?", nombreUsuario, contrasenia);

        if(!usuarios.isEmpty()){
            aperturaBusqueda(usuarios.get(0).getId());
        } else {
            etUsuario.setText("");
            etContrasenia.setText("");
            Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrecto",Toast.LENGTH_SHORT).show();
        }
    }

    //Ingresa a BuscarEmpleadoActivity
    private void aperturaBusqueda(Long usu_id){
        SessionManager sesion = new SessionManager(getApplicationContext());
        sesion.crearSesion(usu_id);
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

    private void inicializarVariablesTemp(){
        Date fecha_actual = new Date();
        Usuario usu_doctor = new Usuario();
        Usuario usu_enfermera = new Usuario();
        usu_doctor.setUsuario("j");
        usu_doctor.setContrasenia("j");
        usu_doctor.save();
        usu_enfermera.setUsuario("a");
        usu_enfermera.setContrasenia("a");
        usu_enfermera.save();

        Empleado empTemp=new Empleado("03214567323","Jorge","García",
                "jorergar@espol.edu.ec","FAE",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Doctor",fecha_actual,fecha_actual,30,R.drawable.modelo, usu_doctor);
        empTemp.save();


        Empleado empTemp2=new Empleado("0967547365","Anni","Santacruz",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "Enfermera",fecha_actual,fecha_actual,20,R.drawable.modelo,usu_enfermera);
        empTemp2.save();

        Date fecha = new Date();

        AtencionEnfermeria atencion1 = new AtencionEnfermeria(fecha,empTemp,"Dolor de estomago",
                "Tiene un dolor en la boca del estomago","Tomar mucha agua, con paracetamol, y descanso");
        atencion1.save();
        AtencionEnfermeria atencion2 = new AtencionEnfermeria(fecha,empTemp,"Dolor",
                "Tiene un dolor","Tomar mucha agua, con paracetamol");
        atencion2.save();

        ConsultaMedica consultaMedica1 = new ConsultaMedica(empTemp,fecha_actual,"prov1","rev1","pres1", "ex1","motivo1");
        consultaMedica1.save();
        ConsultaMedica consultaMedica2 = new ConsultaMedica(empTemp,fecha_actual,"prov2","rev2","pres2","ex2", "motivo2");
        consultaMedica2.save();
        ConsultaMedica consultaMedica3 = new ConsultaMedica(empTemp,fecha_actual,"prov3","rev3","pres3", "ex3","motivo3");
        consultaMedica3.save();
        Enfermedad enf1=new Enfermedad("enf1","111");
        enf1.save();
        Enfermedad enf2=new Enfermedad("enf2","121");
        enf2.save();
        Enfermedad enf3=new Enfermedad("enf3","131");
        enf3.save();
        Enfermedad enf4=new Enfermedad("enf4","141");
        enf4.save();

    }

}
