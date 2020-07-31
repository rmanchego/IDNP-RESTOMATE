package com.practica02.myapplication.Model.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.practica02.myapplication.Controller.PerfilMensajeriaActivity;
import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;
import com.practica02.myapplication.Model.Entidades.Logica.LRestaurante;
import com.practica02.myapplication.Model.Holder.RestauranteViewHolder;
import com.practica02.myapplication.Model.Utilidades.Constantes;
import com.practica02.myapplication.R;

public class VerRestaurantesFragment extends Fragment {

    private RecyclerView rvRestaurantes;
    private FirebaseRecyclerAdapter adapter;

    public VerRestaurantesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ver_resturantes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final FragmentActivity c = getActivity();
        super.onActivityCreated(savedInstanceState);
        rvRestaurantes = getView().findViewById(R.id.rvRestaurantes2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
        rvRestaurantes.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance() //consulta
                .getReference()
                .child(Constantes.NODO_DE_RESTAURANTES);

        FirebaseRecyclerOptions<Restaurante> options =
                new FirebaseRecyclerOptions.Builder<Restaurante>()
                        .setQuery(query, Restaurante.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Restaurante, RestauranteViewHolder>(options) {
            @Override
            public RestauranteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_restaurantes, parent, false);
                return new RestauranteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(RestauranteViewHolder holder, int position, Restaurante model) {
                Glide.with(VerRestaurantesFragment.this).load(model.getFotoPerfilURL()).into(holder.getCivFotoPerfil());
                holder.getTxtNombreRestaurante().setText(model.getNombre());
                //Obtiene toda la data de la lista //  Key en getSpanpshot 2
                final LRestaurante lRestaurante = new LRestaurante(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getLayaoutPrincipal().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PerfilMensajeriaActivity.class);
                        intent.putExtra("key_receptor", lRestaurante.getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        rvRestaurantes.setAdapter(adapter);
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