package com.example.cltcontrol.historialmedico.models;

import com.orm.dsl.Unique;

import java.util.Date;

public class Empleado extends Persona{
    @Unique
    private String cedula;
    private String profesion, estadoCivil, sexo, lugarNacimiento, cargoProfesional;
    private int edad;
    private Date fechaNacimiento;

    //adicion nuevos campos
    private int foto;
    private String area;

    public Empleado() {
        super();
    }

    public Empleado(String profesion, String estadoCivil, String sexo, String lugarNacimiento, String cargoProfesional, Date fechaNacimiento) {
        this.profesion = profesion;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.lugarNacimiento = lugarNacimiento;
        this.cargoProfesional = cargoProfesional;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Empleado(String nombre, String area, int foto){
        super(nombre);
        //this.nombres = nombres;
        this.area = area;
        this.foto = foto;

    }


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
        return cargoProfesional;
    }

    public void setCargoProfesional(String cargoProfesional) {
        this.cargoProfesional = cargoProfesional;
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

    /*public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }*/

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
