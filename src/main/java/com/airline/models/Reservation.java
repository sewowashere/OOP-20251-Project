package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class Reservation implements CSVConvertible {
    private String reservationCode;
    private String flightNum;      // Flight nesnesinin ID'si
    private String passengerID;    // Passenger nesnesinin ID'si
    private String seatNum;
    private String dateOfReservation;
    private Ticket ticket;         // Bilet detaylarını içinde barındırır

    public Reservation() {}

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public void setDateOfReservation(String dateOfReservation) {
        this.dateOfReservation = dateOfReservation;
    }
    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }
    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }
    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    @Override
    public String toCSV() {
        // Sondaki fiyattan sonra bagaj ağırlığını da ekliyoruz
        return String.join(",", reservationCode, flightNum, passengerID,
                seatNum, dateOfReservation, String.valueOf(ticket.getPrice()),
                String.valueOf(ticket.getBaggage().getWeight())); // EKLEDİK
    }

    @Override
    public void fromCSV(String row) {
        String[] d = row.split(",");
        this.reservationCode = d[0];
        this.flightNum = d[1];
        this.passengerID = d[2];
        this.seatNum = d[3];
        this.dateOfReservation = d[4];

        // Dosyadan okunan ağırlıkla (d[6]) bilet nesnesini oluşturuyoruz
        double price = Double.parseDouble(d[5]);
        double weight = (d.length > 6) ? Double.parseDouble(d[6]) : 20.0; // EKLEDİK
        this.ticket = new Ticket("T-" + d[0], price, new Baggage(weight));
    }

    @Override public String getId() { return reservationCode; }

    // Reservation kurucu metodunda Ticket ve Baggage otomatik oluşturulabilir

    public String getReservationCode() {
        return reservationCode;
    }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public String getFlightNum() { return flightNum; }
    public String getPassengerID() { return passengerID; }
    public String getSeatNum() {
        return seatNum;
    }

    public String getDateOfReservation() {
        return this.dateOfReservation;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
