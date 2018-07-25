package com.example.cltcontrol.historialmedico.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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

public class RequestService {

    IResult mResultCallback = null;
    Context mContext;
    ProgressDialog dialog;
    public RequestService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void postDataRequest(final String requestType, String url,JSONObject sendObj){

        dialog=new ProgressDialog(mContext);
        dialog.setMessage("Cargando...");
        dialog.show();
        try {
            final RequestQueue queue = Volley.newRequestQueue(mContext);

            final JsonObjectRequest jsonObj = new JsonObjectRequest(url,sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null){
                        mResultCallback.notifySuccess(requestType,response);
                    }
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null){
                        mResultCallback.notifyMsjError(requestType,"No tiene conexión");
                    }
                    dialog.dismiss();
                }
            });

            queue.add(jsonObj);

        }catch(Exception e){
            if(mResultCallback != null) {
                mResultCallback.notifyMsjError(requestType, "No tiene conexión");
            }
            dialog.dismiss();

        }
    }

    public void getDataRequest(final String requestType, String url){
        dialog=new ProgressDialog(mContext);
        dialog.setMessage("Cargando...");
        dialog.show();
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
                            dialog.dismiss();
                        }catch(JSONException e){
                            getOneObject(requestType, response);
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mResultCallback != null)
                    mResultCallback.notifyError(requestType, error);
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
            dialog.dismiss();
        } catch (JSONException e) {
            if(mResultCallback != null)
                mResultCallback.notifyJSONError(requestType, e);
            dialog.dismiss();
        }


    }

}
