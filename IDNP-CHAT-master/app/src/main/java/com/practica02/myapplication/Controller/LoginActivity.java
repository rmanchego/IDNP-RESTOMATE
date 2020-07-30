package com.practica02.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practica02.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreo, txtContraseña;
    private Button btnLogin;
    private TextView txtRegistro;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText) findViewById(R.id.idCorreoLogin);
        txtContraseña = (EditText) findViewById(R.id.idContraseñaLogin);
        btnLogin = (Button) findViewById(R.id.idLoginLogin);
        txtRegistro = findViewById(R.id.idRegistroLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = txtCorreo.getText().toString();
                if(isValidEmail(correo) && validarContraseña()){
                    String contraseña = txtContraseña.getText().toString();
                    mAuth.signInWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this,"Se logeo correctamente.", Toast.LENGTH_SHORT).show();
                                        nextActivity();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this,"Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }else {
                    Toast.makeText(LoginActivity.this,"Validaciones funcionando.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        //UsuarioDAO.getInstancia().añadirFotoDePerfilALosUsuariosQueNoTienenFoto();
    }

    private boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarContraseña(){
        String contraseña;
        contraseña = txtContraseña.getText().toString();
        if(contraseña.length() >= 6 && contraseña.length() <=16) {
            return true;
        }else return false;
    }

    @Override
    protected void onResume() {
        // se llama cada vez que entremo a la actividad
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser(); //verifica si el usuario se logeo anteriormente
        if(currentUser != null) {
            Toast.makeText(this, "Usuario logeado.", Toast.LENGTH_SHORT).show();
            nextActivity();
        }
    }

    private void nextActivity(){
        startActivity(new Intent(LoginActivity.this, VistaInicialActivity.class));
        finish();
    }
}
