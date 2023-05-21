package edu.kocaeli.actofgod_android.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.databinding.ActivityMainBinding;
import edu.kocaeli.actofgod_android.model.Location;
import edu.kocaeli.actofgod_android.model.Person;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;
import edu.kocaeli.actofgod_android.service.PersonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String BASE_URL = "http://192.168.1.49:8080/v1/";
    Retrofit retrofit;
    private List<Location> locations = new ArrayList<>();
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
            Call<List<Location>> call = apiService.getLocations();
            call.enqueue(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            locations.addAll(response.body());
                            for (Location location : response.body()) {
                                System.out.println(location.toString());
                            }
                        }
                    } else {
                        Log.d("MainActivity", "Failed to load locations");
                    }
                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
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
               //     if (response.body()) {
                    if (true){
                        PersonService personService = new PersonService();
                        Person toSave = personService.validateToPerson(validateDto, androidId);
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

    public void savePerson(Person person) {
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

        TcNoValidateDto validateDto = new TcNoValidateDto(firstName, lastName, birthYear, tcNo);

//        if (tcNoValidate(validateDto)){
//        double latitude = 40.8010419;
//        double longitude = 29.9496113;
//        String uri = "geo:" + latitude + "," + longitude;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        intent.setPackage("com.google.android.apps.maps");
//
//
//        startActivity(intent);
//        }else {
//            Toast.makeText(getApplicationContext(), "Lütfen doğru ve eksiksiz bilgi giriniz!", Toast.LENGTH_SHORT).show();
//        }

        System.out.println("!!!!!!!!!!!!!!!!!!!");
        tcNoValidate(validateDto);
        System.out.println("!!!!!!!!!!!!!!!!!!");

    }

}