package com.airline.facade;

import com.airline.services.*;
import com.airline.models.*;
import java.util.List;

public class AirlineFacade {
    private FlightManager flightManager = FlightManager.getInstance();
    private SeatManager seatManager = SeatManager.getInstance();
    private ReservationManager reservationManager = ReservationManager.getInstance();

    // GUI'den gelen uçuş arama isteği
    public List<Flight> getAvailableFlights() {
        return flightManager.getAllFlights();
    }

    // Rezervasyon işlemi (Multithreading burada tetiklenebilir)
    public void bookSeat(Flight flight, String seatNum, boolean sync) {
        reservationManager.reserveRandomSeat(flight.getPlane().getSeatMatrix(), sync);
    }
}