package com.example.cltcontrol.historialmedico.utils;

import com.example.cltcontrol.historialmedico.models.Enfermedad;
import java.util.List;

public class ListaEnfermedades {

    public static List<Enfermedad> enfermedadList;

    /*
    * Guarda las enfermedades que están en la base de datos, en una lista
    */
    public static List<Enfermedad> readEnfermedadesAll(){
        try{
            enfermedadList = Enfermedad.listAll(Enfermedad.class);
        }catch (Exception e){
            //Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return enfermedadList;
    }
}
