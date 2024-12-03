package com.citypark;

public class Reservation {
    private String parkingId, userId, date;

    public Reservation() {} // Default constructor for Firebase

    public Reservation(String parkingId, String userId, String date) {
        this.parkingId = parkingId;
        this.userId = userId;
        this.date = date;
    }

    public String getParkingId() { return parkingId; }
    public String getUserId() { return userId; }
    public String getDate() { return date; }
}
