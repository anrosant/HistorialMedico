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
import com.example.cltcontrol.historialmedico.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestService {

    IResult mResultCallback = null;
    Context mContext;

    public RequestService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataRequest(final String requestType, String url,JSONObject sendObj){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(url,sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(requestType,response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType,error);
                }
            });

            queue.add(jsonObj);

        }catch(Exception e){
            if(mResultCallback != null)
                mResultCallback.notifyMsjError(requestType,"No tiene conexión");
        }
    }

    public void getDataRequest(final String requestType, String url){
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
                        }catch(JSONException e){
                            getOneObject(requestType, response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mResultCallback != null)
                    mResultCallback.notifyError(requestType, error);
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
        //queue.add(stringRequest);
    }

    private void getOneObject(String requestType, String response){
        try {
            JSONObject objectJSON = new JSONObject(response);
            if(mResultCallback != null)
                mResultCallback.notifySuccess(requestType, objectJSON);
        } catch (JSONException e) {
            if(mResultCallback != null)
                mResultCallback.notifyJSONError(requestType, e);
        }


    }

}
