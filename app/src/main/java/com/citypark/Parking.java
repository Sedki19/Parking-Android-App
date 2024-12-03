package com.citypark;

import java.io.Serializable;

public class Parking implements Serializable {
    private String id, name, address;
    private int available, capacity;
    private double price;
    private double latitude, longitude;

    public Parking() {} // Default constructor for Firebase

    public Parking(String id, String name, String address, int available, int capacity, double price) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.available = available;
        this.capacity = capacity;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getAvailable() { return available; }
    public int getCapacity() { return capacity; }
    public double getPrice() { return price; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
