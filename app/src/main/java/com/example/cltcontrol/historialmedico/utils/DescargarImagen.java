package com.example.cltcontrol.historialmedico.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DescargarImagen extends AsyncTask<String,Void,Bitmap> {

    private String DIRECTORIO= "Medicos/examenes/";
    final int min = 10;
    final int max = 250188;
    final int random = new Random().nextInt((max - min) + 1) + min;

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap imagen = descargarImagen(url);
        return imagen;
    }

    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }

    //Esto se ejecuta luego que termina doInBackground
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Long consecutivo = (System.currentTimeMillis() / 1000);
        String nombreImagen = consecutivo.toString() + ".jpg";

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + DIRECTORIO +random+ nombreImagen);
        //String ruta = file.getAbsolutePath().toString();
        //Log.d("Ruta","Path: "+ruta);

        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            result.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
            ostream.flush();
            ostream.close();
        } catch (IOException e) {
            Log.e("IOException", e.getLocalizedMessage());
        }

    }
}