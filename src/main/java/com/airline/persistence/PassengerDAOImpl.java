package com.airline.persistence;

import com.airline.core.dao.AbstractCSVDAO;
import com.airline.models.Passenger;

public class PassengerDAOImpl extends AbstractCSVDAO<Passenger, String> {
    public PassengerDAOImpl() {
        super("passengers.csv");
    }

    @Override
    protected Passenger createInstance() {
        return new Passenger();
    }
}

