package com.practica02.myapplication.Model.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.practica02.myapplication.Controller.PerfilMensajeriaActivity;
import com.practica02.myapplication.Model.Entidades.Firebase.Restaurante;
import com.practica02.myapplication.Model.Entidades.Logica.LRestaurante;
import com.practica02.myapplication.Model.Entidades.Logica.UbicacionRestaurante;
import com.practica02.myapplication.Model.Holder.RestauranteViewHolder;
import com.practica02.myapplication.Model.Utilidades.Constantes;
import com.practica02.myapplication.R;

import java.util.ArrayList;

public class MapaRestaurantesFragment extends Fragment implements OnMapReadyCallback {
    View view;
    private GoogleMap googleMap;
    private MapView mapView;
    private RecyclerView rvRestaurantes;
    private FirebaseRecyclerAdapter adapter;

    public MapaRestaurantesFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity c = getActivity();
        super.onActivityCreated(savedInstanceState);
        rvRestaurantes = getView().findViewById(R.id.rvRestaurantes3);

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
                Glide.with(MapaRestaurantesFragment.this).load(model.getFotoPerfilURL()).into(holder.getCivFotoPerfil());
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mapa_restaurantes, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView!=null){
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }else{
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rest = database.getReference();
        rest.child(Constantes.NODO_DE_RESTAURANTES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float zoomlvl=16.0f;
                for(DataSnapshot objSS: dataSnapshot.getChildren()){
                    Restaurante restaurante =objSS.getValue(Restaurante.class);

                    if(restaurante.getLatitud()!=0 && restaurante.getLongitud()!=0){
                        UbicacionRestaurante ubis = new UbicacionRestaurante(restaurante.getNombre(),
                                new LatLng(restaurante.getLatitud(),restaurante.getLongitud()));
                        googleMap.addMarker(new MarkerOptions().position(ubis.getUbicacion()).title(ubis.getNombre())
                                .icon(bitmapDescriptorFromVector(getContext(), R.drawable.ic_restaurant_map)));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubis.getUbicacion(), zoomlvl));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        this.googleMap = googleMap;

    }

    private BitmapDescriptor bitmapDescriptorFromVector ( Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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