package com.example.cltcontrol.historialmedico.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.ArrayList;

public class SignosVitales extends SugarRecord {
    @Unique
    private int id_serv;
    private int presion_sistolica, presion_distolica, pulso;
    private float temperatura;
    private ConsultaMedica consulta_medica;
    private AtencionEnfermeria atencion_enfermeria;
    private Empleado empleado;

    private int status;

    public SignosVitales() {}

    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, ConsultaMedica consultaMedica, int status) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.consulta_medica = consultaMedica;
        this.status = status;
    }

    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, AtencionEnfermeria atencion_enfermeria, int status) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.atencion_enfermeria = atencion_enfermeria;
        this.status = status;
    }
    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public ArrayList<SignosVitales> getSignosVitalesUnsynced(){
        ArrayList<SignosVitales> signosVitalesUnsynced = (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class, "status = ?", String.valueOf(0));
        return signosVitalesUnsynced;
    }

}