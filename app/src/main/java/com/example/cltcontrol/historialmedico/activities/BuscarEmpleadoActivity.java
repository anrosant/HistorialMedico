package com.example.cltcontrol.historialmedico.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.cltcontrol.historialmedico.adapter.AdapterItemEmpleado;
import com.example.cltcontrol.historialmedico.adapter.RecyclerItemClickListener;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.cltcontrol.historialmedico.utils.Identifiers.quitaDiacriticos;

public class BuscarEmpleadoActivity extends AppCompatActivity{
    private static List<Empleado> empleadoList;
    private AdapterItemEmpleado adaptadorEmpleados;
    private EditText etBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_empleados);

        etBuscar = findViewById(R.id.etBusquedaUsuario);
        ImageView ivFlechaLimpiar = findViewById(R.id.ivFlechaLimpiar);
        RecyclerView recyclerEmpleados = findViewById(R.id.rvlistaempleados);
        recyclerEmpleados.setLayoutManager(new LinearLayoutManager(this));


        //Obtener el nombre del usuario que inició sesión
        SessionManager sesion = new SessionManager(getApplicationContext());

        String nombreUsuario = sesion.obtenerInfoUsuario().get("nombre_usuario");

        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle(nombreUsuario);


        readEmpleadosAll();

        ivFlechaLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etBuscar.setText("");
            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            /*
             * Funcion que escucha caracter a caracter que se ingresa en el Edittext de busqueda de nombres empleados
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = quitaDiacriticos(etBuscar.getText().toString().toLowerCase());
                if(newText.length() != 0 && adaptadorEmpleados.validarBusqueda(newText)){
                    List<Empleado> newList = new ArrayList<>();
                    for(Empleado empleado: empleadoList){
                        String nombre = empleado.getNombre().toLowerCase();
                        String apellido = empleado.getApellido().toLowerCase();
                        if(nombre.contains(newText) || apellido.contains(newText)){
                            newList.add(empleado);
                        }
                    }
                    adaptadorEmpleados.setFilter(newList);
                } else {
                    adaptadorEmpleados.setFilter(empleadoList);
                }
            }
        });

        adaptadorEmpleados = new AdapterItemEmpleado(empleadoList);
        recyclerEmpleados.setAdapter(adaptadorEmpleados);

        recyclerEmpleados.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerEmpleados,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Long id_empleado = adaptadorEmpleados.getListaEmpleados().get(position).getId();
                        //Intent envia el id del empleado a MenuEmpleadoActivity
                        Intent i = new Intent(getApplicationContext(), MenuEmpleadoActivity.class);
                        i.putExtra("ID_EMPLEADO", String.valueOf(id_empleado));
                        startActivity(i);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    /*
     * Crea una instancia del menú
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_usuario, menu);

        return true;

    }

    /*
     * Menú de opciones
     * Si selecciona el primer item va a la actividad Acerca sino cierra sesión
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_acerca:

                startActivity(new Intent(this, AcercaActivity.class));

                return true;

            case R.id.item_cerrar_sesion:

                cerrarSesion();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }



    }

    /*
     * Guada todos los empleados que se encuentran en la base de datos en una lista
     * */
    private void readEmpleadosAll(){
        try{
            empleadoList = Empleado.listAll(Empleado.class);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /*
    * Cierra la sesión
    * */
    private void cerrarSesion(){
        SessionManager sesion = new SessionManager(getApplicationContext());
        sesion.cerrarSesion(getApplicationContext());
        finish();
    }

    /*
     * Si presiona atrás puede salir de la sesión
     * */
    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(this);
        //seleccionamos la cadena a mostrar
        alertbox.setMessage("¿Desea cerrar sesión?");
        //elegimos un positivo SI
        alertbox.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            //Funcion llamada cuando se pulsa el boton Si
            public void onClick(DialogInterface arg0, int arg1) {
                cerrarSesion();
            }
        });
        //elegimos un positivo NO
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //mostramos el alertbox
        alertbox.show();
    }

}