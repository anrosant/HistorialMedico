package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class Diagnostico extends SugarRecord{
    private String descripcion;
    private ConsultaMedica consulta_medica;

    public Diagnostico() {
    }

    public Diagnostico(String descripcion, ConsultaMedica consulta_medica) {
        this.descripcion = descripcion;
        this.consulta_medica = consulta_medica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ConsultaMedica getConsulta_medica() {
        return consulta_medica;
    }

    public void setConsulta_medica(ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
    }
}
