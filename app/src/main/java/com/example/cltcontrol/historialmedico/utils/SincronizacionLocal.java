package com.example.cltcontrol.historialmedico.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_EMPLEADO;

public class SincronizacionLocal {

    private Context context;
    private IResult mResultCallback;

    public SincronizacionLocal() {}

    public SincronizacionLocal(Context context, IResult mResultCallback) {
        this.context = context;
        this.mResultCallback = mResultCallback;
    }

    public void obtenerEmpleadosDesdeServidor(){
        RequestService requestService = new RequestService(mResultCallback, context);
        requestService.getDataRequest("POSTCALL", URL_EMPLEADO);
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                Log.d("NOTIFY", "SUCCESSSSS");

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
