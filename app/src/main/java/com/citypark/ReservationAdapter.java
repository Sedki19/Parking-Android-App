package com.citypark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private ArrayList<Reservation> reservationList;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("parkings");

    public ReservationAdapter(Context context, ArrayList<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        database.child(reservation.getParkingId()).get().addOnSuccessListener(snapshot -> {
            Parking parking = snapshot.getValue(Parking.class);
            holder.parkingName.setText("Parking : " + parking.getName());
        });

        holder.reservationDate.setText("Date: " + reservation.getDate());
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView parkingName, reservationDate;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingName = itemView.findViewById(R.id.parkingName);
            reservationDate = itemView.findViewById(R.id.reservationDate);
        }
    }
}
