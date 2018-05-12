package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

import java.util.Date;

public class Persona extends SugarRecord {

    // ATRIBUTOS DE CLASE
    private String cedula, nombre, apellido, direccion;
    private Date fecha_ingreso;

    // CONSTRUCTORES
    public Persona(){

    }

    public Persona(String nombre){ this.nombre = nombre; }

    public Persona(String cedula, String nombre, String apellido, String direccion, Date fecha_ingreso) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.fecha_ingreso = fecha_ingreso;
    }

    // METODOS GETTER Y SETTER DE CLASE
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}
