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

        /*imgModificarPerfil = findViewById(R.id.fotoModificarPerfil);
        txtModificarNombre = findViewById(R.id.idModificarNombre);
        txtModificarCorreo = findViewById(R.id.idModificarCorreo);
        txtModificarContraseña = findViewById(R.id.idModificarContraseña);
        getTxtModificarContraseñaRepetida = findViewById(R.id.idModificarContraseñaRepetida);
        txtFechaDeNacimiento = findViewById(R.id.txtModificarFechaDeNacimiento);
        rdModificarHombre = findViewById(R.id.rdModificarHombre);
        rdModificarMujer = findViewById(R.id.rdModificarMujer);

        btnModificar = findViewById(R.id.idModificar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fotoModificarUri = null;

        imagePicker = new ImagePicker(this);
        cameraPicker = new CameraImagePicker(this);

        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);

        UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLlave(UsuarioDAO.getInstancia().getKeyUsuario(), new UsuarioDAO.IDevolverUsuario() {
            @Override
            public void devolverUsuario(LUsuario lUsuario) {
                String url = lUsuario.getUsuario().getFotoPerfilURL();
                loadImageFromURL(url);
                //fotoModificarUri = imgModificarPerfil.
                txtModificarNombre.setText(lUsuario.getUsuario().getNombre());
                txtModificarCorreo.setText(lUsuario.getUsuario().getCorreo());
                txtFechaDeNacimiento.setText(LUsuario.obtenerFechaDeNacimiento(lUsuario.getUsuario().getFechaDeNacimiento()));
                String genero = lUsuario.getUsuario().getGenero();

                if(genero.equals("Hombre")){
                    rdModificarHombre.setChecked(true);
                }else{
                    rdModificarMujer.setChecked(true);
                }
            }
            @Override
            public void devolverError(String error) {
            }
        });

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path = list.get(0).getOriginalPath();
                    fotoModificarUri = Uri.parse(path);
                    imgModificarPerfil.setImageURI(fotoModificarUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(ModificarPerfilActivity.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });

        cameraPicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                String path = list.get(0).getOriginalPath();
                fotoModificarUri = Uri.fromFile(new File(path));
                imgModificarPerfil.setImageURI(fotoModificarUri);
            }

            @Override
            public void onError(String s) {
                Toast.makeText(ModificarPerfilActivity.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });

        imgModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModificarPerfilActivity.this);
                dialog.setTitle("Foto de perfil");

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

        txtFechaDeNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ModificarPerfilActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int mes, int dia) {
                        Calendar calendarResultado = Calendar.getInstance();
                        calendarResultado.set(Calendar.YEAR, year);
                        calendarResultado.set(Calendar.MONTH, mes);
                        calendarResultado.set(Calendar.DAY_OF_MONTH, dia);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = calendarResultado.getTime();
                        String fechaDeNacimientoTexto = simpleDateFormat.format(date);
                        fechaDeNacimiento = date.getTime();
                        txtFechaDeNacimiento.setText(fechaDeNacimientoTexto);
                    }
                },calendar.get(Calendar.YEAR)-18,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
/*
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String correo = txtModificarCorreo.getText().toString();
                final String nombre = txtModificarNombre.getText().toString();
                if(isValidEmail(correo) && validarContraseña() && validarNombre(nombre)){
                    String contraseña = txtModificarContraseña.getText().toString();
                    mAuth.getCurrentUser().updatePassword(contraseña)
                            .addOnCompleteListener(ModificarPerfilActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final String genero;
                                        if(rdModificarHombre.isChecked()){
                                            genero = "Hombre";
                                        } else {
                                            genero = "Mujer";
                                        }
                                        if(fotoModificarUri!=null) { //Si selecciono una foto
                                            UsuarioDAO.getInstancia().subirFotoUri(fotoModificarUri, new UsuarioDAO.IDevolverURLFoto() {
                                                @Override
                                                public void devolverUrlString(String url) {
                                                    Toast.makeText(ModificarPerfilActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                                    Usuario usuario = new Usuario();
                                                    usuario.setCorreo(correo);
                                                    usuario.setNombre(nombre);
                                                    usuario.setFechaDeNacimiento(fechaDeNacimiento);
                                                    usuario.setGenero(genero);
                                                    usuario.setFotoPerfilURL(url);
                                                    FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                                                    DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid()); //guarda el mismo uid del usuario en la database
                                                    reference.setValue(usuario);
                                                    finish();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(ModificarPerfilActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                            Usuario usuario = new Usuario();
                                            usuario.setCorreo(correo);
                                            usuario.setNombre(nombre);
                                            usuario.setFechaDeNacimiento(fechaDeNacimiento);
                                            usuario.setGenero(genero);
                                            usuario.setFotoPerfilURL(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                                            FirebaseUser currentUser = mAuth.getCurrentUser(); //esto funciona cuando esta registrado correctamente
                                            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid()); //guarda el mismo uid del usuario en la database
                                            reference.setValue(usuario);
                                            finish();
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(ModificarPerfilActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }else{
                    Toast.makeText(ModificarPerfilActivity.this,"Validaciones funcionando.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Glide.with(this).load(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS).into(imgModificarPerfil); //Automaticamente se va a cargar una foto de perfil por defecto
        */
    }
/*
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

    public boolean validarContraseña(){
        String contraseña, contraseñaRepetida;
        contraseña = txtModificarContraseña.getText().toString();
        contraseñaRepetida = getTxtModificarContraseñaRepetida.getText().toString();
        if(contraseña.equals(contraseñaRepetida)){
            if(contraseña.length() >= 6 && contraseña.length() <=16) {
                return true;
            }else return false;
        }else return false;
    }

    public boolean validarNombre(String nombre){
        return !nombre.isEmpty();
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

    private void loadImageFromURL(String url){
        Picasso.with(this).load(url).into(imgModificarPerfil,new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
    }
*/
}