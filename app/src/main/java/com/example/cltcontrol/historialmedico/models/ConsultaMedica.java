package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class ConsultaMedica extends SugarRecord {
    private Empleado empleado;
    //private String cedulaEmpleado;
    private Date fechaConsulta;
    private String probActual, prescripcion, motivo, revision_medica, examen_fisico;

    public ConsultaMedica() {
    }

    public ConsultaMedica(Empleado empleado, Date fechaConsulta, String probActual,
                          String revisionMedica, String prescripcion, String examen_fisico,
                          String motivo) {
        this.empleado = empleado;
        this.fechaConsulta = fechaConsulta;
        this.probActual = probActual;
        this.revision_medica = revisionMedica;
        this.prescripcion = prescripcion;
        this.examen_fisico = examen_fisico;
        this.motivo = motivo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
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

    public String getPrescripcion() {
        return prescripcion;
    }

    public void setPrescripcion(String prescripcion) {
        this.prescripcion = prescripcion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getRevision_medica() {
        return revision_medica;
    }

    public void setRevision_medica(String revision_medica) {
        this.revision_medica = revision_medica;
    }

    public String getExamen_fisico() {
        return examen_fisico;
    }

    public void setExamen_fisico(String examen_fisico) {
        this.examen_fisico = examen_fisico;
    }

    public int validarCampoTexto(String texto){
        if(texto.equals(""))
            return 0;
        else if(texto.matches("^[0-9]+$")){
            return 1;
        }
        return 2;
    }

}
