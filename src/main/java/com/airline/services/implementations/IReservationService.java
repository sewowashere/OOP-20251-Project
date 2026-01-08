package com.airline.services.implementations;

import com.airline.models.Reservation;
import com.airline.models.Seat;

public interface IReservationService {
    public void reserveRandomSeat(Seat[][] matrix, boolean isSynchronized);
    public Reservation createReservation(String flightNum, String passengerID, String seatNum, double weight);
    public boolean cancelReservation(String reservationCode);
}
