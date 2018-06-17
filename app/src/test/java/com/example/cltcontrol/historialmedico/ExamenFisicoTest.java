package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.ConsultaMedica;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExamenFisicoTest {
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
    public void testExamenFisico_valida1(){
        res = consultaMedica.validarCampoTexto("Se examino su torso y presenta moretones");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testExamenFisico_valida2(){
        res = consultaMedica.validarCampoTexto("Se le hizo una prueba de reflejos y estan correctos");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testExamenFisico_invalida1(){
        res = consultaMedica.validarCampoTexto("");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testExamenFisico_invalida2(){
        res = consultaMedica.validarCampoTexto("1233");
        Assert.assertEquals(1, res);
    }
}
