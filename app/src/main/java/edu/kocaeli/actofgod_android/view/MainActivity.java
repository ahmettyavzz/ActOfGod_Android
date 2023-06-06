package edu.kocaeli.actofgod_android.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
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
import edu.kocaeli.actofgod_android.service.BackgroundLocationService;
import edu.kocaeli.actofgod_android.service.PersonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String BASE_URL = "http://192.168.1.24:8080/v1/";
    public static Retrofit retrofit;
    private List<LocationDto> locations = new ArrayList<>();
    String androidId;

    public static LocationDto destination = null;
    public static Location currentLocation;

    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;

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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
            }
        };

        registerLauncher();
        checkLocationPermission();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.editTextFirstName.getText().toString();
                String lastName = binding.editTextLastName.getText().toString();
                String birthYear = binding.editTextBirthYear.getText().toString();
                String tcNo = binding.editTextTcNo.getText().toString();

                TcNoValidateDto validateDto = new TcNoValidateDto(firstName, lastName, birthYear, tcNo);

                tcNoValidate(validateDto);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "closed");
        Intent serviceIntent = new Intent(this, BackgroundLocationService.class);
        this.stopService(serviceIntent);
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

    private void tcNoValidate(TcNoValidateDto validateDto) {
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
                        if (currentLocation == null) {
                            Toast.makeText(MainActivity.this, "Konum alınıyor...", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
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

    private void savePerson(PersonDto person) {
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Permission needed for maps", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                            }
                        }).show();
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result) {

                }
                else {
                    Toast.makeText(MainActivity.this, "Permission needed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}