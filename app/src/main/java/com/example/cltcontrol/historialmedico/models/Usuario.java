package com.example.cltcontrol.historialmedico.models;

import java.util.Date;

public class Usuario extends Persona {
    private String nombreUsuario, contrasenia, correo;
    public Usuario(String cedula, String nombre, String apellido, String direccion,
                   Date fechaIngreso) {
        super(cedula, nombre, apellido, direccion, fechaIngreso);
    }

    public Usuario(String cedula, String nombre, String apellido, String direccion,
                   Date fechaIngreso, String usuario,
                   String contrasenia, String correo) {
        super(cedula, nombre, apellido, direccion, fechaIngreso);
        this.nombreUsuario = usuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
