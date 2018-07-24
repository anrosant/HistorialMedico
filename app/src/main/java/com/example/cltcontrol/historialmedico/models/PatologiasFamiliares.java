package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class PatologiasFamiliares extends SugarRecord {
    @Unique
    private int id_serv, id_ficha;

    private int status;
    private Enfermedad enfermedad;
    private String parentesco;

    public PatologiasFamiliares() {
    }

    public PatologiasFamiliares(Enfermedad enfermedad, String parentesco) {
        this.enfermedad = enfermedad;
        this.parentesco = parentesco;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
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

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
