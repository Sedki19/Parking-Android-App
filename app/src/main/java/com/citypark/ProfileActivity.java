package com.citypark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private TextView userEmail;
    private TextView emailTextView;
    private Button logoutButton,resButton;
    private BottomNavigationView bnm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userEmail = findViewById(R.id.userEmail);
        emailTextView = findViewById(R.id.email);
        logoutButton = findViewById(R.id.logoutButton);
        resButton = findViewById(R.id.reservationButton);
        bnm = findViewById(R.id.bnmProfile);

        // Get the current user's email
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userEmail.setText(String.valueOf(email.charAt(0)).toUpperCase()+email.substring(1,email.indexOf("@")));
        emailTextView.setText(email);

        // Handle logout
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        //Reservations per Profile
        resButton.setOnClickListener(v ->{
            Intent intent = new Intent(ProfileActivity.this, ReservationListActivity.class);
            startActivity(intent);

        });

        //Menu
        bnm.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.list){
                startActivity(new Intent(this, ParkingListActivity.class));
            }

            return true;
        });
    }
}
