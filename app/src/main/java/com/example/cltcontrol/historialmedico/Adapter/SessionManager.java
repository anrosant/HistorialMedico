package com.example.cltcontrol.historialmedico.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.HashMap;
import java.util.List;

public class SessionManager {
    private SharedPreferences sharedPrefer;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String SESION = "";
    private static final String NOMBRE_USUARIO = "";
    private static final String CARGO = "";

    public SessionManager(Context context) {
        this.context = context;
        sharedPrefer = context.getSharedPreferences(SESION, PRIVATE_MODE);
        editor = sharedPrefer.edit();
    }

    public void crearSesion(Long usu_id) {
        List<Empleado> empleado = Empleado.find(Empleado.class, "usuario = ?", String.valueOf(usu_id));
        editor.putString(NOMBRE_USUARIO, empleado.get(0).getUsuario().getUsuario());
        editor.putString(CARGO, empleado.get(0).getOcupacion());
        editor.commit();
    }

    public HashMap<String, String> obtenerInfoUsuario() {
        HashMap<String, String> usuario = new HashMap<String, String>();
        usuario.put("nombre_usuario",sharedPrefer.getString(NOMBRE_USUARIO, null));
        usuario.put("cargo",sharedPrefer.getString(CARGO, null));
        return usuario;
    }
}