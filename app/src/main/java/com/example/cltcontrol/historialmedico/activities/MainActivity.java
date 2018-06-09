package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.utils.EnfermedadesSQL;
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

        //Almacena datos temporales solo si es que no existen datos
        List<Empleado> referencia = Empleado.listAll(Empleado.class);
        if(referencia.isEmpty()){
            Toast.makeText(getApplicationContext(),"Inicializando datos",Toast.LENGTH_SHORT).show();
            inicializarVariablesTemp();
            llenarEnfermedades();
        }
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
        Empleado emp_temp;
        ConsultaMedica consulta;
        usu_doctor.setUsuario("jorgegarcia");
        usu_doctor.setContrasenia("jorgegarcia");
        usu_doctor.save();
        usu_enfermera.setUsuario("anni1997");
        usu_enfermera.setContrasenia("anni1997");
        usu_enfermera.save();

        emp_temp = new Empleado("03214567323","Jorge","García García",
                "jorergar@espol.edu.ec","FAE",
                "Analista de datos","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Seguridad",fecha_actual,fecha_actual,30,R.drawable.modelo, usu_doctor);
        emp_temp.save();

        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov1","rev1","pres1", "ex1","motivo1");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov2","rev2","pres2","ex2", "motivo2");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov3","rev3","pres3", "ex3","motivo3");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov4","rev4","pres4", "ex4","motivo4");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov5","rev5","pres5","ex5", "motivo5");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp,fecha_actual,"prov6","rev6","pres6", "ex6","motivo6");
        consulta.save();

        emp_temp = new Empleado("0967547365","Anni","Santacruz Hernández",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "Dept. de Sistemas",fecha_actual,fecha_actual,20,R.drawable.modelo,usu_enfermera);
        emp_temp.save();

        emp_temp = new Empleado("0913620589","Renato","Illescas Rodríguez",
                "rillesca@espol.edu.ec","Sauces",
                "Licenciado en Redes","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Redes",fecha_actual,fecha_actual,20,R.drawable.modelo);
        emp_temp.save();

        emp_temp = new Empleado("0962983345","Daniel","Castro Peñafiel",
                "danijo@espol.edu.ec","Sauces",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Dept. Desarrollo de Software",fecha_actual,fecha_actual,20,R.drawable.modelo);
        emp_temp.save();
    }

    public void llenarEnfermedades(){
        List<Enfermedad> enfermedades = Enfermedad.find(Enfermedad.class, "CODIGO = ?","A00");

        if(enfermedades.isEmpty()){
            try{
                Toast.makeText(getApplicationContext(),"Llenando Enfermedades",Toast.LENGTH_LONG).show();
                Enfermedad.executeQuery(EnfermedadesSQL.REGISTRO_ENFERMEDADES);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

}
