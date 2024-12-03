package com.citypark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.citypark.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ParkingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ParkingAdapter adapter;
    private ArrayList<Parking> parkingList;
    private DatabaseReference database;
    private BottomNavigationView bnm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);

        recyclerView = findViewById(R.id.recyclerView);
        bnm = findViewById(R.id.bnm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        parkingList = new ArrayList<>();
        adapter = new ParkingAdapter(this, parkingList);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("parkings");

        Log.d("testiinggg", String.valueOf(database));

        //Menu
        bnm.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profile){
                startActivity(new Intent(this, ProfileActivity.class));
            }

            return true;
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkingList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Parking parking = data.getValue(Parking.class);
                    parkingList.add(parking);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ParkingListActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(this, ParkingDetailsActivity.class);
            intent.putExtra("parking", parkingList.get(position));
            startActivity(intent);
        });
    }
}
