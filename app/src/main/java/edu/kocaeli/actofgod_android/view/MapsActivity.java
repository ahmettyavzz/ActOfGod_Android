package edu.kocaeli.actofgod_android.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.kocaeli.actofgod_android.R;
import edu.kocaeli.actofgod_android.api.ApiService;
import edu.kocaeli.actofgod_android.databinding.ActivityMapsBinding;
import edu.kocaeli.actofgod_android.model.LocationDto;
import edu.kocaeli.actofgod_android.model.route.Route;
import edu.kocaeli.actofgod_android.model.route.RouteParameters;
import edu.kocaeli.actofgod_android.service.BackgroundLocationService;
import edu.kocaeli.actofgod_android.utils.BitmapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    List<LocationDto> locationList = new ArrayList<>();
    ArrayList<Marker> markerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // getIntent() metodu buradan kaldırılıyor
//        MainActivity.currentLocation = LocationWorker.getLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent(); // Intent nesnesi burada alınıyor

        locationList = (List<LocationDto>) intent.getSerializableExtra("locations");

        for (LocationDto l : locationList) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.getLatitude(), l.getLongitude()))
                    .title(l.getName())
                    .snippet(l.getId().toString()));
            markerList.add(marker);
        }

        int drawableResId = R.drawable.my_location_24; // Drawable kaynağını buraya yerleştirin
        BitmapDescriptor bitmapDescriptor = BitmapUtils.drawableToBitmapDescriptor(this, drawableResId);

        LatLng currentLocation = new LatLng(MainActivity.currentLocation.getLatitude(), MainActivity.currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentLocation)
                .icon(bitmapDescriptor);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14.0f));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LocationDto location = getLocationFromMarker(marker);
                MainActivity.destination = location;

                try {
                    ApiService apiService = MainActivity.retrofit.create(ApiService.class);
                    Location location1 = MainActivity.currentLocation;

                    Call<Route> call = apiService.getRoute(new RouteParameters(location1.getLatitude(), location1.getLongitude(), location.getLatitude(), location.getLongitude()));
                    call.enqueue(new Callback<Route>() {
                        @Override
                        public void onResponse(Call<Route> call, Response<Route> response) {
                            if (response.isSuccessful()) {
                                Route roadData = response.body();
                                LatLng markerPosition = marker.getPosition();
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

                                Call<LocationDto> call2 = apiService.getLocationById(MainActivity.destination.getId());
                                call2.enqueue(new Callback<LocationDto>() {
                                    @Override
                                    public void onResponse(Call<LocationDto> call, Response<LocationDto> response) {
                                        if (response.isSuccessful()) {
                                            LocationDto locationDto = response.body();
                                            builder.setTitle(locationDto.getName());
                                            builder.setMessage("Mesafe: " + roadData.getDistance() + " m" +
                                                    "\nSüre: " + roadData.getDuration() + " dk" +
                                                    "\nKapasite: " + locationDto.getCapacity() +
                                                    "\nGitmek istediğinize emin misiniz?");
                                            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Servisi başlatmak için
                                                    Intent serviceIntent = new Intent(MapsActivity.this, BackgroundLocationService.class);
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        startForegroundService(serviceIntent);
                                                    } else {
                                                        startService(serviceIntent);
                                                    }

                                                    String uri = "http://maps.google.com/maps?daddr=" + markerPosition.latitude + "," + markerPosition.longitude;
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                    intent.setPackage("com.google.android.apps.maps");
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.setNegativeButton("Hayır", null);
                                            builder.show();
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

                return true;
            }
        });
    }

    private LocationDto getLocationFromMarker(Marker marker) {
        for (LocationDto location : locationList) {
            if (location.getId().toString().equals(marker.getSnippet())) {
                return location;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "closed");
        Intent serviceIntent = new Intent(this, BackgroundLocationService.class);
        this.stopService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocations();

        Intent serviceIntent = new Intent(this, BackgroundLocationService.class);
        this.stopService(serviceIntent);

        if (mMap != null) {
            LatLng currentLocation = new LatLng(MainActivity.currentLocation.getLatitude(), MainActivity.currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14.0f));
        }
    }

    private void loadLocations() {
        try {
            ApiService apiService = MainActivity.retrofit.create(ApiService.class);
            Call<List<LocationDto>> call = apiService.getLocations();
            call.enqueue(new Callback<List<LocationDto>>() {
                @Override
                public void onResponse(Call<List<LocationDto>> call, Response<List<LocationDto>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (locationList.size() > 0) {
                                locationList.clear();
                                locationList.addAll(response.body());
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
}
