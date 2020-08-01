package com.practica02.myapplication.Model.Persistencia;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.practica02.myapplication.Model.Entidades.Firebase.Plato;
import com.practica02.myapplication.Model.Entidades.Logica.LPlato;
import com.practica02.myapplication.Model.Utilidades.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlatoDAO {
    public interface IDevolverPlato{
        public void devolverPlato(LPlato lPlato);
        public void devolverError(String error);
    }

    public interface IDevolverURLFoto{
        public void devolverUrlString(String url);
    }

    private static PlatoDAO platoDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference referencePlatos;
    private StorageReference referenceFotoDePlato;

    public static PlatoDAO getInstancia(){
        if(platoDAO==null) platoDAO = new PlatoDAO();
        return platoDAO;
    }

    private PlatoDAO(){
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        referencePlatos = database.getReference(Constantes.NODO_DE_PLATOS);
        referenceFotoDePlato = storage.getReference("Fotos/FotoPerfil/Platos/" + getKeyPlato());
    }

    public String getKeyPlato(){
        return FirebaseAuth.getInstance().getUid();
    }

    public void obtenerInformacionDePlatoPorLlave(final String key, final PlatoDAO.IDevolverPlato iDevolverPlato){
        referencePlatos.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plato plato = dataSnapshot.getValue(Plato.class);
                LPlato lPlato = new LPlato(key,plato);
                iDevolverPlato.devolverPlato(lPlato);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
                iDevolverPlato.devolverError(databaseError.getMessage());
            }
        });  //ejecuta una sola vez, no tiene listener por si ocurre cambio

    }
/*
    public void añadirFotoDePerfilALosRestaurantesQueNoTienenFoto() {
        referenceRestaurantes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LRestaurante> lRestaurantesLista = new ArrayList<>();
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    Restaurante restaurante = childDataSnapshot.getValue(Restaurante.class);
                    LRestaurante lRestaurante = new LRestaurante(childDataSnapshot.getKey(),restaurante);
                    lRestaurantesLista.add(lRestaurante);
                }

                for(LRestaurante lRestaurante : lRestaurantesLista){
                    if(lRestaurante.getRestaurante().getFotoPerfilURL()==null){
                        referenceRestaurantes.child(lRestaurante.getKey()).child("fotoPerfilURL").setValue(Constantes.URL_FOTO_POR_DEFECTO_USUARIOS);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //Trae a todos los restaurantes solo una vez.
    }
*/
    public void subirFotoUri(Uri uri, final PlatoDAO.IDevolverURLFoto iDevolverURLFoto){   //Uri -> foto que se elige con el celular
        String nombreFoto = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());  //Guardar en Firebase por fecha
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = referenceFotoDePlato.child(nombreFoto);
        //Uri u = taskSnapshot.getDownloadUrl();
        fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    iDevolverURLFoto.devolverUrlString(uri.toString());
                }
            }
        });
    }

}
