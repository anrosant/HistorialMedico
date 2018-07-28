package com.example.cltcontrol.historialmedico.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PermisoMedico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PermisosMedicosFragment extends Fragment {
    private String id_empleado, id_consulta_medica, cargo, precedencia;
    private Empleado empleado;
    private ConsultaMedica consultaMedica;
    private Diagnostico diagnostico;
    private List<Diagnostico> diagnosticosList;
    private ArrayList<String> lista_enfermedades_diagnostico;
    private ArrayList<PermisoMedico> permisoMedicoList;
    private EditText fecha_hasta, observaciones;
    private Button btn_guardar;
    private Switch switch_generar;
    private TextView fecha_desde, numero_dias;
    private Spinner sp_enfermedades_diagnostico;
    private int dia, mes, anio;
    private Calendar calendar;
    private Date fecha_ini, fecha_fin;
    private PermisoMedico permisoMedico;
    private SimpleDateFormat simpleDateFormat;

    public PermisosMedicosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflado del fragment xml asociado
        View view = inflater.inflate(R.layout.fragment_permisos_medicos, container, false);

        //llamado de los views
        fecha_desde = view.findViewById(R.id.txt_fecha_desde);
        fecha_hasta = view.findViewById(R.id.txt_fecha_hasta);
        observaciones = view.findViewById(R.id.txt_observacion);
        switch_generar = view.findViewById(R.id.sw_generar_permiso);
        numero_dias = view.findViewById(R.id.tv_numero_dias);
        sp_enfermedades_diagnostico = view.findViewById(R.id.sp_enfermedades_diagnostico);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        sp_enfermedades_diagnostico.setEnabled(false);
        btn_guardar.setEnabled(false);

        //inicializar fecha de inicio
        fecha_desde.setPadding(fecha_hasta.getPaddingStart(), fecha_hasta.getPaddingTop(),
                fecha_hasta.getPaddingRight(), fecha_hasta.getPaddingBottom());
        fecha_desde.setGravity(Gravity.CENTER_VERTICAL);
        Date hoy = new Date();
        try {
            simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date fecha_actual = simpleDateFormat.parse(hoy.toString());
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            fecha_desde.setText(simpleDateFormat.format(fecha_actual));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //paso de parametros provenientes de activity nueva consulta medica
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        // extraccion del bundle {clave,valor} de los campos necesitados
        if (extras != null) {
            id_consulta_medica = extras.getString("ID_CONSULTA_MEDICA");
            id_empleado = extras.getString("ID_EMPLEADO");
            empleado = Empleado.findById(Empleado.class, Long.valueOf(id_empleado));
            cargo = extras.getString("CARGO");
            precedencia = extras.getString("PRECEDENCIA");
        }


        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(id_consulta_medica));

        //Obtiene el permiso medico
        permisoMedicoList = (ArrayList<PermisoMedico>) PermisoMedico.find(PermisoMedico.class, "consultamedica = ?",String.valueOf(id_consulta_medica));
        if(permisoMedicoList.size()==0){
            permisoMedico = null;
        }else{
            permisoMedico = permisoMedicoList.get(0);
        }

        if(cargo.equals("Enfermera")){
            btn_guardar.setVisibility(View.GONE);
            switch_generar.setVisibility(View.GONE);
        }

        // se obtiene la lista de Diagnosticos del empleado
        diagnosticosList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", String.valueOf(id_consulta_medica));

        //se crea un nueva lista con los nombres de las enfermedades
        lista_enfermedades_diagnostico = new ArrayList<>();

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

        ArrayAdapter<Diagnostico> adapter = new ArrayAdapter(
                Objects.requireNonNull(getActivity()).getBaseContext(),android.R.layout.simple_spinner_dropdown_item, lista_enfermedades_diagnostico);
        sp_enfermedades_diagnostico.setAdapter(adapter);

        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        switch_generar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_generar.isChecked()) {
                    sp_enfermedades_diagnostico.setEnabled(true);
                    fecha_hasta.setEnabled(true);
                    observaciones.setEnabled(true);
                    btn_guardar.setEnabled(true);
                } else {
                    sp_enfermedades_diagnostico.setEnabled(false);
                    fecha_hasta.setEnabled(false);
                    observaciones.setEnabled(false);
                    btn_guardar.setEnabled(false);
                }
            }
        });

        fecha_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialogFin();
            }

        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPermisoMedico();
            }
        });

        if(precedencia.equals("consultar")) {
            if(permisoMedico!=null){
                simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                try {
                    Date a = simpleDateFormat.parse(permisoMedico.getFecha_inicio().toString());
                    Date b = simpleDateFormat.parse(permisoMedico.getFecha_fin().toString());
                    simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    fecha_desde.setText(simpleDateFormat.format(a));
                    fecha_hasta.setText(simpleDateFormat.format(b));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                numero_dias.setText(String.valueOf(permisoMedico.getDias_permiso()));
                observaciones.setText(permisoMedico.getObsevaciones_permiso());
            }
            btn_guardar.setText("Editar");
        }
        return view;
    }
    /*
     * Guarda los datos de un permiso médico
     * */
    public void guardarPermisoMedico(){
        if(diagnosticosList.size()!=0) {
            String enfermedadPrincipalText = sp_enfermedades_diagnostico.getSelectedItem().toString();
            String fechaInicioText = fecha_desde.getText().toString();
            String fechaFinText = fecha_hasta.getText().toString();
            String diasPermisoText = numero_dias.getText().toString();
            String observacionesPermisoText = observaciones.getText().toString();

            if (!switch_generar.isChecked() || enfermedadPrincipalText.equals("") || fechaInicioText.equals("") ||
                    fechaFinText.equals("") || diasPermisoText.equals("") || observacionesPermisoText.equals("")) {
                Toast.makeText(getContext(), "No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
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

            if (consultaMedica.getEmpleado() == null) {
                //Guarda el id del empleado en la consulta y la fecha de consulta
                consultaMedica.setEmpleado(empleado);
                consultaMedica.setFechaConsulta(new Date());
            }
            if (permisoMedico == null) {
                PermisoMedico permisoMed = new PermisoMedico(diagnostico, fecha_inicio, fecha_fin, dias_permiso, observacionesPermisoText, consultaMedica);
                permisoMed.save();
            } else {
                permisoMedico.setDiagnostico(diagnostico);
                permisoMedico.setFecha_inicio(fecha_inicio);
                permisoMedico.setFecha_fin(fecha_fin);
                permisoMedico.setDias_permiso(dias_permiso);
                permisoMedico.setObsevaciones_permiso(observacionesPermisoText);
                permisoMedico.setConsulta_medica(consultaMedica);
                permisoMedico.save();
            }
            Toast.makeText(getContext(), "Se ha guardado con éxito", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "No existen diagnosticos para generar permiso medico", Toast.LENGTH_SHORT).show();
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
                calcNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(getContext(), listener, anio, mes, dia);
        Calendar c = Calendar.getInstance();
        String fecha[] = fecha_desde.getText().toString().split("/");
        c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
        long milis = c.getTimeInMillis()-1000;
        dpDialog.getDatePicker().setMinDate(milis);
        dpDialog.show();
    }

    private void calcNumDias() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String string_fecha_ini = fecha_desde.getText().toString();
        String string_fecha_fin = fecha_hasta.getText().toString();

        if (!string_fecha_ini.equals("") && !string_fecha_fin.equals("")) {
            try {
                fecha_ini = simpleDateFormat.parse(string_fecha_ini);
                fecha_fin = simpleDateFormat.parse(string_fecha_fin);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dias_mili = Math.abs(fecha_fin.getTime() - fecha_ini.getTime());
            long numDias = TimeUnit.DAYS.convert(dias_mili, TimeUnit.MILLISECONDS);
            numero_dias.setText(Long.toString(numDias + 1));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!fecha_desde.getText().toString().isEmpty() && !fecha_hasta.getText().toString().isEmpty())
            calcNumDias();
    }
}