package edu.kocaeli.actofgod_android.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationService {
    private final String BASE_URL = "http://192.168.1.49:8080/v1/";
    private List<Location> locations= new ArrayList<>();
    Retrofit retrofit;

    public LocationService() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void  loadLocations() {
        try {
            ApiService apiService = retrofit.create(ApiService.class);
            Call<List<Location>> call = apiService.getLocations();
            call.enqueue(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                    if (response.isSuccessful()) {
                        if (response.body()!=null){
                            locations.addAll(response.body());
                            for (Location location: response.body()){
                                System.out.println(location.toString());
                            }
                        }
                    }
                    else {
                        Log.d("MainActivity", "Failed to load locations");
                    }
                }
                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
                    Log.e("MainActivity", "Error loading locations" +t);
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Exception while loading locations", e);
        }
    }
}
