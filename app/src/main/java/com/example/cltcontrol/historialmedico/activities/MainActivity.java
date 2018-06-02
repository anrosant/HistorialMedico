package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.facebook.stetho.Stetho;
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

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        //Almacena datos temporales
        //inicializarVariablesTemp();

    }
    /*
    * Verifica si el usuario y contrasenia son correctos
    * si lo son, va a la funcion aperturarBusqeuda
    * Caso contrario, imprime un mensaje de error*/
    public void ingresoSistema(View v){
        String nombreUsuario = etUsuario.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        //Usuario usuario;

        List<Usuario> usuarios = Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?",nombreUsuario, contrasenia);

        if(!usuarios.isEmpty()){
            //usuario = usuarios.get(0);
            aperturaBusqueda();
            Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
        }else{
            etContrasenia.setText("");
            Toast.makeText(getApplicationContext(), "No se encuentra el usuario",Toast.LENGTH_SHORT).show();
        }
    }

    //Ingresa a BuscarEmpleadoActivity
    private void aperturaBusqueda(){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

    private void inicializarVariablesTemp(){
        Date fecha_actual = new Date();

        Usuario userTemp = new Usuario();
        userTemp.setUsuario("a");
        userTemp.setContrasenia("a");
        userTemp.save();

        Empleado empTemp=new Empleado("03214567323","Jorge","García",
                "jorergar@espol.edu.ec","FAE",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Sistemas",fecha_actual,fecha_actual,30,R.drawable.modelo);
        empTemp.save();


        Empleado empTemp2=new Empleado("0967547365","Anni","Santacruz",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "enfermera",fecha_actual,fecha_actual,20,R.drawable.modelo,userTemp);
        empTemp2.save();

        Date fecha = new Date();

        AtencionEnfermeria atencion1 = new AtencionEnfermeria(fecha,empTemp,"Dolor de estomago",
                "Tiene un dolor en la boca del estomago","Tomar mucha agua, con paracetamol, y descanso");
        atencion1.save();
        AtencionEnfermeria atencion2 = new AtencionEnfermeria(fecha,empTemp,"Dolor",
                "Tiene un dolor","Tomar mucha agua, con paracetamol");
        atencion2.save();

        ConsultaMedica consultaMedica1 = new ConsultaMedica(empTemp,fecha_actual,"prov1","rev1","pres1");
        consultaMedica1.save();
        ConsultaMedica consultaMedica2 = new ConsultaMedica(empTemp,fecha_actual,"prov2","rev2","pres2");
        consultaMedica2.save();
        ConsultaMedica consultaMedica3 = new ConsultaMedica(empTemp,fecha_actual,"prov3","rev3","pres3");
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
