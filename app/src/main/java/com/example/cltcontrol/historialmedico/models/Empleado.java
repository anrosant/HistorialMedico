package com.example.cltcontrol.historialmedico.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cltcontrol.historialmedico.utils.VolleySingleton;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_EMPLEADO;

public class Empleado extends SugarRecord {
    @Unique
    private int id_serv;
    private String cedula;

    private String nombre, apellido, correo, direccion,profesion, estadoCivil, sexo,
            lugarNacimiento, ocupacion;
    private Date fechaNacimiento, fechaRegistro;
    private int edad,foto, status;
    private Usuario usuario;
    private Context context;

    //Constructor vacío
    public Empleado(){super();}

    //Constructor sin foreignkey Usuario
    public Empleado(String cedula, String nombre, String apellido, String correo,
                    String direccion, String profesion, String estadoCivil, String sexo,
                    String lugarNacimiento, String ocupacion, Date fechaNacimiento,
                    Date fechaRegistro, int edad, int foto) {
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

    //Constructor con foreignkey Usuario
    public Empleado(String cedula, String nombre, String apellido, String correo,
                    String direccion, String profesion, String estadoCivil, String sexo,
                    String lugarNacimiento, String ocupacion, Date fechaNacimiento,
                    Date fechaRegistro, int edad, int foto, Usuario usuario) {
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
        this.usuario=usuario;
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

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }


}