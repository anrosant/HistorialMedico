package com.example.cltcontrol.historialmedico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cltcontrol.historialmedico.R;
import com.example.cltcontrol.historialmedico.models.AtencionEnfermeria;
import com.example.cltcontrol.historialmedico.models.SignosVitales;

import java.util.List;
import java.util.Objects;

public class SignosVitalesEnfermeriaFragment extends Fragment {

    private String idAtencion=null;
    private String idAtencion2=null;
    private String presedencia;
    private Bundle bun;
    private EditText etPresionSistolica;
    private EditText etPresionDistolica;
    private EditText etPulso;
    private EditText etTemperatura;
    private Button boton;


    public SignosVitalesEnfermeriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate y vinculaciones de las variables globales
        View view = inflater.inflate(R.layout.fragment_signos_vitales, container, false);
        etPresionSistolica = view.findViewById(R.id.etPSistolica);
        etPresionDistolica = view.findViewById(R.id.etPDistolica);
        etPulso = view.findViewById(R.id.etPulso);
        etTemperatura = view.findViewById(R.id.etTemperatura);
        boton = view.findViewById(R.id.btnGuardar);
        bun = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        //Obtencion de parametros de ventana contenedora AtencionEnfermeriaActivity
        idAtencion= bun.getString("ID_ATENCION");
        presedencia= bun.getString("PRESEDENCIA");


        if(presedencia.equals("consultar")){//funcionalidad para cargar un signo vital con el id de Atencion
            Toast.makeText(getContext(),"Viniste a consultar",Toast.LENGTH_SHORT).show();
            List<SignosVitales> signos = SignosVitales.find(SignosVitales.class,"atencionenfermeria = ?",idAtencion);
            if(!signos.isEmpty()) {
                etPresionSistolica.setText(String.valueOf(signos.get(0).getPresion_sistolica()));
                etPresionDistolica.setText(String.valueOf(signos.get(0).getPresion_distolica()));
                etPulso.setText(String.valueOf(signos.get(0).getPulso()));
                etTemperatura.setText(String.valueOf(signos.get(0).getTemperatura()));

                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pDis = Integer.valueOf(etPresionDistolica.getText().toString());
                        int pSis = Integer.valueOf(etPresionSistolica.getText().toString());
                        int pulso = Integer.valueOf(etPulso.getText().toString());
                        float temp = Float.valueOf(etTemperatura.getText().toString());
                        guardarSigno(idAtencion,pDis,pSis,pulso,temp);
                    }
                });
            }
        }else{//funcionalidad para guardar un signo vital nuevo que fue creado en la ventana contenedora AtencionEnfermeriaActivity
            // al presionar el boton + en historial de Atencion Medica
            Toast.makeText(getContext(),"Viniste a crear",Toast.LENGTH_SHORT).show();
            idAtencion2 = bun.getString("ID_ATENCION2");

            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pDis = Integer.valueOf(etPresionDistolica.getText().toString());
                    int pSis = Integer.valueOf(etPresionSistolica.getText().toString());
                    int pulso = Integer.valueOf(etPulso.getText().toString());
                    float temp = Float.valueOf(etTemperatura.getText().toString());
                    guardarSigno(idAtencion2,pDis,pSis,pulso,temp);
                }
            });
        }

        return view;
    }

    public void guardarSigno(String idAten, int pDis, int pSis,int pulso,float temp){
        //Se obtiene la atencion para setearla en el nuevo signo vital
        AtencionEnfermeria atencionEnfermeria = AtencionEnfermeria.findById(AtencionEnfermeria.class,Long.valueOf(idAten));

        if(pDis == 0  || pSis ==0 ||temp == 0 || pulso==0){
            Toast.makeText(getContext(),"No ha ingresado todos los datos", Toast.LENGTH_SHORT).show();
        }else{
            try{
                if(presedencia.equals("consultar")){
                    SignosVitales nuevo = SignosVitales.find(SignosVitales.class,"atencionEnfermeria = ?",idAten).get(0);
                    nuevo.setAtencion_enfermeria(atencionEnfermeria);
                    nuevo.setPresion_sistolica(pSis);
                    nuevo.setPresion_distolica(pDis);
                    nuevo.setPulso(pulso);
                    nuevo.setTemperatura(temp);
                    nuevo.save();
                }else{
                    SignosVitales nuevo = new SignosVitales();
                    nuevo.setAtencion_enfermeria(atencionEnfermeria);
                    nuevo.setPresion_sistolica(pSis);
                    nuevo.setPresion_distolica(pDis);
                    nuevo.setPulso(pulso);
                    nuevo.setTemperatura(temp);
                    nuevo.save();
                }
                Toast.makeText(getContext(),"Datos Guardados con exito", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
