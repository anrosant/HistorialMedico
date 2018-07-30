package com.example.cltcontrol.historialmedico.activities;

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
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.example.cltcontrol.historialmedico.utils.BuscarTexto;
import com.example.cltcontrol.historialmedico.utils.CalculoDias;
import com.example.cltcontrol.historialmedico.utils.ListaEnfermedades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PermisosMedicosActivity extends FragmentActivity {

    private EditText fecha_desde, fecha_hasta, txt_observaciones, txt_doctor,txt_buscar_enfermedades;
    private RecyclerView rv_lista_enfermedades;
    private RadioGroup rg_tipo_enfermedad;
    private RadioButton radioButton;
    private Switch sw_generar_diagnostico_particular;
    private LinearLayout ly_enfermedad;
    private TextView numero_dias,tvNombresEmpleado,txt_titulo_permiso_medico;
    private Button btn_guardar,btn_guardar_diagnostico_permiso;

    private AdapterEnfermedades adaptadorEnfermedades;
    public static List<Enfermedad> listEnfermedades;
    private List<Enfermedad> newListEnfermedades;
    private Diagnostico diagnostico;
    private PermisoMedico permisoMedico;
    private Enfermedad enfermedad;
    private Usuario doctor;
    private Empleado empleado;
    private Calendar calendar;
    private String tipo_enfermedad,id_empleado;
    private Date fecha_ini, fecha_fin;
    private int dia, mes, anio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso_medico_particular);

        sw_generar_diagnostico_particular = findViewById(R.id.sw_generar_diagnostico_particular);
        rg_tipo_enfermedad = findViewById(R.id.rg_tipo_enfermedad);
        txt_observaciones = findViewById(R.id.txt_observacion);
        ly_enfermedad = findViewById(R.id.ly_lista_enfermedades);
        fecha_desde = findViewById(R.id.txt_permiso_fecha_desde);
        fecha_hasta = findViewById(R.id.txt_permiso_fecha_hasta);
        numero_dias = findViewById(R.id.tv_numero_dias);
        txt_doctor = findViewById(R.id.txt_doctor);
        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        txt_titulo_permiso_medico = findViewById(R.id.txt_titulo_permiso_medico);
        btn_guardar_diagnostico_permiso = findViewById(R.id.btn_guardar_diagnostico_permiso);

        btn_guardar = findViewById(R.id.btn_guardar);

        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        final Bundle extras = Objects.requireNonNull(this).getIntent().getExtras();

        //Recibe el id de consulta medica desde Historial de consulta medica
        id_empleado = extras.getString("ID_EMPLEADO");
        empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        sw_generar_diagnostico_particular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw_generar_diagnostico_particular.isChecked()) {
                    //txt_titulo_permiso_medico.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_check_circle_green_24dp,0);
                    ly_enfermedad.setVisibility(View.VISIBLE);
                    txt_buscar_enfermedades.setEnabled(true);
                }else{
                    ly_enfermedad.setVisibility(View.GONE);
                    txt_buscar_enfermedades.setText("No refiere enfermedad");
                    txt_buscar_enfermedades.setEnabled(false);
                }
            }
        });

        listEnfermedades = ListaEnfermedades.readEnfermedadesAll();

        rv_lista_enfermedades = findViewById(R.id.rv_lista_enfermedades);
        rv_lista_enfermedades.setLayoutManager(new LinearLayoutManager(this));
        txt_buscar_enfermedades = findViewById(R.id.txt_buscar_enfermedades);

        fecha_desde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogInicio();
            }
        });
        fecha_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }
        });

        //Muestra la lista de enfermedades
        adaptadorEnfermedades = new AdapterEnfermedades(listEnfermedades);
        rv_lista_enfermedades.setAdapter(adaptadorEnfermedades);

        txt_buscar_enfermedades.addTextChangedListener(new TextWatcher() {

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
                    txt_buscar_enfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_cancel_grey_24dp,0);
                    newTest = BuscarTexto.quitaDiacriticos(txt_buscar_enfermedades.getText().toString().toLowerCase());
                    newListEnfermedades = new ArrayList<>();
                    for (Enfermedad enfermedad:listEnfermedades){
                        String nombre = BuscarTexto.quitaDiacriticos(enfermedad.getNombre().toLowerCase());
                        String codigo = enfermedad.getCodigo().toString().toLowerCase();
                        if(nombre.contains(newTest) || codigo.contains(newTest)){
                            newListEnfermedades.add(enfermedad);
                        }
                    }
                    adaptadorEnfermedades.setFilter((List<Enfermedad>) newListEnfermedades);
                }else{
                    txt_buscar_enfermedades.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    adaptadorEnfermedades.setFilter((List<Enfermedad>) listEnfermedades);
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
                        txt_buscar_enfermedades.setText(enfermedad.getNombre());
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
                tipo_enfermedad = (String) radioButton.getText();
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

        txt_buscar_enfermedades.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        if (motionEvent.getX() >= (txt_buscar_enfermedades.getRight() - txt_buscar_enfermedades.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            txt_buscar_enfermedades.setText("");
                            txt_buscar_enfermedades.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }catch(NullPointerException e){
                        txt_buscar_enfermedades.requestFocus();
                        //Llamada al teclado
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_buscar_enfermedades, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                return true;
            }
        });
    }

    private void guardarDiagnostico() {
        if(enfermedad == null || tipo_enfermedad==null){
            Toast.makeText(this,"No ha seleccionado todo los datos de enfemedad",Toast.LENGTH_SHORT).show();
        }else {
            empleado = ConsultaMedica.findById(Empleado.class, Long.valueOf(id_empleado));

            //Se guarda la consulta medica en diagnostico
            diagnostico = new Diagnostico(null,enfermedad, tipo_enfermedad);
            diagnostico.save();

            Toast.makeText(this,"Se han guardado los datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarPermisoMedico(){
        String enfermedadPrincipalText;
        if(!sw_generar_diagnostico_particular.isChecked()){
            enfermedadPrincipalText = txt_buscar_enfermedades.getText().toString();
        }else{
            enfermedadPrincipalText = "Sin diagnostico medico ";
        }

        String fechaInicioText = fecha_desde.getText().toString();
        String fechaFinText = fecha_hasta.getText().toString();
        String diasPermisoText = numero_dias.getText().toString();
        String observacionesPermisoText = txt_observaciones.getText().toString();
        String doctorPermisoText = txt_doctor.getText().toString();

        if (!sw_generar_diagnostico_particular.isChecked() || enfermedadPrincipalText.equals("") || fechaInicioText.equals("") ||
                fechaFinText.equals("") || diasPermisoText.equals("") || observacionesPermisoText.equals("") ||
                doctorPermisoText.equals("")) {
            Toast.makeText(this, "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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

    public void DateDialogInicio() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fecha_desde.setText(simpleDateFormat.format(date));
                //calcNumDias()
                long num = CalculoDias.calcNumDias(fecha_desde,fecha_hasta);
                numero_dias.setText(Long.toString(num+1));
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
        //dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        if(!fecha_hasta.getText().toString().equals("")){
            Calendar c = Calendar.getInstance();
            String fecha[] = fecha_hasta.getText().toString().split("/");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
            //Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();
            dpDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            dpDialog.show();
        }
        dpDialog.show();
    }

    public void DateDialogFin() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fecha_hasta.setText(simpleDateFormat.format(date));
                long num = CalculoDias.calcNumDias(fecha_desde,fecha_hasta);
                numero_dias.setText(Long.toString(num+1));
            }
        };
        try{
            DatePickerDialog dpDialog = new DatePickerDialog(this, listener, anio, mes, dia);
            Calendar c = Calendar.getInstance();
            String fecha[] = fecha_desde.getText().toString().split("/");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
            long milis = c.getTimeInMillis()-1000;
            dpDialog.getDatePicker().setMinDate(milis);
            dpDialog.show();
        }catch (Exception e){
            Toast.makeText(this,"No ha asignado fecha de inicio",Toast.LENGTH_SHORT).show();
        }
    }

