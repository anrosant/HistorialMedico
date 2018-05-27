package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class Diagnostico extends SugarRecord{
    private String descripcion;
    private ConsultaMedica consultaMedica;

    public Diagnostico() {
    }

    public Diagnostico(String descripcion, ConsultaMedica consultaMedica) {
        this.descripcion = descripcion;
        this.consultaMedica = consultaMedica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ConsultaMedica getConsultaMedica() {
        return consultaMedica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consultaMedica = consultaMedica;
    }
}
