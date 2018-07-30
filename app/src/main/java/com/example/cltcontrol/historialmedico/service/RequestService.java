package com.example.cltcontrol.historialmedico.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
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
import java.util.Iterator;
import java.util.Map;

public class RequestService {

    private IResult mResultCallback;
    private Context mContext;
    private ProgressDialog dialog=null;

    public RequestService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void postDataRequest(final String requestType, String url, final Map<String, String> sendObj, final String token){
        if (mContext instanceof Activity){
            dialog=new ProgressDialog(mContext);
            dialog.setMessage("Cargando...");
            dialog.show();
        }
        final RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest sr = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(mResultCallback != null){
                    try {
                        mResultCallback.notifySuccess(requestType, new JSONObject(response));
                    } catch (JSONException e) {
                        Log.e("ERROR JSONOBJECT", "No se pudo convertir a JSON");
                    }
                }
                if(dialog!=null)
                    dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Log.d("PARAMS1", String.valueOf(sendObj));
                return sendObj;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                if(token==null){
                    Log.d("TOKEN", "null");
                }else{
                    Log.d("TOKEN", String.valueOf(token));
                }

                params.put("Authorization","JWT "+token);
                Log.d("PARAMS", String.valueOf(params));

                return params;
            }
        };
        queue.add(sr);
    }

    public void postDataRequest2(final String requestType, String url, JSONObject sendObj, final String token){
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
                        mResultCallback.notifyMsjError(requestType,"No tiene conexión");
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            }){@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","JWT "+token);
                Log.d("PARAMS", String.valueOf(params));

                return params;
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
                        mResultCallback.notifyMsjError(requestType,"No tiene conexión");
                    }
                    if(dialog!=null)
                        dialog.dismiss();
                }
            });
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
                            Log.d("HERELENGTH", String.valueOf(obj.length()));
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject objectJSON = obj.getJSONObject(i);
                                if(mResultCallback != null)
                                    Log.d("HERE-RESPONSE", String.valueOf(objectJSON));
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


    private static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Log.d("KEY", key);
            String value = object.getString(key);
            Log.d("BOOLEAN", String.valueOf(object.getString(key).equals("''")));
            Log.d("VALUEE", value);
            Log.d("BOOLEANSTRING", String.valueOf(object.getString(key).equals("")));
            Log.d("VALUEE", value);
            if(object.getString(key).equals("")){
                value = " ";
            }

            map.put(key, value);
        }
        return map;
    }
}
