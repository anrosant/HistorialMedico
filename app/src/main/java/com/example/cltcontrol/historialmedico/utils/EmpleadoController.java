package com.example.cltcontrol.historialmedico.utils;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.activities.BuscarEmpleadoActivity;
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

    public void llenadoEnfermedades(MainActivity miActivity) {
        List<Enfermedad> enfermedades = Enfermedad.find(Enfermedad.class, "CODIGO = ?", "A00");
        if (enfermedades.isEmpty()) {
            try {
                Toast.makeText(miActivity.getApplicationContext(), "Llenando Enfermedades", Toast.LENGTH_LONG).show();
                Enfermedad.executeQuery(EnfermedadesSQL.REGISTRO_ENFERMEDADES);
            } catch (Exception e) {
                Toast.makeText(miActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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

    //Busqueda de un Usuario
    public List<Usuario> obtenerUsuario(List<Usuario> misListaUsuarios, String etUsuario, String etContrasenia){
        List<Usuario> encontrado = null;
        for(Usuario buscarUsuario: misListaUsuarios){
            encontrado = Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?", etUsuario, etContrasenia);
            //aperturaBusqueda(encontrado.get(0).getId());
        }
        return encontrado;
    }

    //funcionalidad ingreso
    public Boolean validarIngreso(List<Usuario> misListaUsuarios, String etUsuario, String etContrasenia){
        if(obtenerUsuario(misListaUsuarios,etUsuario,etContrasenia).size()==0){
            return true;
        }
        return false;
    }

    public void ingresoSistema(List<Usuario> misListaUsuarios, String etUsuario, String etContrasenia){
        if(validarIngreso(misListaUsuarios,etUsuario,etContrasenia)){
            aperturaBusqueda(obtenerUsuario(misListaUsuarios,etUsuario,etContrasenia).get(0).getId());
            //Log.e("validarIngreso",""+validarIngreso(misListaUsuarios,etUsuario,etContrasenia));
            //Toast.makeText(miActivity.getApplicationContext(), "Acceso Sistema",Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(miActivity.getApplicationContext(), "Usuario y/o contraseña incorrecto",Toast.LENGTH_SHORT).show();
        }
    }

    public void aperturaBusqueda(Long idUsuario){
        SessionManager sesion = new SessionManager(miActivity.getApplicationContext());
        sesion.crearSesion(idUsuario);
    }

}
