package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class ConsultaMedica extends SugarRecord{
    private Empleado empleado;
    //private String cedulaEmpleado;
    private Date fechaConsulta;
    private String probActual, revAparatos, prescripcion;

    public ConsultaMedica() {
    }

    public ConsultaMedica(Empleado empleado, Date fechaConsulta, String probActual, String revAparatos, String prescripcion) {
        this.empleado = empleado;
        this.fechaConsulta = fechaConsulta;
        this.probActual = probActual;
        this.revAparatos = revAparatos;
        this.prescripcion = prescripcion;
    }

    public Empleado getCedulaEmpleado() {
        return empleado;
    }

    public void setCedulaEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getProbActual() {
        return probActual;
    }

    public void setProbActual(String probActual) {
        this.probActual = probActual;
    }

    public String getRevAparatos() {
        return revAparatos;
    }

    public void setRevAparatos(String revAparatos) {
        this.revAparatos = revAparatos;
    }

    public String getPrescripcion() {
        return prescripcion;
    }

    public void setPrescripcion(String prescripcion) {
        this.prescripcion = prescripcion;
    }
}
