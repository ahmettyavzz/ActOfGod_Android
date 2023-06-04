package edu.kocaeli.actofgod_android.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import edu.kocaeli.actofgod_android.R;

public class BackgroundLoggingService extends Service {

    private static final String TAG = "BackgroundLoggingService";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "background_logging_channel";
    private static final String CHANNEL_NAME = "Background Logging";

    private Handler handler;
    private Runnable logRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        logRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "1");
                handler.postDelayed(this, 1000); // 1 saniye aralıklarla log yaz
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLogging();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopLogging();
        super.onDestroy();
    }

    private void startLogging() {
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Logging")
                .setContentText("Logging in progress...")
                .setSmallIcon(R.drawable.logo)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        handler.postDelayed(logRunnable, 5000);
    }

    private void stopLogging() {
        handler.removeCallbacks(logRunnable);
        stopForeground(true);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
