package com.practica02.myapplication.Model.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.practica02.myapplication.Model.Entidades.Logica.LMensaje;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Holder.MensajeriaHolder;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MensajeriaAdaptador extends RecyclerView.Adapter<MensajeriaHolder>{

    private List<LMensaje> listMensaje = new ArrayList<>();   //Lista de logica de mensaje
    private Context c;

    public MensajeriaAdaptador(Context c) {
         this.c = c;
    }

    public int addMensaje(LMensaje LMensaje){
        listMensaje.add(LMensaje);
        int posicion = listMensaje.size()-1;
        notifyItemInserted(listMensaje.size());
        return posicion;
    }

    public void actualizarMensaje(int posicion, LMensaje lMensaje){
        listMensaje.set(posicion, lMensaje);
        notifyItemChanged(posicion);  // solo actualice mensaje x, los demas no lo toca
    }

    @Override
    public MensajeriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_emisor,parent,false);
        }else{
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor,parent,false);
        }
        return new MensajeriaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeriaHolder holder, int position) {

        LMensaje lMensaje = listMensaje.get(position);
        LUsuario lUsuario = lMensaje.getlUsuario();

        if(lUsuario!=null){
            holder.getNombre().setText(lUsuario.getUsuario().getNombre()); //Setear nombre
            Glide.with(c).load(lUsuario.getUsuario().getFotoPerfilURL()).into(holder.getFotoMensajePerfil());  //descargar imagen - si el mensaje tiene foto de perfil del usuario

        }

        holder.getMensaje().setText(lMensaje.getMensaje().getMensaje());
        if(lMensaje.getMensaje().isContieneFoto()){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(lMensaje.getMensaje().getUrlFoto()).into(holder.getFotoMensaje()); //Si existe foto debe cargar url y setearlo para mostrar la foto

        }else{
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }


        holder.getHora().setText(lMensaje.fechaDeCreacionDelMensaje());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(listMensaje.get(position).getlUsuario()!=null){
            if(listMensaje.get(position).getlUsuario().getKey().equals(UsuarioDAO.getInstancia().getKeyUsuario())){ //Si el mensaje es nuestro
                return 1;
            } else {
                return -1;
            }
        }else{
            return -1;
        }

        //return super.getItemViewType(position);
    }
}
