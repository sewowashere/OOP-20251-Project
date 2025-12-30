package ReservationTicketingModule;

public class EconomyTickets extends Ticket {

    public EconomyTickets(String ticketID, Reservation reservationInf, float price, boolean baggageAllowance) {
        super(ticketID, reservationInf, price, baggageAllowance);
    }
}
