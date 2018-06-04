package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class Diagnostico extends SugarRecord{
    private ConsultaMedica consulta_medica;
    private Enfermedad enfermedad;
    private String tipoEnfermedad;

    public Diagnostico() {
    }

    public Diagnostico(ConsultaMedica consultaMedica, Enfermedad enfermedad, String tipoEnfermedad) {
        this.consulta_medica = consultaMedica;
        this.enfermedad = enfermedad;
        this.tipoEnfermedad = tipoEnfermedad;
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
