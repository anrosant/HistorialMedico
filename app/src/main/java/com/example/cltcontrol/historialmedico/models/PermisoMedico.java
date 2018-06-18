package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class PermisoMedico extends SugarRecord{
    private Diagnostico diagnostico;
    private Date fecha_inicio, fecha_fin;
    private int dias_permiso;
    private String obsevaciones_permiso;
    private ConsultaMedica consulta_medica;

    public PermisoMedico(){}

    public PermisoMedico(Diagnostico diagnostico, Date fecha_inicio, Date fecha_fin, int dias_permiso, String obsevaciones_permiso, ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
        this.diagnostico = diagnostico;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.dias_permiso = dias_permiso;
        this.obsevaciones_permiso = obsevaciones_permiso;
    }

    public ConsultaMedica getConsulta_medica() {
        return consulta_medica;
    }

    public void setConsulta_medica(ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getDias_permiso() {
        return dias_permiso;
    }

    public void setDias_permiso(int dias_permiso) {
        this.dias_permiso = dias_permiso;
    }

    public String getObsevaciones_permiso() {
        return obsevaciones_permiso;
    }

    public void setObsevaciones_permiso(String obsevaciones_permiso) {
        this.obsevaciones_permiso = obsevaciones_permiso;
    }

    public void validarPermiso(){

    }

}