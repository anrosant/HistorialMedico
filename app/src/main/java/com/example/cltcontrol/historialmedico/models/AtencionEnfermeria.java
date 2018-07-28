package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class AtencionEnfermeria extends SugarRecord {
    private int id_serv;
    private Date fechaAtencion;
    //private String cedulaEmpleado;
    private Empleado empleado;
    private String motivoAtencion, diagnosticoEnfermeria, planCuidados;
    private int status;

    public AtencionEnfermeria() {
    }

    public AtencionEnfermeria(Date fecha_atencion, Empleado empleado, String motivoAtencion, String diagnosticoEnfermeria, String planCuidados) {
        this.fechaAtencion = fecha_atencion;
        this.empleado = empleado;
        this.motivoAtencion = motivoAtencion;
        this.diagnosticoEnfermeria = diagnosticoEnfermeria;
        this.planCuidados = planCuidados;
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFecha_atencion() {
        return fechaAtencion;
    }

    public void setFecha_atencion(Date fecha_atencion) {
        this.fechaAtencion = fecha_atencion;
    }

    public String getMotivoAtencion() {
        return motivoAtencion;
    }

    public void setMotivoAtencion(String motivoAtencion) {
        this.motivoAtencion = motivoAtencion;
    }

    public String getDiagnosticoEnfermeria() {
        return diagnosticoEnfermeria;
    }

    public void setDiagnosticoEnfermeria(String diagnosticoEnfermeria) {
        this.diagnosticoEnfermeria = diagnosticoEnfermeria;
    }

    public String getPlanCuidados() {
        return planCuidados;
    }

    public void setPlanCuidados(String planCuidados) {
        this.planCuidados = planCuidados;
    }

    public int validarCampoTexto(String texto){
        if(texto.equals(""))
            return 0;
        else if(texto.matches("^[0-9]+$")){
            return 1;
        }
        return 2;
    }

    public ArrayList<AtencionEnfermeria> getAtencionEnfermeriaUnsynced(){
        return (ArrayList<AtencionEnfermeria>) AtencionEnfermeria.find(AtencionEnfermeria.class, "status = ?", String.valueOf(0));
    }

    public static JSONObject getJSONAtencionEnfermeria(String id_empleado_servidor, Date fecha_consulta,
                                                       String motivo, String diagnostico, String plan){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'empleado': "+String.valueOf(id_empleado_servidor)+", " +
                    "'fecha': '"+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_consulta))+"', " +
                    "'motivo': '"+motivo+"', "+
                    "'diagnostico': '"+diagnostico+"',"+
                    "'plan_cuidados': '"+plan+"'"+
                    "}");
            Log.d("ENDOBJ", String.valueOf(sendObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }
}
