package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class SignosVitales extends SugarRecord {
    private int presion_sistolica, presion_distolica, pulso, temperatura;
    private ConsultaMedica consultaMedica;
    private AtencionEnfermeria atencionEnfermeria;

    public SignosVitales() {
    }

    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, int temperatura, ConsultaMedica consultaMedica) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.consultaMedica = consultaMedica;
    }
    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, int temperatura, AtencionEnfermeria atencionEnfermeria) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.atencionEnfermeria = atencionEnfermeria;
    }
    public int getPresion_sistolica() {
        return presion_sistolica;
    }

    public void setPresion_sistolica(int presion_sistolica) {
        this.presion_sistolica = presion_sistolica;
    }

    public int getPresion_distolica() {
        return presion_distolica;
    }

    public void setPresion_distolica(int presion_distolica) {
        this.presion_distolica = presion_distolica;
    }

    public int getPulso() {
        return pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public ConsultaMedica getConsultaMedica() {
        return consultaMedica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consultaMedica = consultaMedica;
    }

    public AtencionEnfermeria getAtencionEnfermeria() {
        return atencionEnfermeria;
    }

    public void setAtencionEnfermeria(AtencionEnfermeria atencionEnfermeria) {
        this.atencionEnfermeria = atencionEnfermeria;
    }
}
