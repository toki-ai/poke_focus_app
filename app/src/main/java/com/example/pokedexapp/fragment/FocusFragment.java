package com.example.pokedexapp.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokedexapp.R;
import com.example.pokedexapp.adapter.GridBorderDecoration;
import com.example.pokedexapp.adapter.TimeEntryAdapter;
import com.example.pokedexapp.api.ApiService;
import com.example.pokedexapp.data.model.TimeEntry;
import com.example.pokedexapp.data.model.pokemon.Pokemon;
import com.example.pokedexapp.data.repository.BlockTimingRepository;
import com.example.pokedexapp.data.repository.CoinRepository;
import com.example.pokedexapp.service.CountdownService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FocusFragment extends Fragment {

    private ImageView pokeRandomImg;
    private TextView tvCountdown, quitButton, playButton, pauseButton, tvTotalCoin;

    private CoinRepository coinRepository;
    private BlockTimingRepository timeRepository;
    private SharedPreferences sharedPreferences;
    private int remainTime;

    public FocusFragment() {}

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            remainTime = intent.getIntExtra(CountdownService.BROADCAST_INTENT_TIME, CountdownService.DEFAULT_TIMMING);
            if(remainTime <= 0){
                tvCountdown.setText("Time's up");
                coinRepository.saveCoin(coinRepository.getCoin() + CoinRepository.DEFAULT_INCREASE_COIN);
                tvTotalCoin.setText(String.valueOf(coinRepository.getCoin()));
                playButton.setVisibility(View.VISIBLE);
                quitButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.GONE);
            }else {
                setCountdownClock(remainTime);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);
        sharedPreferences = getActivity().getSharedPreferences(CountdownService.SHAREPFR_NAME, Context.MODE_PRIVATE);
        coinRepository = new CoinRepository(getContext());
        timeRepository = new BlockTimingRepository(getContext());
        playButton = view.findViewById(R.id.btn_play_countdown);
        pauseButton = view.findViewById(R.id.btn_pause_countdown);
        quitButton = view.findViewById(R.id.btn_quit_countdown);
        pokeRandomImg = view.findViewById(R.id.iv_icon_pokemon);
        tvCountdown = view.findViewById(R.id.tv_countdown);
        tvTotalCoin = view.findViewById(R.id.tv_total_coin);
        timeRepository.save(new TimeEntry(2025,2,1, 4));
        timeRepository.save(new TimeEntry(2025,2,2, 6));
        timeRepository.save(new TimeEntry(2025,2,3, 7));
        timeRepository.save(new TimeEntry(2025,2,4, 0));
        timeRepository.save(new TimeEntry(2025,2,5, 5));
        timeRepository.save(new TimeEntry(2025,2,6, 1));
        timeRepository.save(new TimeEntry(2025,2,7, 3));
        timeRepository.save(new TimeEntry(2025,2,8, 9));
        timeRepository.save(new TimeEntry(2025,2,9, 11));
        timeRepository.save(new TimeEntry(2025,2,10, 16));
        RecyclerView recyclerView = view.findViewById(R.id.time_block_tracker);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        TimeEntryAdapter adapter = new TimeEntryAdapter(requireContext(), timeRepository.getMonthBlockEntry(2, 2025), 2025, 2);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(
                new GridBorderDecoration(
                        requireContext(),  // context
                        1,                 // 1 dp border width
                        ContextCompat.getColor(requireContext(), R.color.black)
                )
        );


        tvTotalCoin.setText(String.valueOf(coinRepository.getCoin()));

        int ave = sharedPreferences.getInt(CountdownService.SHARERFR_TIME, CountdownService.DEFAULT_TIMMING);
        if(ave <= 0){
            setCountdownClock(CountdownService.DEFAULT_TIMMING);
        }else{
            setCountdownClock(ave);
        }

        playButton.setOnClickListener(v -> {
            requestNotificationPermissionSilently();
            playButton.setVisibility(View.GONE);
            quitButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            startCountdown();
        });

        pauseButton.setOnClickListener(v -> {
            pauseCountdown();
            playButton.setVisibility(View.VISIBLE);
            quitButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
        });

        quitButton.setOnClickListener(v -> {
            quitCountdown();
            playButton.setVisibility(View.VISIBLE);
            quitButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.GONE);
        });

        getRandomPokeImg();
        return view;
    }

    public void startCountdown(){
        Intent serviceIntent = new Intent(requireContext(), CountdownService.class);
        ContextCompat.startForegroundService(requireContext(), serviceIntent);
    }

    private final ActivityResultLauncher<String> requestNotificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCountdown();
                } else {
                    Toast.makeText(requireContext(), "Notification permission is required for countdown", Toast.LENGTH_SHORT).show();
                }
            });

    public void requestNotificationPermissionSilently() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                startCountdown();
            }
        } else {
            startCountdown();
        }
    }

    private void quitCountdown(){
        Intent serviceIntent = new Intent(getContext(), CountdownService.class);
        getContext().stopService(serviceIntent);
        sharedPreferences.edit().putInt(CountdownService.SHARERFR_TIME, 0).apply();
        setCountdownClock(CountdownService.DEFAULT_TIMMING);
    }

    private void pauseCountdown(){
        Intent serviceIntent = new Intent(getContext(), CountdownService.class);
        getContext().stopService(serviceIntent);
        sharedPreferences.edit().putInt(CountdownService.SHARERFR_TIME, remainTime).apply();
    }

    private void setCountdownClock(int remain){
        tvCountdown.setText(String.format("%02d:%02d", remain / 60, remain % 60));
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

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(receiver, new IntentFilter("COUNTDOWN"), Context.RECEIVER_EXPORTED);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
    }
}