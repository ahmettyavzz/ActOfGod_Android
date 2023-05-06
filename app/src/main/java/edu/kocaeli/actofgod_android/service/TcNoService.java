package edu.kocaeli.actofgod_android.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TcNoService {

    private static final String BASE_URL = "http://192.168.1.49:8080/v1/";
    Retrofit retrofit;

    public TcNoService() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void tcNoValidate(TcNoValidateDto validateDto) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Boolean> call = apiService.tcNoValidate(validateDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                // istek başarılı
                System.out.println(validateDto.toString());
                System.out.println(response.body());

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("MainActivity", "Error loading locations", t);
            }
        });
    }
}
