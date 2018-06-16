package com.example.cltcontrol.historialmedico.utils;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.activities.MainActivity;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmpleadoController {

    //Atributos
    private List<Empleado> misListaEmpleados;
    private List<Usuario> misListaUsuarios;
    private Empleado misEmpleados;
    private Usuario misUsuarios;
    private MainActivity miActivity;
    private Date fecha_actual;
    private Usuario usu_doctor, usu_enfermera;

    //Constructores
    public EmpleadoController() {

    }

    public EmpleadoController(List<Empleado> misListaEmpleados, MainActivity miActivity) {
        this.misListaEmpleados = misListaEmpleados;
        this.miActivity = miActivity;
    }

    //Metodos
    //Setter de registros Usuarios y Empleados
    public void llenadoUsuarios(){

        misUsuarios = new Usuario("dgarcia","dgarcia");
        misUsuarios.save();

        misUsuarios = new Usuario("anni","anni");
        misUsuarios.save();
    }

    public void llenadoEmpleados(){

        misEmpleados = new Empleado("03214567323","Jorge","García García",
                "jorergar@espol.edu.ec","FAE",
                "Analista de datos","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Seguridad",fecha_actual,fecha_actual,30,R.drawable.modelo, usu_doctor);
        misEmpleados.save();

        misEmpleados = new Empleado("0967547365","Anni","Santacruz Hernández",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "Dept. de Sistemas",fecha_actual,fecha_actual,20,R.drawable.modelo,usu_enfermera);
        misEmpleados.save();

        misEmpleados = new Empleado("0913620589","Renato","Illescas Rodríguez",
                "rillesca@espol.edu.ec","Sauces",
                "Licenciado en Redes","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Redes",fecha_actual,fecha_actual,20,R.drawable.modelo);
        misEmpleados.save();

        misEmpleados = new Empleado("0962983345","Daniel","Castro Peñafiel",
                "danijo@espol.edu.ec","Sauces",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Dept. Desarrollo de Software",fecha_actual,fecha_actual,20,R.drawable.modelo);
        misEmpleados.save();
    }

    //conteo cantidad de registros en la lista
    public int countItemLista(List<?> miLista){
        return miLista.size();
    }

    //Getter de registros Usuarios y Empleados
    public List<Usuario> getUsuarios() {
        misListaUsuarios = misUsuarios.listAll(Usuario.class);
        if(misListaUsuarios.isEmpty()){
            //
        }
        return misListaUsuarios;
    }

    public List<Empleado> getEmpleados() {
        misListaEmpleados = misEmpleados.listAll(Empleado.class);
        if (misListaEmpleados.isEmpty()) {
            //
            //inicializarVariablesTemp();
            //llenarEnfermedades(miActivity);
        }
        return misListaEmpleados;
    }

    //Show de registros Usuarios y Empleados
    public void mostrarEmpleados(List<Empleado> misListaEmpleados) {
        for (Empleado verEmpleados : misListaEmpleados) {
            Log.e("aqui nombre", verEmpleados.getApellido());
        }
    }

    public void mostrarUsuarios(List<Usuario> misListaUsuarios) {
        for (Usuario verUsuarios : misListaUsuarios) {
            Log.e("aqui usuario", verUsuarios.getUsuario());
        }
    }

    //funcionalidad ingreso
    public Boolean validarIngreso(List<Usuario> miLista, String etUsuario, String etContrasenia){

        for(Usuario buscarUsuarios: miLista){
            if((buscarUsuarios.getUsuario().equals(etUsuario)) && (buscarUsuarios.getContrasenia().equals(etContrasenia))) {
                return true;
            }/*
            else
                Toast.makeText(miActivity.getApplicationContext(), "Usuario y/o contraseña incorrecto",Toast.LENGTH_SHORT).show();
        */}
        return false;
    }

    /*private void inicializarVariablesTemp() {
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

        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov1", "rev1", "pres1", "ex1", "motivo1");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov2", "rev2", "pres2", "ex2", "motivo2");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov3", "rev3", "pres3", "ex3", "motivo3");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov4", "rev4", "pres4", "ex4", "motivo4");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov5", "rev5", "pres5", "ex5", "motivo5");
        consulta.save();
        consulta = new ConsultaMedica(emp_temp, fecha_actual, "prov6", "rev6", "pres6", "ex6", "motivo6");
        consulta.save();

        emp_temp = new Empleado("03214567323", "Jorge", "García García",
                "jorergar@espol.edu.ec", "FAE",
                "Analista de datos", "Soltero",
                "Masculino", "Guayaquil",
                "Dept. de Seguridad", fecha_actual, fecha_actual, 30, R.drawable.modelo, usu_doctor);
        emp_temp.save();

        emp_temp = new Empleado("0967547365", "Anni", "Santacruz Hernández",
                "anrosant@espol.edu.ec", "Sauces",
                "Ingeniera en Ciencias Computacionales", "Soltera",
                "Femenino", "Guayaquil",
                "Dept. de Sistemas", fecha_actual, fecha_actual, 20, R.drawable.modelo, usu_enfermera);
        emp_temp.save();

        emp_temp = new Empleado("0913620589", "Renato", "Illescas Rodríguez",
                "rillesca@espol.edu.ec", "Sauces",
                "Licenciado en Redes", "Soltero",
                "Masculino", "Guayaquil",
                "Dept. de Redes", fecha_actual, fecha_actual, 20, R.drawable.modelo);
        emp_temp.save();

        emp_temp = new Empleado("0962983345", "Daniel", "Castro Peñafiel",
                "danijo@espol.edu.ec", "Sauces",
                "Ingeniero en Ciencias Computacionales", "Soltero",
                "Masculino", "Guayaquil",
                "Dept. Desarrollo de Software", fecha_actual, fecha_actual, 20, R.drawable.modelo);
        emp_temp.save();

    }*/

    public void llenarEnfermedades(MainActivity miActivity) {
        List<Enfermedad> enfermedades = Enfermedad.find(Enfermedad.class, "CODIGO = ?", "A00");

        if (enfermedades.isEmpty()) {
            try {
                Toast.makeText(miActivity.getApplicationContext(), "Llenando Enfermedades", Toast.LENGTH_LONG).show();
                Enfermedad.executeQuery(EnfermedadesSQL.REGISTRO_ENFERMEDADES);
            } catch (Exception e) {
                Toast.makeText(miActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
