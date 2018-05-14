package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.facebook.stetho.Stetho;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        Date fecha_actual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String nacimientoString = "01-25-1988";

        Usuario userTemp = new Usuario();
        userTemp.setCedula("0321456789");
        userTemp.setNombre("Jorge");
        userTemp.setApellido("García");
        userTemp.setUsuario("a");
        userTemp.setContrasenia("a");
        userTemp.setFoto(R.drawable.modelo);
        userTemp.setArea_trabajo("Sistemas");
        userTemp.setProfesion("Ingeniero en Ciencias Computacionales");
        userTemp.setCorreo("jorergar@espol.edu.ec");
        userTemp.setDireccion("FAE");
        userTemp.setEdad(30);
        userTemp.setEstado_civil("Soltero");
        userTemp.setRol("Empleado");
        userTemp.setLugar_nacimiento("Guayaquil");
        userTemp.setFecha_registro(fecha_actual);

        try { //Se necesita usar try catch por si el string de la fecha este mal construido
            Date fecha_nacimiento = sdf.parse(nacimientoString); //Aqui parsea el String de fecha de nacimiento de acuerdo al patron en el simpleDateFormat sdf
            userTemp.setFecha_nacimiento(fecha_nacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userTemp.save();

        Usuario userTemp2 = new Usuario();
        userTemp2.setCedula("03214567323");
        userTemp2.setNombre("Anni");
        userTemp2.setApellido("No se");
        userTemp2.setUsuario("b");
        userTemp2.setContrasenia("b");
        userTemp2.setFoto(R.drawable.modelo);
        userTemp2.setArea_trabajo("Sistemas");
        userTemp2.setProfesion("Ingeniero en Ciencias Computacionales");
        userTemp2.setCorreo("jorergar@espol.edu.ec");
        userTemp2.setDireccion("FAE");
        userTemp2.setEdad(30);
        userTemp2.setEstado_civil("Soltero");
        userTemp2.setRol("Medico");
        userTemp2.setLugar_nacimiento("Guayaquil");
        userTemp2.setFecha_registro(fecha_actual);

        try { //Se necesita usar try catch por si el string de la fecha este mal construido
            Date fecha_nacimiento = sdf.parse(nacimientoString); //Aqui parsea el String de fecha de nacimiento de acuerdo al patron en el simpleDateFormat sdf
            userTemp2.setFecha_nacimiento(fecha_nacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userTemp2.save();

        Usuario userTemp3 = new Usuario();
        userTemp3.setCedula("0322323");
        userTemp3.setNombre("Daniel");
        userTemp3.setApellido("No se");
        userTemp3.setUsuario("c");
        userTemp3.setContrasenia("c");
        userTemp3.setFoto(R.drawable.modelo);
        userTemp3.setArea_trabajo("Sistemas");
        userTemp3.setProfesion("Ingeniero en Ciencias Computacionales");
        userTemp3.setCorreo("jorergar@espol.edu.ec");
        userTemp3.setDireccion("FAE");
        userTemp3.setEdad(30);
        userTemp3.setEstado_civil("Soltero");
        userTemp3.setRol("Empleado");
        userTemp3.setLugar_nacimiento("Guayaquil");
        userTemp3.setFecha_registro(fecha_actual);

        try { //Se necesita usar try catch por si el string de la fecha este mal construido
            Date fecha_nacimiento = sdf.parse(nacimientoString); //Aqui parsea el String de fecha de nacimiento de acuerdo al patron en el simpleDateFormat sdf
            userTemp3.setFecha_nacimiento(fecha_nacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userTemp3.save();

        Usuario userTemp4 = new Usuario();
        userTemp4.setCedula("032245");
        userTemp4.setNombre("Renato");
        userTemp4.setApellido("No se");
        userTemp4.setUsuario("d");
        userTemp4.setContrasenia("d");
        userTemp4.setFoto(R.drawable.modelo);
        userTemp4.setArea_trabajo("Sistemas");
        userTemp4.setProfesion("Ingeniero en Ciencias Computacionales");
        userTemp4.setCorreo("jorergar@espol.edu.ec");
        userTemp4.setDireccion("FAE");
        userTemp4.setEdad(30);
        userTemp4.setEstado_civil("Soltero");
        userTemp4.setRol("Enfermera");
        userTemp4.setLugar_nacimiento("Guayaquil");
        userTemp4.setFecha_registro(fecha_actual);

        try { //Se necesita usar try catch por si el string de la fecha este mal construido
            Date fecha_nacimiento = sdf.parse(nacimientoString); //Aqui parsea el String de fecha de nacimiento de acuerdo al patron en el simpleDateFormat sdf
            userTemp4.setFecha_nacimiento(fecha_nacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userTemp4.save();



        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

    }

    public void ingresoSistema(View v){
        String nombreUsuario = etUsuario.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        Usuario usuario;

        List<Usuario> usuarios = Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?",nombreUsuario,contrasenia);

        if(!usuarios.isEmpty()){
            usuario = usuarios.get(0);
            if(usuario.getContrasenia().equals(contrasenia)){
                Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
                aperturaBusqueda(null);
            }else{
                Toast.makeText(getApplicationContext(), "Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                etContrasenia.setText("");
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se encuentra el usuario",Toast.LENGTH_SHORT).show();
        }
    }

    public void aperturaBusqueda(View v){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

}
