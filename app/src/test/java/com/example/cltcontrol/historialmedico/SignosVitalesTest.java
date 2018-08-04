package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.SignosVitales;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class SignosVitalesTest {
    private SignosVitales signos;
    private int res;
    private SimpleDateFormat formato;
    private Date fecha;
    private String fechaText;

    @Before
    public void beforeEachTest(){
        signos = new SignosVitales();
        res = -1;
        formato = new SimpleDateFormat("yyyy-MM-dd");
        fecha = new Date();
        fechaText = formato.format(fecha);
    }

    @After
    public void afterEachTest(){
        signos = null;
        res = -1;
        formato = null;
        fecha = null;
        fechaText = "";
    }

    @Test
    public void testSignos_correctos(){
        res = signos.validarSignosTest("120", "90", "80", "37", fechaText);
        Assert.assertEquals(3, res);
    }

    @Test
    public void testPresion_Sistolica_invalida1(){
        res = signos.validarSignosTest("138", "80", "70", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Sistolica_invalida2(){
        res = signos.validarSignosTest("-76", "80", "70", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Sistolica_invalida3(){
        res = signos.validarSignosTest("wr", "80", "70", "35.4", fechaText);
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPresion_Sistolica_invalida4(){
        res = signos.validarSignosTest("", "80", "70", "35.4", fechaText);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testPresion_Distolica_invalida1(){
        res = signos.validarSignosTest("120", "100", "70", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Distolica_invalida2(){
        res = signos.validarSignosTest("120", "68", "70", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPresion_Distolica_invalida3(){
        res = signos.validarSignosTest("120", "iq", "70", "35.4", fechaText);
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPresion_Distolica_invalida4(){
        res = signos.validarSignosTest("120", "", "70", "35.4", fechaText);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testPulso_invalido1(){
        res = signos.validarSignosTest("120", "80", "110", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPulso_invalido2(){
        res = signos.validarSignosTest("120", "80", "-20", "35.4", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testPulso_invalido3(){
        res = signos.validarSignosTest("120", "80", "k2d", "35.4", fechaText);
        Assert.assertEquals(2, res);
    }

    @Test
    public void testPulso_invalido4(){
        res = signos.validarSignosTest("120", "80", "", "35.4", fechaText);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testTemperatura_invalida1(){
        res = signos.validarSignosTest("120", "80", "72", "44.2", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testTemperatura_invalida2(){
        res = signos.validarSignosTest("120", "80", "72", "23", fechaText);
        Assert.assertEquals(1, res);
    }

    @Test
    public void testTemperatura_invalida3(){
        res = signos.validarSignosTest("120", "80", "72", "kji", fechaText);
        Assert.assertEquals(2, res);
    }

    @Test
    public void testTemperatura_invalida4(){
        res = signos.validarSignosTest("120", "80", "72", "", fechaText);
        Assert.assertEquals(0, res);
    }

    @Test
    public void testFecha_valida(){
        res = signos.validarSignosTest("120", "80", "72",
                "36", fechaText);
        Assert.assertEquals(3, res);
    }

    @Test
    public void testFecha_invalida1(){
        res = signos.validarSignosTest("120", "80", "72",
                "36", "2016-03-24");
        Assert.assertEquals(1, res);
    }

    @Test
    public void testFecha_invalida2(){
        res = signos.validarSignosTest("120", "80", "72",
                "36", "");
        Assert.assertEquals(0, res);
    }

}