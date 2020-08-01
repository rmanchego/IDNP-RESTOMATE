package com.practica02.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.practica02.myapplication.Model.Entidades.Firebase.Plato;
import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;
import com.practica02.myapplication.Model.Entidades.Logica.LRestaurante;
import com.practica02.myapplication.Model.Entidades.Logica.LUsuario;
import com.practica02.myapplication.Model.Holder.PlatoViewHolder;
import com.practica02.myapplication.Model.Holder.RestauranteViewHolder;
import com.practica02.myapplication.Model.Persistencia.RestauranteDAO;
import com.practica02.myapplication.Model.Persistencia.UsuarioDAO;
import com.practica02.myapplication.Model.Utilidades.Constantes;
import com.practica02.myapplication.Model.fragments.VerRestaurantesFragment;
import com.practica02.myapplication.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilMensajeriaActivity extends AppCompatActivity {

    private CircleImageView fotoPerfilMensajeria;
    private TextView nombrePerfilMensajeria;
    private TextView direccionPerfilMensajeria;
    private TextView descripcionPerfilMensajeria;

    private RecyclerView rvPlatos;
    private FirebaseRecyclerAdapter adapter;

    private Button btnRegresar;
    private Button btnChatear;

    private String KEY_RECEPTOR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mensajeria);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            KEY_RECEPTOR = bundle.getString("key_receptor");
        }else{
            finish();
        }

        fotoPerfilMensajeria = findViewById(R.id.fotoPerfilMensajeria);
        nombrePerfilMensajeria = findViewById(R.id.idPerfilMensajeriaNombre);
        direccionPerfilMensajeria = findViewById(R.id.idPerfilMensajeriaDireccion);
        descripcionPerfilMensajeria = findViewById(R.id.idPerfilMensajeriaDescripción);

        rvPlatos = findViewById(R.id.rvPlatos);

        btnRegresar = findViewById(R.id.btnPerfilMensajeriaRegresar);
        btnChatear = findViewById(R.id.btnPerfilMensajeriaChatear);

        RestauranteDAO.getInstancia().obtenerInformacionDeRestaurantePorLlave(KEY_RECEPTOR, new RestauranteDAO.IDevolverRestaurante() {
            @Override
            public void devolverRestaurante(LRestaurante lRestaurante) {
                //String url = lUsuario.getUsuario().getFotoPerfilURL();
                String url = lRestaurante.getRestaurante().getFotoPerfilURL();
                loadImageFromURL(url);
                nombrePerfilMensajeria.setText(lRestaurante.getRestaurante().getNombre());
                direccionPerfilMensajeria.setText(lRestaurante.getRestaurante().getDireccion());
                descripcionPerfilMensajeria.setText(lRestaurante.getRestaurante().getDescripcion());
            }

            @Override
            public void devolverError(String error) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPlatos.setLayoutManager(linearLayoutManager);

        Query query = FirebaseDatabase.getInstance() //consulta
                .getReference()
                .child(Constantes.NODO_DE_PLATOS)
                .child(KEY_RECEPTOR);

        FirebaseRecyclerOptions<Plato> options =
                new FirebaseRecyclerOptions.Builder<Plato>()
                        .setQuery(query, Plato.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Plato, PlatoViewHolder>(options) {
            @Override
            public PlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_platos, parent, false);
                return new PlatoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PlatoViewHolder holder, int position, Plato model) {
                Glide.with(PerfilMensajeriaActivity.this).load(model.getFotoURL()).into(holder.getCivFotoPerfil());
                holder.getTxtNombrePlato().setText(model.getNombre());
                holder.getTxtPrecioPlato().setText("S/. " + model.getPrecio());
                //holder.getTxtPrecioPlato().setText(""+model.getPrecio());
                //Obtiene toda la data de la lista //  Key en getSpanpshot 2
                /*final LRestaurante lRestaurante = new LRestaurante(getSnapshots().getSnapshot(position).getKey(),model);

                holder.getLayaoutPrincipal().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PerfilMensajeriaActivity.class);
                        intent.putExtra("key_receptor", lRestaurante.getKey());
                        startActivity(intent);
                    }
                });
                */
            }
        };
        rvPlatos.setAdapter(adapter);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnChatear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilMensajeriaActivity.this, MensajeriaActivity.class);
                intent.putExtra("key_receptor", KEY_RECEPTOR);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadImageFromURL(String url){
        Picasso.with(this).load(url).into(fotoPerfilMensajeria,new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
