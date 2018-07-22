package com.example.cltcontrol.historialmedico.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface IResult {
    void notifySuccess(String requestType, JSONObject response);
    void notifyError(String requestType, VolleyError error);
    void notifyMsjError(String requestType, String error);
    void notifyJSONError(String requestType, JSONException error);
}
