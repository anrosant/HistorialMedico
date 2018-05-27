package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Date;

public class Usuario extends SugarRecord {

    // ATRIBUTOS DE CLASE
    @Unique
    private String usuario;
    private String contrasenia;
    //pensar si se necesita tener atributo tipo de usuario (medico o enfermera)

    // CONSTRUCTORES DE CLASE
    public Usuario() {
        super();
    }

    public Usuario(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
