package com.example.pokedexapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokedexapp.R;
import com.example.pokedexapp.api.ApiService;
import com.example.pokedexapp.data.model.Pokemon;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FocusFragment extends Fragment {

    ImageView pokeRandomImg;
    TextView tvCountdown;

    public FocusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        TextView playButton = view.findViewById(R.id.btn_play_countdown);
        TextView pauseButton = view.findViewById(R.id.btn_pause_countdown);
        TextView quitButton = view.findViewById(R.id.btn_quit_countdown);
        pokeRandomImg = view.findViewById(R.id.iv_icon_pokemon);
        tvCountdown = view.findViewById(R.id.tv_countdown);

        quitButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);
        playButton.setOnClickListener(v -> {
            playButton.setVisibility(View.GONE);
            quitButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            startCountdown();
        });

        getRandomPokeImg();
        return view;
    }

    public void startCountdown(){
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tvCountdown.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("Time's up!");
            }
        }.start();
    }

    private void getRandomPokeImg(){
        int max = 1000;
        int randomId = (int) Math.round(Math.random() * max);
        ApiService.apiService.getPokemonById(randomId).enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                if(pokemon != null){
                    String imageUrl = pokemon.getSprites().getOther().getOfficialArtwork().getFrontDefault();
                    Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.img_poke_ball_icon)
                            .error(R.drawable.img_coin)
                            .into(pokeRandomImg);
                }
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Toast.makeText(getContext(), "Call Api Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}