package com.example.cltcontrol.historialmedico.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CalculoDias {

    public static long calcNumDias(TextView fecha_desde, TextView fecha_hasta) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
            long dias_mili = Math.abs(fecha_fin.getTime() - fecha_ini.getTime());
            numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            //numero_dias.setText(Long.toString(numDias + 1));
        }
        return numDias;
    }

}
