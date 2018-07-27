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
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.ExamenImagen;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SIGNOS;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;
    private AtencionEnfermeria atencionEnfermeria;
    private ConsultaMedica consultaMedica;
    private SignosVitales signosVitales;
    private Diagnostico diagnostico = new Diagnostico();
    private Empleado empleado = new Empleado();
    private Enfermedad enfermedad = new Enfermedad();
    private ExamenImagen examenImagen = new ExamenImagen();
    private PatologiasPersonales patologiasPersonales = new PatologiasPersonales();
    private PermisoMedico permisoMedico = new PermisoMedico();

    private IResult mResultCallback;
    private String TAGATENCION="atencion", TAGCONSULTA="consulta",
            TAGDIAGNOSTICO="diagnostico", TAGEMPLEADO="empleado", TAGENFERMEDAD="enfermedad",
            TAGEXAMENIMAGEN="imagen", TAGPATOLOGIASPERS="patpersonales", TAGPERMISO="permiso",
            TAGSIGNOS="signos";


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //Si hay conexión
        if (activeNetwork != null) {
            //Si está conectado con wifi o con datos
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //Obtiene todas las consultas medicas que no han sido sincronizadas
                List<ConsultaMedica> consultaMedicasUnsynced = consultaMedica.getConsultaMedicaUnsynced();
                for(ConsultaMedica consultas : consultaMedicasUnsynced) {
                    consultaMedica = consultas;
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
                        guardarConsultaMedicaLocal( consultas.getEmpleado().getId_serv(), consultas.getFechaConsulta(), problemaActual,
                                revisionMedica, preescripcion, examenFisico, motivo);
                    }
                }
                //Obtiene todos los signos vitales unsynced
                List<SignosVitales> signosVitalesUnsynced = signosVitales.getSignosVitalesUnsynced();
                for(SignosVitales signos : signosVitalesUnsynced){
                    signosVitales = signos;
                    guardarSignosVitalesLocal(signos.getEmpleado().getId_serv(), String.valueOf(signos.getPresion_distolica()),
                            String.valueOf(signos.getPresion_sistolica()), String.valueOf(signos.getPulso()),
                            String.valueOf(signos.getTemperatura()), signos.getAtencion_enfermeria().getId_serv(),
                            signos.getConsultaMedica().getId_serv());
                }
                //Obtiene todos los diagnósticos unsynced
                List<Diagnostico> diagnosticosUnsynced = diagnostico.getDiagnosticoUnsynced();
                for(Diagnostico diagnosticos : diagnosticosUnsynced){
                    diagnostico = diagnosticos;
                    guardarDiagnosticoLocal(diagnosticos.getConsulta_medica().getId_serv(),
                            diagnosticos.getEnfermedad().getId_serv(), diagnosticos.getTipoEnfermedad());

                }
                //Obtiene todos los diagnósticos unsynced
                List<PermisoMedico> permisoMedicoUnsynced = permisoMedico.getPermisoMedicoUnsynced();
                for(PermisoMedico permisosMedico : permisoMedicoUnsynced){
                    permisoMedico = permisosMedico;
                    guardarPermisoMedicoLocal(permisosMedico.getEmpleado().getId_serv(), permisosMedico.getDiagnostico().getId_serv(),
                            permisosMedico.getConsulta_medica().getId_serv(), permisosMedico.getFecha_inicio(), permisosMedico.getFecha_fin(),
                            permisosMedico.getDias_permiso(), permisosMedico.getObsevaciones_permiso());

                }
            }
        }
    }
    /*
     * Toma todos los argumentos de los diagnósticos
     * Los almacena en el servidor
     * */
    public void guardarDiagnosticoLocal(int id_consulta, int id_enfermedad, String tipo_enfermedad){
        initRequestCallback(TAGDIAGNOSTICO);
        String id_consulta_medica = "";

        if(id_consulta!=0){
            id_consulta_medica = String.valueOf(id_consulta);
        }

        RequestService requestService = new RequestService(mResultCallback, context);

        JSONObject sendObj = Diagnostico.getJSONDiagnostico(id_consulta_medica,
                tipo_enfermedad, String.valueOf(id_enfermedad));
        requestService.postDataRequest("POSTCALL", URL_DIAGNOSTICO, sendObj);
    }

    /*
     * Toma todos los argumentos de los signos vitales
     * Los almacena en el servidor
     * */
    private void guardarSignosVitalesLocal(final int id_empleado, final String presionDistolica, final String presionSistolica, final String pulso, final String temperatura,
                                     final int id_atencion, final int id_consulta)
    {
        initRequestCallback(TAGSIGNOS);
        String id_consulta_medica = "";
        String id_atencion_enfermeria = "";
        String id_empleado_servidor = "";
        if(id_atencion != 0)
            id_atencion_enfermeria = String.valueOf(id_atencion);

        if(id_consulta!=0)
            id_consulta_medica = String.valueOf(id_consulta);

        if(id_empleado!=0)
            id_empleado_servidor = String.valueOf(id_empleado);

        RequestService requestService = new RequestService(mResultCallback, context);

        JSONObject sendObj = SignosVitales.getJSONSignosVitales(id_empleado_servidor,id_consulta_medica,
                id_atencion_enfermeria,presionSistolica,presionDistolica,pulso,temperatura);
        requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj);
    }

    /*
     * Toma todos los argumentos de los consulta médica
     * Los almacena en el servidor
     * */
    private void guardarConsultaMedicaLocal(final int id_empleado, Date fecha_consulta, final String problema_actual,
                                       final String revision_medica, final String prescripcion, final String examen_fisico,
                                       final String motivo) {
        initRequestCallback(TAGCONSULTA);
        String id_empleado_servidor = "";

        if(id_empleado!=0)
            id_empleado_servidor = String.valueOf(id_empleado);

        RequestService requestService = new RequestService(mResultCallback, context);

        JSONObject sendObj = ConsultaMedica.getJSONConsultaMedica(id_empleado_servidor,fecha_consulta, motivo, problema_actual, revision_medica, prescripcion, examen_fisico);
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj);
    }

    /*
     * Toma todos los argumentos de los consulta médica
     * Los almacena en el servidor
     * */
    private void guardarPermisoMedicoLocal(int id_empleado, int id_diagnostico, int id_consulta,Date fecha_inicio,
                                           Date fecha_fin, int dias, String observaciones) {
        initRequestCallback(TAGCONSULTA);
        String id_empleado_servidor = "";
        String id_consulta_servidor = "";
        String id_diagnositco_servidor = "";

        if(id_empleado!=0)
            id_empleado_servidor = String.valueOf(id_empleado);

        if(id_diagnostico!=0)
            id_diagnositco_servidor = String.valueOf(id_diagnostico);

        if(id_consulta!=0)
            id_consulta_servidor = String.valueOf(id_consulta);
        RequestService requestService = new RequestService(mResultCallback, context);

        JSONObject sendObj = PermisoMedico.getJSONPermisoMedico(id_empleado_servidor, id_diagnositco_servidor,
                id_consulta_servidor, fecha_inicio, fecha_fin, String.valueOf(dias), observaciones);
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj);
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                    try {
                        String pk = response.getString("pk");
                        if(TAG.equalsIgnoreCase(TAGATENCION)){
                            atencionEnfermeria.setId_serv(Integer.parseInt(pk));
                            atencionEnfermeria.setStatus(NAME_SYNCED_WITH_SERVER);
                            atencionEnfermeria.save();
                        }else if(TAG.equalsIgnoreCase(TAGCONSULTA)){
                            consultaMedica.setId_serv(Integer.parseInt(pk));
                            consultaMedica.setStatus(NAME_SYNCED_WITH_SERVER);
                            consultaMedica.save();
                        }else if(TAG.equalsIgnoreCase(TAGDIAGNOSTICO)){
                            diagnostico.setId_serv(Integer.parseInt(pk));
                            diagnostico.setStatus(NAME_SYNCED_WITH_SERVER);
                            diagnostico.save();
                        }else if(TAG.equalsIgnoreCase(TAGEMPLEADO)){
                            empleado.setId_serv(Integer.parseInt(pk));
                            empleado.setStatus(NAME_SYNCED_WITH_SERVER);
                            empleado.save();
                        }else if(TAG.equalsIgnoreCase(TAGENFERMEDAD)){
                            enfermedad.setId_serv(Integer.parseInt(pk));
                            enfermedad.setStatus(NAME_SYNCED_WITH_SERVER);
                            enfermedad.save();
                        }else if(TAG.equalsIgnoreCase(TAGPATOLOGIASPERS)){
                            patologiasPersonales.setId_serv(Integer.parseInt(pk));
                            patologiasPersonales.setStatus(NAME_SYNCED_WITH_SERVER);
                            patologiasPersonales.save();
                        }else if(TAG.equalsIgnoreCase(TAGENFERMEDAD)){
                            enfermedad.setId_serv(Integer.parseInt(pk));
                            enfermedad.setStatus(NAME_SYNCED_WITH_SERVER);
                            enfermedad.save();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.e("ERROR", String.valueOf(error));
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
                Log.d("HEREJSONERROR", String.valueOf(error));
            }
        };

    }

}
