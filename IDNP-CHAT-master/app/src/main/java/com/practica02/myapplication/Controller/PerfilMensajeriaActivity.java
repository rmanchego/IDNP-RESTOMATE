package com.practica02.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.practica02.myapplication.Model.Entidades.Logica.LRestaurante;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Persistencia.RestauranteDAO;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilMensajeriaActivity extends AppCompatActivity {

    private CircleImageView fotoPerfilMensajeria;
    private TextView nombrePerfilMensajeria;
    private TextView direccionPerfilMensajeria;
    private TextView descripcionPerfilMensajeria;

    private Button btnRegresar;
    private Button btnChatear;

    private String KEY_RECEPTOR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mensajeria);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            KEY_RECEPTOR = bundle.getString("key_receptor");
        }else{
            finish();
        }

        fotoPerfilMensajeria = findViewById(R.id.fotoPerfilMensajeria);
        nombrePerfilMensajeria = findViewById(R.id.idPerfilMensajeriaNombre);
        direccionPerfilMensajeria = findViewById(R.id.idPerfilMensajeriaDireccion);
        descripcionPerfilMensajeria = findViewById(R.id.idPerfilMensajeriaDescripción);

        btnRegresar = findViewById(R.id.btnPerfilMensajeriaRegresar);
        btnChatear = findViewById(R.id.btnPerfilMensajeriaChatear);

        RestauranteDAO.getInstancia().obtenerInformacionDeRestaurantePorLlave(KEY_RECEPTOR, new RestauranteDAO.IDevolverRestaurante() {
            @Override
            public void devolverRestaurante(LRestaurante lRestaurante) {
                //String url = lUsuario.getUsuario().getFotoPerfilURL();
                String url = lRestaurante.getRestaurante().getFotoPerfilURL();
                loadImageFromURL(url);
                nombrePerfilMensajeria.setText(lRestaurante.getRestaurante().getNombre());
                direccionPerfilMensajeria.setText(lRestaurante.getRestaurante().getDireccion());
                descripcionPerfilMensajeria.setText(lRestaurante.getRestaurante().getDescripcion());
            }

            @Override
            public void devolverError(String error) {

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnChatear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilMensajeriaActivity.this, MensajeriaActivity.class);
                intent.putExtra("key_receptor", KEY_RECEPTOR);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadImageFromURL(String url){
        Picasso.with(this).load(url).into(fotoPerfilMensajeria,new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
    }
}
