package com.airline.persistence;

import com.airline.core.dao.AbstractCSVDAO;
import com.airline.models.Reservation;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationDAOImpl extends AbstractCSVDAO<Reservation, String> {
    public ReservationDAOImpl() {
        super("reservations.csv");
    }

    @Override
    protected Reservation createInstance() {
        return new Reservation();
    }

    public List<Reservation> getReservationsByPassenger(String passengerID) {
        return getAll().stream()
                .filter(r -> r.getPassengerID().equals(passengerID))
                .collect(Collectors.toList());
    }

    public void deleteByFlightNum(String flightNum) {
        List<Reservation> all = getAll(); // Mevcut tüm rezervasyonları oku
        all.removeIf(r -> r.getFlightNum().equals(flightNum)); // Uçuşa ait olanları listeden at
        saveAll(all); // Kalanları dosyaya geri yaz (Dosyayı overwrite eder)
    }

    public List<Reservation> getReservationsByFlight(String flightNum) {
        return getAll().stream()
                .filter(r -> r.getFlightNum().equals(flightNum))
                .collect(Collectors.toList());
    }
}