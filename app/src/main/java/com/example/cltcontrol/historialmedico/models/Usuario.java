package com.example.cltcontrol.historialmedico.models;

import java.util.Date;

public class Usuario extends Persona {
    private String usuario, contrasenia, correo;

    public Usuario(){
        super();
    }

    public Usuario(String cedula, String nombre, String apellido, String direccion,
                   Date fecha_ingreso) {
        super(cedula, nombre, apellido, direccion, fecha_ingreso);
    }

    public Usuario(String cedula, String nombre, String apellido, String direccion,
                   Date fecha_ingreso, String usuario,
                   String contrasenia, String correo) {
        super(cedula, nombre, apellido, direccion, fecha_ingreso);
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
