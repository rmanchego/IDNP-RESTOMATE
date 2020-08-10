package com.practica02.myapplication.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.practica02.myapplication.Model.Entidades.Firebase.Plato;
import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;
import com.practica02.myapplication.Model.Entidades.Firebase.Usuario;
import com.practica02.myapplication.Model.Persistencia.PlatoDAO;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.Model.Utilidades.Constantes;
import com.practica02.myapplication.R;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarPlatoActivity extends AppCompatActivity {

    private CircleImageView fotoPerfilPlato;

    private EditText txtNombrePlato;
    private EditText txtPrecioPlato;

    private Button btnRegistrar;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;

    private String pickerPath;

    private Uri fotoPlatoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_plato);

        fotoPerfilPlato = findViewById(R.id.PlatofotoPerfil);
        txtNombrePlato = findViewById(R.id.idRegistroPlatoNombre);
        txtPrecioPlato = findViewById(R.id.idRegistroPlatoPrecio);

        btnRegistrar = (Button) findViewById(R.id.idRegistroPlatoRegistrar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path = list.get(0).getOriginalPath();
                    fotoPlatoUri = Uri.parse(path);
                    fotoPerfilPlato.setImageURI(fotoPlatoUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(AgregarPlatoActivity.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                fotoPlatoUri = Uri.fromFile(new File(path));
                fotoPerfilPlato.setImageURI(fotoPlatoUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(AgregarPlatoActivity.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfilPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AgregarPlatoActivity.this);
                dialog.setTitle("Foto del Plato");

                String[] items = {"Galeria", "Camara"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                //Hizo click en la galeria
                                imagePicker.pickImage();
                                break;
                            case 1:
                                //Hizo click en la camara
                                pickerPath = cameraPicker.pickImage();
                                break;
                        }
                    }
                });

                AlertDialog dialogContruido = dialog.create();
                dialogContruido.show();

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = txtNombrePlato.getText().toString();
                final String precio = txtPrecioPlato.getText().toString();
                if(validarNombre(nombre) && validarString(precio)){
                    if(fotoPlatoUri!=null) { //Si selecciono una foto
                        PlatoDAO.getInstancia().subirFotoUri(fotoPlatoUri, new PlatoDAO.IDevolverURLFoto() {
                            @Override
                            public void devolverUrlString(String url) {
                                Toast.makeText(AgregarPlatoActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                Plato plato = new Plato();
                                plato.setNombre(nombre);
                                plato.setPrecio(Float.parseFloat(precio));
                                plato.setFotoURL(url);
                                FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                                DatabaseReference reference = database.getReference("Platos/" + currentUser.getUid() + "/" + plato.getNombre().replace(" ", "")); //guarda el mismo uid del usuario en la database
                                reference.setValue(plato);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(AgregarPlatoActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                        Plato plato = new Plato();
                        plato.setNombre(nombre);
                        plato.setPrecio(Float.parseFloat(precio));
                        plato.setFotoURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                        FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                        DatabaseReference reference = database.getReference("Platos/" + currentUser.getUid() + "/" + plato.getNombre()); //guarda el mismo uid del usuario en la database
                        reference.setValue(plato);
                        finish();
                    }
                        /*
                        UsuarioDAO.getInstancia().subirFotoUri(fotoPerfilUri, new UsuarioDAO.IDevolverURLFoto() {
                            @Override
                            public void devolverUrlString(String url) {
                                Toast.makeText(AgregarPlatoActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                Restaurante restaurante = new Restaurante();
                                restaurante.setCorreo(correo);
                                restaurante.setNombre(nombre);
                                restaurante.setFotoPerfilURL(url);
                                restaurante.setDescripcion(descripcion);
                                restaurante.setLatitud(Double.parseDouble(latitud));
                                restaurante.setLongitud(Double.parseDouble(longitud));
                                restaurante.setDireccion(direccion);
                                FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                                DatabaseReference reference = database.getReference("Restaurantes/" + currentUser.getUid()); //guarda el mismo uid del usuario en la database
                                reference.setValue(restaurante);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(RegistroRestauranteActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                        Restaurante restaurante = new Restaurante();
                        restaurante.setCorreo(correo);
                        restaurante.setNombre(nombre);
                        restaurante.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                        restaurante.setDescripcion(descripcion);
                        restaurante.setLatitud(Double.parseDouble(latitud));
                        restaurante.setLongitud(Double.parseDouble(longitud));
                        restaurante.setDireccion(direccion);
                        FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                        DatabaseReference reference = database.getReference("Restaurantes/" + currentUser.getUid()); //guarda el mismo uid del usuario en la database
                        reference.setValue(restaurante);
                        finish();
                    }

                         */

                }else{
                    Toast.makeText(AgregarPlatoActivity.this,"Validaciones funcionando.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Glide.with(this).load(Constantes.URL_FOTO_POR_DEFECTO_PLATOS).into(fotoPerfilPlato); //Automaticamente se va a cargar una foto de perfil por defecto

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //data imagen que vamos a seleccionar
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){  //Si no cancelo al seleccionar una foto
            imagePicker.submit(data);
        }else if(requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK){
            cameraPicker.reinitialize(pickerPath);
            cameraPicker.submit(data);
        }

    }

    private boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }

    public boolean validarString(String string){
        return !string.isEmpty();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}