package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PERMISO_MEDICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.convertirFecha;

public class PermisosMedicosFragment extends Fragment {

    private Switch switch_generar_permiso;
    private Spinner sp_enfermedades_diagnostico;
    private EditText txt_fecha_hasta, txt_observaciones;
    private TextView txt_fecha_desde, txt_numero_dias;
    private Button btn_guardar;

    private PermisoMedico permisoMedico;
    private List<Diagnostico> diagnosticosList;
    private Diagnostico diagnostico;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;

    private String id_consulta_medica;
    private String cargo;
    private String enfermedadPrincipalText;
    private String fechaInicioText;
    private String fechaFinText;
    private String diasPermisoText;
    private String observacionesPermisoText;
    private Date fecha_inicio, fecha_fin, fecha_consulta_medica;
    private int dia, mes, anio;

    //POST
    private IResult mResultCallback = null;
    private RequestService requestService;
    private int id_empleado_Servidor;

    private BroadcastReceiver broadcastReceiver;

    public PermisosMedicosFragment() {}

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflado del fragment xml asociado
        View view = inflater.inflate(R.layout.fragment_permisos_medicos, container, false);

        //llamado de los views
        switch_generar_permiso = view.findViewById(R.id.sw_generar_permiso);
        sp_enfermedades_diagnostico = view.findViewById(R.id.sp_enfermedades_diagnostico);
            sp_enfermedades_diagnostico.setEnabled(false);
        txt_fecha_desde = view.findViewById(R.id.txt_fecha_desde);
        txt_fecha_hasta = view.findViewById(R.id.txt_fecha_hasta);
        txt_numero_dias = view.findViewById(R.id.txt_numero_dias);
        txt_observaciones = view.findViewById(R.id.txt_observacion);
        btn_guardar = view.findViewById(R.id.btn_guardar);
            btn_guardar.setEnabled(false);

        txt_fecha_desde.setPadding(txt_fecha_hasta.getPaddingStart(), txt_fecha_hasta.getPaddingTop(), txt_fecha_hasta.getPaddingRight(), txt_fecha_hasta.getPaddingBottom());
        txt_fecha_desde.setGravity(Gravity.CENTER_VERTICAL);

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
            txt_fecha_desde.setText(simpleDateFormat.format(fecha_actual));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //paso de parametros provenientes de activity nueva consulta medica
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        // extraccion del bundle {clave,valor} de los campos necesitados
        if (extras != null) {
            cargo = extras.getString("CARGO");
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            String id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            id_empleado_Servidor = empleado.getId_serv();
        }

