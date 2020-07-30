package com.practica02.myapplication.Model.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practica02.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView civFotoPerfil;
    private TextView txtNombre;
    private TextView txtMensaje;
    private LinearLayout layaoutPrincipal;


    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);

        civFotoPerfil = itemView.findViewById(R.id.idcvcivFotoPerfil);
        txtNombre = itemView.findViewById(R.id.idcvNombreUsuario);
        txtMensaje = itemView.findViewById(R.id.idcvMensaje);
        layaoutPrincipal = itemView.findViewById(R.id.layaoutPrincipal2);
    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(TextView txtNombre) {
        this.txtNombre = txtNombre;
    }

    public TextView getTxtMensaje() {
        return txtMensaje;
    }

    public void setTxtMensaje(TextView txtMensaje) {
        this.txtMensaje = txtMensaje;
    }

    public LinearLayout getLayaoutPrincipal() {
        return layaoutPrincipal;
    }

    public void setLayaoutPrincipal(LinearLayout layaoutPrincipal) {
        this.layaoutPrincipal = layaoutPrincipal;
    }
}
