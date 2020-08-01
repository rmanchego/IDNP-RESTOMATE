package com.practica02.myapplication.Model.Entidades.Logica;

import com.practica02.myapplication.Model.Entidades.Firebase.Plato;
import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;

public class LPlato {

    private String key;
    private Plato plato;

    public LPlato (String key, Plato plato) {
        this.key = key;
        this.plato = plato;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }
}
