package com.example.cltcontrol.historialmedico.utils;

import android.graphics.Matrix;
import android.media.ExifInterface;

/*
    Clase que captura la orientacion de la foto/imagen y
    establece orientacion de la foto en la App.
 */

public class ImageOrientation {

    public ImageOrientation() {
    }

    public static Matrix orientacionImagen(final int orientationSalida) {
        Matrix matrix = new Matrix();
        switch (orientationSalida) {
            case ExifInterface.ORIENTATION_ROTATE_270: {
                matrix.postRotate(270);
                break;
            }

            case ExifInterface.ORIENTATION_ROTATE_180: {
                matrix.postRotate(180);
                break;
            }

            case ExifInterface.ORIENTATION_ROTATE_90: {
                matrix.postRotate(90);
                break;
            }

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL: {
                matrix.preScale(-1.0f, 1.0f);
                break;
            }

            case ExifInterface.ORIENTATION_FLIP_VERTICAL: {
                matrix.preScale(1.0f, -1.0f);
                break;
            }

            case ExifInterface.ORIENTATION_TRANSPOSE: {
                matrix.preRotate(-90);
                matrix.preScale(-1.0f, 1.0f);
                break;
            }

            case ExifInterface.ORIENTATION_TRANSVERSE: {
                matrix.preRotate(90);
                matrix.preScale(-1.0f, 1.0f);
                break;
            }
        }
        return matrix;
    }

}
