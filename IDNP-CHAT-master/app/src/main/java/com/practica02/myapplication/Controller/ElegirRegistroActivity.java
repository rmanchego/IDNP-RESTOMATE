package com.practica02.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.practica02.myapplication.R;

public class ElegirRegistroActivity extends AppCompatActivity {

    private Button btnRegistroUsuario;
    private Button btnRegistroRestaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_registro);

        btnRegistroUsuario = findViewById(R.id.btnRegistroUsuario);
        btnRegistroRestaurante = findViewById(R.id.btnRegistroRestaurante);

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElegirRegistroActivity.this, RegistroActivity.class));
                finish();
            }
        });

        btnRegistroRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElegirRegistroActivity.this, RegistroRestauranteActivity.class));
                finish();
            }
        });

    }
}