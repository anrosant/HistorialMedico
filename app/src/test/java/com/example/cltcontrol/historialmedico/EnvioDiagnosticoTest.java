package com.example.cltcontrol.historialmedico;

import com.example.cltcontrol.historialmedico.models.Diagnostico;

import org.junit.Test;

public class EnvioDiagnosticoTest {
    @Test
    private void EnvioParametrosValidos(){
        Diagnostico.getJSONDiagnostico("","","");
    }

}
