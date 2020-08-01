package com.practica02.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.practica02.myapplication.Model.fragments.Chat;
import com.practica02.myapplication.Model.fragments.MapaRestaurantesFragment;
import com.practica02.myapplication.Model.fragments.VerRestaurantesFragment;
import com.practica02.myapplication.Model.fragments.VerUsuariosFragment;
import com.practica02.myapplication.Model.Adaptadores.ViewPagerAdapter;
import com.practica02.myapplication.R;

public class VistaInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    //private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle newToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inicial);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        tabLayout= (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.myViewPager);
        navigationView = findViewById(R.id.navigationview);

        //drawerLayout = (DrawerLayout) findViewById(R.id.drawer)

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.mdVerPerfil:
                startActivity(new Intent(VistaInicialActivity.this, PerfilActivity.class));
                return true;
            case R.id.mdAyuda:
                startActivity(new Intent(VistaInicialActivity.this, AcercaDeActivity.class));
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.agregar_plato_main_menu:
                agregarPlato();
                return true;
            case R.id.compartir_app_main_menu:
                compartirapp();
                return true;
            case R.id.cerrar_sesion_main_menu:
                FirebaseAuth.getInstance().signOut();
                returnLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void modificarPerfil(){
        startActivity(new Intent(VistaInicialActivity.this, ModificarPerfilActivity.class));
    }

    private void agregarPlato(){
        startActivity(new Intent(VistaInicialActivity.this, AgregarPlatoActivity.class));
    }

    private void compartirapp(){
        Intent compartir = new Intent(android.content.Intent.ACTION_SEND);
        compartir.setType("text/plain");
        String subject = "Copcar";
        String mensaje = "Te recomiendo esta app, para que puedas contectar con distintos conductores de manera rapida.";
        compartir.putExtra(Intent.EXTRA_SUBJECT, subject);
        compartir.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
        startActivity(Intent.createChooser(compartir, "Compartir vía:"));
    }

    private void returnLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setupViewPager(ViewPager viewPager){
        Log.i("tab","metodo");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Chat(),"Chat");
        //viewPagerAdapter.addFragment(new VerUsuariosFragment(),"Usuarios");
        viewPagerAdapter.addFragment(new VerRestaurantesFragment(),"Restaurantes");
        viewPagerAdapter.addFragment(new MapaRestaurantesFragment(), "Mapa");
        viewPager.setAdapter(viewPagerAdapter);
    }
}
