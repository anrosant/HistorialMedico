package com.example.cltcontrol.historialmedico.utils;

import android.annotation.SuppressLint;
import android.widget.TextView;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Identifiers {

    public static final String URL_SIGNOS = "http://historialmedico.pythonanywhere.com/api/signosVitales/";
    public static final String URL_USUARIO = "http://historialmedico.pythonanywhere.com/api/usuario/";
    public static final String URL_EMPLEADO = "http://historialmedico.pythonanywhere.com/api/empleado/";
    public static final String URL_CONSULTA_MEDICA = "http://historialmedico.pythonanywhere.com/api/consultaMedica/";
    public static final String URL_ATENCION_ENFERMERIA = "http://historialmedico.pythonanywhere.com/api/atencionEnfermeria/";
    public static final String URL_DIAGNOSTICO = "http://historialmedico.pythonanywhere.com/api/diagnostico/";
    public static final String URL_PERMISO_MEDICO = "http://historialmedico.pythonanywhere.com/api/permisoMedico/";
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

    /*
     * Convierte la fecha(String) en Date
     * */
    public static Date convertirFecha(String fecha){
        //Fechas
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mmm");
        Date fechaNueva = null;
        try {
            fechaNueva = formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaNueva;
    }

    /*
    * Funcion para quitar las tildes y ñ
    * */
    public static String quitaDiacriticos(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    /*
    * Calcula el número de días que hay entre una fecha
    * @params fecha_desde fecha de tipo TextView desde donde inicia la fecha
    * @params fecha_hasta fecha de tipo TextView hasta donde termina la fecha
    * @return numDias (long) retorna el número de días transcurridos entre esas fechas
    * */
    public static long calcNumDias(TextView fecha_desde, TextView fecha_hasta) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        long numDias=0;

        Date fecha_ini=null, fecha_fin=null;
        String string_fecha_ini = fecha_desde.getText().toString();
        String string_fecha_fin = fecha_hasta.getText().toString();

        if (!string_fecha_ini.equals("") && !string_fecha_fin.equals("")) {
            try {
                fecha_ini = simpleDateFormat.parse(string_fecha_ini);
                fecha_fin = simpleDateFormat.parse(string_fecha_fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert fecha_fin != null;
            long dias_mili = Math.abs(fecha_fin.getTime() - fecha_ini.getTime());
            numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            //numero_dias.setText(Long.toString(numDias + 1));
        }
        return numDias;
    }

}
