package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class DiagnosticoEnfermedad extends SugarRecord{
    private Diagnostico diagnostico;
    private Enfermedad enfermedad;

    public DiagnosticoEnfermedad() {
    }

    public DiagnosticoEnfermedad(Diagnostico diagnostico, Enfermedad enfermedad) {
        this.diagnostico = diagnostico;
        this.enfermedad = enfermedad;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Enfermedad getConsultaMedica() {
        return enfermedad;
    }

    public void setConsultaMedica(Enfermedad consultaMedica) {
        this.enfermedad = consultaMedica;
    }
}
