package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class PatologiasPersonales extends SugarRecord {
    @Unique
    private int id_serv;
    private ConsultaMedica consulta_medica;
    private String lugar, detalle;
    private int status;

    public PatologiasPersonales() { }

    public PatologiasPersonales(ConsultaMedica consulta_medica, String lugar, String detalle) {
        this.consulta_medica = consulta_medica;
        this.lugar = lugar;
        this.detalle = detalle;
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
    public ConsultaMedica getConsultaMedica() {
        return consulta_medica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consulta_medica = consultaMedica;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
