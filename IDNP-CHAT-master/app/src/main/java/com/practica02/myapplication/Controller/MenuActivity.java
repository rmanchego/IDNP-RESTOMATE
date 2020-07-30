package com.practica02.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnVerUsuarios;
    private Button btnCerrarSesion;
    private Button btnVerPerfil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnVerUsuarios = findViewById(R.id.btnVerUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);

        btnVerUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VerUsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });
    }
    private void returnLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UsuarioDAO.getInstancia().isUsuarioLogeado()){
            //El usuairo esa logeado y hacemos algo
        }else {
            returnLogin();
        }
    }

}
