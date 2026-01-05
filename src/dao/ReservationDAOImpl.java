package dao;

import ReservationTicketingModule.Reservation;

public class ReservationDAOImpl extends AbstractCSVDAO<Reservation, String> {
    public ReservationDAOImpl() {
        super("reservations.csv");
    }

    @Override
    protected Reservation createInstance() {
        return new Reservation();
    }
}
