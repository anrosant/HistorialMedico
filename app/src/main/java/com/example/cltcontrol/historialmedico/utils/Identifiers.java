package com.example.cltcontrol.historialmedico.utils;

public class Identifiers {

    public static final String URL_SIGNOS = "http://historialmedico.pythonanywhere.com/api/signosVitales/";
    public static final String URL_USUARIO = "http://historialmedico.pythonanywhere.com/api/usuario/";
    public static final String URL_EMPLEADO = "http://historialmedico.pythonanywhere.com/api/empleado/";
    public static final String URL_CONSULTA_MEDICA = "http://historialmedico.pythonanywhere.com/api/consultaMedica/";
    public static final String URL_ATENCION_ENFERMERIA = "http://historialmedico.pythonanywhere.com/api/atencionEnfermeria/";
    public static final String URL_DIAGNOSTICO = "http://historialmedico.pythonanywhere.com/api/diagnostico/";
    public static final String URL_PERMISO = "http://historialmedico.pythonanywhere.com/api/permisoMedico/";
    public static final String URL_PATOLOGIAS_PERSONALES = "http://historialmedico.pythonanywhere.com/api/antecedentePatologicoPersonal/";
    public static final String URL_PATOLOGIAS_FAMILIARES = "http://historialmedico.pythonanywhere.com/api/antecedentePatologicoFamiliar/";
    public static final String URL_AUTH_JWT = "http://historialmedico.pythonanywhere.com/auth-jwt/";
    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;



    //un broadcast para saber si la data está sincronizada o no
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Sesion
    public static final String CARGO = "cargo";

}
