package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.utils.EmpleadoController;
import com.example.cltcontrol.historialmedico.utils.SessionManager;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.ConsultaMedica;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.utils.EnfermedadesSQL;
import com.example.cltcontrol.historialmedico.models.Usuario;
import com.facebook.stetho.Stetho;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasenia;
    private Button btnIngresoSistema;
    private List<Empleado> empleados = null;
    private List<Usuario> usuarios = null;
    private EmpleadoController miController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        btnIngresoSistema = findViewById(R.id.btnIngresar);
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        miController = new EmpleadoController();
        miController.llenadoEmpleados();
        miController.llenadoUsuarios();
        usuarios = miController.getUsuarios();
        empleados = miController.getEmpleados();
        miController.mostrarEmpleados(empleados);
        miController.mostrarUsuarios(usuarios);

        Toast.makeText(this.getApplicationContext(), "# empleados: "+ miController.countItemLista(empleados), Toast.LENGTH_SHORT).show();
        Toast.makeText(this.getApplicationContext(), "# usuarios: "+ miController.countItemLista(usuarios), Toast.LENGTH_SHORT).show();

        miController.llenadoEnfermedades(this);

        btnIngresoSistema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario nuevo = new Usuario();
                miController.ingresoSistema(usuarios,etUsuario.getText().toString(),etContrasenia.getText().toString());
                siguienteActivity();
            }
        });
    }

    private void siguienteActivity(){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

}
