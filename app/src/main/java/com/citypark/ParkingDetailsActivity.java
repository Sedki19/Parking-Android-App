package com.citypark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkingDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView name, address, price, availability;
    private Button reserveButton;
    private GoogleMap googleMap;
    private LatLng parkingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        name = findViewById(R.id.parkingNameDetails);
        address = findViewById(R.id.parkingAddressDetails);
        price = findViewById(R.id.parkingPrice);
        availability = findViewById(R.id.parkingAvailability);
        reserveButton = findViewById(R.id.reserveButton);

        // Get the Parking object from intent
        Parking parking = (Parking) getIntent().getSerializableExtra("parking");

        // Populate UI fields
        name.setText(parking.getName());
        address.setText(parking.getAddress());
        price.setText("Price: " + parking.getPrice()+"Dt Per Hour");
        availability.setText("Available: " + parking.getAvailable());

        // Set up the map
        parkingLocation = new LatLng(parking.getLatitude(), parking.getLongitude());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Handle Reserve button click
        reserveButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParkingDetailsActivity.this, ReservationActivity.class);
            intent.putExtra("parking", parking);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add marker for the parking location
        googleMap.addMarker(new MarkerOptions().position(parkingLocation).title("Parking Location"));

        // Move the camera to the parking location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, 15));
    }
}
