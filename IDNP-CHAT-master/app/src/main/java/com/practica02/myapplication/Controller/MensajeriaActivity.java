package com.practica02.myapplication.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.practica02.myapplication.Model.Adaptadores.MensajeriaAdaptador;
import com.practica02.myapplication.Model.Entidades.Firebase.Mensaje;
import com.practica02.myapplication.Model.Entidades.Logica.LMensaje;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Persistencia.MensajeriaDAO;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.R;
import com.practica02.myapplication.Model.Utilidades.Constantes;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajeriaActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombrePerfil;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private MensajeriaAdaptador adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseStorage storage; //Cargar archivos BD
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCadena;

    private String NOMBRE_USUARIO;

    private String KEY_RECEPTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            KEY_RECEPTOR = bundle.getString("key_receptor");
        }else{
            finish();
        }

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombrePerfil = (TextView) findViewById(R.id.nombrePerfil);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new MensajeriaAdaptador(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLlave(KEY_RECEPTOR, new UsuarioDAO.IDevolverUsuario() {
            @Override
            public void devolverUsuario(LUsuario lUsuario) {
                nombrePerfil.setText(lUsuario.getUsuario().getPlaca() + " / " + lUsuario.getUsuario().getNombre());
                String url = lUsuario.getUsuario().getFotoPerfilURL();
                loadImageFromURL(url);
            }

            @Override
            public void devolverError(String error) {

            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.addMensaje(new Mensaje(txtMensaje.getText().toString(),nombre.getText().toString(),"","1","00;00"));
                String mensajeEnviar = txtMensaje.getText().toString();
                if(!mensajeEnviar.isEmpty()){
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(mensajeEnviar);
                    mensaje.setContieneFoto(false);
                    mensaje.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                    MensajeriaDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,mensaje);
                    txtMensaje.setText("");
                }
            }
        });

        btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"), PHOTO_SEND);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto"), PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        FirebaseDatabase.
                getInstance().
                getReference(Constantes.NODO_MENSAJES).
                child(UsuarioDAO.getInstancia().getKeyUsuario()).
                child(KEY_RECEPTOR).addChildEventListener(new ChildEventListener() {

            //traer la informacion del usuario
            //guardamos la informacion del usuario en una lista temporal
            //obtenemos la informacion guardad por la llave

            Map<String, LUsuario> mapUsuariosTemporales = new HashMap<>();


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);  //obtenemos mensaje
                final LMensaje lMensaje = new LMensaje(dataSnapshot.getKey(),mensaje);  //convertirlo a un LMensaje
                final int posicion = adapter.addMensaje(lMensaje);  //obtenemos posicion del mensaje

                if(mapUsuariosTemporales.get(mensaje.getKeyEmisor())!=null){ //Si la lista temporal tiene usuario, ya no llama otra vez a la BD por que esta consumiendo la misma informacion de la BD y evitar que consuma datos de Internet
                    lMensaje.setlUsuario(mapUsuariosTemporales.get(mensaje.getKeyEmisor()));
                    adapter.actualizarMensaje(posicion, lMensaje);
                }else{
                    UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLlave(mensaje.getKeyEmisor(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            mapUsuariosTemporales.put(mensaje.getKeyEmisor(),lUsuario);
                            lMensaje.setlUsuario(lUsuario);
                            adapter.actualizarMensaje(posicion,lMensaje);
                        }

                        @Override
                        public void devolverError(String error) {
                            Toast.makeText(MensajeriaActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        verifyStoragePermissions(this);

    }

    private void loadImageFromURL(String url){
        Picasso.with(this).load(url).into(fotoPerfil,new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();  //foto que se elige con el celular
            storageReference = storage.getReference("imagenes_chat"); //imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            //Uri u = taskSnapshot.getDownloadUrl();
            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException(); //throw -> llama a la excepcion
                    }
                    return fotoReferencia.getDownloadUrl(); //Si se eligio una foto y se subio a la BD, agarra la url del archivo
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {  //este metodo captura la url de la foto
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult(); //url de la foto que se sube a la BS
                        Mensaje mensaje = new Mensaje();
                        mensaje.setMensaje("El usuario ha enviado una foto");
                        mensaje.setUrlFoto(uri.toString());
                        mensaje.setContieneFoto(true);
                        mensaje.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                        MensajeriaDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR,mensaje);
                    }
                }
            });
        }/*else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("foto_perfil"); //imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException(); //throw -> llama a la excepcion
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult(); //url de la foto que se sube a la BS
                        fotoPerfilCadena = uri.toString();
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_USUARIO + " ha actualizado su foto de perfil",uri.toString(), NOMBRE_USUARIO,fotoPerfilCadena,"2",ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        Glide.with(MensajeriaActivity.this).load(uri.toString()).into(fotoPerfil);
                    }
                }
            });
        }*/
    }
}
