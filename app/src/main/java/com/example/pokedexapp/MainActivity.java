package com.example.pokedexapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pokedexapp.fragment.FocusFragment;
import com.example.pokedexapp.fragment.HomeFragment;
import com.example.pokedexapp.fragment.PokeBeginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (savedInstanceState == null){
            loadFragment(new FocusFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selected = null;
            if(id == R.id.nav_home){
                selected = new PokeBeginFragment();
            } else if (id == R.id.nav_focus){
                selected = new FocusFragment();
            }
            if(selected != null){
                loadFragment(selected);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_main, fragment)
                .commit();
    }
}