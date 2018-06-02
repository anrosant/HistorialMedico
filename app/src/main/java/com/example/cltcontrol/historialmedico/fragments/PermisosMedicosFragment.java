package com.example.cltcontrol.historialmedico.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsConsultaMedica;
import com.example.cltcontrol.historialmedico.Adapter.EnfermedadesAdapter;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Enfermedad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermisosMedicosFragment extends Fragment {
    EditText fecha_desde, fecha_hasta;
    Switch switch_generar;
    TextView numero_dias;
    AutoCompleteTextView nombre_cie_10;
    AutoCompleteTextView codigo_cie_10;
    int dia, mes, anio;
    Calendar calendar;
    Date fecha_ini, fecha_fin;
    List<Enfermedad> enfermedades;
    ArrayList<String> enf_nombre = new ArrayList<>();
    ArrayList<String> enf_codigo = new ArrayList<>();

    public PermisosMedicosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permisos_medicos, container, false);
        fecha_desde = view.findViewById(R.id.txt_fecha_desde);
        fecha_hasta = view.findViewById(R.id.txt_fecha_hasta);
        switch_generar = view.findViewById(R.id.switchGenerarPermiso);
        numero_dias = view.findViewById(R.id.tvNumeroDias);
        nombre_cie_10 = view.findViewById(R.id.ac_nombre_cie10);
        codigo_cie_10 = view.findViewById(R.id.ac_codigo_cie10);
        enfermedades = Enfermedad.listAll(Enfermedad.class);

        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);

        switch_generar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_generar.isChecked()) {
                    fecha_desde.setEnabled(true);
                    fecha_hasta.setEnabled(true);
                    nombre_cie_10.setEnabled(true);
                    codigo_cie_10.setEnabled(true);
                } else {
                    fecha_desde.setEnabled(false);
                    fecha_hasta.setEnabled(false);
                    nombre_cie_10.setEnabled(false);
                    codigo_cie_10.setEnabled(false);
                }
            }
        });

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

        EnfermedadesAdapter adapter = new EnfermedadesAdapter(enfermedades);
        for(Enfermedad enf: enfermedades){
            enf_nombre.add(enf.getNombre());
        }
        for(Enfermedad enf: enfermedades){
            enf_codigo.add(enf.getCodigo());
        }
        inicializarAutocompletado(enf_nombre, enf_codigo);

        return view;
    }

    private void inicializarAutocompletado(final ArrayList<String> nombresCie10, final ArrayList<String> codigosCie10) {
        ArrayAdapter<String> adaptador;

        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresCie10);
        final AutoCompleteTextView tv_nombre_cie10 = nombre_cie_10;
        tv_nombre_cie10.setAdapter(adaptador);

        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, codigosCie10);
        final AutoCompleteTextView tv_codigo_cie10 = codigo_cie_10;
        tv_codigo_cie10.setAdapter(adaptador);

        tv_nombre_cie10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = (String) parent.getItemAtPosition(position);
                int pos = -1;
                for (int i = 0; i < nombresCie10.size(); i++) {
                    if (nombre.equalsIgnoreCase(nombresCie10.get(i))) {
                        pos = i;
                        break;
                    }
                }
                String codigo = codigosCie10.get(pos);
                tv_codigo_cie10.setText(codigo);
            }
        });

        tv_codigo_cie10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigo = (String) parent.getItemAtPosition(position);
                int pos = -1;
                for (int i = 0; i < codigosCie10.size(); i++) {
                    if (codigo.equalsIgnoreCase(codigosCie10.get(i))) {
                        pos = i;
                        break;
                    }
                }
                String nombre = nombresCie10.get(pos);
                tv_nombre_cie10.setText(nombre);
            }
        });

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

                calcNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(getContext(), listener, anio, mes, dia);
        dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        if(!fecha_hasta.getText().toString().equals("")){
            Calendar c = Calendar.getInstance();
            String fecha[] = fecha_hasta.getText().toString().split("/");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1
                    , Integer.parseInt(fecha[0]));
            //Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();
            dpDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
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

                calcNumDias();
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(getContext(), listener, anio, mes, dia);
        if(fecha_desde.getText().toString().equals("")){
            dpDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        } else {
            Calendar c = Calendar.getInstance();
            String fecha[] = fecha_desde.getText().toString().split("/");
            c.set(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
            Toast.makeText(getContext(), c.toString(), Toast.LENGTH_SHORT).show();
            dpDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        }
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
        calcNumDias();
        super.onResume();
    }

}