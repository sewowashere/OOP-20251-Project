package ReservationTicketingModule;

import dao.CSVConvertible;

public class Reservation implements CSVConvertible {
    private String reservationCode;
    private String flightNum;      // Flight nesnesinin ID'si
    private String passengerID;    // Passenger nesnesinin ID'si
    private String seatNum;
    private String dateOfReservation;
    private Ticket ticket;         // Bilet detaylarını içinde barındırır

    public Reservation() {}

    @Override
    public String toCSV() {
        return String.join(",", reservationCode, flightNum, passengerID, 
                seatNum, dateOfReservation, String.valueOf(ticket.getPrice()));
    }

    @Override
    public void fromCSV(String row) {
        String[] d = row.split(",");
        this.reservationCode = d[0];
        this.flightNum = d[1];
        this.passengerID = d[2];
        this.seatNum = d[3];
        this.dateOfReservation = d[4];
        // Bilet nesnesini basitçe fiyatla tekrar oluşturuyoruz
        this.ticket = new Ticket("T-" + d[0], Double.parseDouble(d[5]), new Baggage(20.0));
    }

    @Override public String getId() { return reservationCode; }
    
    // Reservation kurucu metodunda Ticket ve Baggage otomatik oluşturulabilir
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public String getFlightNum() { return flightNum; }
    public String getPassengerID() { return passengerID; }
}
