package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario extends SugarRecord {

    // ATRIBUTOS DE CLASE
    @Unique
    private int id_serv;

    private int status;
    private String usuario;
    private Empleado empleado;

    // CONSTRUCTORES DE CLASE
    public Usuario() {
        super();
    }

    public Usuario(String usuario) {
        this.usuario = usuario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public static JSONObject getJSONUsuario(String usuario, String contrasenia){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'usuario': "+usuario+", " +
                    "'password': "+contrasenia+
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;
    }

}
