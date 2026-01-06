package com.airline.services;


import java.util.List;

import com.airline.models.Flight;
import com.airline.persistence.FlightDAOImpl;

public class FlightManager {
    private static FlightManager instance;
    private FlightDAOImpl flightDao;

    private FlightManager() {
        this.flightDao = new FlightDAOImpl();
    }

    public static FlightManager getInstance() {
        if (instance == null) {
            instance = new FlightManager();
        }
        return instance;
    }

    // Yeni Uçuş Ekleme
    public void createNewFlight(Flight flight) {
        if (flightDao.findById(flight.getFlightNum()) == null) {
            flightDao.save(flight);
        } else {
            System.out.println("Hata: Uçuş zaten mevcut: " + flight.getFlightNum());
        }
    }

    // Uçuş Güncelleme
    public void updateFlight(Flight flight) {
        flightDao.update(flight);
    }

    // Uçuş Silme
    public void removeFlight(int flightNum) {
        flightDao.delete(flightNum);
    }

    // Tüm Uçuşları Listeleme
    public List<Flight> getAllFlights() {
        return flightDao.getAll();
    }

    // Tekil Uçuş Sorgulama
    public Flight findFlight(int flightNum) {
        return flightDao.findById(flightNum);
    }
}
