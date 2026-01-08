package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class Seat implements CSVConvertible {
    private String seatNum; // "15A"
    private String planeID; // Hangi uçağa ait olduğu bilgisi (Foreign Key mantığı)
    private SeatClass seatClass; // Enum: ECONOMY, BUSINESS
    private double price;
    private boolean reserved;

    public enum SeatClass { ECONOMY, BUSINESS }

    public Seat() {}

    public Seat(String seatNum, String planeID, SeatClass seatClass, double price, boolean reserved) {
        this.seatNum = seatNum;
        this.planeID = planeID;
        this.seatClass = seatClass;
        this.price = price;
        this.reserved = reserved;
    }

    // Getters
    public String getSeatNum() {
        return seatNum;
    }
    public String getPlaneID() {
        return planeID;
    }
    public SeatClass getSeatClass() {
        return seatClass;
    }
    public double getPrice() {
        return price;
    }
    public boolean isReserved() {
        return reserved;
    }

    // Setters
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toCSV() {
        return seatNum + "," + planeID + "," + seatClass + "," + price + "," + reserved;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.seatNum = data[0];
        this.planeID = data[1];
        this.seatClass = SeatClass.valueOf(data[2]);
        this.price = Double.parseDouble(data[3]);
        this.reserved = Boolean.parseBoolean(data[4]);
    }

    @Override
    public String getId() {
        return planeID + "-" + seatNum; // Bileşik ID
    }
}