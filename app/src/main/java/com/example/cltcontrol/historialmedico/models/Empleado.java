package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Date;

public class Empleado extends SugarRecord {
    @Unique
    private String cedula;
    private String nombre, apellido, correo, direccion,profesion, estado_civil, sexo,
            lugar_nacimiento, area_trabajo;
    private Date fecha_nacimiento, fecha_registro;
    private int edad,foto;
    private Usuario usuario;

    //Constructor vacío
    public Empleado(){super();}

    //Constructor sin foreignkey Usuario
    public Empleado(String cedula, String nombre, String apellido, String correo,
                    String direccion, String profesion, String estado_civil, String sexo,
                    String lugar_nacimiento, String area_trabajo, Date fecha_nacimiento,
                    Date fecha_registro, int edad, int foto) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.direccion = direccion;
        this.profesion = profesion;
        this.estado_civil = estado_civil;
        this.sexo = sexo;
        this.lugar_nacimiento = lugar_nacimiento;
        this.area_trabajo = area_trabajo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_registro = fecha_registro;
        this.edad = edad;
        this.foto = foto;
    }

    //Constructor con foreignkey Usuario
    public Empleado(String cedula, String nombre, String apellido, String correo,
                    String direccion, String profesion, String estado_civil, String sexo,
                    String lugar_nacimiento, String area_trabajo, Date fecha_nacimiento,
                    Date fecha_registro, int edad, int foto, Usuario usuario) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.direccion = direccion;
        this.profesion = profesion;
        this.estado_civil = estado_civil;
        this.sexo = sexo;
        this.lugar_nacimiento = lugar_nacimiento;
        this.area_trabajo = area_trabajo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_registro = fecha_registro;
        this.edad = edad;
        this.foto = foto;
        this.usuario=usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getLugar_nacimiento() {
        return lugar_nacimiento;
    }

    public void setLugar_nacimiento(String lugar_nacimiento) {
        this.lugar_nacimiento = lugar_nacimiento;
    }

    public String getArea_trabajo() {
        return area_trabajo;
    }

    public void setArea_trabajo(String area_trabajo) {
        this.area_trabajo = area_trabajo;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
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
}
