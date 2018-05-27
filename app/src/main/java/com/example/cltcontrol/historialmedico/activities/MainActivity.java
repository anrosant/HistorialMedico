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
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.DiagnosticoEnfermedad;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.facebook.stetho.Stetho;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText etUsuario, etContrasenia;
    public Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        btnIngresar = findViewById(R.id.btnIngresar);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
        inicializarVariablesTemp();

    }

    public void ingresoSistema(View v){
        String nombreUsuario = etUsuario.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        Usuario usuario;

        List<Usuario> usuarios = Usuario.find(Usuario.class, "usuario = ?",nombreUsuario);

        if(!usuarios.isEmpty()){
            usuario = usuarios.get(0);
            if(usuario.getContrasenia().equals(contrasenia)){
                Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
                aperturaBusqueda();
            }else{
                Toast.makeText(getApplicationContext(), "Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                etContrasenia.setText("");
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se encuentra el usuario",Toast.LENGTH_SHORT).show();
        }
    }

    public void aperturaBusqueda(){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

    public void inicializarVariablesTemp(){
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

        AtencionEnfermeria atencion1 = new AtencionEnfermeria(fecha,"0967547365","Dolor de estomago",
                "Tiene un dolor en la boca del estomago","Tomar mucha agua, con paracetamol, y descanso");
        atencion1.save();
        AtencionEnfermeria atencion2 = new AtencionEnfermeria(fecha,"0967547365","Dolor",
                "Tiene un dolor","Tomar mucha agua, con paracetamol");
        atencion2.save();

        ConsultaMedica consultaMedica1 = new ConsultaMedica(empTemp.getCedula(),fecha_actual,"prov1","rev1","pres1");
        consultaMedica1.save();
        ConsultaMedica consultaMedica2 = new ConsultaMedica(empTemp.getCedula(),fecha_actual,"prov2","rev2","pres2");
        consultaMedica2.save();
        ConsultaMedica consultaMedica3 = new ConsultaMedica(empTemp2.getCedula(),fecha_actual,"prov3","rev3","pres3");
        consultaMedica3.save();
        Enfermedad enf1=new Enfermedad("111","enf1");
        enf1.save();
        Enfermedad enf2=new Enfermedad("121","enf2");
        enf2.save();
        Enfermedad enf3=new Enfermedad("131","enf3");
        enf3.save();
        Enfermedad enf4=new Enfermedad("141","enf4");
        enf4.save();

        Diagnostico diagn1=new Diagnostico("desc1", consultaMedica1);
        diagn1.save();
        Diagnostico diagn2=new Diagnostico("desc2", consultaMedica1);
        diagn1.save();
        Diagnostico diagn3=new Diagnostico("desc3", consultaMedica2);
        diagn3.save();

        DiagnosticoEnfermedad diagnosticoEnfermedad = new DiagnosticoEnfermedad(diagn1, enf1);
        diagnosticoEnfermedad.save();
        DiagnosticoEnfermedad diagnosticoEnfermedad2 = new DiagnosticoEnfermedad(diagn2, enf1);
        diagnosticoEnfermedad2.save();
        DiagnosticoEnfermedad diagnosticoEnfermedad3 = new DiagnosticoEnfermedad(diagn2, enf2);
        diagnosticoEnfermedad3.save();

    }

}
