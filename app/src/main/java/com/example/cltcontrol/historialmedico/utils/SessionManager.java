package com.example.cltcontrol.historialmedico.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Usuario;

import java.util.HashMap;
import java.util.List;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.CARGO;

public class SessionManager {
    private SharedPreferences sharedPrefer;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String LOGGED_IN_PREF = "logged_in_status";
    private static final String TOKEN = "token";
    private static final String SESION = "sesion";
    private static final String NOMBRE_USUARIO = "nombre_usuario";



    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        int PRIVATE_MODE = 0;
        sharedPrefer = context.getSharedPreferences(SESION, PRIVATE_MODE);
        editor = sharedPrefer.edit();
    }
    /*
     * Guarda el usuario y su token
     * @param usu_id id del usuario tipo Long
     * @param token es el token que recibe del servidor cuando ingresa a la app
     * */
    public void crearSesion(Long usu_id, String token) {
        Usuario usuario = Usuario.findById(Usuario.class, usu_id);
        editor.putString(NOMBRE_USUARIO, usuario.getUsuario());
        editor.putString(CARGO, usuario.getEmpleado().getOcupacion());
        editor.putString(TOKEN, token);
        editor.apply();
        setLoggedIn(context,true);


    }
    /*
    * Permite obtener los datos del usuario almacenado en las preferencias
    * */
    public HashMap<String, String> obtenerInfoUsuario() {
        HashMap<String, String> usuario = new HashMap<String, String>();
        usuario.put("nombre_usuario",sharedPrefer.getString(NOMBRE_USUARIO, null));
        usuario.put("cargo",sharedPrefer.getString(CARGO, null));
        usuario.put("token",sharedPrefer.getString(TOKEN, null));
        return usuario;
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Cambia el status de login
     * @param context contexto desde donde es llamado
     * @param loggedIn indica si está logueado o no, tipo boolean
     */
    private static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Obtiene el status de login
     * @param context contexto desde donde es llamado
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    /*
    * Elimina la preferencia
    * @param context contexto desde donde es llamado
    * */
    public void cerrarSesion(Context context) {
        setLoggedIn(context, false);
        editor = sharedPrefer.edit();
        editor.clear();
        editor.apply();
    }
}