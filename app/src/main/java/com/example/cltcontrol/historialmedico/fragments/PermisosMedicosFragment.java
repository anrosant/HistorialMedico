package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_CONSULTA_MEDICA;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PERMISO_MEDICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class PermisosMedicosFragment extends Fragment {

    private Switch switchGenerarPermiso;
    private Spinner spEnfermedadesDiagnostico;
    private EditText etFechaHasta, etObservaciones;
    private TextView etFechaDesde, etNumeroDias;
    private Button btnGuardar;

    private PermisoMedico permisoMedico;
    private List<Diagnostico> diagnosticosList;
    private Diagnostico diagnostico;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    private String idConsultaMedica;
    private String cargo;
    private String enfermedadPrincipalText;
    private String fechaInicioText;
    private String fechaFinText;
    private String diasPermisoText;
    private String observacionesPermisoText;
    private Date fechaInicio, fechaFin, fechaConsultaMedica;
    private int dia, mes, anio, idEmpleadoServidor;

    //POST
    private IResult mResultCallback = null;
    private RequestService requestService;

    public PermisosMedicosFragment() {}

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflado del fragment xml asociado
        View view = inflater.inflate(R.layout.fragment_permisos_medicos, container, false);

        //llamado de los views
        switchGenerarPermiso = view.findViewById(R.id.swGenerarPermiso);
        spEnfermedadesDiagnostico = view.findViewById(R.id.spEnfermedadesDiagnostico);
        spEnfermedadesDiagnostico.setEnabled(false);
        etFechaDesde = view.findViewById(R.id.etFechaDesde);
        etFechaHasta = view.findViewById(R.id.etFechaHasta);
        etNumeroDias = view.findViewById(R.id.txt_numero_dias);
        etObservaciones = view.findViewById(R.id.etObservaciones);
        btnGuardar = view.findViewById(R.id.btnGuardarPermiso);
        btnGuardar.setEnabled(false);

        etFechaDesde.setPadding(etFechaHasta.getPaddingStart(), etFechaHasta.getPaddingTop(), etFechaHasta.getPaddingRight(), etFechaHasta.getPaddingBottom());
        etFechaDesde.setGravity(Gravity.CENTER_VERTICAL);

        //Datos para  el calculo de dias entre 2 fechas
        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        //inicializar fecha de inicio como la fecha de hoy
        Date hoy = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date fecha_actual = simpleDateFormat.parse(hoy.toString());
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            etFechaDesde.setText(simpleDateFormat.format(fecha_actual));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //paso de parametros provenientes de activity nueva consulta medica
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        // extraccion del bundle {clave,valor} de los campos necesitados
        if (extras != null) {
            cargo = extras.getString("CARGO");
            idConsultaMedica = extras.getString("ID_CONSULTA_MEDICA");
            String id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            idEmpleadoServidor = empleado.getId_serv();
        }

        //Se verifica si el rol es el de la enfermera para deshabilitar el boton de agregar nueva consulta medica
        if(cargo.equals("Enfermera")){
            btnGuardar.setVisibility(View.GONE);
            switchGenerarPermiso.setVisibility(View.GONE);
        }

        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(idConsultaMedica));

        //Obtiene el permiso medico
        ArrayList<PermisoMedico> permisoMedicoList = (ArrayList<PermisoMedico>) PermisoMedico.find(PermisoMedico.class, "consultamedica = ?", String.valueOf(idConsultaMedica));
        if(permisoMedicoList.size()==0){
            permisoMedico = null;
        }else{
            permisoMedico = permisoMedicoList.get(0);
        }

        // se obtiene la lista de Diagnosticos del empleado
        diagnosticosList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", String.valueOf(idConsultaMedica));
        //se crea un nueva lista con los nombres de las enfermedades
        ArrayList<String> lista_enfermedades_diagnostico = new ArrayList<>();
        //se llena la lista de los nombres de las enfermedades
        for(Diagnostico nuevaListaDiagnostico: diagnosticosList){
            lista_enfermedades_diagnostico.add(nuevaListaDiagnostico.getEnfermedad().getNombre());
        }
        spEnfermedadesDiagnostico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                diagnostico = diagnosticosList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        @SuppressWarnings("unchecked") ArrayAdapter<Diagnostico> adapter = new ArrayAdapter(
                Objects.requireNonNull(getActivity()).getBaseContext(),android.R.layout.simple_spinner_dropdown_item, lista_enfermedades_diagnostico);
        spEnfermedadesDiagnostico.setAdapter(adapter);

        //Se habilitan/deshabilitan las vistas para el ingreso de los datos
        switchGenerarPermiso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchGenerarPermiso.isChecked()) {
                    spEnfermedadesDiagnostico.setEnabled(true);
                    etFechaHasta.setEnabled(true);
                    etObservaciones.setEnabled(true);
                    btnGuardar.setEnabled(true);
                } else {
                    spEnfermedadesDiagnostico.setEnabled(false);
                    etFechaHasta.setEnabled(false);
                    etObservaciones.setEnabled(false);
                    btnGuardar.setEnabled(false);
                }
            }
        });

        //Para el ingreso de fecha maxima de permiso medico
        etFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }
        });

        //Boton guardar permiso medico
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnosticosList.size()!=0) {
                    int res=-1;
                    enfermedadPrincipalText = spEnfermedadesDiagnostico.getSelectedItem().toString();
                    fechaInicioText = etFechaDesde.getText().toString();
                    fechaFinText = etFechaHasta.getText().toString();
                    diasPermisoText = etNumeroDias.getText().toString();
                    observacionesPermisoText = etObservaciones.getText().toString();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    fechaInicio = null;
                    fechaFin = null;
                    try {
                        fechaInicio = format.parse(fechaInicioText);
                        fechaFin = format.parse(fechaFinText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    permisoMedico = new PermisoMedico();
                    if(switchGenerarPermiso.isChecked()){
                        res = permisoMedico.validarPermisoMedico(enfermedadPrincipalText,fechaInicioText,fechaFinText,diasPermisoText,observacionesPermisoText);
                    }

                    if(res == 0) {
                        Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                        PermisoMedico.delete(permisoMedico);
                    } else if(res == 1) {
                        //Si ingresa desde consulta medica y es la primera vez que la crea
                        if (idConsultaMedica !=null && consultaMedica.getEmpleado() == null) {

                            fechaConsultaMedica = new Date();
                            Log.d("FECHA", String.valueOf(fechaConsultaMedica));

                            //Guarda el id del empleado en la atención y la fecha de atención
                            postConsultaMedica(fechaConsultaMedica);
                        }else{
                            Log.d("ELSE", "else");
                            //Toast.makeText(getContext(), "estoy aqui permi", Toast.LENGTH_SHORT).show();
                            postPermisoMedico(String.valueOf(consultaMedica.getId_serv()));
                        }
                    }
                }
                else
                    Toast.makeText(getContext(), "No existen diagnosticos para generar permiso medico", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    /*
     * Envía datos de Consulta médica al servidor
     * */
    private void postConsultaMedica(final Date fecha_consulta){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        Log.d("HERE", "2");
        String TAGCONSULTA = "tagconsulta";
        initRequestCallback(TAGCONSULTA);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(String.valueOf(idEmpleadoServidor), fecha_consulta,"","","","","");
        requestService.postDataRequest("POSTCALL", URL_CONSULTA_MEDICA, sendObj, token);
    }

    /*
     * Envía datos de Permiso Medico al servidor
     * */
    private void postPermisoMedico(String id_consulta_medica){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGPERMISO = "tagpermiso";
        initRequestCallback(TAGPERMISO);
        requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = PermisoMedico.getHashMapPermisoMedico(String.valueOf(idEmpleadoServidor),id_consulta_medica, fechaInicio, fechaFin,diasPermisoText,observacionesPermisoText,"dario");
        requestService.postDataRequest("POSTCALL", URL_PERMISO_MEDICO, sendObj, token);
    }

    /*
     * Función que guarda una consulta médica localmente
     * */
    private void guardarConsultaMedicaLocal(Date fechaConsulta, int id_servidor, int status){
        consultaMedica.setId_serv(id_servidor);
        consultaMedica.setEmpleado(empleado);
        consultaMedica.setFechaConsulta(fechaConsulta);
        consultaMedica.setStatus(status);
        consultaMedica.save();

        postPermisoMedico(String.valueOf(id_servidor));
    }

    /*
     * Función que guarda los Permiso mMdico localmente
     * @param idServ id del permiso en el servidor de tipo int. Si es 0 no se pudo enviar
     * @param status si es 0 no se pudo enviar, caso contrario será 1
     * */
    private void guardarPermisoMedicoLocal(int idServ, int status){
        permisoMedico.setId_serv(idServ);
        permisoMedico.setConsulta_medica(consultaMedica);
        permisoMedico.setFecha_inicio(fechaInicio);
        permisoMedico.setFecha_fin(fechaFin);
        permisoMedico.setDias_permiso(Integer.parseInt(diasPermisoText));
        permisoMedico.setObsevaciones_permiso(observacionesPermisoText);
        permisoMedico.setEmpleado(empleado);
        permisoMedico.setStatus(status);
        permisoMedico.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
            putDiagnostico(idServ);
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
            diagnostico.setPermiso_medico(permisoMedico);
            diagnostico.setStatus(0);
        }
    }

    /*
    * getHashMapDiagnostico(String idConsulta, String idPermiso,
                                                            String tipoEnfermedad, String idEnfermedad)
                                                            */

    public void putDiagnostico(int idPermiso){
        initRequestCallback("PUT");
        String idDiagnosticoServidor = String.valueOf(diagnostico.getId_serv());
        String idConsultaServidor= String.valueOf(diagnostico.getConsulta_medica().getId_serv());
        RequestService requestService = new RequestService(mResultCallback, getActivity());
        Map<String, String> sendObj = Diagnostico.getHashMapDiagnostico(idConsultaServidor,
                String.valueOf(idPermiso), diagnostico.getTipo_enfermedad(),
                String.valueOf(diagnostico.getEnfermedad().getId_serv()));
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        requestService.putDataRequest("PUTCALL", URL_DIAGNOSTICO+idDiagnosticoServidor+"/", sendObj, token);
    }

    /*
     * Inicializar las llamadas a Request
     * Dependiendo de las respuestas, ejecuta una de las siguientes funciones
     * */
    private void initRequestCallback(final String TAG){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    try {
                        //Si ha realizado post en ConsultaMedica
                        String fechaConsulta = response.getString("fecha");
                        Date fecha = convertirFecha(fechaConsulta);
                        String pk = response.getString("pk");

                        guardarConsultaMedicaLocal(fecha,Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(TAG.equalsIgnoreCase("tagpermiso")){
                    try {
                        //Si ha realizado post en Permiso Medico
                        String pk = response.getString("pk");
                        guardarPermisoMedicoLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    try {
                        //Si ha realizado put en diagnóstico
                        String idPermisoServidor = response.getString("permiso_medico");
                        List<PermisoMedico> permisoMedicoList = PermisoMedico.find(PermisoMedico.class, "idServ = ?", idPermisoServidor);
                        if(!permisoMedicoList.isEmpty()){
                            diagnostico.setPermiso_medico(permisoMedicoList.get(0));
                        }


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fechaConsultaMedica, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else if(TAG.equalsIgnoreCase("tagpermiso")){
                    guardarPermisoMedicoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }else{
                    diagnostico.setStatus(0);
                }
            }
            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fechaConsultaMedica, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else if(TAG.equalsIgnoreCase("tagpermiso")){
                    guardarPermisoMedicoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }else{
                    diagnostico.setStatus(0);
                }
            }
            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };
    }

    private void DateDialogFin() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = simpleDateFormat.parse("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                etFechaHasta.setText(simpleDateFormat.format(date));
                calcNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), listener, anio, mes, dia);
        Calendar c = Calendar.getInstance();
        String fecha[] = etFechaDesde.getText().toString().split("-");
        c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
        long milis = c.getTimeInMillis()-1000;
        dpDialog.getDatePicker().setMinDate(milis);
        dpDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void calcNumDias() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String string_fecha_ini = etFechaDesde.getText().toString();
        String string_fecha_fin = etFechaHasta.getText().toString();
        if (!string_fecha_ini.equals("") && !string_fecha_fin.equals("")) {
            try {
                fechaInicio = simpleDateFormat.parse(string_fecha_ini);
                fechaFin = simpleDateFormat.parse(string_fecha_fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dias_mili = Math.abs(fechaFin.getTime() - fechaInicio.getTime());
            long numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            etNumeroDias.setText(Long.toString(numDias + 1));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!etFechaDesde.getText().toString().isEmpty() && !etFechaHasta.getText().toString().isEmpty())
            calcNumDias();
    }
}