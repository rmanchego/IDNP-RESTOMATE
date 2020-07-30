package com.practica02.myapplication.Model.Persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practica02.myapplication.Model.Entidades.Firebase.Mensaje;
import com.practica02.myapplication.Model.Entidades.Firebase.UltimoMensaje;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Utilidades.Constantes;

public class MensajeriaDAO {

    private static MensajeriaDAO mensajeriaDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenceMensajeria;

    public static MensajeriaDAO getInstancia(){
        if(mensajeriaDAO==null) mensajeriaDAO = new MensajeriaDAO();
        return mensajeriaDAO;
    }

    private MensajeriaDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajeria = database.getReference(Constantes.NODO_MENSAJES);
        //storage = FirebaseStorage.getInstance();
        //referenceUsuarios = database.getReference(Constantes.NODO_DE_USUARIOS);
        //referenceFotoDePerfil = storage.getReference("Fotos/FotoPerfil/" + getKeyUsuario());
    }

    public void nuevoMensaje(final String keyEmisor, final String keyReceptor, final Mensaje mensaje){ //bloc
        DatabaseReference referenceEmisor = referenceMensajeria.child(keyEmisor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceMensajeria.child(keyReceptor).child(keyEmisor);

        referenceEmisor.push().setValue(mensaje);
        referenceReceptor.push().setValue(mensaje);

        DatabaseReference referenceUsuarioEmisor = FirebaseDatabase.getInstance().getReference(Constantes.NODO_DE_USUARIOS);
        referenceUsuarioEmisor.child(keyEmisor);

        final UltimoMensaje ultimoMensajeEmisor = new UltimoMensaje();
        final UltimoMensaje ultimoMensajeReceptor = new UltimoMensaje();

        final FirebaseDatabase databaseUltimoUsuario;
        databaseUltimoUsuario = FirebaseDatabase.getInstance();

        UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLlave(keyEmisor, new UsuarioDAO.IDevolverUsuario() {
            @Override
            public void devolverUsuario(LUsuario lUsuario) {
                ultimoMensajeEmisor.setKeyUsuario(lUsuario.getKey());
                if(mensaje.isContieneFoto() == true){
                    ultimoMensajeEmisor.setMensaje("El usuario ha enviado una foto");
                }else{
                    ultimoMensajeEmisor.setMensaje(mensaje.getMensaje());
                }
                ultimoMensajeEmisor.setNombreUsuario(lUsuario.getUsuario().getNombre());
                ultimoMensajeEmisor.setUrlFotoUsuario(lUsuario.getUsuario().getFotoPerfilURL());
                DatabaseReference referenceUltimoUsuario1 = databaseUltimoUsuario.getReference(Constantes.NODO_ULTIMO_MENSAJE+"/"+keyReceptor+"/"+keyEmisor);
                referenceUltimoUsuario1.setValue(ultimoMensajeEmisor);
            }

            @Override
            public void devolverError(String error) {

            }
        });

        UsuarioDAO.getInstancia().obtenerInformacionDeUsuarioPorLlave(keyReceptor, new UsuarioDAO.IDevolverUsuario() {
            @Override
            public void devolverUsuario(LUsuario lUsuario) {
                ultimoMensajeReceptor.setKeyUsuario(lUsuario.getKey());
                if(mensaje.isContieneFoto() == true){
                    ultimoMensajeReceptor.setMensaje("El usuario ha enviado una foto");
                }else{
                    ultimoMensajeReceptor.setMensaje(mensaje.getMensaje());
                }
                ultimoMensajeReceptor.setNombreUsuario(lUsuario.getUsuario().getNombre());
                ultimoMensajeReceptor.setUrlFotoUsuario(lUsuario.getUsuario().getFotoPerfilURL());
                DatabaseReference referenceUltimoUsuario2 = databaseUltimoUsuario.getReference(Constantes.NODO_ULTIMO_MENSAJE+"/"+keyEmisor+"/"+keyReceptor);
                referenceUltimoUsuario2.setValue(ultimoMensajeReceptor);
            }

            @Override
            public void devolverError(String error) {

            }
        });

    }
}
