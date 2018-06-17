package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.utils.EmpleadoController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {

    @Test
    public void testCredenciales_correctas(){
        EmpleadoController ec = new EmpleadoController();
        assertTrue(ec.ingresoSistema("jgarcia", "jgarcia"));
    }

    @Test
    public void testCredenciales_incorrectas(){
        EmpleadoController ec = new EmpleadoController();
        assertFalse(ec.ingresoSistema("jorgeg", "jorgeg"));
    }

    @Test
    public void testCredenciales_en_blanco(){
        EmpleadoController ec = new EmpleadoController();
        assertFalse(ec.ingresoSistema("", ""));
    }

}