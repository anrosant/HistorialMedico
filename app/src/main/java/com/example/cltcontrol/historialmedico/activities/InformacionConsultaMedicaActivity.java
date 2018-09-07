package com.example.cltcontrol.historialmedico.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.adapter.AdapterItemDiagnostico;
import com.example.cltcontrol.historialmedico.adapter.AdapterPatologiasPersonales;
import com.example.cltcontrol.historialmedico.adapter.AdapterSignosVitales;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Diagnostico;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.PatologiasPersonales;
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import java.util.List;

public class InformacionConsultaMedicaActivity extends AppCompatActivity {

    private TextView tvNombresEmpleado, tvDatosMotivoConsulta, tvDatosProblemaActual,
            tvDatosRevisionMedica, tvDatosExamenFisico, tvDatosPrescripcion;
    private ListView lvCargaSignosVitales,lvCargaPatologiasPersonales, lvCargaDiagnosticos;
    private AdapterSignosVitales adapterSignosVitales;
    private AdapterPatologiasPersonales adapterPatologiaPers;
    private AdapterItemDiagnostico adapterItemDiagnostico;

    private List<SignosVitales> signosVitalesList;
    private ConsultaMedica consultaMedica;
    private Empleado empleado;
    private String idEmpleado,idConsultaMedica;
    private int idEmpleadoServidor;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_consulta_medica);

        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);
        lvCargaSignosVitales = findViewById(R.id.lvCargaSignosVitales);
        tvDatosMotivoConsulta = findViewById(R.id.tvDatosMotivoConsulta);
        lvCargaPatologiasPersonales = findViewById(R.id.lvCargaPatologiasPersonales);
        tvDatosProblemaActual = findViewById(R.id.tvDatosProblemaActual);
        tvDatosRevisionMedica = findViewById(R.id.tvDatosRevisionMedica);
        tvDatosExamenFisico = findViewById(R.id.tvDatosExamenFisico);
        lvCargaDiagnosticos = findViewById(R.id.lvCargaDiagnosticos);
        tvDatosPrescripcion = findViewById(R.id.tvDatosPrescripcion);

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        Intent inConsultaMedica = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID_EMPLEADO");
        idConsultaMedica = inConsultaMedica.getStringExtra("ID_CONSULTA_MEDICA");

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        Empleado empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+ empleado.getNombre());
        // Busca la consulta medica por el id
        consultaMedica = ConsultaMedica.findById(ConsultaMedica.class, Long.valueOf(idConsultaMedica));

        /*
         * Carga los signos vitales de la consulta medica
         */
        //Obtiene los signos vitales de un empleado
        signosVitalesList = SignosVitales.find(SignosVitales.class, "consultamedica = ?", String.valueOf(idConsultaMedica));
        //Crea un adapterItemAtencionEnfermeria de dicha lista y la muestra en un listview
        adapterSignosVitales = new AdapterSignosVitales(this, signosVitalesList);
        lvCargaSignosVitales.setAdapter(adapterSignosVitales);

        /*
         * Carga el motivo de la consulta medica
         */
        tvDatosMotivoConsulta.setText(consultaMedica.getMotivo());

        /*
         * Carga las Patologias Personales de la consulta medica
         */
        //Obtenemos la lista de patologias personales que existan
        List<PatologiasPersonales> patologiasPersonalesList = PatologiasPersonales.find(PatologiasPersonales.class, "consultamedica = ?", String.valueOf(idConsultaMedica));

        //Crea un adapterItemAtencionEnfermeria de dicha lista y la muestra en un listview
        adapterPatologiaPers = new AdapterPatologiasPersonales(this, patologiasPersonalesList);
        lvCargaPatologiasPersonales.setAdapter(adapterPatologiaPers);

        /*
         * Carga el Problema Actual de la consulta medica
         */
        tvDatosProblemaActual.setText(consultaMedica.getProb_actual());

        /*
         * Carga la Revision Medica de la consulta medica
         */
        tvDatosRevisionMedica.setText(consultaMedica.getRevision_medica());

        /*
         * Carga el Examen Fisico de la consulta medica
         */
        tvDatosExamenFisico.setText(consultaMedica.getExamen_fisico());

        /*
         * Carga las imagenes de los Examenes
         */
        // -------------------------- falta -----------------


        /*
         * Carga los Diagnosticos de la consulta medica
         */
        //Muestra la lista de diagnosticos
        List<Diagnostico> diagnosticoList = Diagnostico.find(Diagnostico.class, "consultamedica = ?", idConsultaMedica);
        //Crea un adapterItemAtencionEnfermeria de dicha lista y la muestra en un listview
        adapterItemDiagnostico = new AdapterItemDiagnostico(this, diagnosticoList);
        lvCargaDiagnosticos.setAdapter(adapterItemDiagnostico);

        /*
         * Carga la Preescripcion de la consulta medica
         */
        tvDatosPrescripcion.setText(consultaMedica.getPrescripcion());


    }

}
