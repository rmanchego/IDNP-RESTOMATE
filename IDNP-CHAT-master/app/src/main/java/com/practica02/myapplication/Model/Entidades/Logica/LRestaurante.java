package com.practica02.myapplication.Model.Entidades.Logica;

import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LRestaurante {

    private String key;
    private Restaurante restaurante;

    public LRestaurante (String key, Restaurante restaurante) {
        this.key = key;
        this.restaurante = restaurante;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String obtenerFechaDeCreacion(){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = new Date(UsuarioDAO.getInstancia().fechaDeCreacionLong());
            return simpleDateFormat.format(date);
        }

        public String obtenerFechaDeUltimaVezQueSeLogeo(){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = new Date(UsuarioDAO.getInstancia().fechaDeUltimaVezQueSeLogeoLong());
            return simpleDateFormat.format(date);
    }

}
