package ReservationTicketingModule;

public abstract class Ticket {
    protected final String ticketID;
    protected Reservation reservationInf;
    protected float price;
    protected boolean baggageAllowance;

    public Ticket(String ticketID, Reservation reservationInf, float price, boolean baggageAllowance) {
        this.ticketID = ticketID;
        this.reservationInf = reservationInf;
        this.price = price;
        this.baggageAllowance = baggageAllowance;
    }

    // Common Getters
    public float getPrice() {
        return this.price;
    }
    public boolean getBaggageAllowance() {
        return this.baggageAllowance;
    }
    public Reservation getReservationInf() {
        return reservationInf;
    }
}
