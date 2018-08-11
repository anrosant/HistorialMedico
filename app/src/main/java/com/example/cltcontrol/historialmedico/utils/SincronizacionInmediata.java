package com.example.cltcontrol.historialmedico.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_ATENCION_ENFERMERIA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PATOLOGIAS_PERSONALES;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PERMISO_MEDICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_SIGNOS;

/*
* Esta clase se ejecuta cuando detecta conexión a internet
* Para sincronizar los datos hace lo siguiente:
* 1. Valida si existen atenciones enfermerías o consultas médicas que no hayan sido enviadas al servidor
* En caso de que no existan, recorre signos vitales directamente.
* 2. Valida si no existen consultas para sincronizar, en caso de que existan, recorre los diagnósticos
* y luego las patologías y diagnósticos (En caso de que hayan diagnósticos sin sincronizar).
* Caso contrario, recorre las patologías directamente, y si existen diagnósticos sin sincronizar,
* primero recorre los diagnósticos y luego los permisos. Caso contrario recorre directamente los permisos.
* 3. Si no entra en ningún caso, recorre las consultas y atenciones y dentro de cada uno recorre
* signos, patologías, diagnóstico y permisos médicos en caso de que hayan.
* */

public class SincronizacionInmediata extends BroadcastReceiver {

    private Context context;
    private AtencionEnfermeria atencionEnfermeria = new AtencionEnfermeria();
    private ConsultaMedica consultaMedica= new ConsultaMedica();
    private SignosVitales signosVitales=new SignosVitales();
    private Diagnostico diagnostico = new Diagnostico();
    private PatologiasPersonales patologiasPersonales = new PatologiasPersonales();
    private PermisoMedico permisoMedico = new PermisoMedico();

    private List<ConsultaMedica> consultaMedicasUnsynced;
    private List<AtencionEnfermeria> atencionesUnsynced;
    private List<SignosVitales> signosVitalesUnsynced;
    private List<PermisoMedico> permisoMedicoUnsynced;
    private List<Diagnostico> diagnosticosUnsynced;
    private List<PatologiasPersonales> patologiasPersonalesUnsynced;

    private IResult mResultCallback;
    private String TAGATENCION="atencion", TAGCONSULTA="consulta",
            TAGDIAGNOSTICO="diagnostico", TAGPATOLOGIASPERS="patpersonales", TAGPERMISO="permiso",
            TAGSIGNOS="signos";

