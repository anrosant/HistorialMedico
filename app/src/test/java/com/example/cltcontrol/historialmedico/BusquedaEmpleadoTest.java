package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.adapter.AdapterItemEmpleado;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BusquedaEmpleadoTest {
    private AdapterItemEmpleado adapter = new AdapterItemEmpleado();

    @Test
    public void testBusqueda_valida() {
        assertTrue(adapter.validarBusqueda("Santacruz"));
    }

    @Test
    public void testBusqueda_invalida() {
        assertFalse(adapter.validarBusqueda("Ann213 "));
    }

}