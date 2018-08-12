package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExamenImagen extends SugarRecord {

    private int id_serv;
    private ConsultaMedica consulta;
    private String ruta_movil;
    private int status;

    public ExamenImagen() {

    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
    }

    public ConsultaMedica getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaMedica consulta) {
        this.consulta = consulta;
    }

    public String getRuta_movil() {
        return ruta_movil;
    }

    public void setRuta_movil(String ruta_movil) {
        this.ruta_movil = ruta_movil;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static Map<String, String> getHashMapExamenImagen(String id_consulta_servidor,
                                                               String imagenBase64){

        Map<String, String> params = new HashMap<>();
        params.put("imagen", imagenBase64);
        params.put("consulta_medica", id_consulta_servidor);


        return params;

    }


}

