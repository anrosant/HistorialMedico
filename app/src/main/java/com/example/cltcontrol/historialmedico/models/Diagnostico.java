package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Diagnostico extends SugarRecord{
    @Unique
    private int id_serv;
    private ConsultaMedica consulta_medica;
    private Enfermedad enfermedad;
    private String tipoEnfermedad;
    private int status;

    public Diagnostico() {
    }

    public Diagnostico(ConsultaMedica consultaMedica, Enfermedad enfermedad, String tipoEnfermedad) {
        this.consulta_medica = consultaMedica;
        this.enfermedad = enfermedad;
        this.tipoEnfermedad = tipoEnfermedad;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public ConsultaMedica getConsulta_medica() {
        return consulta_medica;
    }

    public void setConsulta_medica(ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getTipoEnfermedad() {
        return tipoEnfermedad;
    }

    public void setTipoEnfermedad(String tipoEnfermedad) {
        this.tipoEnfermedad = tipoEnfermedad;
    }
}
