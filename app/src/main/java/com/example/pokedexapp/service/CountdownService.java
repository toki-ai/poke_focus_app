package com.example.pokedexapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.pokedexapp.R;

public class CountdownService extends Service {

    private final String CHANNEL_ID = "Channel Service Countdown";
    public final static String SHAREPFR_NAME = "Countdown";
    public final static String SHARERFR_TIME = "time";
    public final static String BROADCAST_INTENT_TIME = "remainTime";

    public static int DEFAULT_TIMMING = 10; //cho user set muốn 1 block bao nhiêu tiếng
    private int remainTime = DEFAULT_TIMMING;
    private Handler handler = new Handler();
    private Runnable countdownRunnable;
    private  boolean isRunning;
    private SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHAREPFR_NAME, Context.MODE_PRIVATE);
        int ave = sharedPreferences.getInt(SHARERFR_TIME, DEFAULT_TIMMING);
        if (ave <= 0){
            remainTime = DEFAULT_TIMMING;
        }else{
            remainTime = ave;
        }
        createNotificationChannel();
        startForeground(1, createNotification("Starting Countdown..."));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (!isRunning) {
            isRunning = true;
            startCountdown();
        } else {
            Log.d("Countdown Service", "Countdown already running");
        }
        return START_NOT_STICKY;
    }

    private void startCountdown() {
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                remainTime--;
                if (remainTime >= 0) {
                    Log.d("Countdown Service", "Time left: " + remainTime);
                    Notification noti = createNotification(String.valueOf(remainTime));
                    NotificationManager notiManager = getSystemService(NotificationManager.class);
                    notiManager.notify(1, noti);

                    Intent intent = new Intent("COUNTDOWN");
                    intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent.putExtra(BROADCAST_INTENT_TIME, remainTime);
                    sendBroadcast(intent);

                    handler.postDelayed(this, 1000);
                } else {
                    stopSelf();
                    isRunning = false;
                }
            }
        };

        handler.post(countdownRunnable);
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Countdown Timer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("This channel is used for the countdown timer");
        channel.enableLights(true);
        channel.enableVibration(true);
    }

    private Notification createNotification(String time) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Countdown Timer")
                .setContentText("Time left: " + time + " seconds")
                .setSmallIcon(R.drawable.img_poke_ball_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        Notification stickyNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Countdown Finished")
                .setContentText("Press to restart countdown")
                .setSmallIcon(R.drawable.img_poke_ball_icon)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(false)
                .build();

        notificationManager.notify(1, stickyNotification);
    }
}