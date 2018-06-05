package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.Adapter.AdapterItemsAtencionEnfermeria;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class HistorialAtencionEnfermeria extends FragmentActivity {
    private String idEmpleado;
    private TextView tvNombresEmpleado;
    private ListView lv;
    private List<AtencionEnfermeria> atencionEnfermeriaList;
    private AdapterItemsAtencionEnfermeria adapter;
    private Empleado empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_atencion_enfermeria);

        lv = findViewById(R.id.lvAtencionEnfermeria);
        tvNombresEmpleado = findViewById(R.id.tvNombresEmpleado);

        //Recibe el id del empleado desde MenuEmpleadoActivity
        Intent inMenuEmpleado = getIntent();
        idEmpleado = inMenuEmpleado.getStringExtra("ID");


        //Obtiene las atenciones de enfermeria de un empleado
        atencionEnfermeriaList = AtencionEnfermeria.find(AtencionEnfermeria.class, "empleado = ?", idEmpleado);

        //Crea un adapter de dicha lista y la muestra en un listview
        adapter = new AdapterItemsAtencionEnfermeria(this, (ArrayList<AtencionEnfermeria>) atencionEnfermeriaList);
        lv.setAdapter(adapter);

        //Busca al empleado con el id y muestra la informacion en el frgagment de informacion
        empleado = Empleado.findById(Empleado.class, Long.parseLong(idEmpleado));
        tvNombresEmpleado.setText(empleado.getApellido()+" "+empleado.getNombre());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView idAtencion =(TextView)view.findViewById(R.id.tvIdAtencion);
                //Toast.makeText(getApplicationContext(),idAtencion.getText(),Toast.LENGTH_SHORT).show();
                Intent atencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
                atencionEnfermeria.putExtra("ID_ATENCION",idAtencion.getText());
                atencionEnfermeria.putExtra("PRESEDENCIA","consultar");
                startActivity(atencionEnfermeria);

            }
        });


    }

    public void irNuevaAtencion(View v){
        Intent atencionEnfermeria = new Intent(getApplicationContext(),AtencionEnfemeriaActivity.class);
        atencionEnfermeria.putExtra("PRESEDENCIA","crear");
        atencionEnfermeria.putExtra("ID_EMPLEADO",idEmpleado);
        startActivity(atencionEnfermeria);
    }



}
