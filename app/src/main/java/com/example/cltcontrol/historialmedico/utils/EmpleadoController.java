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

    //Constructores
    public EmpleadoController(){ }

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