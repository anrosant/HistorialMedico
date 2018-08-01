package com.example.cltcontrol.historialmedico.utils;

import android.content.Context;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EmpleadoController {
    private List<Usuario> lista_usuarios;
    private Context miActivity;
    private Usuario usuario_doctor;

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
    }

    //Metodos
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