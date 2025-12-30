package FlightManagementModule;

import java.util.HashMap;
import java.util.Map;

public class Plane {
    private final int capacity;
    private final String planeID;
    private PlaneModel planeModel;
    private final Seat[][] seatMatrix;

    public Plane (int capacity, String planeID, String planeModel, Seat[][] seatMatrix, int row, int column){
        this.capacity = row * column;
        this.planeID = planeID;
        this.seatMatrix = new Seat[row][column];
        super
    }

    private int getCapacity() {
        return this.capacity;
    }

    private String getPlaneID() {
        return this.planeID;
    }

    private Seat[][] getSeatMatrix() {
        return this.seatMatrix;
    }
    // Bunun setterı olmalı mı?


    




}
