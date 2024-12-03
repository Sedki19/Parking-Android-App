package com.citypark;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;


public class ReservationActivity extends AppCompatActivity {

    private Button confirmButton;
    private DatabaseReference database,parkDatabase;
    private Parking parking;
    private CalendarView calendarView;
    private String cDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        TextView parkingName = findViewById(R.id.selectedParkingName);

        confirmButton = findViewById(R.id.confirmReservationButton);
        calendarView = findViewById(R.id.calendarView);
        database = FirebaseDatabase.getInstance().getReference("reservations");
        parkDatabase = FirebaseDatabase.getInstance().getReference("parkings");
        cDate = String.valueOf(LocalDate. now());
        parking = (Parking) getIntent().getSerializableExtra("parking");
        parkingName.setText(parking.getName());

        confirmButton.setOnClickListener(v -> saveReservation());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //cDate = month + "/" + dayOfMonth + "/" + year ;
                cDate = year + "-" + month + "-" + dayOfMonth;
            }
        });

    }



    private void saveReservation() {

        Log.d("Test Date",this.cDate);

        if (this.cDate.isEmpty()) {
            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String reservationId = database.push().getKey();
        Reservation reservation = new Reservation(parking.getId(), userId, this.cDate);

        database.child(reservationId).setValue(reservation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateAvailableSpots();
                        Toast.makeText(this, "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(this, "Failed to confirm reservation", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void updateAvailableSpots() {
        DatabaseReference parkingRef = parkDatabase.child(parking.getId()).child("available");

        // Perform a transaction to increment the available spots atomically
        parkingRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                int currentAvailable = snapshot.getValue(Integer.class);
                int newAvailable = currentAvailable - 1; // Decrease by 1
                if (newAvailable < 0) {
                    Toast.makeText(this, "No available spots!", Toast.LENGTH_SHORT).show();
                    return;
                }

                parkingRef.setValue(newAvailable).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update availability", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Parking data not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
