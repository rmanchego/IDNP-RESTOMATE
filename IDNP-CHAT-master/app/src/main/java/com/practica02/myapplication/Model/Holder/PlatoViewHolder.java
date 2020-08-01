package com.practica02.myapplication.Model.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practica02.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlatoViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView civFotoPerfil;
    private TextView txtNombrePlato;
    private TextView txtPrecioPlato;
    private LinearLayout layaoutPrincipal;

    public PlatoViewHolder(@NonNull View itemView) {
        super(itemView);

        civFotoPerfil = itemView.findViewById(R.id.civFotoPerfilPlato);
        txtNombrePlato = itemView.findViewById(R.id.txtNombrePlato);
        txtPrecioPlato = itemView.findViewById(R.id.txtPrecioPlato1);
        layaoutPrincipal = itemView.findViewById(R.id.layaoutPrincipal4);
    }

    public CircleImageView getCivFotoPerfil() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(CircleImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTxtNombrePlato() {
        return txtNombrePlato;
    }

    public void setTxtNombrePlato(TextView txtNombrePlato) {
        this.txtNombrePlato = txtNombrePlato;
    }

    public TextView getTxtPrecioPlato() {
        return txtPrecioPlato;
    }

    public void setTxtPrecioPlato(TextView txtPrecioPlato) {
        this.txtPrecioPlato = txtPrecioPlato;
    }

    public LinearLayout getLayaoutPrincipal() {
        return layaoutPrincipal;
    }

    public void setLayaoutPrincipal(LinearLayout layaoutPrincipal) {
        this.layaoutPrincipal = layaoutPrincipal;
    }
}
