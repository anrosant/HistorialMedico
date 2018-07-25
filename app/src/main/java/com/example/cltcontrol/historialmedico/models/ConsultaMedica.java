package com.example.cltcontrol.historialmedico.models;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ConsultaMedica extends SugarRecord {
    @Unique
    private int id_serv;
    private Context context;
    private Empleado empleado;
    private Date fechaConsulta;
    private String prob_actual, prescripcion, motivo, revision_medica, examen_fisico;
    private int status;

    public ConsultaMedica() {
    }

    public ConsultaMedica(Empleado empleado, Date fechaConsulta, String prob_actual,
                          String revisionMedica, String prescripcion, String examen_fisico,
                          String motivo, int status) {
        this.empleado = empleado;
        this.fechaConsulta = fechaConsulta;
        this.prob_actual = prob_actual;
        this.revision_medica = revisionMedica;
        this.prescripcion = prescripcion;
        this.examen_fisico = examen_fisico;
        this.motivo = motivo;
        this.status = status;
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

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getProb_actual() {
        return prob_actual;
    }

    public void setProb_actual(String prob_actual) {
        this.prob_actual = prob_actual;
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

    public ArrayList<ConsultaMedica> getConsultaMedicaUnsynced(){
        ArrayList<ConsultaMedica> consultasMedicaUnsynced = (ArrayList<ConsultaMedica>) ConsultaMedica.find(ConsultaMedica.class, "status = ?", String.valueOf(0));
        return consultasMedicaUnsynced;
    }

    public static JSONObject getJSONConsultaMedica(String id_empleado_servidor, Date fecha_consulta,
                                                   String motivo, String prob_actual, String revision_medica,
                                                   String prescripcion, String examen_fisico){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'empleado': "+String.valueOf(id_empleado_servidor)+", " +
                    "'fecha': '"+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_consulta))+"', " +
                    "'motivo': '"+motivo+"', "+
                    "'problema_actual': '"+prob_actual+"',"+
                    "'revision': '"+revision_medica+"', "+
                    "'prescripcion': '"+prescripcion+"',"+
                    "'examen_fisico': '"+examen_fisico+"'"+
                    "}");
            Log.d("ENDOBJ", String.valueOf(sendObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }
}
