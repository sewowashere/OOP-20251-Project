package dao;

import ReservationTicketingModule.Passenger;

public class PassengerDAOImpl extends AbstractCSVDAO<Passenger, String> {
    public PassengerDAOImpl() {
        super("passengers.csv");
    }

    @Override
    protected Passenger createInstance() {
        return new Passenger();
    }
}

