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

public class Diagnostico extends SugarRecord{
    private int id_serv;
    private ConsultaMedica consulta_medica;
    private Enfermedad enfermedad;
    private String tipoEnfermedad;
    private int status;

    public Diagnostico() {
    }

    public Diagnostico(ConsultaMedica consultaMedica, Enfermedad enfermedad, String tipoEnfermedad) {
        this.consulta_medica = consultaMedica;
        this.enfermedad = enfermedad;
        this.tipoEnfermedad = tipoEnfermedad;
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

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getTipoEnfermedad() {
        return tipoEnfermedad;
    }

    public void setTipoEnfermedad(String tipoEnfermedad) {
        this.tipoEnfermedad = tipoEnfermedad;
    }

    public ArrayList<Diagnostico> getDiagnosticoCreadoUnsynced(){
        return (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class, "status = ? and idserv = ?", String.valueOf(0), String.valueOf(0));
    }

    public ArrayList<Diagnostico> getDiagnosticoEditadoUnsynced(){
        return (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class, "status = ?", String.valueOf(0));
    }

    public static JSONObject getJSONDiagnostico(String id_consulta, String tipo_enfermedad, String id_enfermedad){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'consulta_medica': "+id_consulta+", " +
                    "'enfermedad': '"+id_enfermedad+"', "+
                    "'tipoEnfermedad': '"+tipo_enfermedad+"'"+
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }

    public static Map<String, String> getHashMapDiagnostico(String id_consulta, String tipo_enfermedad, String id_enfermedad){

        Map<String, String> params = new HashMap<>();
        params.put("consulta_medica", id_consulta);
        params.put("enfermedad", id_enfermedad);
        params.put("tipoEnfermedad", tipo_enfermedad);

        Log.d("PARAMSDIAGNOSTICO", String.valueOf(params));
        return params;

    }
}
