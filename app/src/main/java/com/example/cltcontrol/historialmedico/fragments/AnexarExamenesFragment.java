package com.example.cltcontrol.historialmedico.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.ImageAdapter;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.ExamenImagen;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.DescargarImagen;
import com.example.cltcontrol.historialmedico.utils.ImageOrientation;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.github.chrisbanes.photoview.PhotoView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_EXAMEN_IMAGEN;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class AnexarExamenesFragment extends Fragment{

    //Interface
    private IResult mResultCallback;

    private ImageView idImage;
    private Button btnCargarImagen;

    private String path;

    //Constantes
    //Se define carpeta Raiz para las imagenes
    private static final String CARPETA_RAIZ = "Medicos/";
    //Se define la carpeta donde se guardaran las imagenes de examenes
    private static final String CARPETA_IMAGENES = "examenes";
    //Se define la ruta donde se guardan la fotos
    public static final String DIRECTORIO = CARPETA_RAIZ + CARPETA_IMAGENES;
    private static final int COD_SELECCION = 10;
    private static final int COD_CAMARA = 20;

    private GridView gridView;
    private List<Bitmap> misFotosBitmap;
    private Empleado empleado;
    private ConsultaMedica consultaMedica;
    private String id_empleado_servidor;

    private String id_consulta_servidor;

    String imagenBase64;


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
        gridView = view.findViewById(R.id.gridImagenes);

        if(validaPermisos()){
            btnCargarImagen.setEnabled(true);
        }else{
            btnCargarImagen.setEnabled(false);
        }

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        String id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));

        String id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        id_empleado_servidor = String.valueOf(empleado.getId_serv());

        List<ExamenImagen> listaExamenesIMG = ExamenImagen.find(ExamenImagen.class, "consulta = ?", id_consulta_medica);

        List<String> urlList = new ArrayList<>();
        for(ExamenImagen actual : listaExamenesIMG){
            if(actual.getDescargada()==0){
                urlList.add(actual.getUrl());
            }
        }

        for(String actual: urlList){
            DescargarImagen nuevaTarea = new DescargarImagen();
            nuevaTarea.execute(actual);
        }

        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////Empieza galeria de fotos///////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Obtener todas las rutas desde la base

        List<ExamenImagen> listaExamenesIMGStored = ExamenImagen.find(ExamenImagen.class, "consulta = ?", id_consulta_medica);
        List<String> rutas = new ArrayList<>();

        //Se las setea a la lista de String
        for(ExamenImagen actual : listaExamenesIMGStored){
            if(actual.getDescargada()==1){
                rutas.add(actual.getRuta_movil());
            }
        }

        //Si la lista de rutas no esta vacia se carga la galeria
        if(!rutas.isEmpty()){
            //Se inicializa el adaptador enviandole el context y la lista de rutas
            gridView.setAdapter(new ImageAdapter(getContext(), rutas));


            //Funcionalidad de zoom cuando se le da click a alguna imagen de la galeria
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Se trae del adapter la lista de fotos
                    misFotosBitmap=((ImageAdapter)gridView.getAdapter()).getMisFotosBitmap();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                    PhotoView photoView = mView.findViewById(R.id.imageView);

                    //Se setea la imagen a la que se dio click en el photo view que se abre
                    photoView.setImageBitmap(misFotosBitmap.get(position));

                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////Termina galeria de fotos///////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

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

    //Funcion para seleccionar las opciones de tomar foto, cargar imagen o cerrar
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
                String ruta;
                try{
                    Uri miPath = data.getData();
                    ruta = Objects.requireNonNull(miPath).toString();
                    Toast.makeText(getContext(),"ruta: " + ruta,Toast.LENGTH_LONG).show();
                    idImage.setImageURI(miPath);

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

                guardarImagen();

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


    //Guarda la foto en la base de datos del servidor y localmente
    public void guardarImagen(){
        String rutaCargar = path;
        try{
            //1: Se manda a transformar la imagen a un String codificado en base 64
            imagenBase64 = toStringBase64(rutaCargar);

            //2: se verifica si aun no se ha creado la consulta
            if (consultaMedica.getEmpleado() == null) {
                //Se guarda la consulta
                postConsultaMedica(new Date());
            }else{//Si la consulta ya fue creada simplemente guarda la imagen
                postExamenImagen(imagenBase64);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Transforma (pasando la ruta) la imagen a un string codificado en base64
    private String toStringBase64(String rutaArchivo) throws IOException {

        File file = new File(rutaArchivo);
        byte[] bytes = transformaArchivo(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedString;
    }

    //Transforma un archivo a un arreglo de bytes
    private static byte[] transformaArchivo(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }


    private void guardarConsultaLocal(Date fecha, int status, int id_serv, String porDondeEntre){
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setId_serv(id_serv);
        consultaMedica.setFechaConsulta(fecha);
        consultaMedica.setStatus(status);
        consultaMedica.save();
        String por= new String();
        por= porDondeEntre;

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos de consulta "+por, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos de consulta se guardarán localmente "+por, Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Envía datos de Consulta médica al servidor (Solo se usa si todavia no se ha guardado la consuta medica)
     * */
    private void postConsultaMedica(final Date fechaConsulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("tagconsulta");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(id_empleado_servidor,fechaConsulta, "","","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
    }

    private void postExamenImagen(String imagenBase64){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        initRequestCallback("tagImagen");
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ExamenImagen.getHashMapExamenImagen(id_consulta_servidor,imagenBase64);
        requestService.postDataRequest("POSTCALL", URL_EXAMEN_IMAGEN, sendObj, token);
    }

    //Guarda la imagen en la base de datos local
    private void guardarImagenLocalmente(String ruta, int status, int id_imagen_servidor, String porDondeEntre){
        ExamenImagen imagenExamen = new ExamenImagen();
        imagenExamen.setId_serv(id_imagen_servidor);
        imagenExamen.setConsulta(consultaMedica);
        imagenExamen.setRuta_movil(ruta);
        imagenExamen.setStatus(status);
        imagenExamen.setDescargada(1);

        String por= new String();
        por= porDondeEntre;

        imagenExamen.save();

        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos de la imagen "+por, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"Hubo un error de conexión. Los datos de la imagen se guardarán localmente "+por, Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                //Si ha realizado post en ConsultaMedica
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    try {
                        String fechaConsulta = response.getString("fechaConsulta");
                        Date fecha = convertirFecha(fechaConsulta);
                        //Seteamos el id de la consulta del servidor para luego usar ese id para guardar la imagen
                        id_consulta_servidor = response.getString("pk");
                        guardarConsultaLocal(fecha,NAME_SYNCED_WITH_SERVER,Integer.parseInt(id_consulta_servidor), "tagconsulta Success");
                        postExamenImagen(imagenBase64);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{ //Si ha realizado post para guardar la imagen
                    try {
                        String pk = response.getString("pk");
                        guardarImagenLocalmente(path,NAME_SYNCED_WITH_SERVER,Integer.valueOf(pk),"imagen Success" );
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaLocal(new Date(), NAME_NOT_SYNCED_WITH_SERVER,0,"tagconsulta NotifyError");

                }else {
                    guardarImagenLocalmente(path,NAME_NOT_SYNCED_WITH_SERVER,0, "imagen Notifyerror");
                }
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaLocal(new Date(), 0,NAME_NOT_SYNCED_WITH_SERVER, "tagconsulta NotifyMsjError");

                }else {
                    guardarImagenLocalmente(path,NAME_NOT_SYNCED_WITH_SERVER,0, "imagen requestType"+ requestType+" msj error = "+error);
                }
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaLocal(new Date(), 0,NAME_NOT_SYNCED_WITH_SERVER, "tagconsulta json error");

                }else {
                    guardarImagenLocalmente(path,NAME_NOT_SYNCED_WITH_SERVER,0, "imagen json error");
                }
            }
        };

    }

}
