package edu.kocaeli.actofgod_android.view;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.databinding.ActivityMainBinding;
import edu.kocaeli.actofgod_android.model.LocationDto;
import edu.kocaeli.actofgod_android.model.PersonDto;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;
import edu.kocaeli.actofgod_android.model.route.Route;
import edu.kocaeli.actofgod_android.model.route.RouteParameters;
import edu.kocaeli.actofgod_android.service.BackgroundLoggingService;
import edu.kocaeli.actofgod_android.service.PersonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String BASE_URL = "http://192.168.1.24:8080/v1/";
    Retrofit retrofit;
    private List<LocationDto> locations = new ArrayList<>();
    String androidId;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        loadLocations();
    }

    private void loadLocations() {
        try {
            ApiService apiService = retrofit.create(ApiService.class);
            Call<List<LocationDto>> call = apiService.getLocations();
            call.enqueue(new Callback<List<LocationDto>>() {
                @Override
                public void onResponse(Call<List<LocationDto>> call, Response<List<LocationDto>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            locations.addAll(response.body());
                            for (LocationDto location : response.body()) {
                                System.out.println(location.toString());
                            }
                        }
                    } else {
                        Log.d("MainActivity", "Failed to load locations");
                    }
                }

                @Override
                public void onFailure(Call<List<LocationDto>> call, Throwable t) {
                    Log.e("MainActivity", "Error loading locations" + t);
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Exception while loading locations", e);
        }
    }

    public void tcNoValidate(TcNoValidateDto validateDto) {
        try {
            ApiService apiService = retrofit.create(ApiService.class);
            Call<Boolean> call = apiService.tcNoValidate(validateDto);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.body()) {
                        PersonService personService = new PersonService();
                        PersonDto toSave = personService.validateToPerson(validateDto, androidId);
                        savePerson(toSave);

                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                         intent.putExtra("locations", (Serializable) locations);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Lütfen doğru ve eksiksiz bilgi giriniz!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Bağlantı sorunu.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Exception while loading locations", e);
        }
    }

    public void savePerson(PersonDto person) {
        try {
            ApiService apiService = retrofit.create(ApiService.class);
            Call<Void> call = apiService.savePerson(person);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    System.out.println("122");
                    Log.e("MainActivity", "KAYDETTIII" );

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    System.out.println("129");
                    Log.e("MainActivity", "Error loading locations" + t);
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Exception while loading locations", e);
        }
    }

    public void connect(View view) {
        String firstName = binding.editTextFirstName.getText().toString();
        String lastName = binding.editTextLastName.getText().toString();
        String birthYear = binding.editTextBirthYear.getText().toString();
        String tcNo = binding.editTextTcNo.getText().toString();

//        TcNoValidateDto validateDto = new TcNoValidateDto(firstName, lastName, birthYear, tcNo);
//
//        tcNoValidate(validateDto);

        // Servisi başlatmak için
        Intent serviceIntent = new Intent(this, BackgroundLoggingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(serviceIntent);
        } else {
            this.startService(serviceIntent);
        }
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

    public void route() {
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Route> call = apiService.getRoute(new RouteParameters(49.75332, 6.50322, 49.71482, 6.49944));
        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Route roadData = response.body();
                    System.out.println(roadData.getDistance());
                    System.out.println(roadData.getDuration());
                } else {
                    System.out.println("API Request failed. Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                System.out.println("API Request failed. Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "closed");
        Intent serviceIntent = new Intent(this, BackgroundLoggingService.class);
        this.stopService(serviceIntent);
    }
}