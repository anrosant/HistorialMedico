package com.example.cltcontrol.historialmedico.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cltcontrol.historialmedico.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c){
        mContext = c;
    }

    public int getCount(){
        return mThumbsIds.length;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(300,300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8,8,8,8);
        imageView.setImageResource(mThumbsIds[position]);
        return imageView;
    }

    private Integer[] mThumbsIds = {
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2,
            R.drawable.perrito1,
            R.drawable.perrito2
    };

}
