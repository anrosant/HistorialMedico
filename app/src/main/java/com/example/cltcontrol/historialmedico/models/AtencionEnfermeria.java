package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class AtencionEnfermeria extends SugarRecord {
    private Date fechaAtencion;
    //private String cedulaEmpleado;
    private Empleado empleado;
    private String motivoAtencion, diagnosticoEnfermeria, planCuidados;

    public AtencionEnfermeria() {
    }

    public AtencionEnfermeria(Date fecha_atencion, Empleado empleado, String motivoAtencion, String diagnosticoEnfermeria, String planCuidados) {
        this.fechaAtencion = fecha_atencion;
        this.empleado = empleado;
        this.motivoAtencion = motivoAtencion;
        this.diagnosticoEnfermeria = diagnosticoEnfermeria;
        this.planCuidados = planCuidados;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFecha_atencion() {
        return fechaAtencion;
    }

    public void setFecha_atencion(Date fecha_atencion) {
        this.fechaAtencion = fecha_atencion;
    }

    public String getMotivoAtencion() {
        return motivoAtencion;
    }

    public void setMotivoAtencion(String motivoAtencion) {
        this.motivoAtencion = motivoAtencion;
    }

    public String getDiagnosticoEnfermeria() {
        return diagnosticoEnfermeria;
    }

    public void setDiagnosticoEnfermeria(String diagnosticoEnfermeria) {
        this.diagnosticoEnfermeria = diagnosticoEnfermeria;
    }

    public String getPlanCuidados() {
        return planCuidados;
    }

    public void setPlanCuidados(String planCuidados) {
        this.planCuidados = planCuidados;
    }
}
