package com.example.cltcontrol.historialmedico.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Usuario;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText etUsuario, etContrasenia;
    public Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        btnIngresar = findViewById(R.id.btnIngresar);

        Date fecha = new Date(2018,5,25);

        Usuario userTemp = new Usuario("095363","Anni","Santacruz","sauces", fecha,"anrosant","anni","hjhsdfj");
        userTemp.save();
    }

    public void ingresoSistema(View v){
        String nombreUsuario = etUsuario.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        Usuario usuario;

        List<Usuario> usuarios = Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?",nombreUsuario,contrasenia);

        if(!usuarios.isEmpty()){
            usuario = usuarios.get(0);
            if(usuario.getContrasenia().equals(contrasenia)){
                Toast.makeText(getApplicationContext(), "Ingreso exitoso",Toast.LENGTH_SHORT).show();
                aperturaBusqueda(null);
            }else{
                Toast.makeText(getApplicationContext(), "Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                etContrasenia.setText("");
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se encuentra el usuario",Toast.LENGTH_SHORT).show();
        }
    }

    public void aperturaBusqueda(View v){
        Intent inbuscarempleado = new Intent(this, BuscarEmpleadoActivity.class);
        startActivity(inbuscarempleado);
    }

}
