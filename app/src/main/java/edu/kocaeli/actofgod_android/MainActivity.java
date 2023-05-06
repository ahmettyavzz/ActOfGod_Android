package edu.kocaeli.actofgod_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import edu.kocaeli.actofgod_android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loadLocations();
    }

    private void loadLocations() {
        LocationService locationService = new LocationService();
        locationService.loadLocations();
    }

    private void tcNoValidate(TcNoValidateDto dto) {
        TcNoService tcNoService = new TcNoService();
        tcNoService.tcNoValidate(dto);
    }

    public void connect(View view) {
//        String name=binding.editTextName.getText().toString();
//
//        Intent intent=new Intent(MainActivity.this,MapsActivity.class);
//        startActivity(intent);
        double latitude = 40.8010419;
        double longitude = 29.9496113;
        String uri = "geo:" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");


        startActivity(intent);
    }


}