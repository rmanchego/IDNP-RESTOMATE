package com.practica02.myapplication.Model.Entidades.Logica;

import com.google.android.gms.maps.model.LatLng;

public class UbicacionRestaurante {
    private String nombre;
    private LatLng ubicacion;

    public UbicacionRestaurante(){}
    public UbicacionRestaurante(String nombre, LatLng ubicacion){
        this.nombre=nombre;
        this.ubicacion=ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }
}
