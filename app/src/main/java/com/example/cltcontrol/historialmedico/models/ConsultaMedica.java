package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class ConsultaMedica extends SugarRecord{
    private Empleado empleado;
    private Date fecha_consulta;
    private String prob_actual, rev_aparatos, preescripcion;

    public ConsultaMedica() {
    }

    public ConsultaMedica(Empleado empleado, Date fecha_consulta, String prob_actual, String rev_aparatos, String preescripcion) {
        this.empleado = empleado;
        this.fecha_consulta = fecha_consulta;
        this.prob_actual = prob_actual;
        this.rev_aparatos = rev_aparatos;
        this.preescripcion = preescripcion;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFecha_consulta() {
        return fecha_consulta;
    }

    public void setFecha_consulta(Date fecha_consulta) {
        this.fecha_consulta = fecha_consulta;
    }

    public String getProb_actual() {
        return prob_actual;
    }

    public void setProb_actual(String prob_actual) {
        this.prob_actual = prob_actual;
    }

    public String getRev_aparatos() {
        return rev_aparatos;
    }

    public void setRev_aparatos(String rev_aparatos) {
        this.rev_aparatos = rev_aparatos;
    }

    public String getPreescripcion() {
        return preescripcion;
    }

    public void setPreescripcion(String preescripcion) {
        this.preescripcion = preescripcion;
    }
}
