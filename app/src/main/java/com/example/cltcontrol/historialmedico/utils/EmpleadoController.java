package com.example.cltcontrol.historialmedico.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.Empleado;
import com.example.cltcontrol.historialmedico.models.Enfermedad;
import com.example.cltcontrol.historialmedico.models.Usuario;
import java.util.Date;
import java.util.List;

public class EmpleadoController {
    private List<Empleado> lista_empleados;
    private List<Usuario> lista_usuarios;
    private Context miActivity;
    private Usuario usuario_doctor, usuario_enfermera;

    //Constructores
    public EmpleadoController(){
        this.llenadoEmpleados();
        this.llenadoUsuarios();
    }

    public EmpleadoController(Context miActivity) {
        this.miActivity = miActivity;
        this.llenadoUsuarios();
        this.llenadoEmpleados();
    }

    //Getter de registros Usuarios y Empleados
    public List<Empleado> getLista_empleados() {
        return lista_empleados;
    }

    public List<Usuario> getLista_usuarios() {
        return lista_usuarios;
    }

    public void setLista_empleados(List<Empleado> lista_empleados) {
        this.lista_empleados = lista_empleados;
    }

    public void setLista_usuarios(List<Usuario> lista_usuarios) {
        this.lista_usuarios = lista_usuarios;
    }

    //conteo cantidad de registros en la lista
    public int countItemLista(List<?> miLista){
        return miLista.size();
    }

    //Metodos
    //Setter de registros Usuarios y Empleados
    private void llenadoUsuarios(){
        usuario_doctor = new Usuario("jgarcia", "jgarcia");
        usuario_doctor.save();
        usuario_enfermera = new Usuario("asantacruz","asantacruz");
        usuario_enfermera.save();
        this.setLista_usuarios(Usuario.listAll(Usuario.class));
    }

    private void llenadoEmpleados(){
        Empleado miEmpleado = new Empleado("03214567323", "Jorge", "García García",
                "jorergar@espol.edu.ec", "FAE",
                "Analista de datos", "Soltero",
                "Masculino", "Guayaquil",
                "Dept. de Seguridad", new Date(), new Date(), 30, R.drawable.modelo, usuario_doctor);
        miEmpleado.save();

        miEmpleado = new Empleado("0967547365","Anni","Santacruz Hernández",
                "anrosant@espol.edu.ec","Sauces",
                "Ingeniera en Ciencias Computacionales","Soltera",
                "Femenino","Guayaquil",
                "Dept. de Sistemas",new Date(),new Date(),20,R.drawable.modelo, usuario_enfermera);
        miEmpleado.save();

        miEmpleado = new Empleado("0913620589","Renato","Illescas Rodríguez",
                "rillesca@espol.edu.ec","Sauces",
                "Licenciado en Redes","Soltero",
                "Masculino","Guayaquil",
                "Dept. de Redes",new Date(),new Date(),20,R.drawable.modelo);
        miEmpleado.save();

        miEmpleado = new Empleado("0962983345","Daniel","Castro Peñafiel",
                "danijo@espol.edu.ec","Sauces",
                "Ingeniero en Ciencias Computacionales","Soltero",
                "Masculino","Guayaquil",
                "Dept. Desarrollo de Software",new Date(),new Date(),20,R.drawable.modelo);
        miEmpleado.save();
        this.setLista_empleados(Empleado.listAll(Empleado.class));
    }

    public void llenadoEnfermedades(Context miActivity) {
        List<Enfermedad> enfermedades = Enfermedad.find(Enfermedad.class, "CODIGO = ?", "A00");
        if (enfermedades.isEmpty()) {
            try {
                Enfermedad.executeQuery(EnfermedadesSQL.REGISTRO_ENFERMEDADES);
            } catch (Exception e) {
                Toast.makeText(miActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Show de registros Usuarios y Empleados
    public void mostrarEmpleados(List<Empleado> lista_empleados) {
        for (Empleado verEmpleados : lista_empleados) {
            Log.e("aqui nombre", verEmpleados.getApellido());
        }
    }

    public void mostrarUsuarios(List<Usuario> lista_usuarios) {
        for (Usuario verUsuarios : lista_usuarios) {
            Log.e("aqui usuario", verUsuarios.getUsuario());
        }
    }

    public Boolean ingresoSistema(String etUsuario, String etContrasenia){
        return validarIngreso(etUsuario, etContrasenia);
    }

    //funcionalidad ingreso
    private Boolean validarIngreso(String etUsuario, String etContrasenia){
        return obtenerUsuario(etUsuario, etContrasenia).size() != 0;
    }

    //Busqueda de un Usuario
    private List<Usuario> obtenerUsuario(String etUsuario, String etContrasenia){
        return Usuario.find(Usuario.class, "usuario = ? and contrasenia = ?", etUsuario, etContrasenia);
    }

}