package com.example.cltcontrol.historialmedico.models;

import com.orm.SugarRecord;

public class ExamenImagen extends SugarRecord {
    private int status;
    private String ruta_movil;

    public ExamenImagen() {}

    public ExamenImagen(int status, String ruta_movil) {
        this.status = status;
        this.ruta_movil = ruta_movil;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRuta_movil() {
        return ruta_movil;
    }

    public void setRuta_movil(String ruta_movil) {
        this.ruta_movil = ruta_movil;
    }
}
