package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProblemaActualTest {
    private ConsultaMedica consultaMedica;
    private int res;

    @Before
    public void beforeEachTest(){
        consultaMedica = new ConsultaMedica();
        res = -1;
    }

    @After
    public void afterEachTest(){
        consultaMedica = null;
        res = -1;
    }

    @Test
    public void testProblemaActual_valida1(){
        res = consultaMedica.validarCampoTexto("Vomito continuo");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testProblemaActual_valida2(){
        res = consultaMedica.validarCampoTexto("Presenta diarrea");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testProblemaActual_invalida1(){
        res = consultaMedica.validarCampoTexto("");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testProblemaActual_invalida2(){
        res = consultaMedica.validarCampoTexto("1233");
        Assert.assertEquals(1, res);
    }
}
