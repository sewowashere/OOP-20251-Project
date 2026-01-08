package com.airline.services.implementations;

import com.airline.models.Seat;

public interface ISeatService {
    public Seat[][] getSeatsForFlight(int flightNum);
    public void reserveSeat(Seat[][] matrix, String seatNum);
    public Seat[][] createSeatingArrangement(String planeID, int capacity);
    public int reservedSeatsCount(Seat[][] matrix);
}
