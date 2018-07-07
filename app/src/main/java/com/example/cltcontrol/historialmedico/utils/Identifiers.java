package com.example.cltcontrol.historialmedico.utils;

public class Identifiers {

    public static final String URL_SAVE_SIGNOS = "http://annrosant.pythonanywhere.com/signosVitales/";
    public static final String URL_SAVE_USUARIO = "http://annrosant.pythonanywhere.com/usuario/";
    public static final String URL_SAVE_EMPLEADO = "http://annrosant.pythonanywhere.com/empleado/";
    public static final String URL_SAVE_CONSULTA_MEDICA = "http://annrosant.pythonanywhere.com/consultaMedica/";
    public static final String URL_SAVE_ATENCION_ENFERMERIA = "http://annrosant.pythonanywhere.com/atencionEnfermeria/";

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
}
