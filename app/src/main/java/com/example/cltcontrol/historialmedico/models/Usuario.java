package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Usuario extends SugarRecord {

    // ATRIBUTOS DE CLASE
    @Unique
    private int id_serv;

    private int status;
    private String usuario;
    private Empleado empleado;
    //private String contrasenia;
    //pensar si se necesita tener atributo tipo de usuario (medico o enfermera)

    // CONSTRUCTORES DE CLASE
    public Usuario() {
        super();
    }

    public Usuario(String usuario, Empleado empleado) {
        this.usuario = usuario;
        //this.contrasenia = contrasenia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

}
