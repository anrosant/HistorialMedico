package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import junit.framework.Assert;

import org.junit.Test;

public class PreescripcionTest {
    private ConsultaMedica consultaMedica = new ConsultaMedica();
    private int res;

    @Test
    public void testPreescripcion_valida(){
        res = consultaMedica.validarCampoTexto("Tomar 2 paracetamol");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPreescripcion_invalida(){
        res = consultaMedica.validarCampoTexto("");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testPreescripcion_invalida2(){
        res = consultaMedica.validarCampoTexto("1233");
        Assert.assertEquals(1, res);
    }

}
