package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Usuario extends SugarRecord {

    // ATRIBUTOS DE CLASE
    @Unique
    private int id_serv, status;
    private String usuario;
    //private String contrasenia;
    //pensar si se necesita tener atributo tipo de usuario (medico o enfermera)

    // CONSTRUCTORES DE CLASE
    public Usuario() {
        super();
    }

    public Usuario(String usuario) {
        this.usuario = usuario;
        //this.contrasenia = contrasenia;
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

    /*public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }*/
}
