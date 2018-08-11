package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.adapter.AdapterEnfermedades;
import com.example.cltcontrol.historialmedico.interfaces.IResult;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.service.RequestService;
import com.example.cltcontrol.historialmedico.utils.ListaEnfermedades;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_NOT_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.NAME_SYNCED_WITH_SERVER;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_DIAGNOSTICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.URL_PERMISO_MEDICO;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.calcNumDias;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.quitaDiacriticos;

public class PermisosMedicosActivity extends FragmentActivity {

    private EditText etFechaDesde, etFechaHasta, etObservaciones, etDoctor, etBuscarEnfermedades;
    private RecyclerView rvListaEnfermedades;
    private RadioGroup rgTipoEnfermedad;
    private RadioButton radioButton;
    private Switch swGenerarDiagnosticoParticular;
    private LinearLayout lyEnfermedad;
    private TextView tvNombresEmpleado, tvNumeroDias;
    private Button btnGuardarPermisoParticular;

    private AdapterEnfermedades adaptadorEnfermedades;
    private static List<Enfermedad> listEnfermedades;
    private List<Enfermedad> newListEnfermedades;
    private PermisoMedico permisoMedico, permisoMedicoServ;
    private Diagnostico diagnostico;
    private Enfermedad enfermedad;
    private Empleado empleado;

    private String tipoEnfermedad, idEmpleado, enfermedadText, fechaInicioText, fechaFinText,
            diasPermisoText, observacionesPermisoText, doctorText;
    private Date fechaInicio, fechaFin;
    private int dia, mes, anio, idEmpleadoServidor, idPermisoServidor;

