package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatologiasPersonales extends SugarRecord {
    private int id_serv;
    private ConsultaMedica consulta_medica;
    private String lugar, detalle;
    private int status, id_ficha;

    public PatologiasPersonales() { }

    public PatologiasPersonales(ConsultaMedica consulta_medica, String lugar, String detalle) {
        this.consulta_medica = consulta_medica;
        this.lugar = lugar;
        this.detalle = detalle;
    }

    public ConsultaMedica getConsulta_medica() {
        return consulta_medica;
    }

    public void setConsulta_medica(ConsultaMedica consulta_medica) {
        this.consulta_medica = consulta_medica;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
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
    public ConsultaMedica getConsultaMedica() {
        return consulta_medica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consulta_medica = consultaMedica;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public ArrayList<PatologiasPersonales> getPatologiasPersonalesUnsynced(){
        return (ArrayList<PatologiasPersonales>) PatologiasPersonales.find(PatologiasPersonales.class, "status = ?", String.valueOf(0));
    }
    public static JSONObject getJSONPatologiasPersonales(String id_ficha, String id_consulta, String lugar, String detalle){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'ficha_medica': '"+id_ficha+"', " +
                    "'consulta_medica': '"+id_consulta+"', "+
                    "'lugar_patologia': '"+lugar+"',"+
                    "'detalle_patologia': '"+detalle+"'"+
                    "}");
            Log.d("ENDOBJ", String.valueOf(sendObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }
    public static Map<String,String> getHashMapPatologiasPersonales(String id_ficha, String id_consulta, String lugar, String detalle){
        Map<String, String> params = new HashMap<>();
        params.put("ficha_medica", id_ficha);
        params.put("consulta_medica", id_consulta);
        params.put("lugar_patologia", lugar);
        params.put("detalle_patologia", detalle);

        return params;

    }
}
