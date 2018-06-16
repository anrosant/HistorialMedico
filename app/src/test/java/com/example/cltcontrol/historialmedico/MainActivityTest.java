package com.example.cltcontrol.historialmedico;

import android.widget.Button;
import android.widget.EditText;

import com.example.cltcontrol.historialmedico.activities.MainActivity;
import com.example.cltcontrol.historialmedico.utils.EmpleadoController;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {
    private MainActivity myActivity;
    private EditText etContrasenia;
    private EditText etUsuario;

    private EmpleadoController empleado;

    @Before
    public void setUp() throws Exception {
        myActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
        etUsuario = myActivity.findViewById(R.id.etUsuario);
        etContrasenia = myActivity.findViewById(R.id.etContrasenia);
        empleado = new EmpleadoController();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        EditText etUsuario = myActivity.findViewById(R.id.etUsuario);
        EditText etContrasenia = myActivity.findViewById(R.id.etContrasenia);
        assertNotNull(etUsuario);
        assertNotNull(etContrasenia);
        assertNotNull(myActivity);
    }

    @Test
    public void addition_isCorrect(){
        etUsuario.setText("dgarcia");
        etContrasenia.setText("dgarcia");

        assertEquals(true, empleado.validarIngreso(empleado.getUsuarios(),etUsuario.getText().toString(),etContrasenia.getText().toString()));
    }
/*
    @Test
    public void buttonClickShouldStartNewActivity() throws Exception
    {
        Button button = myActivity.findViewById( R.id.btnIngresar );
        if(button.performClick()){
            myActivity.ingresoSistema(null);

        }

        assertEquals("","" );
    }
*/
}