    private IResult mResultCallback = null;
    private RequestService requestService;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_medicos_externos);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        swGenerarDiagnosticoParticular = findViewById(R.id.swGenerarDiagnosticParticular);
        etBuscarEnfermedades = findViewById(R.id.etBuscarEnfermedades);
        etBuscarEnfermedades.setText("No refiere enfermedad");
        rgTipoEnfermedad = findViewById(R.id.rgTipoEnfermedad);
        etObservaciones = findViewById(R.id.etObservaciones);
        lyEnfermedad = findViewById(R.id.lyListaEnfermedades);
        etFechaDesde = findViewById(R.id.etPermisoFechaDesde);
        etFechaHasta = findViewById(R.id.etPermisoFechaHasta);
        tvNumeroDias = findViewById(R.id.tvNumeroDias);
        etDoctor = findViewById(R.id.etDoctor);
        //btnGuardaDiagnosticoParticular = findViewById(R.id.btnGuardarDiagnosticoParticular);
        btnGuardarPermisoParticular = findViewById(R.id.btnGuardarDiagnosticoParticular);

        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        final Bundle extras = Objects.requireNonNull(this).getIntent().getExtras();

        //Recibe el id de empleado
        assert extras != null;
        idEmpleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(idEmpleado));
        idEmpleadoServidor = empleado.getId_serv();
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        swGenerarDiagnosticoParticular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swGenerarDiagnosticoParticular.isChecked()) {
                    lyEnfermedad.setVisibility(View.VISIBLE);
                    etBuscarEnfermedades.setText("");
                    etBuscarEnfermedades.setHint("No refiere enfermedad");
                    etBuscarEnfermedades.setEnabled(true);
                }else{
                    lyEnfermedad.setVisibility(View.GONE);
                    etBuscarEnfermedades.setText("No refiere enfermedad");
                    etBuscarEnfermedades.setEnabled(false);
                }
            }
        });

        listEnfermedades = ListaEnfermedades.readEnfermedadesAll();

        rvListaEnfermedades = findViewById(R.id.rvListaEnfermedades);
        rvListaEnfermedades.setLayoutManager(new LinearLayoutManager(this));

        etFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogInicio();
            }
        });
        etFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }
        });

        //Muestra la lista de enfermedades
        adaptadorEnfermedades = new AdapterEnfermedades(listEnfermedades);
        rvListaEnfermedades.setAdapter(adaptadorEnfermedades);

        etBuscarEnfermedades.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int star,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int star,
                                      int count, int after) {
                String newTest;
                if(charSequence.length() != 0){
                    etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_cancel_grey_24dp,0);
                    newTest = quitaDiacriticos(etBuscarEnfermedades.getText().toString().toLowerCase());
                    newListEnfermedades = new ArrayList<>();
                    for (Enfermedad enfermedad:listEnfermedades){
                        String nombre = quitaDiacriticos(enfermedad.getNombre().toLowerCase());
                        String codigo = enfermedad.getCodigo().toLowerCase();
                        if(nombre.contains(newTest) || codigo.contains(newTest)){
                            newListEnfermedades.add(enfermedad);
                        }
                    }
                    adaptadorEnfermedades.setFilter(newListEnfermedades);
                }else{
                    etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    adaptadorEnfermedades.setFilter(listEnfermedades);
                }
            }
        });

        //Al dar click en un item, este se guarda en la variable enfermedad
        rvListaEnfermedades.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rvListaEnfermedades, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(this, "Se ha escogido " + adaptadorEnfermedades.getListaEnfermedades().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                        enfermedad = adaptadorEnfermedades.getListaEnfermedades().get(position);
                        etBuscarEnfermedades.setText(enfermedad.getNombre());
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        //Escoge el tipo de enfermedad
        rgTipoEnfermedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                tipoEnfermedad = (String) radioButton.getText();
            }
        });

        etBuscarEnfermedades.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        if (motionEvent.getX() >= (etBuscarEnfermedades.getRight() - etBuscarEnfermedades.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            etBuscarEnfermedades.setText("");
                            etBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }catch(NullPointerException e){
                        etBuscarEnfermedades.requestFocus();
                        //Llamada al teclado
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.showSoftInput(etBuscarEnfermedades, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                return true;
            }
        });

        btnGuardarPermisoParticular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int camposDiagnostico=0, camposPermiso=0;
                enfermedadText = etBuscarEnfermedades.getText().toString();
                fechaInicioText = etFechaDesde.getText().toString();
                fechaFinText = etFechaHasta.getText().toString();
                diasPermisoText = tvNumeroDias.getText().toString();
                observacionesPermisoText = etObservaciones.getText().toString();
                doctorText = etDoctor.getText().toString();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                fechaInicio = null;
                fechaFin = null;
                try {
                    fechaInicio = format.parse(fechaInicioText);
                    fechaFin = format.parse(fechaFinText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                permisoMedico = new PermisoMedico();
                if (swGenerarDiagnosticoParticular.isChecked()) {
                    if (enfermedad == null || tipoEnfermedad == null) {
                        //No se han llenado todos los campos del diagnostico
                        camposDiagnostico = 0;
                    } else {
                        //Se han llenado todos los campos del diagnostico
                        camposDiagnostico = 1;
                    }
                } else{
                    camposDiagnostico = 1;
                }
                /*
                 * Si camposPermiso es 0 no se han llenado todos los campos de permiso
                 * Si camposPermiso es 1 se han llenado todos los campos de permiso
                 */
                 camposPermiso = permisoMedico.validarPermisoMedicoParticular(enfermedadText, fechaInicioText, fechaFinText, diasPermisoText, observacionesPermisoText, doctorText);

                if (camposDiagnostico == 0 &&  camposPermiso == 0) {
                    Toast.makeText(getApplicationContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
                } else if (camposDiagnostico == 0 &&  camposPermiso == 1) {
                    Toast.makeText(getApplicationContext(), "No ha ingresado todos los datos de Diagnostico", Toast.LENGTH_SHORT).show();
                }else if (camposDiagnostico == 1 &&  camposPermiso == 0) {
                    Toast.makeText(getApplicationContext(), "No ha ingresado todos los datos de Permiso", Toast.LENGTH_SHORT).show();
                }else {
                    postPermisoMedico(String.valueOf(idEmpleadoServidor));
                    permisoMedico = PermisoMedico.findById(PermisoMedico.class, Long.valueOf(idEmpleado));
                    idPermisoServidor = permisoMedico.getId_serv();
                    postDiagnostico(String.valueOf(idPermisoServidor));
                }
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Envía datos de Diagnostico Medico al servidor
     * */
    private void postDiagnostico(String idPermiso) {
        String idServ = "";
        if(enfermedad.getId_serv()!=0){
            idServ = String.valueOf(enfermedad.getId_serv());
        }
        Log.d("IDSERV", idServ);
        mResultCallback = null;
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getApplicationContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGDIAGNOSTICO = "tagdiagnostico";
        initRequestCallback(TAGDIAGNOSTICO);
        requestService = new RequestService(mResultCallback, getApplicationContext());
        Map<String, String> sendObj = Diagnostico.getHashMapDiagnostico("", idPermiso, tipoEnfermedad, idServ);
        requestService.postDataRequest("POSTCALL", URL_DIAGNOSTICO, sendObj, token);
    }

    /*
     * Envía datos de Permiso Medico al servidor
     * */
    private void postPermisoMedico(String idEmpleado){
        SessionManager sesion = new SessionManager(Objects.requireNonNull(getApplicationContext()));
        String token = sesion.obtenerInfoUsuario().get("token");
        String TAGPERMISO = "tagpermiso";
        initRequestCallback(TAGPERMISO);
        requestService = new RequestService(mResultCallback, getApplicationContext());
        Map<String, String> sendObj = PermisoMedico.getHashMapPermisoMedico(idEmpleado, "", fechaInicio, fechaFin, diasPermisoText, observacionesPermisoText, doctorText);
        requestService.postDataRequest("POSTCALL", URL_PERMISO_MEDICO, sendObj, token);
    }

    /*
     * Función que guarda un diagnóstico localmente
     * */
    private void guardarDiagnosticoLocal(int id_serv, int status) {
        diagnostico.setEnfermedad(enfermedad);
        diagnostico.setTipo_enfermedad(tipoEnfermedad);
        diagnostico.setId_serv(id_serv);
        diagnostico.setStatus(status);
        diagnostico.setPermiso_medico(permisoMedico);
        diagnostico.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            Toast.makeText(getApplicationContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Hubo un error de conexión. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Función que guarda los Permiso Medico localmente
     * */
    private void guardarPermisoMedicoLocal(int id_serv, int status){
        permisoMedico.setId_serv(id_serv);
        permisoMedico.setEmpleado(empleado);
        permisoMedico.setConsulta_medica(null);
        permisoMedico.setFecha_inicio(fechaInicio);
        permisoMedico.setFecha_fin(fechaFin);
        permisoMedico.setDias_permiso(Integer.parseInt(diasPermisoText));
        permisoMedico.setObsevaciones_permiso(observacionesPermisoText);
        permisoMedico.setDoctor(doctorText);
        permisoMedico.setStatus(status);
        permisoMedico.save();
        if(status==NAME_SYNCED_WITH_SERVER) {
            postDiagnostico(String.valueOf(id_serv));
            Toast.makeText(getApplicationContext(), "Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"No hay conexión a internet. Los datos se guardarán localmente", Toast.LENGTH_LONG).show();
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
                if(TAG.equalsIgnoreCase("tagpermiso")){
                    try {
                        String pk = response.getString("pk");
                        guardarPermisoMedicoLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        //Si ha realizado post en Permiso Medico
                        String pk = response.getString("pk");
                        guardarDiagnosticoLocal(Integer.parseInt(pk),NAME_SYNCED_WITH_SERVER);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("HEREERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagpermiso")){
                    guardarPermisoMedicoLocal(0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarDiagnosticoLocal(0,NAME_NOT_SYNCED_WITH_SERVER);
                }
            }
            @Override
            public void notifyMsjError(String requestType, String error) {
                Log.d("HEREMSJERROR", String.valueOf(error));
                if(TAG.equalsIgnoreCase("tagpermiso")){
                    guardarPermisoMedicoLocal(0,NAME_NOT_SYNCED_WITH_SERVER);
                }else {
                    guardarDiagnosticoLocal(0,NAME_NOT_SYNCED_WITH_SERVER);
                }
            }
            @Override
            public void notifyJSONError(String requestType, JSONException error) {
            }
        };
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------

    private void DateDialogInicio() {
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
                etFechaDesde.setText(simpleDateFormat.format(date));
                calcularNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
        //dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        if(!etFechaHasta.getText().toString().equals("")){
            Calendar c = Calendar.getInstance();
            String fecha[] = etFechaHasta.getText().toString().split("-");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
            //Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();
            dpDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            dpDialog.show();
        }
        dpDialog.show();
    }

    private void DateDialogFin() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
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
                long num = calcNumDias(etFechaDesde, etFechaHasta);
                tvNumeroDias.setText(Long.toString(num+1));
            }
        };
        try{
            DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
            Calendar c = Calendar.getInstance();
            String fecha[] = etFechaDesde.getText().toString().split("-");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
            long milis = c.getTimeInMillis()-1000;
            dpDialog.getDatePicker().setMinDate(milis);
            dpDialog.show();
        }catch (Exception e){
            Toast.makeText(this,"No ha asignado fecha de inicio",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void calcularNumDias() {
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
            tvNumeroDias.setText(Long.toString(numDias + 1));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if(!etFechaDesde.getText().toString().isEmpty() && !etFechaHasta.getText().toString().isEmpty()) {
            long num = calcNumDias(etFechaDesde, etFechaHasta);
            tvNumeroDias.setText(Long.toString(num + 1));
        }
    }
}
