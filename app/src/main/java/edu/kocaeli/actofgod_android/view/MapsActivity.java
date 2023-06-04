package edu.kocaeli.actofgod_android.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.kocaeli.actofgod_android.R;
import edu.kocaeli.actofgod_android.databinding.ActivityMapsBinding;
import edu.kocaeli.actofgod_android.model.LocationDto;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LatLng defaultLocation = new LatLng(40.7682, 29.935);
    List<LocationDto> locationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // getIntent() metodu buradan kaldırılıyor
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent(); // Intent nesnesi burada alınıyor

        ArrayList<Marker> markerList = new ArrayList<>();
        locationList = (List<LocationDto>) intent.getSerializableExtra("locations");

        for (LocationDto l : locationList) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.getLatitude(), l.getLongitude()))
                    .title(l.getName())
                    .snippet(l.getId().toString()));
            markerList.add(marker);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 14.0f));


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng markerPosition = marker.getPosition();
                LocationDto location = getLocationFromMarker(marker);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle(location.getName());
                builder.setMessage("Kapasite: " + location.getCapacity()+
                        "\nGitmek istediğinize emin misiniz?");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uri = "http://maps.google.com/maps?daddr=" + markerPosition.latitude + "," + markerPosition.longitude;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Hayır", null);
                builder.show();
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
}
