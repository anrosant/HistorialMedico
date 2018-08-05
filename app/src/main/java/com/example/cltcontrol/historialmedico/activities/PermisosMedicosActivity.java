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

import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.adapter.AdapterEnfermedades;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;
import com.example.cltcontrol.historialmedico.utils.ListaEnfermedades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.calcNumDias;
import static com.example.cltcontrol.historialmedico.utils.Identifiers.quitaDiacriticos;

public class PermisosMedicosActivity extends FragmentActivity {

    private EditText fechaDesde, fechaHasta, txtObservaciones, txtDoctor, txtBuscarEnfermedades;
    private RadioButton radioButton;
    private Switch swGenerarDiagnosticoParticular;
    private LinearLayout lyEnfermedad;
    private TextView numeroDias;

    private AdapterEnfermedades adaptadorEnfermedades;
    private static List<Enfermedad> listEnfermedades;
    private List<Enfermedad> newListEnfermedades;
    private Diagnostico diagnostico;
    private PermisoMedico permisoMedico;
    private Enfermedad enfermedad;
    private Empleado empleado;
    private String tipoEnfermedad, idEmpleado;
    private Date fechaIni, fechaFin;
    private int dia, mes, anio;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_medicos_externos);

        swGenerarDiagnosticoParticular = findViewById(R.id.sw_generar_diagnostico_particular);
        RadioGroup rg_tipo_enfermedad = findViewById(R.id.rg_tipo_enfermedad);
        txtObservaciones = findViewById(R.id.txt_observacion);
        lyEnfermedad = findViewById(R.id.ly_lista_enfermedades);
        fechaDesde = findViewById(R.id.txt_permiso_fecha_desde);
        fechaHasta = findViewById(R.id.txt_permiso_fecha_hasta);
        numeroDias = findViewById(R.id.tv_numero_dias);
        txtDoctor = findViewById(R.id.txt_doctor);
        TextView tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        Button btn_guardar_diagnostico_permiso = findViewById(R.id.btn_guardar_diagnostico_permiso);

        Button btn_guardar = findViewById(R.id.btn_guardar);

        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        final Bundle extras = Objects.requireNonNull(this).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        assert extras != null;
        idEmpleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        swGenerarDiagnosticoParticular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swGenerarDiagnosticoParticular.isChecked()) {
                    //txt_titulo_permiso_medico.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_check_circle_green_24dp,0);
                    lyEnfermedad.setVisibility(View.VISIBLE);
                    txtBuscarEnfermedades.setEnabled(true);
                }else{
                    lyEnfermedad.setVisibility(View.GONE);
                    txtBuscarEnfermedades.setHint("No refiere enfermedad");
                    txtBuscarEnfermedades.setEnabled(false);
                }
            }
        });

        listEnfermedades = ListaEnfermedades.readEnfermedadesAll();

        RecyclerView rv_lista_enfermedades = findViewById(R.id.rv_lista_enfermedades);
        rv_lista_enfermedades.setLayoutManager(new LinearLayoutManager(this));
        txtBuscarEnfermedades = findViewById(R.id.txt_buscar_enfermedades);

        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogInicio();
            }
        });
        fechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }
        });

        //Muestra la lista de enfermedades
        adaptadorEnfermedades = new AdapterEnfermedades(listEnfermedades);
        rv_lista_enfermedades.setAdapter(adaptadorEnfermedades);

        txtBuscarEnfermedades.addTextChangedListener(new TextWatcher() {

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
                    txtBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_cancel_grey_24dp,0);
                    newTest = quitaDiacriticos(txtBuscarEnfermedades.getText().toString().toLowerCase());
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
                    txtBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    adaptadorEnfermedades.setFilter(listEnfermedades);
                }
            }
        });

        //Al dar click en un item, este se guarda en la variable enfermedad
        rv_lista_enfermedades.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rv_lista_enfermedades, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(this, "Se ha escogido " + adaptadorEnfermedades.getListaEnfermedades().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                        enfermedad = adaptadorEnfermedades.getListaEnfermedades().get(position);
                        txtBuscarEnfermedades.setText(enfermedad.getNombre());
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        //Escoge el tipo de enfermedad
        rg_tipo_enfermedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                tipoEnfermedad = (String) radioButton.getText();
            }
        });

        btn_guardar_diagnostico_permiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDiagnostico();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPermisoMedico();
            }
        });

        txtBuscarEnfermedades.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        if (motionEvent.getX() >= (txtBuscarEnfermedades.getRight() - txtBuscarEnfermedades.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            txtBuscarEnfermedades.setText("");
                            txtBuscarEnfermedades.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }catch(NullPointerException e){
                        txtBuscarEnfermedades.requestFocus();
                        //Llamada al teclado
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.showSoftInput(txtBuscarEnfermedades, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                return true;
            }
        });
    }

    private void guardarDiagnostico() {
        if(enfermedad == null || tipoEnfermedad ==null){
            Toast.makeText(this,"No ha seleccionado todo los datos de enfemedad",Toast.LENGTH_SHORT).show();
        }else {
            empleado = ConsultaMedica.findById(Empleado.class, Long.valueOf(idEmpleado));

            //Se guarda la consulta medica en diagnostico
            diagnostico = new Diagnostico(null,enfermedad, tipoEnfermedad);
            diagnostico.save();

            Toast.makeText(this,"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarPermisoMedico(){
        String enfermedadPrincipalText;
        if(!swGenerarDiagnosticoParticular.isChecked()){
            enfermedadPrincipalText = txtBuscarEnfermedades.getText().toString();
        }else{
            enfermedadPrincipalText = "Sin diagnostico medico ";
        }

        String fechaInicioText = fechaDesde.getText().toString();
        String fechaFinText = fechaHasta.getText().toString();
        String diasPermisoText = numeroDias.getText().toString();
        String observacionesPermisoText = txtObservaciones.getText().toString();
        String doctorPermisoText = txtDoctor.getText().toString();

        if (!swGenerarDiagnosticoParticular.isChecked() || enfermedadPrincipalText.equals("") || fechaInicioText.equals("") ||
                fechaFinText.equals("") || diasPermisoText.equals("") || observacionesPermisoText.equals("") ||
                doctorPermisoText.equals("")) {
            Toast.makeText(this, "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha_inicio = null;
        Date fecha_fin = null;
        try {
            fecha_inicio = format.parse(fechaInicioText);
            fecha_fin = format.parse(fechaFinText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dias_permiso = Integer.parseInt(diasPermisoText);

        if (permisoMedico == null) {
            PermisoMedico permisoMed = new PermisoMedico(diagnostico, fecha_inicio, fecha_fin, dias_permiso, observacionesPermisoText, doctorPermisoText, empleado);
            permisoMed.save();
        } else {
            permisoMedico.setDiagnostico(diagnostico);
            permisoMedico.setFecha_inicio(fecha_inicio);
            permisoMedico.setFecha_fin(fecha_fin);
            permisoMedico.setDias_permiso(dias_permiso);
            permisoMedico.setObsevaciones_permiso(observacionesPermisoText);
            permisoMedico.setDoctor(doctorPermisoText);
            permisoMedico.setEmpleado(empleado);
            permisoMedico.save();
        }
        Toast.makeText(this, "Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "No existen diagnosticos para generar permiso medico", Toast.LENGTH_SHORT).show();
    }

    private void DateDialogInicio() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fechaDesde.setText(simpleDateFormat.format(date));
                calcularNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
        //dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        if(!fechaHasta.getText().toString().equals("")){
            Calendar c = Calendar.getInstance();
            String fecha[] = fechaHasta.getText().toString().split("/");
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fechaHasta.setText(simpleDateFormat.format(date));
                long num = calcNumDias(fechaDesde, fechaHasta);
                numeroDias.setText(Long.toString(num+1));
            }
        };
        try{
            DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
            Calendar c = Calendar.getInstance();
            String fecha[] = fechaDesde.getText().toString().split("/");
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String string_fecha_ini = fechaDesde.getText().toString();
        String string_fecha_fin = fechaHasta.getText().toString();

        if (!string_fecha_ini.equals("") && !string_fecha_fin.equals("")) {
            try {
                fechaIni = simpleDateFormat.parse(string_fecha_ini);
                fechaFin = simpleDateFormat.parse(string_fecha_fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dias_mili = Math.abs(fechaFin.getTime() - fechaIni.getTime());
            long numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            numeroDias.setText(Long.toString(numDias + 1));
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if(!fechaDesde.getText().toString().isEmpty() && !fechaHasta.getText().toString().isEmpty()) {
            long num = calcNumDias(fechaDesde, fechaHasta);
            numeroDias.setText(Long.toString(num + 1));
        }
    }
}
