package com.example.cltcontrol.historialmedico.utils;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Identifiers {
    public static final String URL_SIGNOS = "http://www.sistemamedicoda.com/api/signosVitales/";
    public static final String URL_USUARIO = "http://www.sistemamedicoda.com/api/usuario/";
    public static final String URL_CONSULTA_MEDICA = "http://www.sistemamedicoda.com/api/consultaMedica/";
    public static final String URL_ATENCION_ENFERMERIA = "http://www.sistemamedicoda.com/api/atencionEnfermeria/";
    public static final String URL_DIAGNOSTICO = "http://www.sistemamedicoda.com/api/diagnostico/";
    public static final String URL_PERMISO_MEDICO = "http://www.sistemamedicoda.com/api/permisoMedico/";
    public static final String URL_PATOLOGIAS_PERSONALES = "http://www.sistemamedicoda.com/api/antecedentePatologicoPersonal/";
    public static final String URL_PATOLOGIAS_FAMILIARES = "http://www.sistemamedicoda.com/api/antecedentePatologicoFamiliar/";
    public static final String URL_AUTH_JWT = "http://www.sistemamedicoda.com/auth-jwt/";
    public static final String URL_EXAMEN_IMAGEN= "http://www.sistemamedicoda.com/api/examenConsulta/";
    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

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
     * @params fechaDesdeText fecha de tipo TextView desde donde inicia la fecha
     * @params fechaHastaText fecha de tipo TextView hasta donde termina la fecha
     * @return numDias (long) retorna el número de días transcurridos entre esas fechas
     * */
    public static long calcNumDias(TextView fechaDesdeText, TextView fechaHastaText) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-dd-mm");

        long numDias=0;

        Date fechaIni=null, fechaFin=null;
        String stringFechaIni = fechaDesdeText.getText().toString();
        String stringFechaFin = fechaHastaText.getText().toString();

        if (!stringFechaIni.equals("") && !stringFechaFin.equals("")) {
            try {
                fechaIni = simpleDateFormat.parse(stringFechaIni);
                fechaFin = simpleDateFormat.parse(stringFechaFin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert fechaFin != null;
            long diasMili = Math.abs(fechaFin.getTime() - fechaIni.getTime());
            numDias = TimeUnit.DAYS.convert(diasMili, TimeUnit.MILLISECONDS);
            //numero_dias.setText(Long.toString(numDias + 1));
        }
        return numDias;
    }

    /*
     * Evento para mostrar/ocultar contenedores de vista
     * */
    @SuppressLint("ClickableViewAccessibility")
    public static void mostrarOcultarTabsMenu(final TextView miTextViewTitle, final View miViewContainer){
        miTextViewTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (miTextViewTitle.getRight() - miTextViewTitle.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (!miViewContainer.isShown()){
                            miViewContainer.setVisibility(View.VISIBLE);
                            miTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_keyboard_arrow_up_cyan_24dp,0);
                        }else {
                            miViewContainer.setVisibility(View.GONE);
                            miTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_cyan_24dp,0);
                        }
                    }
                }
                return true;
            }
        });
    }

}
