package com.practica02.myapplication.Controller;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.practica02.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModificarPerfilActivity extends AppCompatActivity {

    private CircleImageView imgModificarPerfil;

    private EditText txtModificarNombre;
    private EditText txtModificarCorreo;
    private EditText txtModificarContraseña;
    private EditText getTxtModificarContraseñaRepetida;
    private EditText txtFechaDeNacimiento;

    private RadioButton rdModificarHombre;
    private RadioButton rdModificarMujer;

    private Button btnModificar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;

    private String pickerPath;

    private long fechaDeNacimiento;

    private Uri fotoModificarUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil_usuario);

    }
}