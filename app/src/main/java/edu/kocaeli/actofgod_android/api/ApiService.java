package edu.kocaeli.actofgod_android.api;

import java.util.List;

import edu.kocaeli.actofgod_android.model.LocationDto;
import edu.kocaeli.actofgod_android.model.PersonDto;
import edu.kocaeli.actofgod_android.model.TcNoValidateDto;
import edu.kocaeli.actofgod_android.model.route.Route;
import edu.kocaeli.actofgod_android.model.route.RouteParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("location")
    Call<List<LocationDto>> getLocations();

    @POST("tcno-validate")
    Call<Boolean> tcNoValidate(@Body TcNoValidateDto dto);

    @POST("user")
    Call<Void> savePerson(@Body PersonDto personDto);

    @POST("route")
    Call<Route> getRoute(@Body RouteParameters routeParameters);
}
