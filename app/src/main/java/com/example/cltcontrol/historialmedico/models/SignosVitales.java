package com.example.cltcontrol.historialmedico.models;

import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignosVitales extends SugarRecord {
    private int id_serv;
    private int presion_sistolica, presion_distolica, pulso;
    private float temperatura;
    private ConsultaMedica consulta_medica;
    private AtencionEnfermeria atencion_enfermeria;
    private Empleado empleado;
    private Date fecha;

    private int status;

    public SignosVitales() {}
    /*
     * Constructor sin Consulta Medica ni atencion enfermeria
     * */
    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, int status) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.status = status;
    }
    /*
     * Constructor con Consulta Medica
     * */
    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, ConsultaMedica consultaMedica, int status) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.consulta_medica = consultaMedica;
        this.status = status;
    }
    /*
     * Constructor con Atencion enfermeria
     * */
    public SignosVitales(int presion_sistolica, int presion_distolica, int pulso, float temperatura, AtencionEnfermeria atencion_enfermeria, int status) {
        this.presion_sistolica = presion_sistolica;
        this.presion_distolica = presion_distolica;
        this.pulso = pulso;
        this.temperatura = temperatura;
        this.atencion_enfermeria = atencion_enfermeria;
        this.status = status;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId_serv() {
        return id_serv;
    }

    public void setId_serv(int id_serv) {
        this.id_serv = id_serv;
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

    public int getPresion_sistolica() {
        return presion_sistolica;
    }

    public void setPresion_sistolica(int presion_sistolica) {
        this.presion_sistolica = presion_sistolica;
    }

    public int getPresion_distolica() {
        return presion_distolica;
    }

    public void setPresion_distolica(int presion_distolica) {
        this.presion_distolica = presion_distolica;
    }

    public int getPulso() {
        return pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public ConsultaMedica getConsultaMedica() {
        return consulta_medica;
    }

    public void setConsultaMedica(ConsultaMedica consultaMedica) {
        this.consulta_medica = consultaMedica;
    }

    public AtencionEnfermeria getAtencion_enfermeria() {
        return atencion_enfermeria;
    }

    public void setAtencion_enfermeria(AtencionEnfermeria atencion_enfermeria) {
        this.atencion_enfermeria = atencion_enfermeria;
    }

    public int validarSignos(String presion_sistolica, String presion_distolica, String pulso, String temperatura){
        if(presion_sistolica.equals("") || presion_distolica.equals("") || temperatura.equals("") || pulso.equals(""))
            return 0;
        try {
            int presionSistolica = Integer.parseInt(presion_sistolica);
            int presionDistolica = Integer.parseInt(presion_distolica);
            float temp = Float.parseFloat(temperatura);
            int pul = Integer.parseInt(pulso);

            if(presionSistolica < 100 || presionSistolica > 135 || presionDistolica < 70 || presionDistolica > 90 ||
                    pul < 60 || pul > 100 || temp < 34 || temp > 43)
                return 1;
        } catch(Exception e){
            return 2;
        }
        return 3;
    }

    public int validarSignosTest(String presion_sistolica, String presion_distolica, String pulso, String temperatura, String fecha_signos){

        if(presion_sistolica.equals("") || presion_distolica.equals("") || temperatura.equals("") || pulso.equals("")
                || fecha_signos.equals(""))
            return 0;
        try {
            int presionSistolica = Integer.parseInt(presion_sistolica);
            int presionDistolica = Integer.parseInt(presion_distolica);
            float temp = Float.parseFloat(temperatura);
            int pul = Integer.parseInt(pulso);
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            fecha = new Date();
            String fechaText = formato.format(fecha);

            if(presionSistolica < 100 || presionSistolica > 135 || presionDistolica < 70 || presionDistolica > 90 ||
                    pul < 60 || pul > 100 || temp < 34 || temp > 43 || !(fechaText.equals(fecha_signos)))
                return 1;
        } catch(Exception e){
            return 2;
        }
        return 3;
    }

    public ArrayList<SignosVitales> getSignosVitalesUnsynced(){
        return (ArrayList<SignosVitales>) SignosVitales.find(SignosVitales.class, "status = ?", String.valueOf(0));
    }

    public static JSONObject getJSONSignosVitales(String id_empleado_servidor, String id_consulta_medica, String id_atencion,
                                                  String presionSistolicaText, String presionDistolicaText,
                                                  String pulsoText, String temperaturatext){
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'consulta_medica': '"+id_consulta_medica+"', " +
                    "'atencion_enfermeria': '"+id_atencion+"',"+
                    "'empleado': '"+id_empleado_servidor+"', " +
                    "'presion_sistolica': '"+presionSistolicaText+"', " +
                    "'presion_distolica': '"+presionDistolicaText+"', " +
                    "'pulso': '"+pulsoText+"', " +
                    "'temperatura': '" +temperaturatext+"'"+
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sendObj;

    }

    public static Map<String, String> getHashMapSignosVitales(String id_empleado_servidor, String id_consulta_medica, String id_atencion,
                                                              String presionSistolicaText, String presionDistolicaText,
                                                              String pulsoText, String temperaturatext,Date fecha){

        Map<String, String> params = new HashMap<>();
        params.put("consulta_medica", id_consulta_medica);
        params.put("atencion_enfermeria", id_atencion);
        params.put("empleado", id_empleado_servidor);
        params.put("presion_sistolica", presionSistolicaText);
        params.put("presion_distolica", presionDistolicaText);
        params.put("pulso", pulsoText);
        params.put("temperatura", temperaturatext);
        params.put("fecha", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fecha)));

        Log.d("PARAMSSIGNOS", String.valueOf(params));
        return params;

    }

}