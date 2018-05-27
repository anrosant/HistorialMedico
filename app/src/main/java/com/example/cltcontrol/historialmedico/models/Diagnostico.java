package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class Diagnostico extends SugarRecord{
    private String idEmpleado;
    private String enfermedad;
    private String codigo;
    private String tipoEnfermedad;

    public Diagnostico() {
    }

    public Diagnostico(String idEmpleado, String enfermedad, String codigo, String tipoEnfermedad) {
        this.idEmpleado = idEmpleado;
        this.enfermedad = enfermedad;
        this.codigo = codigo;
        this.tipoEnfermedad = tipoEnfermedad;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoEnfermedad() {
        return tipoEnfermedad;
    }

    public void setTipoEnfermedad(String tipoEnfermedad) {
        this.tipoEnfermedad = tipoEnfermedad;
    }
}
