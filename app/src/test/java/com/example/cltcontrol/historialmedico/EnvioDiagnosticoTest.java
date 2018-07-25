package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.Diagnostico;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class EnvioDiagnosticoTest {
    @Test
    private void EnvioParametrosValidos(){
        Diagnostico.getJSONDiagnostico("","","");
    }

}
