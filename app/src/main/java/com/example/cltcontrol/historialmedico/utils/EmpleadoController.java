package com.example.cltcontrol.historialmedico.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EmpleadoController {
    private Iterator<Empleado> lista_empleados;
    private List<Usuario> lista_usuarios;
    private Context miActivity;
    private Usuario usuario_doctor, usuario_enfermera;
    Empleado empleado_doctor, empleado_enfermera;

    //Constructores
    public EmpleadoController(){
        /*Iterator<Empleado> lista_empleados = Empleado.findAll(Empleado.class);
        Iterator<Usuario> lista_usuarios = Usuario.findAll(Usuario.class);
        if(!lista_empleados.hasNext() && !lista_usuarios.hasNext()){
            this.llenadoUsuarios();
            this.llenadoEmpleados();

        }*/
    }

    public EmpleadoController(Context miActivity) {
        this.miActivity = miActivity;
        /*Iterator<Empleado> lista_empleados = Empleado.findAll(Empleado.class);
        Iterator<Usuario> lista_usuarios = Usuario.findAll(Usuario.class);
        if(!lista_empleados.hasNext() && !lista_usuarios.hasNext()){
            this.llenadoUsuarios();
            this.llenadoEmpleados();

        }*/
    }

    //Getter de registros Usuarios y Empleados
    public Iterator<Empleado> getLista_empleados() {
        return lista_empleados;
    }

    public List<Usuario> getLista_usuarios() {
        return lista_usuarios;
    }

    public void setLista_empleados(Iterator<Empleado> lista_empleados) {
        this.lista_empleados = lista_empleados;
    }

    public void setLista_usuarios(List<Usuario> lista_usuarios) {
        this.lista_usuarios = lista_usuarios;
    }

    //conteo cantidad de registros en la lista
    public int countItemLista(List<?> miLista){
        return miLista.size();
    }

    //Metodos
    //Setter de registros Usuarios y Empleados
    private void llenadoUsuarios(){
        usuario_doctor = new Usuario("jgarcia");
        usuario_doctor.save();
        this.setLista_usuarios(Usuario.listAll(Usuario.class));
    }

    /*
     * Crea empleados temporales
     * */
    private void llenadoEmpleados(){
        empleado_doctor = new Empleado("03214567323", "Jorge", "García García",
                "jorergar@espol.edu.ec", "FAE",
                "Analista de datos", "Soltero",
                "Masculino", "Guayaquil",
                "Doctor", new Date(), new Date(), 30, R.drawable.modelo, usuario_doctor);
        empleado_doctor.save();

        /*empleado_enfermera = new Empleado("0967547365","Anni","Santacruz Hernández",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "Enfermera",new Date(),new Date(),20,R.drawable.modelo, usuario_enfermera);
        empleado_enfermera.save();

        Empleado miEmpleado3 = new Empleado("0913620589","Renato","Illescas Rodríguez",
                "rillesca@espol.edu.ec","Sauces",
                "Licenciado en Redes","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Redes",new Date(),new Date(),20,R.drawable.modelo);
        miEmpleado3.save();

        Empleado miEmpleado4 = new Empleado("0962983345","Daniel","Castro Peñafiel",
                "danijo@espol.edu.ec","Sauces",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Dept. Desarrollo de Software",new Date(),new Date(),20,R.drawable.modelo);
        miEmpleado4.save();*/


        //this.setLista_empleados(Empleado.findAll(Empleado.class));
    }

    /*
     * Guarda las enfermedades en una lista
     * */
    public void llenadoEnfermedades() {
        List<Enfermedad> enfermedades = Enfermedad.find(Enfermedad.class, "CODIGO = ?", "A00");
        if (enfermedades.isEmpty()) {
            try {
                Enfermedad.executeQuery(EnfermedadesSQL.REGISTRO_ENFERMEDADES);
            } catch (Exception e) {}
        }
    }
    /*
     * Valida el ingreso al sistema
     * */
    public Boolean ingresoSistema(String etUsuario, String etContrasenia){
        return validarIngreso(etUsuario, etContrasenia);
    }

    /*
     * funcionalidad ingreso
     * */
    private Boolean validarIngreso(String etUsuario, String etContrasenia){
        return obtenerUsuario(etUsuario, etContrasenia).size() != 0;
    }

    /*
     * Busqueda de un Usuario
     * */
    private List<Usuario> obtenerUsuario(String etUsuario, String etContrasenia){
        return Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?", etUsuario, etContrasenia);
    }

}