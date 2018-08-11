package com.example.cltcontrol.historialmedico.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestService {

    private final IResult mResultCallback;
    private final Context mContext;
    private ProgressDialog dialog=null;

    public RequestService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }

    /*
    * Realiza el POST sin token
    * @param requestType es el nombre de tipo de request tipo String
    * @param url url a donde se va a realizar el POST, tipo String
    * @param sendObj es el JSON con los datos a enviar al servidor
    * */
    public void postDataRequest(final String requestType, String url, JSONObject sendObj){
        if (mContext instanceof Activity){
            dialog=new ProgressDialog(mContext);
            dialog.setMessage("Cargando...");
            dialog.show();
        }
        try {
            final RequestQueue queue = Volley.newRequestQueue(mContext);

            final JsonObjectRequest jsonObj = new JsonObjectRequest(url,sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null){
                        mResultCallback.notifySuccess(requestType,response);
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null){
                        mResultCallback.notifyMsjError(requestType,"No se puede iniciar sesión. Comprueba tu conexión de red");
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            });
            queue.add(jsonObj);

        }catch(Exception e){
            if(mResultCallback != null) {
                mResultCallback.notifyMsjError(requestType, "Hubo un error de conexión");
            }
            if(dialog!=null)
                dialog.dismiss();

        }
    }

    /*
     * Realiza el POST con token
     * @param requestType es el nombre de tipo de request tipo String
     * @param url url a donde se va a realizar el POST, tipo String
     * @param sendObj es el HashMap con los datos a enviar al servidor
     * @param token es el token con el que autentica el servidor para hacer el POST
     * */

    public void postDataRequest(final String requestType, String url, Map<String, String> sendObj, final String token){
        if (mContext instanceof Activity){
            dialog=new ProgressDialog(mContext);
            dialog.setMessage("Cargando...");
            dialog.show();
        }
        try {
            final RequestQueue queue = Volley.newRequestQueue(mContext);

            final JsonObjectRequest jsonObj = new JsonObjectRequest(url,new JSONObject(sendObj), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null){
                        mResultCallback.notifySuccess(requestType,response);
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null){
                        mResultCallback.notifyMsjError(requestType,String.valueOf(error));
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Authorization","JWT "+token);

                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonObj);
        }catch(Exception e){
            if(mResultCallback != null) {
                mResultCallback.notifyMsjError(requestType, "No tiene conexión");
            }
            if(dialog!=null)
                dialog.dismiss();
        }
    }

    public void getDataRequest(final String requestType, String url){
        if(mContext instanceof Activity){
            dialog=new ProgressDialog(mContext);
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        //RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject objectJSON = obj.getJSONObject(i);
                                if(mResultCallback != null)
                                    mResultCallback.notifySuccess(requestType, objectJSON);
                            }
                            if(dialog!=null)
                                dialog.dismiss();
                        }catch(JSONException e){
                            getOneObject(requestType, response);
                            if(dialog!=null)
                                dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mResultCallback != null)
                    mResultCallback.notifyError(requestType, error);
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
        RequestManager.getInstance(mContext).addToRequestQueue(stringRequest);
        //queue.add(stringRequest);
    }

    private void getOneObject(String requestType, String response){
        try {
            JSONObject objectJSON = new JSONObject(response);
            if(mResultCallback != null)
                mResultCallback.notifySuccess(requestType, objectJSON);
            if(dialog!=null)
                dialog.dismiss();
        } catch (JSONException e) {
            if(mResultCallback != null)
                mResultCallback.notifyJSONError(requestType, e);
            if(dialog!=null)
                dialog.dismiss();
        }
    }

    /*
    * Realiza PUT con token
    * @param requestType es el nombre de tipo de request tipo String
    * @param url url a donde se va a realizar el PUT, tipo String
    * @param sendObj es el HashMap con los datos a enviar al servidor
    * @param token es el token con el que autentica el servidor para hacer el PUT
    * */

    public void putDataRequest(final String requestType, String url, Map<String, String> sendObj, final String token){
        if (mContext instanceof Activity){
            dialog=new ProgressDialog(mContext);
            dialog.setMessage("Cargando...");
            dialog.show();
        }
        try {
            final RequestQueue queue = Volley.newRequestQueue(mContext);

            final JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url,new JSONObject(sendObj), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null){
                        mResultCallback.notifySuccess(requestType,response);
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null){
                        mResultCallback.notifyMsjError(requestType,String.valueOf(error));
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Authorization","JWT "+token);

                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonObj);
        }catch(Exception e){
            if(mResultCallback != null) {
                mResultCallback.notifyMsjError(requestType, "No tiene conexión");
            }
            if(dialog!=null)
                dialog.dismiss();
        }
    }
}
