package com.airline.services.implementations;

import java.util.List;

import com.airline.models.Flight;
import com.airline.models.Passenger;
import com.airline.models.Reservation;

public interface IReservationService {
    public Reservation makeReservation(Flight flight, Passenger passenger, String seat);
    public boolean cancelReservation(String reservationCode);
    public List<Reservation> getAllReservations();
    public void saveReservationsToFile();
}
