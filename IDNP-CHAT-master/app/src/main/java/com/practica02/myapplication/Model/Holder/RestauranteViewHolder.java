package com.practica02.myapplication.Model.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practica02.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestauranteViewHolder extends RecyclerView.ViewHolder  {

    private CircleImageView civFotoPerfil;
    private TextView txtNombreRestaurante;
    private LinearLayout layaoutPrincipal;

    public RestauranteViewHolder(@NonNull View itemView) {
        super(itemView);

        civFotoPerfil = itemView.findViewById(R.id.civFotoPerfilRestaurante);
        txtNombreRestaurante = itemView.findViewById(R.id.txtNombreRestaurante);
        layaoutPrincipal = itemView.findViewById(R.id.layaoutPrincipal3);
    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTxtNombreRestaurante() {
        return txtNombreRestaurante;
    }

    public void setTxtNombreRestaurante(TextView txtNombreRestaurante) {
        this.txtNombreRestaurante = txtNombreRestaurante;
    }

    public LinearLayout getLayaoutPrincipal() {
        return layaoutPrincipal;
    }

    public void setLayaoutPrincipal(LinearLayout layaoutPrincipal) {
        this.layaoutPrincipal = layaoutPrincipal;
    }
}
