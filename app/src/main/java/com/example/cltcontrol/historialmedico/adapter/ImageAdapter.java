package com.example.cltcontrol.historialmedico.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private File imgFile;
    private List<Bitmap> misFotosBitmap;

    private final Context mContext;

    public ImageAdapter(Context context, List<String> rutas){
        mContext = context;
        misFotosBitmap = new ArrayList<>();
        for (String actual : rutas) {
            imgFile = new File(actual);

            if(imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                misFotosBitmap.add(myBitmap);
            }
        }
    }

    public int getCount(){
        return misFotosBitmap.size();
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(250,250));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8,8,8,8);

        imageView.setImageBitmap(misFotosBitmap.get(position));
        return imageView;
    }

    public List<Bitmap> getMisFotosBitmap() {
        return misFotosBitmap;
    }

    public void setMisFotosBitmap(List<Bitmap> misFotosBitmap) {
        this.misFotosBitmap = misFotosBitmap;
    }
}
