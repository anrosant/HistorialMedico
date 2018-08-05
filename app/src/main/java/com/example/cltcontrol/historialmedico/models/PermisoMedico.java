package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PermisoMedico extends SugarRecord{
    private int id_serv;
    private Diagnostico diagnostico;
    private Date fecha_inicio, fecha_fin;
    private int dias_permiso;
    private String obsevaciones_permiso, doctor;
    private ConsultaMedica consulta_medica;
    private Empleado empleado;
    private int status;

    public PermisoMedico(){}

    public PermisoMedico(Diagnostico diagnostico, Date fecha_inicio, Date fecha_fin, int dias_permiso, String obsevaciones_permiso, ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
        this.diagnostico = diagnostico;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.dias_permiso = dias_permiso;
        this.obsevaciones_permiso = obsevaciones_permiso;
    }

    public PermisoMedico(Date fecha_inicio, Date fecha_fin, int dias_permiso, String obsevaciones_permiso, String doctor) {
        this.consulta_medica = consulta_medica;
        this.diagnostico = diagnostico;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.dias_permiso = dias_permiso;
        this.obsevaciones_permiso = obsevaciones_permiso;
        this.doctor = doctor;
    }

    //Usado para un permiso medico otorgado por un Doctor Particular
    public PermisoMedico(Diagnostico diagnostico, Date fecha_inicio, Date fecha_fin, int dias_permiso, String observaciones_permiso, String doctor, Empleado empleado) {
        this.diagnostico = diagnostico;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.dias_permiso = dias_permiso;
        this.obsevaciones_permiso = observaciones_permiso;
        this.doctor = doctor;
        this.empleado = empleado;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
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

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }
    public ConsultaMedica getConsulta_medica() {
        return consulta_medica;
    }

    public void setConsulta_medica(ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getDias_permiso() {
        return dias_permiso;
    }

    public void setDias_permiso(int dias_permiso) {
        this.dias_permiso = dias_permiso;
    }

    public String getObsevaciones_permiso() {
        return obsevaciones_permiso;
    }

    public void setObsevaciones_permiso(String obsevaciones_permiso) {
        this.obsevaciones_permiso = obsevaciones_permiso;
    }

    public ArrayList<PermisoMedico> getPermisoMedicoUnsynced(){
        return (ArrayList<PermisoMedico>) PermisoMedico.find(PermisoMedico.class, "status = ?", String.valueOf(0));
    }


    public static JSONObject getJSONPermisoMedico(String id_empleado_servidor, String id_diagnostico, String id_consulta,Date fecha_inicio,
                                                  Date fecha_fin, String dias, String observaciones){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'diagnostico': "+id_diagnostico+", " +
                    "'empleado': '"+id_empleado_servidor+"', " +
                    "'consulta_medica': '"+id_consulta+"', "+
                    "'fecha_inicio': '"+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_inicio))+"',"+
                    "'fecha_fin': '"+String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_fin))+"', "+
                    "'dias': '"+dias+"',"+
                    "'observaciones': '"+observaciones+"'"+
                    "}");
            Log.d("ENDOBJ", String.valueOf(sendObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }

    public static Map<String, String> getHashMapPermisoMedico(String id_empleado_servidor, String id_diagnostico, String id_consulta,Date fecha_inicio,
                                                              Date fecha_fin, String dias, String observaciones, String doctor){
        Map<String, String> params = new HashMap<>();
        params.put("diagnostico", id_diagnostico);
        params.put("empleado", id_empleado_servidor);
        params.put("consulta_medica", id_consulta);
        params.put("fecha_inicio", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_inicio)));
        params.put("fecha_fin", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha_fin)));
        params.put("dias_permiso", dias);
        params.put("observaciones_permiso", observaciones);
        params.put("doctor", doctor);
        Log.d("PERMISOHERE", String.valueOf(params));
        return params;
    }

    public int validarPermisoMedico(String enfermedad, String fecha_inicio, String fecha_fin, String numero_dias, String observaciones){
        if (enfermedad.equals("") || fecha_inicio.equals("") || fecha_fin.equals("") ||
                numero_dias.equals("") || observaciones.equals(""))
            return 0;
        return 1;
    }
}