package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class AtencionEnfermeria extends SugarRecord {
    private Empleado empleado;
    private Date fecha_atencion;
    private String motivo_atencion, diagnostico_enfermeria, plan_cuidados;

    public AtencionEnfermeria() {
    }

    public AtencionEnfermeria(Empleado empleado, Date fecha_atencion, String motivo_atencion, String diagnostico_enfermeria, String plan_cuidados) {
        this.empleado = empleado;
        this.fecha_atencion = fecha_atencion;
        this.motivo_atencion = motivo_atencion;
        this.diagnostico_enfermeria = diagnostico_enfermeria;
        this.plan_cuidados = plan_cuidados;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFecha_atencion() {
        return fecha_atencion;
    }

    public void setFecha_atencion(Date fecha_atencion) {
        this.fecha_atencion = fecha_atencion;
    }

    public String getMotivo_atencion() {
        return motivo_atencion;
    }

    public void setMotivo_atencion(String motivo_atencion) {
        this.motivo_atencion = motivo_atencion;
    }

    public String getDiagnostico_enfermeria() {
        return diagnostico_enfermeria;
    }

    public void setDiagnostico_enfermeria(String diagnostico_enfermeria) {
        this.diagnostico_enfermeria = diagnostico_enfermeria;
    }

    public String getPlan_cuidados() {
        return plan_cuidados;
    }

    public void setPlan_cuidados(String plan_cuidados) {
        this.plan_cuidados = plan_cuidados;
    }
}
