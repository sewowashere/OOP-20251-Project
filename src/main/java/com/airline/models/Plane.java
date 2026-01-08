package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class Plane implements CSVConvertible {
    private int capacity;
    private String planeID;
    private Seat[][] seatMatrix;

    public Plane() {
        this.capacity = 0;
        this.planeID = "";
        this.seatMatrix = null;
    }

    public Plane(String planeID, int capacity) {
        this.planeID = planeID;

        this.capacity = capacity;
        this.seatMatrix = null;
    }

    public String getPlaneID() {
        return this.planeID;
    }

    @Override
    public String toCSV() {
        return planeID + "," + capacity;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        this.planeID = data[0];
        this.capacity = Integer.parseInt(data[2]);
    }

    @Override
    public String getId() {
        return planeID;
    }
}
