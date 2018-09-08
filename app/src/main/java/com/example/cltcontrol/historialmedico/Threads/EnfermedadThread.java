package com.example.cltcontrol.historialmedico.Threads;

import com.example.cltcontrol.historialmedico.models.Enfermedad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.ENFERMEDADES_LIST;

public class EnfermedadThread extends Thread {
    private String response;

    public EnfermedadThread(String response) {
        this.response = response;
    }

    @Override
    public void run() {
        guardarEnfermedadLocal();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void guardarEnfermedadLocal(){
        String codigo, nombre, grupo;
        JSONArray obj;
        try {
            obj = new JSONArray(this.response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                codigo = fields.getString("codigo");
                nombre = fields.getString("nombre");
                grupo = fields.getString("grupo");
                Enfermedad enfermedad = new Enfermedad(codigo, nombre, grupo);
                enfermedad.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                enfermedad.setStatus(1);
                enfermedad.save();
                ENFERMEDADES_LIST.add(enfermedad);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
