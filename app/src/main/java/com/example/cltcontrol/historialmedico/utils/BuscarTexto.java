package com.example.cltcontrol.historialmedico.utils;

import java.text.Normalizer;

public class BuscarTexto {
    //Funcion para quitar las tildes y ñ
    public static String quitaDiacriticos(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