/*
    @Override
    public void onBackPressed() {
        if(cargo.equals("Doctor")) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            if (precedencia.equals("crear")) {
                //seleccionamos la cadena a mostrar
                alertbox.setMessage("No se guardara la consulta. Desea salir?");
                //elegimos un positivo SI
                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Elimina la consulta vacía
                        ConsultaMedica consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.parseLong(idConsultaMedica));
                        //Elimina en cascada
                        Diagnostico.deleteAll(Diagnostico.class, "consultamedica = ?", idConsultaMedica);
                        SignosVitales.deleteAll(SignosVitales.class, "consultamedica = ?", idConsultaMedica);
                        //Reset el autoincrement
                        ConsultaMedica.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NamingHelper.toSQLName(ConsultaMedica.class) + "'");
                        consultaMedica.delete();
                        ConsultaMedicaNuevoActivity.super.onBackPressed();
                    }
                });
                //Si editó una consulta
            } else {
                //seleccionamos la cadena a mostrar
                alertbox.setMessage("Los datos que no ha guardado se descartarán. ¿Desea salir?");
                //elegimos un positivo SI
                alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    //Funcion llamada cuando se pulsa el boton Si
                    public void onClick(DialogInterface arg0, int arg1) {
                        ConsultaMedicaNuevoActivity.super.onBackPressed();
                    }
                });
            }
            //elegimos un positivo NO
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                //Funcion llamada cuando se pulsa el boton No
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(getApplicationContext(),"Pulsaste NO",Toast.LENGTH_LONG).show();
                }
            });
            //mostramos el alertbox
            alertbox.show();
        }
        else {
            ConsultaMedicaNuevoActivity.super.onBackPressed();
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        if(!fecha_desde.getText().toString().isEmpty() && !fecha_hasta.getText().toString().isEmpty()) {
            long num = CalculoDias.calcNumDias(fecha_desde, fecha_hasta);
            numero_dias.setText(Long.toString(num + 1));
        }
    }
}
