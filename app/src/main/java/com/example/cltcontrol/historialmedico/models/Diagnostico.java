package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Diagnostico extends SugarRecord{
    private int id_serv;
    private ConsultaMedica consulta_medica;
    private Enfermedad enfermedad;
    private PermisoMedico permiso_medico;
    private String tipo_enfermedad;
    private int status;

    public Diagnostico() {
    }

    public Diagnostico(ConsultaMedica consultaMedica, Enfermedad enfermedad, String tipo_enfermedad) {
        this.consulta_medica = consultaMedica;
        this.enfermedad = enfermedad;
        this.tipo_enfermedad = tipo_enfermedad;
    }

    public PermisoMedico getPermiso_medico() {
        return permiso_medico;
    }

    public void setPermiso_medico(PermisoMedico permiso_medico) {
        this.permiso_medico = permiso_medico;
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

    public String getTipo_enfermedad() {
        return tipo_enfermedad;
    }

    public void setTipo_enfermedad(String tipo_enfermedad) {
        this.tipo_enfermedad = tipo_enfermedad;
    }

    public ArrayList<Diagnostico> getDiagnosticoUnsynced(){
        return (ArrayList<Diagnostico>) Diagnostico.find(Diagnostico.class, "status = ?",
                String.valueOf(0));
    }


    public static Map<String, String> getHashMapDiagnostico(String idConsulta, String idPermiso,
                                                            String tipoEnfermedad, String idEnfermedad){

        Map<String, String> params = new HashMap<>();
        params.put("consulta_medica", idConsulta);
        params.put("enfermedad", idEnfermedad);
        params.put("tipo_enfermedad", tipoEnfermedad);
        params.put("permiso_medico", idPermiso);

        Log.d("PARAMSDIAGNOSTICO", String.valueOf(params));
        return params;

    }
}
