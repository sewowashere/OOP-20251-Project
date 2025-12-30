package FlightManagementModule;

import java.util.HashMap;
import java.util.Map;

public class Plane {
    private int capacity;
    private String planeID;
    // seatMatrix - 2D array or Map
    // Map seçtiğim için yapısı şu şekilde olacak;
    // Key = "1A"
    // Value = {seatNum="1A", Class="BUSINESS", price=500, reserveStatus=false}
    // put, get, remove
    Map<String, Seat> seatMatrix;

    public Plane (int capacity, String planeID, Map seatMatrix){
        this.capacity=capacity;
        this.planeID=planeID;
        this.seatMatrix = new HashMap<>();
    }

    




}
