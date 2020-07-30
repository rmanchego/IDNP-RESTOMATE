package com.practica02.myapplication.Model.Entidades.Firebase;

public class UltimoMensaje {

    private String keyUsuario;
    private String urlFotoUsuario;
    private String nombreUsuario;
    private String Mensaje;

    public UltimoMensaje() {
    }

    public String getKeyUsuario() {
        return keyUsuario;
    }

    public void setKeyUsuario(String keyReceptor) {
        this.keyUsuario = keyReceptor;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getUrlFotoUsuario() {
        return urlFotoUsuario;
    }

    public void setUrlFotoUsuario(String urlFotoUsuario) {
        this.urlFotoUsuario = urlFotoUsuario;
    }
}
