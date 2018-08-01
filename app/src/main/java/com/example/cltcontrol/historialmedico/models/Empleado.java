package com.example.cltcontrol.historialmedico.models;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Date;

public class Empleado extends SugarRecord {
    @Unique
    private String cedula;

    private String nombre, apellido, correo, direccion,profesion, estadoCivil, sexo,
            lugarNacimiento, ocupacion, foto;
    private Date fechaNacimiento, fechaRegistro;
    private int edad, status, ficha_actual;
    private Context context;
    private int id_serv;

    //Constructor vacío
    public Empleado(){super();}

    //Constructor sin foreignkey Usuario
    public Empleado(String cedula, String nombre, String apellido, String correo,
                    String direccion, String profesion, String estadoCivil, String sexo,
                    String lugarNacimiento, String ocupacion, Date fechaNacimiento,
                    Date fechaRegistro, int edad, String foto) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.direccion = direccion;
        this.profesion = profesion;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.lugarNacimiento = lugarNacimiento;
        this.ocupacion = ocupacion;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.foto = foto;
    }


    public int getFicha_actual() {
        return ficha_actual;
    }

    public void setFicha_actual(int ficha_actual) {
        this.ficha_actual = ficha_actual;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}