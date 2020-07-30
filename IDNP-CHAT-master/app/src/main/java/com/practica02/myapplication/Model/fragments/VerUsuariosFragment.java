package com.practica02.myapplication.Model.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.practica02.myapplication.Controller.PerfilMensajeriaActivity;
import com.practica02.myapplication.Model.Entidades.Firebase.Usuario;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Holder.UsuarioViewHolder;
import com.practica02.myapplication.R;
import com.practica02.myapplication.Model.Utilidades.Constantes;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerUsuariosFragment extends Fragment {

    private RecyclerView rvUsuarios;
    private FirebaseRecyclerAdapter adapter;

    public VerUsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ver_usuarios, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final FragmentActivity c = getActivity();
        super.onActivityCreated(savedInstanceState);
        rvUsuarios = getView().findViewById(R.id.rvUsuarios2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
        rvUsuarios.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance() //consulta
                .getReference()
                .child(Constantes.NODO_DE_USUARIOS);

        FirebaseRecyclerOptions<Usuario> options =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Usuario, UsuarioViewHolder>(options) {
            @Override
            public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_usuario, parent, false);
                return new UsuarioViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsuarioViewHolder holder, int position, Usuario model) {
                Glide.with(VerUsuariosFragment.this).load(model.getFotoPerfilURL()).into(holder.getCivFotoPerfil());
                holder.getTxtNombreUsuario().setText(model.getPlaca() + " / " + model.getNombre());
                //Obtiene toda la data de la lista //  Key en getSpanpshot 2
                final LUsuario lUsuario = new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getLayaoutPrincipal().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PerfilMensajeriaActivity.class);
                        intent.putExtra("key_receptor", lUsuario.getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        rvUsuarios.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
