package com.example.cltcontrol.historialmedico.models;

import com.orm.dsl.Unique;
import java.util.Date;

public class Empleado extends Persona{

    // ATRIBUTOS DE CLASE
    @Unique
    private String cedula;
    private String profesion, estadoCivil, sexo, lugarNacimiento, areaTrabajo;
    private Date fechaNacimiento;
    private int edad,foto;

    // CONSTRUCTORES DE CLASE
    public Empleado() {
        super();
    }

    public Empleado(String nombre, String areaTrabajo, int foto){
        super(nombre);
        this.areaTrabajo = areaTrabajo;
        this.foto = foto;
    }
    
    public Empleado(String profesion, String estadoCivil, String sexo, String lugarNacimiento, String areaTrabajo, Date fechaNacimiento) {
        this.profesion = profesion;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.lugarNacimiento = lugarNacimiento;
        this.areaTrabajo = areaTrabajo;
        this.fechaNacimiento = fechaNacimiento;
    }

    // METODOS GETTER Y SETTER DE CLASE
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public String getCargoProfesional() {
        return areaTrabajo;
    }

    public void setCargoProfesional(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getAreaTrabajo() { return areaTrabajo; }

    public void setAreaTrabajo(String areaTrabajo) { this.areaTrabajo = areaTrabajo; }

}
