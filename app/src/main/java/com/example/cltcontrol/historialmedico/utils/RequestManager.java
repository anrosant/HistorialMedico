package com.example.cltcontrol.historialmedico.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Anni on 04/07/18.
 */
public class RequestManager {

    @SuppressLint("StaticFieldLeak")
    private static RequestManager mInstance;
    private RequestQueue mRequestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private RequestManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}