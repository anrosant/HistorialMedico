package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class PatologiasPersonales extends SugarRecord {
    private ConsultaMedica consulta_medica;
    private String lugar, detalle;

    public PatologiasPersonales() { }

    public PatologiasPersonales(ConsultaMedica consulta_medica, String lugar, String detalle) {
        this.consulta_medica = consulta_medica;
        this.lugar = lugar;
        this.detalle = detalle;
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
