package com.airline.persistence;

import com.airline.core.dao.AbstractCSVDAO;
import com.airline.models.Reservation;

public class ReservationDAOImpl extends AbstractCSVDAO<Reservation, String> {
    public ReservationDAOImpl() {
        super("reservations.csv");
    }

    @Override
    protected Reservation createInstance() {
        return new Reservation();
    }
}
