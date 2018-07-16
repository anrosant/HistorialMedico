package com.example.cltcontrol.historialmedico.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SAVE_CONSULTA_MEDICA;

public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;
    private SignosVitales signosVitales = new SignosVitales();
    private ConsultaMedica consultaMedica = new ConsultaMedica();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //Obtiene todas las consultas medicas que no han sido sincronizadas
                List<ConsultaMedica> consultaMedicasUnsynced = consultaMedica.getConsultaMedicaUnsynced();
                for(ConsultaMedica consultas : consultaMedicasUnsynced) {
                    //Si se ha creado una consulta médica
                    if (consultas.getFechaConsulta()!=null) {
                        String problemaActual = consultas.getProb_actual();
                        String revisionMedica = consultas.getRevision_medica();
                        String preescripcion = consultas.getPrescripcion();
                        String examenFisico = consultas.getExamen_fisico();
                        String motivo = consultas.getMotivo();

                        if (problemaActual == null) {
                            problemaActual = "";
                        }
                        if (revisionMedica == null) {
                            revisionMedica = "";
                        }
                        if (preescripcion == null) {
                            preescripcion = "";
                        }
                        if (examenFisico == null) {
                            examenFisico = "";
                        }
                        if (motivo == null) {
                            motivo = "";
                        }
                        guardarConsultaMedica(consultas, consultas.getEmpleado(), consultas.getFechaConsulta(), problemaActual,
                                revisionMedica, preescripcion, examenFisico, motivo);
                    }
                }
                //Obtiene todos los signos vitales unsynced
                List<SignosVitales> signosVitalesUnsynced = signosVitales.getSignosVitalesUnsynced();
                for(SignosVitales signos : signosVitalesUnsynced){
                    guardarSignosVitales(signos, signos.getPresion_distolica(), signos.getPresion_sistolica(),
                            signos.getPulso(), signos.getTemperatura(), signos.getAtencion_enfermeria(),
                            signos.getConsultaMedica());
                }
            }
        }
    }

    /*
     * Toma todos los argumentos de los signos vitales
     * Los almacena en el servidor
     * Cambiar el status
     * */
    private void guardarSignosVitales(final SignosVitales signos, final int presionDistolica, final int presionSistolica, final int pulso, final float temperatura,
                                     final AtencionEnfermeria atencionEnfermeria, final ConsultaMedica consultaMedica)
    {
        String id_consulta_medica = "";
        String id_atencion_enfermeria = "";
        if(atencionEnfermeria != null){
            id_atencion_enfermeria = String.valueOf(atencionEnfermeria.getId_serv());
        }else if(consultaMedica!=null){
            id_consulta_medica = String.valueOf(consultaMedica.getId_serv());
        }
        final String finalId_consulta_medica = id_consulta_medica;
        final String finalId_atencion_enfermeria = id_atencion_enfermeria;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Identifiers.URL_SAVE_SIGNOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                //obtener id del servidor
                                int id_serv_signos = Integer.parseInt(String.valueOf(obj.get("pk")));
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Identifiers.DATA_SAVED_BROADCAST));
                                //Modificando el status
                                signos.setStatus(Identifiers.NAME_SYNCED_WITH_SERVER);
                                signos.setId_serv(id_serv_signos);
                                signos.save();
                                Toast.makeText(context,"Signos vitales enviados al servidor.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, (CharSequence) error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("presion_sistolica", String.valueOf(presionSistolica));
                params.put("presion_distolica", String.valueOf(presionDistolica));
                params.put("temperatura", String.valueOf(temperatura));
                params.put("pulso", String.valueOf(pulso));
                params.put("consulta_medica", finalId_consulta_medica);
                params.put("atencion_enfermeria", finalId_atencion_enfermeria);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void guardarConsultaMedica(final ConsultaMedica consulta, Empleado empleado, final Date fechaConsulta, final String probActual,
                                       final String revisionMedica, final String prescripcion, final String examen_fisico,
                                       final String motivo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_CONSULTA_MEDICA,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.has("error")) {
                                //obtener id del servidor
                                int id_serv_consulta = Integer.parseInt(String.valueOf(obj.get("pk")));
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Identifiers.DATA_SAVED_BROADCAST));
                                //Modificando el status
                                consulta.setId_serv(id_serv_consulta);
                                consulta.setStatus(Identifiers.NAME_SYNCED_WITH_SERVER);
                                consulta.save();
                                Toast.makeText(context,"Consultas médicas enviadas al servidor.",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "No se pudo sincronizar", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, (CharSequence) error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Falta Examen fisico
                Map<String, String> params = new HashMap<>();
                params.put("empleado", "1");
                params.put("fecha", String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", fechaConsulta)));
                params.put("motivo", motivo);
                params.put("problema_actual", probActual);
                params.put("revision", revisionMedica);
                params.put("prescripcion", prescripcion);
                params.put("examen_fisico", examen_fisico);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
