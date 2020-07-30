package com.practica02.myapplication.Model.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practica02.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView civFotoPerfil;
    private TextView txtNombreUsuario;
    private LinearLayout layaoutPrincipal;

    public UsuarioViewHolder(@NonNull View itemView) {
        super(itemView);

        civFotoPerfil = itemView.findViewById(R.id.civFotoPerfil);
        txtNombreUsuario = itemView.findViewById(R.id.txtNombreUsuario);
        layaoutPrincipal = itemView.findViewById(R.id.layaoutPrincipal);
    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public void setTxtNombreUsuario(TextView txtNombreUsuario) {
        this.txtNombreUsuario = txtNombreUsuario;
    }

    public LinearLayout getLayaoutPrincipal() {
        return layaoutPrincipal;
    }

    public void setLayaoutPrincipal(LinearLayout layaoutPrincipal) {
        this.layaoutPrincipal = layaoutPrincipal;
    }
}
