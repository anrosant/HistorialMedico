package com.example.cltcontrol.historialmedico.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.HashMap;
import java.util.List;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.CARGO;

public class SessionManager {
    private SharedPreferences sharedPrefer;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String PREFS_NAME = "PREFS";
    private static int PRIVATE_MODE = 0;
    private static final String SESION = "sesion";
    private static final String NOMBRE_USUARIO = "nombre_usuario";



    public SessionManager(Context context) {
        this.context = context;
        sharedPrefer = context.getSharedPreferences(SESION, PRIVATE_MODE);
        editor = sharedPrefer.edit();
    }

    public void crearSesion(Long usu_id) {
        List<Empleado> empleado = Empleado.find(Empleado.class, "usuario = ?", String.valueOf(usu_id));
        if(empleado.size()!=0){
            editor.putString(NOMBRE_USUARIO, empleado.get(0).getUsuario().getUsuario());
            editor.putString(CARGO, empleado.get(0).getOcupacion());
            editor.apply();
            //editor.commit();
            setLoggedIn(context,true);
        }

    }

    public HashMap<String, String> obtenerInfoUsuario() {
        HashMap<String, String> usuario = new HashMap<String, String>();
        usuario.put("nombre_usuario",sharedPrefer.getString(NOMBRE_USUARIO, null));
        usuario.put("cargo",sharedPrefer.getString(CARGO, null));
        return usuario;
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    /**
     * Cambia el status de login
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Obtiene el status de login
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }
    /*
    * Elimina la preferencia
    * */
    public void cerrarSesion(Context context) {
        setLoggedIn(context, false);
        editor = sharedPrefer.edit();
        editor.clear();
        editor.apply();
    }
}