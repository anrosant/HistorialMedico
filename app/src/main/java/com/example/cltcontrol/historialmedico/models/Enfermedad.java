package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Enfermedad extends SugarRecord{
    private int id_serv;
    private String codigo;
    private String nombre;
    private String grupo;
    private int status;

    public Enfermedad(){
    }

    public Enfermedad(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Enfermedad(String codigo, String nombre, String grupo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.grupo = grupo;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