        //Se verifica si el rol es el de la enfermera para deshabilitar el boton de agregar nueva consulta medica
        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            switch_generar_permiso.setVisibility(View.GONE);
        }

        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        //Obtiene el permiso medico
        ArrayList<PermisoMedico> permisoMedicoList = (ArrayList<PermisoMedico>) PermisoMedico.find(PermisoMedico.class, "consultamedica = ?", String.valueOf(id_consulta_medica));
        if(permisoMedicoList.size()==0){
            permisoMedico = null;
        }else{
            permisoMedico = permisoMedicoList.get(0);
        }

        // se obtiene la lista de Diagnosticos del empleado
        diagnosticosList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", String.valueOf(id_consulta_medica));
        //se crea un nueva lista con los nombres de las enfermedades
        ArrayList<String> lista_enfermedades_diagnostico = new ArrayList<>();
        //se llena la lista de los nombres de las enfermedades
        for(Diagnostico nuevaListaDiagnostico: diagnosticosList){
            lista_enfermedades_diagnostico.add(nuevaListaDiagnostico.getEnfermedad().getNombre());
        }
        sp_enfermedades_diagnostico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        sp_enfermedades_diagnostico.setAdapter(adapter);

        //Se habilitan/deshabilitan las vistas para el ingreso de los datos
        switch_generar_permiso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_generar_permiso.isChecked()) {
                    sp_enfermedades_diagnostico.setEnabled(true);
                    txt_fecha_hasta.setEnabled(true);
                    txt_observaciones.setEnabled(true);
                    btn_guardar.setEnabled(true);
                } else {
                    sp_enfermedades_diagnostico.setEnabled(false);
                    txt_fecha_hasta.setEnabled(false);
                    txt_observaciones.setEnabled(false);
                    btn_guardar.setEnabled(false);
                }
            }
        });

        //Para el ingreso de fecha maxima de permiso medico
        txt_fecha_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }
        });

        //Boton guardar permiso medico
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnosticosList.size()!=0) {
                    int res=-1;
                    enfermedadPrincipalText = sp_enfermedades_diagnostico.getSelectedItem().toString();
                    fechaInicioText = txt_fecha_desde.getText().toString();
                    fechaFinText = txt_fecha_hasta.getText().toString();
                    diasPermisoText = txt_numero_dias.getText().toString();
                    observacionesPermisoText = txt_observaciones.getText().toString();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    fecha_inicio = null;
                    fecha_fin = null;
                    try {
                        fecha_inicio = format.parse(fechaInicioText);
                        fecha_fin = format.parse(fechaFinText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    permisoMedico = new PermisoMedico();
                    if(switch_generar_permiso.isChecked()){
                        res = permisoMedico.validarPermisoMedico(enfermedadPrincipalText,fechaInicioText,fechaFinText,diasPermisoText,observacionesPermisoText);
                    }

                    if(res == 0) {
                        Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                        PermisoMedico.delete(permisoMedico);
                    } else if(res == 1) {
                        //Si ingresa desde consulta medica y es la primera vez que la crea
                        if (id_consulta_medica!=null && consultaMedica.getEmpleado() == null) {

                            fecha_consulta_medica = new Date();
                            Log.d("FECHA", String.valueOf(fecha_consulta_medica));

                            //Guarda el id del empleado en la atención y la fecha de atención
                            postConsultaMedica(fecha_consulta_medica);
                        }else{
                            Log.d("ELSE", "else");
                            //Toast.makeText(getContext(), "estoy aqui permi", Toast.LENGTH_SHORT).show();
                            postPermisoMedico(String.valueOf(consultaMedica.getId_serv()));
                        }
                    }
                    //cargarSignosVitales(atencionEnfermeria.getId());
                    //Broadcast receiver to know the sync status
                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            //Confirmar que se ha guardado
                            Toast.makeText(getContext(),"Datos enviados al servidor ",Toast.LENGTH_SHORT).show();
                            //cargarSignosVitales(atencionEnfermeria.getId());
                        }
                    };
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
        Map<String, String> sendObj = ConsultaMedica.getHashMapConsultaMedica(String.valueOf(id_empleado_Servidor), fecha_consulta,"","","","","");
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
        Map<String, String> sendObj = PermisoMedico.getHashMapPermisoMedico(String.valueOf(id_empleado_Servidor),String.valueOf(diagnostico.getId_serv()),id_consulta_medica,fecha_inicio,fecha_fin,diasPermisoText,observacionesPermisoText,"dario");
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
     * */
    private void guardarPermisoMedicoLocal(int id_serv, int status){
        permisoMedico.setId_serv(id_serv);
        permisoMedico.setDiagnostico(diagnostico);
        permisoMedico.setConsulta_medica(consultaMedica);
        permisoMedico.setFecha_inicio(fecha_inicio);
        permisoMedico.setFecha_fin(fecha_fin);
        permisoMedico.setDias_permiso(Integer.parseInt(diasPermisoText));
        permisoMedico.setObsevaciones_permiso(observacionesPermisoText);
        permisoMedico.setEmpleado(empleado);
        permisoMedico.setStatus(status);
        permisoMedico.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
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
                }else{
                    try {
                        //Si ha realizado post en Permiso Medico
                        String pk = response.getString("pk");
                        guardarPermisoMedicoLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fecha_consulta_medica, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarPermisoMedicoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
                }
            }
            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagconsulta")){
                    guardarConsultaMedicaLocal(fecha_consulta_medica, 0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarPermisoMedicoLocal(0, NAME_NOT_SYNCED_WITH_SERVER);
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    date = simpleDateFormat.parse("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txt_fecha_hasta.setText(simpleDateFormat.format(date));
                calcNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), listener, anio, mes, dia);
        Calendar c = Calendar.getInstance();
        String fecha[] = txt_fecha_desde.getText().toString().split("-");
        c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
        long milis = c.getTimeInMillis()-1000;
        dpDialog.getDatePicker().setMinDate(milis);
        dpDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void calcNumDias() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String string_fecha_ini = txt_fecha_desde.getText().toString();
        String string_fecha_fin = txt_fecha_hasta.getText().toString();
        if (!string_fecha_ini.equals("") && !string_fecha_fin.equals("")) {
            try {
                fecha_inicio = simpleDateFormat.parse(string_fecha_ini);
                fecha_fin = simpleDateFormat.parse(string_fecha_fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dias_mili = Math.abs(fecha_fin.getTime() - fecha_inicio.getTime());
            long numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            txt_numero_dias.setText(Long.toString(numDias + 1));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!txt_fecha_desde.getText().toString().isEmpty() && !txt_fecha_hasta.getText().toString().isEmpty())
            calcNumDias();
    }
}