package com.example.cltcontrol.historialmedico.Identifiers;

import android.widget.Toast;

public class Validaciones {

    public static boolean validarSoloLetras(String valor){
        return valor.matches("[a-zA-Z.? ]*");
    }
    public static boolean validarUsuario(String usuario){
        return usuario.matches("[a-z]([a-z0-9]+)*");
    }
    public static boolean validarContrasenias(String contrasenia){
        return contrasenia.matches("([a-z0-9])+");
    }
}
