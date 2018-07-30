package com.example.cltcontrol.historialmedico.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ExamenImagen;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.ImageOrientation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AnexarExamenesFragment extends Fragment {

    private ImageView idImage;
    private Button btnCargarImagen;

    private String path;
    private IResult mResultCallback;

    //Constantes
    //Se define carpeta Raiz para las imagenes
    private static final String CARPETA_RAIZ = "Medicos/";
    //Se define la carpeta donde se guardaran las imagenes de examenes
    private static final String CARPETA_IMAGENES = "examenes";
    //Se define la ruta donde se guardan la fotos
    private static final String DIRECTORIO = CARPETA_RAIZ + CARPETA_IMAGENES;
    private static final int COD_SELECCION = 10;
    private static final int COD_CAMARA = 20;
    private Bitmap bitmap;

    public AnexarExamenesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anexar_examenes, container, false);

        //referencia hacia los views
        idImage = view.findViewById(R.id.idImage);
        btnCargarImagen = view.findViewById(R.id.btnCargarImagen);

        if(validaPermisos()){
            btnCargarImagen.setEnabled(true);
        }else{
            btnCargarImagen.setEnabled(false);
        }

        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });
        return view;
    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        if((ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar permiso en la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                btnCargarImagen.setEnabled(true);
            }
        }
    }

    private void cargarImagen() {
        //Arreglo con opciones para el cuadro de dialogo
        final CharSequence[] opciones = {"Tomar foto","Seleccionar imagen","Cancelar"};
        final AlertDialog.Builder alertaOpciones = new AlertDialog.Builder(getContext());
        alertaOpciones.setTitle("Seleccione una opcion");
        alertaOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar foto")){
                    tomarFoto();
                }else{
                    if(opciones[i].equals("Seleccionar imagen")){
                        Intent intentCamara = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intentCamara.setType("image/");
                        startActivityForResult(Intent.createChooser(intentCamara,"Seleccione una imagen"),COD_SELECCION);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertaOpciones.show();
    }

    private void tomarFoto() {
        //Archivo para abrir la ruta donde se guardara la foto
        File myFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO);
        //Variable Booleana para validar que la foto fue creada
        boolean directorioEsCreado = myFile.exists();

        if(!directorioEsCreado){
            directorioEsCreado = myFile.mkdirs();
        }
        if(directorioEsCreado) {
            //Asignacion nombre de la imagen
            //currentTimeMillis devuelve la hora en milisegundos
            Long consecutivo = (System.currentTimeMillis() / 1000);
            String nombreImagen = consecutivo.toString() + ".jpg";

            //Ruta completa con nombre de la imagen
            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO + File.separator + nombreImagen;

            //GUARDA LA RUTA EN LA BD LOCAL
            ExamenImagen examenImagen = new ExamenImagen(0, path);
            examenImagen.save();

            //Archivo para tomar la foto
            File fileImagen = new File(path);
            //Lanzar la camara
            Intent fotoCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fotoCamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            startActivityForResult(fotoCamara, COD_CAMARA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCION:
                String ruta = "";
                try{
                    Uri miPath = data.getData();
                    ruta = Objects.requireNonNull(miPath).toString();
                    Toast.makeText(getContext(),"ruta: " + ruta,Toast.LENGTH_LONG).show();
                    idImage .setImageURI(miPath);

                    //postImagen(ruta);
                }catch (Exception e){
                    //
                }
                break;

            case COD_CAMARA:
                //permitir que imagenes se almacenen en dispositivo
                MediaScannerConnection.scanFile(getContext(),new String[] {path},null,
                        new MediaScannerConnection.OnScanCompletedListener(){
                            //metodo para saber si proceso completo
                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                                //Log.d("Ruta de Almacenamiento","Path: "+path);
                            }
                        });

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Matrix matrix;
                if(bitmap !=null){
                    try{
                        ruta = path;
                        Toast.makeText(getContext(),"ruta: "+ ruta,Toast.LENGTH_LONG).show();
                        //proceso para adaptar orientacion de la foto tomada por la camara atraves de la App
                        final ExifInterface exif = new ExifInterface(path);
                        final int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        matrix = ImageOrientation.orientacionImagen(exifOrientation);
                        Bitmap transformedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        int nh = (int) ( transformedBitmap.getHeight() * (512.0 / transformedBitmap.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(transformedBitmap, 512, nh, true);
                        idImage.setImageBitmap(scaled);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private void postImagen(String filename){
        initRequestCallback();
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        JSONObject sendObj = null;

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        try {
            sendObj = new JSONObject("{" +
                    "'empleado': 2, "+
                    "'chequeo': 1, "+
                    "foto: '"+imageString+"'"+
                    "}");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 5) PASAR LOS DATOS A LA FUNCIÓN
        requestService.postDataRequest("POSTCALL", "http://historialmedico.pythonanywhere.com/api/fichaMedica", sendObj);
    }

    private void initRequestCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                try {
                    //Log.d("HERECONSULTA", String.valueOf(response));
                    //Si ha realizado post en ConsultaMedica
                    //PASO 6) FINAL
                    String fechaConsulta = response.getString("fecha");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));


                Log.e("ERROR", String.valueOf(error));
                Toast.makeText(getContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };

    }

}
