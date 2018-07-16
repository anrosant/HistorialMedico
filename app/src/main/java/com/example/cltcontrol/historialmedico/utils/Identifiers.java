package com.example.cltcontrol.historialmedico.utils;

public class Identifiers {

    public static final String URL_SAVE_SIGNOS = "http://historialmedico.pythonanywhere.com/signosVitales/";
    public static final String URL_SAVE_USUARIO = "http://historialmedico.pythonanywhere.com/usuario/";
    public static final String URL_SAVE_EMPLEADO = "http://historialmedico.pythonanywhere.com/empleado/";
    public static final String URL_SAVE_CONSULTA_MEDICA = "http://historialmedico.pythonanywhere.com/consultaMedica/";
    public static final String URL_SAVE_ATENCION_ENFERMERIA = "http://historialmedico.pythonanywhere.com/atencionEnfermeria/";

    public static final String URL_SAVE_DIAGNOSTICO = "http://historialmedico.pythonanywhere.com/diagnostico/";
    public static final String URL_SAVE_PERMISO = "http://historialmedico.pythonanywhere.com/permisoMedico/";
    public static final String URL_SAVE_PATOLOGIAS_PERSONALES = "http://historialmedico.pythonanywhere.com/antecedentePatologicoPersonal/";
    public static final String URL_SAVE_PATOLOGIAS_FAMILIARES = "http://historialmedico.pythonanywhere.com/antecedentePatologicoFamiliar/";


    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
}
