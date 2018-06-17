package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.utils.EmpleadoController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {
    private EmpleadoController ec;

    @Before
    public void beforeEachTest(){
        ec = new EmpleadoController();
    }

    @After
    public void afterEachTest(){
        ec = null;
    }

    @Test
    public void testCredenciales_correctas(){
        assertTrue(ec.ingresoSistema("jgarcia", "jgarcia"));
    }

    @Test
    public void testUsuario_incorrecto1(){
        assertFalse(ec.ingresoSistema("jorgeg", "jgarcia"));
    }

    @Test
    public void testUsuario_incorrecto2(){
        assertFalse(ec.ingresoSistema("", "jgarcia"));
    }

    @Test
    public void testContrasenia_incorrecta1(){
        assertFalse(ec.ingresoSistema("jgarcia", "jor"));
    }

    @Test
    public void testContrasenia_incorrecta2(){
        assertFalse(ec.ingresoSistema("jgarcia", ""));
    }

}