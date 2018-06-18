package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.SignosVitales;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignosVitalesTest {
    private SignosVitales signos;
    private int res;

    @Before
    public void beforeEachTest(){
        signos = new SignosVitales();
        res = -1;
    }

    @After
    public void afterEachTest(){
        signos = null;
        res = -1;
    }

    @Test
    public void testSignos_correctos(){
        res = signos.validarSignos("120", "90", "80", "37");
        Assert.assertEquals(3, res);
    }

    @Test
    public void testPresion_Sistolica_invalida1(){
        res = signos.validarSignos("138", "80", "70", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Sistolica_invalida2(){
        res = signos.validarSignos("-76", "80", "70", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Sistolica_invalida3(){
        res = signos.validarSignos("wr", "80", "70", "35.4");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPresion_Sistolica_invalida4(){
        res = signos.validarSignos("", "80", "70", "35.4");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testPresion_Distolica_invalida1(){
        res = signos.validarSignos("120", "100", "70", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Distolica_invalida2(){
        res = signos.validarSignos("120", "68", "70", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Distolica_invalida3(){
        res = signos.validarSignos("120", "iq", "70", "35.4");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPresion_Distolica_invalida4(){
        res = signos.validarSignos("120", "", "70", "35.4");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testPulso_invalido1(){
        res = signos.validarSignos("120", "80", "110", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPulso_invalido2(){
        res = signos.validarSignos("120", "80", "-20", "35.4");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPulso_invalido3(){
        res = signos.validarSignos("120", "80", "k2d", "35.4");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPulso_invalido4(){
        res = signos.validarSignos("120", "80", "", "35.4");
        Assert.assertEquals(0, res);
    }

    @Test
    public void testTemperatura_invalida1(){
        res = signos.validarSignos("120", "80", "72", "44.2");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testTemperatura_invalida2(){
        res = signos.validarSignos("120", "80", "72", "23");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testTemperatura_invalida3(){
        res = signos.validarSignos("120", "80", "72", "kji");
        Assert.assertEquals(2, res);
    }

    @Test
    public void testTemperatura_invalida4(){
        res = signos.validarSignos("120", "80", "72", "");
        Assert.assertEquals(0, res);
    }

}