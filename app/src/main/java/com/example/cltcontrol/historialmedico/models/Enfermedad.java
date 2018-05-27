package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Enfermedad extends SugarRecord{

    String codigo;
    String nombre;
    String grupo;

    public Enfermedad(){
    }

    public Enfermedad(String codigo, String nombre, String grupo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.grupo = grupo;
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
