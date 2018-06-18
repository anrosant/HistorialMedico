package com.example.cltcontrol.historialmedico.models;

import android.content.Context;
import android.widget.Toast;

import com.orm.SugarRecord;

public class SignosVitales extends SugarRecord {
    private int presion_sistolica, presion_distolica, pulso;
    private float temperatura;
    private ConsultaMedica consulta_medica;
    private AtencionEnfermeria atencion_enfermeria;

    public SignosVitales() {}

    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, ConsultaMedica consultaMedica) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.consulta_medica = consultaMedica;
    }

    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, AtencionEnfermeria atencion_enfermeria) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.atencion_enfermeria = atencion_enfermeria;
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

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public ConsultaMedica getConsultaMedica() {
        return consulta_medica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consulta_medica = consultaMedica;
    }

    public AtencionEnfermeria getAtencion_enfermeria() {
        return atencion_enfermeria;
    }

    public void setAtencion_enfermeria(AtencionEnfermeria atencion_enfermeria) {
        this.atencion_enfermeria = atencion_enfermeria;
    }

    public int validarSignos(String presion_sistolica, String presion_distolica, String pulso, String temperatura){
        if(presion_sistolica.equals("") || presion_distolica.equals("") || temperatura.equals("") || pulso.equals(""))
            return 0;
        try {
            int presionSistolica = Integer.parseInt(presion_sistolica);
            int presionDistolica = Integer.parseInt(presion_distolica);
            float temp = Float.parseFloat(temperatura);
            int pul = Integer.parseInt(pulso);
            if(presionSistolica < 100 || presionSistolica > 135 || presionDistolica < 70 || presionDistolica > 90 ||
                    pul < 60 || pul > 100 || temp < 34 || temp > 43)
                return 1;
        } catch(Exception e){
            return 2;
        }
        return 3;
    }

}