package ReservationTicketingModule;

import FlightManagementModule.Flight;
import FlightManagementModule.Seat;
import java.time.LocalDate;

public class Reservation {
    private String reservationCode;
    private Flight flightInf;
    private Passenger passengerInf;
    private Seat seatInf;
    private String dateOfReservation;

    public Reservation(String reservationCode, Flight flight, Passenger passenger, Seat seat, String dateOfReservation) {
        this.reservationCode  =reservationCode;
        this.flightInf = flight;
        this.passengerInf = passenger;
        this.seatInf = seat;
        this.dateOfReservation = dateOfReservation;
    }

    public boolean isExpired() {
        LocalDate flightDate = LocalDate.parse(this.flightInf.getDate());
        return flightDate.isBefore(LocalDate.now());
    }

    // ?
    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    /**
     * Getters
     */
    public String getReservationCode() {
        return reservationCode;
    }
    public Flight getFlightInf() {
        return flightInf;
    }
    public Passenger getPassengerInf() {
        return passengerInf;
    }
    public Seat getSeatInf() {
        return seatInf;
    }

    public String toCSV() {
        return reservationCode + "," +
               passengerInf.getPassengerID() + "," +
               flightInf.getFlightNum() + "," +
               seatInf.getSeatNum() + "," +
               dateOfReservation;
    }
}