    private String token;


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SessionManager sesion = new SessionManager(context);
        token = sesion.obtenerInfoUsuario().get("token");

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        //Si hay conexión
        if (activeNetwork != null) {
            //Si está conectado con wifi o con datos
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                /*
                 * Obtiene todas las clases que no han sido sincronizadas
                 * */
                consultaMedicasUnsynced = consultaMedica.getConsultaMedicaUnsynced();
                atencionesUnsynced = atencionEnfermeria.getAtencionEnfermeriaUnsynced();
                signosVitalesUnsynced = signosVitales.getSignosVitalesUnsynced();
                permisoMedicoUnsynced = permisoMedico.getPermisoMedicoUnsynced();
                diagnosticosUnsynced = diagnostico.getDiagnosticoUnsynced();
                patologiasPersonalesUnsynced = patologiasPersonales.getPatologiasPersonalesUnsynced();

                //Si no existen atenciones creadas sin sincronizar ni consultas sin sincronizar
                if(atencionesUnsynced.size()==0 && consultaMedicasUnsynced.size()==0){
                    //Recorrer directamente los signos vitales
                    recorrerSignos("no","no");

                }
                //Si no existen consultas sin sincronizar
                if(consultaMedicasUnsynced.size()==0){
                    //Si no existen diagnósticos sin sincronizar
                    if(permisoMedicoUnsynced.size()==0)
                        recorrerDiagnostico("no","no");
                    else
                        recorrerPermisos("no");

                    recorrerPatologias("no");
                }

                recorrerConsultas();
                recorrerAtenciones();


            }
        }
    }

    //-----------------------------------------RECORRE LAS CLASES----------------------------------------

    /*
     * Recorre la lista de consultas no sincronizadas
     * Si consultas.getId_serv() es igual a cero, quiere decir que no existe en el servidor, por lo tanto
     * va a realizar POST. Caso contrario, va a realizar PUT
     * @see enviarConsultaMedicaAlServidor
     * */

    public void recorrerConsultas(){
        for(ConsultaMedica consultas : consultaMedicasUnsynced) {
            //Si se ha creado una consulta médica
            if (consultas.getFechaConsulta()!=null) {
                String id_empleado = "";
                if(consultas.getEmpleado()!=null && consultas.getEmpleado().getId_serv()!=0)
                    id_empleado = String.valueOf(consultas.getEmpleado().getId_serv());
                if(consultas.getId_serv()==0){
                    enviarConsultaMedicaAlServidor(consultas, id_empleado, consultas.getFechaConsulta(),
                            consultas.getProb_actual(), consultas.getRevision_medica(),
                            consultas.getPrescripcion(), consultas.getExamen_fisico(),
                            consultas.getMotivo(), "POST");
                }
                else{
                    enviarConsultaMedicaAlServidor(consultas, id_empleado, consultas.getFechaConsulta(),
                            consultas.getProb_actual(), consultas.getRevision_medica(),
                            consultas.getPrescripcion(), consultas.getExamen_fisico(),
                            consultas.getMotivo(), "PUT");
                }

            }
        }
    }

    /*
     * Recorre la lista de atenciones no sincronizadas
     * Si consultas.getId_serv() es igual a cero, quiere decir que no existe en el servidor, por lo tanto
     * va a realizar POST. Caso contrario, va a realizar PUT
     * @see enviarAtencionEnfermeriaAlServidor
     * */

    public void recorrerAtenciones(){
        for(AtencionEnfermeria atenciones : atencionesUnsynced) {
            //Si se ha creado una atención enfermería
            if (atenciones.getFecha_atencion()!=null) {
                String id_empleado="";

                if(atenciones.getEmpleado()!=null && atenciones.getEmpleado().getId_serv()!=0)
                    id_empleado = String.valueOf(atenciones.getEmpleado().getId_serv());

                if(atenciones.getId_serv()==0) {
                    enviarAtencionEnfermeriaAlServidor(atenciones, id_empleado, atenciones.getFecha_atencion(),
                            atenciones.getMotivoAtencion(), atenciones.getDiagnosticoEnfermeria(),
                            atenciones.getPlanCuidados(), "POST");
                }else{
                    enviarAtencionEnfermeriaAlServidor(atenciones, id_empleado, atenciones.getFecha_atencion(),
                            atenciones.getMotivoAtencion(), atenciones.getDiagnosticoEnfermeria(),
                            atenciones.getPlanCuidados(), "PUT");
                }
            }
        }
    }

    /*
     * Recorre la lista de diagnóstico no sincronizados
     * @param idConsulta id de la consulta en la que se realizó el diagnóstico, puede ser " ", "no"
     * o un número tipo string,
     * Si es "no", quiere decir que la consulta ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí la consulta, hice POST y obtuve el id del servidor
     * @param idPermiso id de el permiso al que pertenece el diagnóstico, puede ser " ", "no"
     * o un número tipo string,
     * Si es "no", quiere decir que el permiso ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí el permiso, hice POST y obtuve el id del servidor
     * @see enviarDiagnosticoAlServidor
     * */

    public void recorrerDiagnostico(String idConsulta, String idPermiso){
        for(Diagnostico diagnosticos : diagnosticosUnsynced){
            String idEnfermedad="";
            if(diagnosticos.getEnfermedad()!=null && diagnosticos.getEnfermedad().getId_serv()!=0){
                idEnfermedad = String.valueOf(diagnosticos.getEnfermedad().getId_serv());
            }

            //si la consulta ya existe en el servidor
            Log.d("HERE", String.valueOf(diagnosticos.getConsulta_medica().getId_serv()));
            if(idConsulta.equals("no") && diagnosticos.getConsulta_medica()!=null){
                idConsulta = String.valueOf(diagnosticos.getConsulta_medica().getId_serv());
            }
            //si el permiso ya existe en el servidor obtiene el id del servidor
            if(idPermiso.equals("no") && diagnosticos.getPermiso_medico()!=null){
                idPermiso = String.valueOf(diagnosticos.getPermiso_medico().getId_serv());
            }

            if(diagnosticos.getId_serv()==0){
                enviarDiagnosticoAlServidor(diagnosticos, idConsulta, idEnfermedad, idPermiso,
                        diagnosticos.getTipo_enfermedad(), "POST");
            }else{
                enviarDiagnosticoAlServidor(diagnosticos, idConsulta, idEnfermedad, idPermiso,
                        diagnosticos.getTipo_enfermedad(), "PUT");
            }


        }
    }

    /*
     * Recorre la lista de patologías no sincronizadas
     * @param idConsulta id de la consulta en la que se registró la patología, puede ser " ", "no"
     * o un número tipo string,
     * Si es "no", quiere decir que la consulta ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí la consulta, hice POST y obtuve el id del servidor
     * @see enviarPatologiaPersonalAlServidor
     * */

    public void recorrerPatologias(String id_consulta){

        for(PatologiasPersonales patologias : patologiasPersonalesUnsynced){
            //si la consulta ya existe en el servidor obtengo el id del servidor
            if(id_consulta.equals("no")){
                id_consulta = String.valueOf(patologias.getConsultaMedica().getId_serv());
            }
            enviarPatologiaPersonalAlServidor(patologias, patologias.getId_ficha(),
                    patologias.getConsultaMedica().getId_serv(),
                    patologias.getLugar(), patologias.getDetalle());

        }
    }

    /*
     * Recorre la lista de permisos no sincronizados
     * @param idConsulta id de la consulta en la que se realizó el permiso, puede ser " ", "no" o un
     * número tipo string,
     * Si es "no", quiere decir que la consulta ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí la consulta, hice POST y obtuve el id del servidor
     * */

    public void recorrerPermisos(String idConsulta){
        for(PermisoMedico permisosMedico : permisoMedicoUnsynced){
            //si la consulta ya existe en el servidor obtiene el id del servidor
            if(idConsulta.equals("no") && permisosMedico.getConsulta_medica()!=null){
                idConsulta = String.valueOf(permisosMedico.getConsulta_medica().getId_serv());
            }

            if(permisosMedico.getId_serv()==0){
                enviarPermisoMedicoAlservidor(permisosMedico, permisosMedico.getEmpleado().getId_serv(),
                        idConsulta, permisosMedico.getFecha_inicio(),
                        permisosMedico.getFecha_fin(), permisosMedico.getDias_permiso(),
                        permisosMedico.getObsevaciones_permiso(), permisosMedico.getDoctor(), "POST");
            }else{
                enviarPermisoMedicoAlservidor(permisosMedico, permisosMedico.getEmpleado().getId_serv(),
                        idConsulta, permisosMedico.getFecha_inicio(),
                        permisosMedico.getFecha_fin(), permisosMedico.getDias_permiso(),
                        permisosMedico.getObsevaciones_permiso(), permisosMedico.getDoctor(), "PUT");
            }


        }
    }

    /*
     * Recorre la lista de signos no sincronizados
     * @param idConsulta id de la consulta a la que pertenece el signo, puede ser " ", "no" o un
     * número tipo string,
     * Si es "no", quiere decir que la consulta ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí la consulta, hice POST y obtuve el id del servidor
     * @param idAtencion id de la atención a la que pertenece el signo, puede ser " ", "no" o un
     * número tipo string,
     * Si es "no", quiere decir que la consulta ya existe en el servidor, y no fue recorrida,
     * Si es un número quiere decir que primero recorrí la atención, hice POST y obtuve el id del servidor
     * @see enviarSignosVitalesAlServidor
     * */

    public void recorrerSignos(String idConsulta, String idAtencion){
        for(SignosVitales signos : signosVitalesUnsynced){
            String idEmpleado="";
            if(signos.getEmpleado()!=null && signos.getEmpleado().getId_serv()!=0){
                idEmpleado = String.valueOf(signos.getEmpleado().getId_serv());
            }
            if(idConsulta.equals("no") && signos.getConsultaMedica()!=null){
                idConsulta = String.valueOf(signos.getConsultaMedica().getId_serv());
                idAtencion="";
            }
            else if(idAtencion.equals("no") && signos.getAtencion_enfermeria()!=null){
                idAtencion = String.valueOf(signos.getAtencion_enfermeria().getId_serv());
                idConsulta = "";
            }
            //Si desea crear un signo el id del servidor es 0
            if(signos.getId_serv()==0){
                enviarSignosVitalesAlServidor(signos, idEmpleado, String.valueOf(signos.getPresion_distolica()),
                        String.valueOf(signos.getPresion_sistolica()), String.valueOf(signos.getPulso()),
                        String.valueOf(signos.getTemperatura()), idAtencion,
                        idConsulta, signos.getFecha(), "POST");
            }
            //Si desea editar un signo ya tiene un id del servidor
            else{
                enviarSignosVitalesAlServidor(signos, idEmpleado, String.valueOf(signos.getPresion_distolica()),
                        String.valueOf(signos.getPresion_sistolica()), String.valueOf(signos.getPulso()),
                        String.valueOf(signos.getTemperatura()), idAtencion,
                        idConsulta, signos.getFecha(), "PUT");
            }

        }
    }

    //---------------------------------ENVÍA DATOS AL SERVIDOR--------------------------------------------------

    /*
     * Método que realiza POST o PUT de las patologías personales al servidor
     * @param patPers la patología personal que recorro de tipo PatologiaPersonal
     * @param idFicha id de la ficha actual del empleado de tipo entero
     * @param idConsulta id de la consulta a la que pertenece la patología personal de tipo entero
     * @param lugar lugar en que tiene la patología de tipo String
     * @param detalle detalle de la patología de tipo String
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST con la función postDataRequest y PUT con la función putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */

    private void enviarPatologiaPersonalAlServidor(PatologiasPersonales patPers, int idFicha, int idConsulta,
                                                   String lugar, String detalle) {
        initRequestCallback(TAGPATOLOGIASPERS, null, null, null,
                patPers, null, null);
        String idFichaServidor="";
        String idConsultaMedica = "";

        if(idConsulta!=0){
            idConsultaMedica = String.valueOf(idConsulta);
        }
        if(idFicha!=0){
            idFichaServidor = String.valueOf(idFicha);
        }

        RequestService requestService = new RequestService(mResultCallback, context);

        Map<String, String> sendObj = PatologiasPersonales.getHashMapPatologiasPersonales(idFichaServidor,
                idConsultaMedica,lugar,detalle);
        requestService.postDataRequest("POSTCALL", URL_PATOLOGIAS_PERSONALES, sendObj, token);
    }

    /*
     * Método que realiza POST o PUT de los diagnósticos no sincronizados al servidor
     * @param diagnóstico el diagnóstico que recorro de tipo Diagnóstico
     * @param idConsulta id de la consulta a la que pertenece el diagnóstico de tipo string
     * @param idEnfermdad id de la enfermedad detectada en el diagnóstico de tipo string
     * @param idPermiso id del permiso al que pertenece el diagnóstico de tipo string
     * @param tipoEnfermedad tipo de la enfermedad de tipo String
     * @param metodo puede ser PUT o POST
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST con la función postDataRequest y PUT con la función putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */

    public void enviarDiagnosticoAlServidor(Diagnostico diagnostico, String idConsulta,
                                            String idEnfermedad, String idPermiso, String tipoEnfermedad,
                                            String metodo){
        initRequestCallback(TAGDIAGNOSTICO, null, null, diagnostico,
                null, null, null);

        RequestService requestService = new RequestService(mResultCallback, context);

        Map<String, String> sendObj = Diagnostico.getHashMapDiagnostico(idConsulta, idPermiso,
                tipoEnfermedad, idEnfermedad);

        if(metodo.equalsIgnoreCase("POST"))
            requestService.postDataRequest("POSTCALL", URL_DIAGNOSTICO, sendObj, token);
        else
            requestService.putDataRequest("PUTCALL", URL_DIAGNOSTICO+
                    String.valueOf(diagnostico.getId_serv())+"/", sendObj, token);
    }

    /*
     * Método que realiza POST o PUT de los signos vitales no sincronizados al servidor
     * @param signosVit el signos vital que recorro de tipo SignosVitales
     * @param idEmpleado el id del empleado al que pertenece el signo vital de tipo String
     * @param presionDistolica presion distólica tomada de tipo String
     * @param presionSistolica presion sistólica tomada de tipo String
     * @param pulso pulso tomado de tipo String
     * @param temperatura temperatura tomada de tipo String
     * @param idConsulta id de la consulta a la que pertenece el signo vital de tipo string
     * @param idAtencion id de la atención a la que pertenece el signo vital de tipo string
     * @param fechaSigno fecha en la que se tomaron los signos de tipo Date
     * @param metodo puede ser POST o PUT de tipo string
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST o PUT con la función postDataRequest y putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */

    private void enviarSignosVitalesAlServidor(SignosVitales signosVit, String idEmpleado,
                                               String presionDistolica, String presionSistolica,
                                               String pulso, String temperatura, String idAtencion,
                                               String  idConsulta, Date fechaSignos, String metodo) {
        initRequestCallback(TAGSIGNOS, null, null, null,
                null, null, signosVit);
        RequestService requestService = new RequestService(mResultCallback, context);
        Map<String, String> sendObj = SignosVitales.getHashMapSignosVitales(idEmpleado,idConsulta,
                idAtencion,presionSistolica,presionDistolica,pulso,temperatura, fechaSignos);
        Log.d("SENDOBJ", String.valueOf(sendObj));
        if(metodo.equalsIgnoreCase("POST")){
            requestService.postDataRequest("POSTCALL", URL_SIGNOS, sendObj, token);
        }else{
            requestService.putDataRequest("PUTCALL",
                    URL_SIGNOS+String.valueOf(signosVit.getId_serv())+"/", sendObj, token);
        }

    }

    /*
     * Método que realiza POST o PUT de las consultas médicas no sincronizadas al servidor
     * @param consultaMed la consulta médica que recorro de tipo ConsultaMedica
     * @param idEmpleado id del empleado en el servidor, al que pertenece la consulta de tipo string
     * @param fechaConsulta fecha en que se realizó la consulta de tipo Date
     * @param problemaActual proble actual descrita en la consulta tipo string
     * @param revisionMedica revisión médica descrita en la consulta tipo string
     * @param prescripcion prescripción del doctor descrita en la consulta tipo string
     * @param examenFisico examen físico realizado por el doctor en la consulta tipo string
     * @param motivo motivo de la consulta tipo string
     * @param metodo puede ser "PUT" o "POST" tipo String
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST con la función postDataRequest y PUT con la función putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */

    private void enviarConsultaMedicaAlServidor(ConsultaMedica consultaMed, String idEmpleado,
                                                Date fechaConsulta, String problemaActual,
                                                String revisionMedica, String prescripcion,
                                                String examenFisico, String motivo, String metodo) {
        if (problemaActual == null) {
            problemaActual = "";
        }
        if (revisionMedica == null) {
            revisionMedica = "";
        }
        if (prescripcion == null) {
            prescripcion = "";
        }
        if (examenFisico == null) {
            examenFisico = "";
        }
        if (motivo == null) {
            motivo = "";
        }
        initRequestCallback(TAGCONSULTA, null,consultaMed, null,
                null, null, null);

        RequestService requestService = new RequestService(mResultCallback, context);

        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(idEmpleado,
                fechaConsulta, motivo, problemaActual, revisionMedica, prescripcion, examenFisico);
        if(metodo.equalsIgnoreCase("POST"))
            requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
        else
            requestService.putDataRequest("PUTCALL", URL_CONSULTA_MEDICA+
                    String.valueOf(consultaMed.getId_serv())+"/", sendObj, token);
    }

    /*
     * Método que realiza POST o PUT de las atenciones de enfermería no sincronizadas al servidor
     * @param atencionEnf la atención enfermería que recorro de tipo AtencionEnfermeria
     * @param idEmpleado id del empleado en el servidor, al que pertenece la atención de tipo string
     * @param fechaAtencion fecha en que se realizó la atención a enfermería de tipo Date
     * @param diagnóstico diagnóstico dado por la enfermera en la atención tipo string
     * @param plan plan de cuidados establecido por la enfermera en la atención tipo string
     * @param motivo motivo de la atencón tipo string
     * @param metodo puede ser PUT o POST tipo string
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST con la función postDataRequest y PUT con la función putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */
    private void enviarAtencionEnfermeriaAlServidor(AtencionEnfermeria atencionEnf, String idEmpleado,
                                                    Date fechaAtencion, String motivo, String diagnostico,
                                                    String plan, String metodo) {
        if (motivo == null) {
            motivo = "";
        }
        if (diagnostico == null) {
            diagnostico = "";
        }
        if (plan == null) {
            plan = "";
        }
        initRequestCallback(TAGATENCION, atencionEnf, null, null,
                null, null, null);

        RequestService requestService = new RequestService(mResultCallback, context);

        Map<String, String> sendObj = AtencionEnfermeria.getHashMapAtencionEnfermeria(idEmpleado,
                fechaAtencion, motivo, diagnostico, plan);
        if(metodo.equalsIgnoreCase("POST"))
            requestService.postDataRequest("POSTCALL", URL_ATENCION_ENFERMERIA, sendObj, token);
        else
            requestService.putDataRequest("PUTCALL", URL_ATENCION_ENFERMERIA+
                    String.valueOf(atencionEnf.getId_serv())+"/", sendObj, token);
    }

    /*
     * Método que realiza POST o PUT de los permisos médicos no sincronizados al servidor
     * @param permisoMed el permiso médico que recorro de tipo PermisoMedico
     * @param idEmpleado el id del empleado al que pertenece el permiso médico de tipo entero
     * @param idConsulta id de la consulta a la que pertenece el permiso médico de tipo string
     * @param fechaInicio fecha desde que inicia el permiso del paciente de tipo Date
     * @param fechaFin fecha en que termina el permiso del paciente de tipo Date
     * @param dias días de permiso que tiene el empleado de tipo entero
     * @param observaciones observaciones realizadas en el permiso de tipo String
     * @param nombreDoctor nombre del doctor que emite el permiso (puede ser externo) de tipo string
     * @param metodo puede ser POST o PUT
     * Con esos datos realiza el hashmap para enviarlos en formato JSON al servidor
     * Inicializa el request con la función initRequestCallback y
     * realiza POST o PUT con la función postDataRequest o putDataRequest
     * @see initRequestCallback la respuesta del servidor la vemos en esta función
     * */
    private void enviarPermisoMedicoAlservidor(PermisoMedico permisoMed, int idEmpleado,
                                               String idConsulta, Date fechaInicio,
                                               Date fechaFin, int dias, String observaciones,
                                               String nombreDoctor, String metodo) {
        initRequestCallback(TAGPERMISO,null, null, null,
                null, permisoMed, null);
        String id_empleado_servidor = "";

        if(idEmpleado!=0)
            id_empleado_servidor = String.valueOf(idEmpleado);
        RequestService requestService = new RequestService(mResultCallback, context);

        if(nombreDoctor==null){
            nombreDoctor = "";
        }

        Map<String, String> sendObj = PermisoMedico.getHashMapPermisoMedico(id_empleado_servidor,
                idConsulta, fechaInicio, fechaFin, String.valueOf(dias), observaciones, nombreDoctor);
        if(metodo.equalsIgnoreCase("POST")) {
            requestService.postDataRequest("POSTCALL", URL_PERMISO_MEDICO, sendObj, token);
        }else{
            requestService.putDataRequest("PUTCALL",
                    URL_PERMISO_MEDICO+String.valueOf(permisoMed.getId_serv())+"/", sendObj, token);
        }

    }

    //------------------------------------------------------------------------------------------------------------

    /*
     * Inicializa las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * @param TAG me permite identificar a qué clase pertenece la respuesta (SignosVitales,
     * Diagnóstico, ..) tipo String
     * @param atencionEnf la atención enfermería que recorro de tipo AtencionEnfermeria
     * @param consulta la consulta médica que recorro de tipo ConsultaMedica
     * @param diagnosticoI el diagnóstico que recorro de tipo Diagnostico
     * @param patologias la patología personale que recorro de tipo PatologiasPersonales
     * @param permiso el permiso médico que recorro de tipo PermisoMedico
     * @param signosV el signo vital que recorro de tipo SignosVitales
     * @see notifySuccess si la respuesta fue exitosa ingresa en esta función
     * @see notifyError si hubo un error de conexión y el mensaje es tipo Error, ingresa en esta función
     * @see notifyMsjError si hubo un error de conexión y el mensaje es String, ingresa en esta función
     * @see notifyJSONError si hubo un error al ralizar el JSON, ingresa en esta función
     * */
    private void initRequestCallback(final String TAG, final AtencionEnfermeria atencionEnf,
                                     final ConsultaMedica consulta, final Diagnostico diagnosticoI,
                                     final PatologiasPersonales patologias, final PermisoMedico permiso,
                                     final SignosVitales signosV){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                    try {
                        String pk = response.getString("pk");
                        if(TAG.equalsIgnoreCase(TAGATENCION)){
                                atencionEnf.setId_serv(Integer.parseInt(pk));
                                atencionEnf.setStatus(NAME_SYNCED_WITH_SERVER);
                                atencionEnf.save();

                                recorrerSignos("", pk);

                        }else if(TAG.equalsIgnoreCase(TAGCONSULTA)){
                            consulta.setId_serv(Integer.parseInt(pk));
                            consulta.setStatus(NAME_SYNCED_WITH_SERVER);
                            consulta.save();
                            if(atencionesUnsynced.size()==0){
                                recorrerSignos(pk, "");
                            }else{
                                recorrerAtenciones();
                            }
                            recorrerPatologias(pk);
                            if(permisoMedicoUnsynced.size()==0){
                                recorrerDiagnostico(pk, "no");
                            }else{
                                recorrerPermisos(pk);
                            }

                        }else if(TAG.equalsIgnoreCase(TAGDIAGNOSTICO)){
                            diagnosticoI.setId_serv(Integer.parseInt(pk));
                            diagnosticoI.setStatus(NAME_SYNCED_WITH_SERVER);
                            diagnosticoI.save();

                        }else if(TAG.equalsIgnoreCase(TAGPATOLOGIASPERS)){
                            patologias.setId_serv(Integer.parseInt(pk));
                            patologias.setStatus(NAME_SYNCED_WITH_SERVER);
                            patologias.save();
                        }else if(TAG.equalsIgnoreCase(TAGPERMISO)){
                            String idConsultaServidor = response.getString("consulta_medica");
                            permiso.setId_serv(Integer.parseInt(pk));
                            permiso.setStatus(NAME_SYNCED_WITH_SERVER);
                            permiso.save();
                            String idConsulta = "";
                            if(idConsultaServidor!=null){
                                List<ConsultaMedica> consultaMedica = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?", idConsultaServidor);
                                if(!consultaMedica.isEmpty()){
                                    permiso.setConsulta_medica(consultaMedica.get(0));
                                    idConsulta=String.valueOf(permiso.getConsulta_medica().getId_serv());
                                }

                            }
                            recorrerDiagnostico(idConsulta, pk);

                        }else if(TAG.equalsIgnoreCase(TAGSIGNOS)){
                            signosV.setId_serv(Integer.parseInt(pk));
                            signosV.setStatus(NAME_SYNCED_WITH_SERVER);
                            signosV.save();
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
                Log.e("HEREMSJERROR", String.valueOf(error));
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
                Log.e("HEREJSONERROR", String.valueOf(error));
            }
        };

    }

}
