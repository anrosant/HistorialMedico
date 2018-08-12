package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.ExamenImagen;
import com.example.cltcontrol.historialmedico.models.PatologiasFamiliares;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.models.SignosVitales;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.EmpleadoController;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_USUARIO;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasenia;
    private EmpleadoController miController;

    private String usuario;
    private String password;

    IResult mResultCallback = null;
    RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        Button btnIngresoSistema = findViewById(R.id.btnIngresar);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
        //Verifica si ya se encuentra con la sesión activa
        if(SessionManager.getLoggedStatus(getApplicationContext())) {

            SessionManager sessionManager = new SessionManager(getApplicationContext());
            Toast.makeText(getApplicationContext(),"usuario" + sessionManager.obtenerInfoUsuario().get("nombre_usuario"),Toast.LENGTH_SHORT).show();
            siguienteActivity();
        }
        btnIngresoSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = etUsuario.getText().toString();
                password = etContrasenia.getText().toString();

                if(!usuario.equals("") && !password.equals("")){
                    iniciarSesion(usuario, password);
                }else{
                    Toast.makeText(getApplicationContext(), "Campos incompletos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnIngresoGaleria = findViewById(R.id.pruebaImagen);
        btnIngresoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPruebaGaleria();
            }
        });

    }

    /*
    * Inicializar las llamadas a Request
    * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
    * */
    void initRequestCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                try {
                    String msj = response.getString("msj");
                    if(msj.equalsIgnoreCase("Ingreso exitoso")){
                        String token = response.getString("token");

                        String empleados = response.getString("empleado");
                        guardarEmpleado(empleados);

                        String usuario = response.getString("usuarioId");
                        String id_empleado = response.getString("empleadoId");
                        guardarUsuario(usuario, id_empleado);

                        String enfermedad = response.getString("enfermedad");
                        guardarEnfermedad(enfermedad);

                        String atencionEnf = response.getString("atencionEnfermeria");
                        guardarAtencionEnfermeria(atencionEnf);

                        String consultaMed = response.getString("consultaMedica");
                        guardarConsultaMedica(consultaMed);

                        String patologiasPers = response.getString("patologiasPersonales");
                        guardarPatologiaPersonales(patologiasPers);

                        String patologiasFam = response.getString("patologiasFamiliares");
                        guardarPatologiaFamiliares(patologiasFam);

                        String signosVitales = response.getString("signosVitales");
                        guardarSignosVitales(signosVitales);

                        String diagnostico = response.getString("diagnostico");
                        //guardarDiagnostico(diagnostico);

                        String permisoMedico = response.getString("permisoMedico");
                        guardarPermisoMedico(permisoMedico);

                        //String examenes = response.getString("consulta_medica");
                        //guardarExamenes(examenes);

                        crearSesion(token);
                        siguienteActivity();

                    }else{
                        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyMsjError(String requestType, String error) {
                Toast.makeText(getApplicationContext(), error,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifyJSONError(String requestType, JSONException error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();
            }
        };

    }

    private void guardarExamenes(String response) {
        int idServ;
        String idConsulta;
        String rutaMovil, urlServidor;
        int status;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                ExamenImagen examenImagen = new ExamenImagen();
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                idServ = Integer.parseInt(objectJSON.getString("pk"));
                idConsulta = fields.getString("consulta_medica");
                urlServidor = fields.getString("imagen");
                List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?", idConsulta);
                if(consultaMedicaList.size()!=0){
                    examenImagen.setConsulta(consultaMedicaList.get(0));
                }

                //GUARDAR EN RUTA_MOVIL LA IMAGEN OBTENIDA DEL SERVIDOR
                examenImagen.setStatus(1);
                examenImagen.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Envía datos de usuario y contraseña al servidor
     * Autentica si está correcto
     * Retorna una respuesta
     * */
    public void iniciarSesion(String usuario, String contrasenia){
        initRequestCallback();
        requestService = new RequestService(mResultCallback, MainActivity.this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject("{" +
                    "'usuario': "+usuario+", " +
                    "'password': "+contrasenia+
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestService.postDataRequest("POSTCALL", URL_USUARIO, sendObj);
    }

    /*
     * Función que guarda usuario localmente
     * */
    public void guardarUsuario(String id_usuario, String id_empelado){
        int idServ;
        Usuario nuevoUsuario = new Usuario();
        Empleado empleado = Empleado.find(Empleado.class, "idserv = ?", id_empelado).get(0);
        idServ = Integer.parseInt(id_usuario);
        nuevoUsuario.setUsuario(usuario);
        nuevoUsuario.setStatus(1);
        nuevoUsuario.setEmpleado(empleado);
        nuevoUsuario.setId_serv(idServ);
        nuevoUsuario.save();
    }

    /*
     * Función que guarda empleado localmente
     * */
    private void guardarEmpleado(String  response) {
        int idServ, edad;
        String cedula, nombre, apellido, correo, direccion, profesion, estado_civil, sexo,
                lugar_nacimiento, ocupacion_actual, foto;
        Date fecha_nacimiento, fecha_registro;
        Empleado nuevoEmpleado;

        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                idServ = Integer.parseInt(objectJSON.getString("pk"));
                cedula = fields.getString("cedula");
                edad = Integer.parseInt(fields.getString("edad"));
                foto = fields.getString("foto");
                nombre = fields.getString("nombre");
                apellido = fields.getString("apellido");
                correo = fields.getString("correo");
                direccion = fields.getString("direccion");
                profesion = fields.getString("profesion");
                estado_civil = fields.getString("estadoCivil");
                sexo = fields.getString("sexo");
                lugar_nacimiento = fields.getString("lugarNacimiento");
                ocupacion_actual = fields.getString("ocupacion");
                String fecha_nacimiento_string = fields.getString("fechaNacimiento");
                String fecha_registro_string = fields.getString("fechaRegistro");
                fecha_nacimiento = convertirFecha(fecha_nacimiento_string);
                fecha_registro = convertirFecha(fecha_registro_string);
                nuevoEmpleado = new Empleado(cedula, nombre, apellido, correo, direccion, profesion,
                        estado_civil, sexo, lugar_nacimiento, ocupacion_actual, fecha_nacimiento,
                        fecha_registro, edad, foto);

                nuevoEmpleado.setId_serv(idServ);
                nuevoEmpleado.setStatus(1);
                nuevoEmpleado.setFicha_actual(Integer.parseInt(fields.getString("ficha_actual")));
                nuevoEmpleado.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
     * Función que guarda enfermedades localmente
     * */
    private void guardarEnfermedad(String response) {
        String codigo, nombre, grupo;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                codigo = fields.getString("codigo");
                nombre = fields.getString("nombre");
                grupo = fields.getString("grupo");
                Enfermedad enfermedad = new Enfermedad(codigo, nombre, grupo);
                enfermedad.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                enfermedad.setStatus(1);
                enfermedad.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Función que guarda Atencion Enfermeria localmente
     * */
    private void guardarAtencionEnfermeria(String response) {
        Date fechaAtencion;
        Empleado empleado = null;
        String motivoAtencion, diagnosticoEnfermeria, planCuidados;

        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");

                motivoAtencion = fields.getString("motivoAtencion");
                diagnosticoEnfermeria = fields.getString("diagnosticoEnfermeria");
                planCuidados = fields.getString("planCuidados");
                fechaAtencion = convertirFecha(fields.getString("fechaAtencion"));
                List<Empleado> empleadoList = Empleado.find(Empleado.class, "idserv = ?",fields.getString("empleado"));

                if(empleadoList.size()!=0){
                    empleado = empleadoList.get(0);
                }

                AtencionEnfermeria atencionEnfermeria = new AtencionEnfermeria(fechaAtencion, empleado, motivoAtencion,
                        diagnosticoEnfermeria, planCuidados);
                atencionEnfermeria.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                atencionEnfermeria.setStatus(1);
                atencionEnfermeria.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Función que guarda Consulta Medica localmente
     * */
    private void guardarConsultaMedica(String response) {
        Date fechaConsulta;
        String prob_actual, prescripcion, motivo, revision_medica, examen_fisico;
        Empleado empleado = null;

        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                prob_actual = fields.getString("prob_actual");
                prescripcion = fields.getString("prescripcion");
                motivo = fields.getString("motivo");
                revision_medica = fields.getString("revision_medica");
                examen_fisico = fields.getString("examen_fisico");
                fechaConsulta = convertirFecha(fields.getString("fechaConsulta"));
                List<Empleado> empleadoList = Empleado.find(Empleado.class, "idserv = ?",fields.getString("empleado"));

                if(empleadoList.size()!=0){
                    empleado = empleadoList.get(0);
                }

                ConsultaMedica consultaMedica = new ConsultaMedica(empleado, fechaConsulta, prob_actual,
                        revision_medica, prescripcion, examen_fisico,
                        motivo, 1);
                consultaMedica.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                consultaMedica.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Función que guarda Patologia Personales localmente
     * */
    private void guardarPatologiaPersonales(String response) {
        ConsultaMedica consulta_medica = null;
        String lugar, detalle;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                lugar = fields.getString("lugar");
                detalle = fields.getString("detalle");
                List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?", fields.getString("consulta_medica"));

                if (consultaMedicaList.size() != 0) {
                    consulta_medica = consultaMedicaList.get(0);
                }
                PatologiasPersonales patologiasPersonales = new PatologiasPersonales(consulta_medica, lugar, detalle);
                patologiasPersonales.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                patologiasPersonales.setId_ficha(Integer.parseInt(fields.getString("ficha")));
                patologiasPersonales.setStatus(1);
                patologiasPersonales.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    * Abre el activity GaleriaFragmentActivity
    * */
    private void abrirPruebaGaleria(){
            Intent prueba = new Intent(this, GaleriaFragmentActivity.class);
            startActivity(prueba);
        }


        /*
     * Función que guarda Signos Vitales localmente
     * */
    private void guardarSignosVitales(String response) {
        ConsultaMedica consulta_medica;
        AtencionEnfermeria atencionEnfermeria;
        int presion_sistolica, presion_distolica, pulso;
        float temperatura;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                SignosVitales signosVitales = null;
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                presion_sistolica = Integer.parseInt(fields.getString("presion_sistolica"));
                presion_distolica = Integer.parseInt(fields.getString("presion_distolica"));
                pulso = Integer.parseInt(fields.getString("pulso"));
                temperatura = Float.parseFloat(fields.getString("temperatura"));
                List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?",fields.getString("consulta_medica"));
                List<AtencionEnfermeria> atencionEnfList = AtencionEnfermeria.find(AtencionEnfermeria.class, "idserv = ?",fields.getString("atencion_enfermeria"));
                List<Empleado> empleadoList = Empleado.find(Empleado.class, "idserv = ?",fields.getString("empleado"));
                if(consultaMedicaList.size()!=0){
                    consulta_medica = consultaMedicaList.get(0);
                    signosVitales = new SignosVitales(presion_sistolica, presion_distolica, pulso, temperatura, consulta_medica, 1);
                }
                else if(atencionEnfList.size()!=0){
                    atencionEnfermeria = atencionEnfList.get(0);
                    signosVitales = new SignosVitales(presion_sistolica, presion_distolica, pulso, temperatura, atencionEnfermeria, 1);
                }else{
                    signosVitales = new SignosVitales(presion_sistolica, presion_distolica, pulso, temperatura, 1);
                }
                if(empleadoList.size()!=0){
                    signosVitales.setEmpleado(empleadoList.get(0));
                }
                signosVitales.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                signosVitales.setStatus(1);
                signosVitales.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Función que guarda Patologia Familiares localmente
     * */
    private void guardarPatologiaFamiliares(String response) {
        String parentesco, patologia, detalle;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                parentesco = fields.getString("parentesco");
                patologia = fields.getString("patologia");
                detalle = fields.getString("detalle");
                PatologiasFamiliares patologiasFamilires = new PatologiasFamiliares();
                patologiasFamilires.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                List<Enfermedad> enfermedadList = Enfermedad.find(Enfermedad.class, "idserv = ?",fields.getString("enfermedad"));
                if(enfermedadList.size()!=0){
                    Enfermedad enfermedad = enfermedadList.get(0);
                    patologiasFamilires.setEnfermedad(enfermedad);
                }
                patologiasFamilires.setParentesco(parentesco);
                patologiasFamilires.setPatologia(patologia);
                patologiasFamilires.setDetalle(detalle);
                patologiasFamilires.setStatus(1);
                patologiasFamilires.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Crea una sesión y va a la siguiente pantalla
     * */

    /*
     * Función que guarda Diagnóstico localmente
     * */
    private void guardarDiagnostico(String response) {
        String tipoEnfermedad;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                Diagnostico diagnostico = new Diagnostico();
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                tipoEnfermedad = fields.getString("tipoEnfermedad");
                List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?",fields.getString("consulta_medica"));
                List<Enfermedad> enfermedadList = Enfermedad.find(Enfermedad.class, "idserv = ?",fields.getString("enfermedad"));
                if(consultaMedicaList.size()!=0){
                    ConsultaMedica consultaMedica = consultaMedicaList.get(0);
                    diagnostico.setConsulta_medica(consultaMedica);
                }
                if(enfermedadList.size()!=0){
                    diagnostico.setEnfermedad(enfermedadList.get(0));
                }
                diagnostico.setTipoEnfermedad(tipoEnfermedad);
                diagnostico.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                diagnostico.setStatus(1);
                diagnostico.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Función que guarda Permiso Médico localmente
     * */
    private void guardarPermisoMedico(String response) {
        Date fecha_inicio, fecha_fin;
        int dias_permiso;
        String obsevaciones_permiso, fecha_inicio_string, fecha_fin_string, doctor;
        JSONArray obj;
        try {
            obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {
                PermisoMedico permisoMedico;
                JSONObject objectJSON = obj.getJSONObject(i);
                JSONObject fields = (JSONObject) objectJSON.get("fields");
                dias_permiso = Integer.parseInt(fields.getString("dias_permiso"));
                obsevaciones_permiso = fields.getString("observaciones_permiso");
                fecha_inicio_string = fields.getString("fecha_inicio");
                fecha_fin_string = fields.getString("fecha_fin");
                doctor = fields.getString("doctor");
                fecha_inicio = convertirFecha(fecha_inicio_string);
                fecha_fin = convertirFecha(fecha_fin_string);
                permisoMedico = new PermisoMedico(fecha_inicio, fecha_fin, dias_permiso, obsevaciones_permiso, doctor);
                List<ConsultaMedica> consultaMedicaList = ConsultaMedica.find(ConsultaMedica.class, "idserv = ?",fields.getString("consulta_medica"));
                List<Empleado> empleadoList = Empleado.find(Empleado.class, "idserv = ?",fields.getString("empleado"));
                List<Diagnostico> diagnosticoList = Diagnostico.find(Diagnostico.class, "idserv = ?",fields.getString("diagnostico"));
                if(diagnosticoList.size()!=0){
                    permisoMedico.setDiagnostico(diagnosticoList.get(0));
                }
                if(consultaMedicaList.size()!=0){
                    permisoMedico.setConsulta_medica(consultaMedicaList.get(0));
                    }
                if(empleadoList.size()!=0){
                    permisoMedico.setEmpleado(empleadoList.get(0));
                }
                permisoMedico.setId_serv(Integer.parseInt(objectJSON.getString("pk")));
                permisoMedico.setStatus(1);
                permisoMedico.save();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * Crea una sesión y guarda el usuario
    * */
    private void crearSesion(String token){
        Log.d("TOKEEN", token);
        SessionManager sesion = new SessionManager(getApplicationContext());
        Long id = Usuario.find(Usuario.class, "usuario = ? ",
                usuario).get(0).getId();
        sesion.crearSesion(id, token);
    }

    /*
     * Lleva a la ventana de BuscarEmpleadoActivity
     * */
    private void siguienteActivity(){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

    /*
    * Convierte la fecha(String) en Date
    * */
    private Date convertirFecha(String fecha){
        //Fechas
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mmm");
        Date fechaNueva = null;
        try {
            fechaNueva = formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaNueva;
    }
}
