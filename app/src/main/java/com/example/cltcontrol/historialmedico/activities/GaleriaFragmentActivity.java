package com.example.cltcontrol.historialmedico.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.ImageAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class GaleriaFragmentActivity extends FragmentActivity {

    private GridView gridView;
    private List<Bitmap> misFotosBitmap;
    private List<String> rutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_galeria);

        //Obtener todas las rutas desde la base
        rutas= new ArrayList<>();
        rutas.add("/storage/emulated/0/Medicos/examenes/dado1.jpg");
        rutas.add("/storage/emulated/0/Medicos/examenes/capitan.jpg");
        rutas.add("/storage/emulated/0/Medicos/examenes/spider.jpg");
        rutas.add("/storage/emulated/0/Medicos/examenes/civil.jpg");

        gridView = findViewById(R.id.gridImagenes);
        gridView.setAdapter(new ImageAdapter(this, rutas));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                misFotosBitmap=((ImageAdapter)gridView.getAdapter()).getMisFotosBitmap();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GaleriaFragmentActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.imageView);

                photoView.setImageBitmap(misFotosBitmap.get(position));


                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


    }

}

