package edu.kocaeli.actofgod_android.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import edu.kocaeli.actofgod_android.R;
import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.model.LocationDto;
import edu.kocaeli.actofgod_android.model.UpdateLocationDto;
import edu.kocaeli.actofgod_android.model.route.Route;
import edu.kocaeli.actofgod_android.model.route.RouteParameters;
import edu.kocaeli.actofgod_android.view.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundLocationService extends Service {

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
                try {
                    ApiService apiService = MainActivity.retrofit.create(ApiService.class);
                    Location location1 = MainActivity.currentLocation;

                    Call<Route> call = apiService.getRoute(new RouteParameters(location1.getLatitude(), location1.getLongitude(), MainActivity.destination.getLatitude(), MainActivity.destination.getLongitude()));
                    call.enqueue(new Callback<Route>() {
                        @Override
                        public void onResponse(Call<Route> call, Response<Route> response) {
                            if (response.isSuccessful()) {
                                Route roadData = response.body();

                                if (roadData.getDistance() < 20) {
                                    showNotification(getApplicationContext(), "Güvenli Alana Ulaştınız", "");
                                }

                                Call<LocationDto> call2 = apiService.getLocationById(MainActivity.destination.getId());
                                call2.enqueue(new Callback<LocationDto>() {
                                    @Override
                                    public void onResponse(Call<LocationDto> call, Response<LocationDto> response) {
                                        if (response.isSuccessful()) {
                                            LocationDto locationDto = response.body();
                                            if (locationDto.getCapacity() == 0) {
                                                showNotification(getApplicationContext(), "Uyarı!", "Gitmekte olduğunuz sığınma noktasının kapasitesi dolmuştur.");
                                            }

                                            UpdateLocationDto updateLocationDto = new UpdateLocationDto();
                                            updateLocationDto.setName(locationDto.getName());
                                            updateLocationDto.setLatitude(locationDto.getLatitude());
                                            updateLocationDto.setLongitude(locationDto.getLongitude());
                                            updateLocationDto.setCapacity(locationDto.getCapacity() - 1);
                                            updateLocationDto.setDistrictId(locationDto.getDistrictId());
                                            if (roadData.getDistance() < 20 && locationDto.getCapacity() > 0) {
                                                Call<LocationDto> updateLocation = apiService.updateLocation(locationDto.getId(), updateLocationDto);
                                                updateLocation.enqueue(new Callback<LocationDto>() {
                                                    @Override
                                                    public void onResponse(Call<LocationDto> call, Response<LocationDto> response) {
                                                        if (response.isSuccessful()) {
                                                            Log.d("UPDATE", "güncellendi");
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<LocationDto> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<LocationDto> call, Throwable t) {

                                    }
                                });
                            } else {
                                System.out.println("API Request failed. Error: " + response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<Route> call, Throwable t) {
                            System.out.println("API Request failed. Error: " + t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.e("MainActivity", "Exception while loading locations", e);
                }
                handler.postDelayed(this, 15000);
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

    public void showNotification(Context context, String title, String message) {
        String CHANNEL_ID = "my_channel_id";
        String CHANNEL_NAME = "My Channel";
        String CHANNEL_DESCRIPTION = "My Channel Description";
        NotificationCompat.Builder builder;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =notificationManager.getNotificationChannel(CHANNEL_ID);

            if (channel==null){

                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(CHANNEL_DESCRIPTION);
                notificationManager.createNotificationChannel(channel);
            }
            builder=new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setAutoCancel(true);
        }
        else {
            builder=new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        notificationManager.notify(1, builder.build());
    }
}
