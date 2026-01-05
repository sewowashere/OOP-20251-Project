package service;

import FlightManagementModule.Flight;
import ReservationTicketingModule.Passenger;
import ReservationTicketingModule.Reservation;
import java.util.List

public interface IReservationService {
    public Reservation makeReservation(Flight flight, Passenger passenger, String seat);
    public boolean cancelReservation(String reservationCode);
    public List<Reservation> getAllReservations();
    public void saveReservationsToFile();
}
