package com.airline.services;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import com.airline.models.Flight;
import com.airline.models.Reservation;
import com.airline.models.Seat;
import com.airline.persistence.FlightDAOImpl;
import com.airline.persistence.ReservationDAOImpl;
import com.airline.persistence.SeatDAOImpl;
import com.airline.services.implementations.IFlightService;

public class FlightManager implements IFlightService {
    private static FlightManager instance;
    private FlightDAOImpl flightDao;

    private FlightManager() {
        this.flightDao = new FlightDAOImpl();
    }

    public static FlightManager getInstance() {
        if (instance == null) instance = new FlightManager();
        return instance;
    }

    @Override
    public List<Flight> getAvailableFlights(String departure, String arrival) {
        List<Flight> allFlights = flightDao.getAll();
        List<Flight> filtered = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Flight f : allFlights) {
            // f.getDeparturePlace() artık Route nesnesinden veriyi çekiyor
            if (f.getDeparturePlace().equalsIgnoreCase(departure) &&
                    f.getArrivalPlace().equalsIgnoreCase(arrival)) {
                if (isFlightInFuture(f, now)) {
                    filtered.add(f);
                }
            }
        }
        return filtered;
    }

    private boolean isFlightInFuture(Flight f, LocalDateTime now) {
        try {
            LocalDate fDate = LocalDate.parse(f.getDate());
            LocalTime fTime = LocalTime.of((int)f.getHour(), 0);
            return LocalDateTime.of(fDate, fTime).isAfter(now);
        } catch (Exception e) {
            return true; // Format hatası varsa risk alma, göster
        }
    }

    @Override
    public Flight findFlight(int flightNum) {
        return flightDao.findById(flightNum);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightDao.getAll();
    }

    @Override
    public void createAndSaveFlight(int num, String dep, String arr, String date, float hour, float dur) {
        // Temiz Mimaride uçağı ve rotayı kurarak Flight nesnesini oluşturuyoruz
        Flight newFlight = new Flight(num, dep, arr, date);
        newFlight.setHour(hour);
        newFlight.setDuration(dur);

        // Route zaten constructor içinde oluşturuluyor, gerekirse ekstra set edilebilir
        flightDao.save(newFlight);
    }

    @Override
    public void deleteFlightWithIntegrity(int flightNum) {
        String flightIdStr = String.valueOf(flightNum);
        String planeId = "PLANE-" + flightNum;

        // 1. Rezervasyonları Temizle (Toplu Silme)
        ReservationDAOImpl resDao = new ReservationDAOImpl();
        List<Reservation> remainingReservations = resDao.getAll().stream()
                .filter(r -> !r.getFlightNum().equals(flightIdStr))
                .toList();
        resDao.saveAll(remainingReservations); // AbstractCSVDAO'ya eklediğimiz yeni metot

        // 2. Koltukları Temizle (Toplu Silme)
        SeatDAOImpl seatDao = new SeatDAOImpl();
        List<Seat> remainingSeats = seatDao.getAll().stream()
                .filter(s -> !s.getPlaneID().equals(planeId))
                .toList();
        seatDao.saveAll(remainingSeats);

        // 3. Uçuşun Kendisini Sil
        flightDao.delete(flightNum); //
    }

    @Override
    public String[] getUniqueCities() {
        return getAllFlights().stream()
                .flatMap(f -> java.util.stream.Stream.of(f.getDeparturePlace(), f.getArrivalPlace()))
                .distinct() // Şehirlerin tekrarlanmasını önler
                .toArray(size -> new String[size]); // Doğru dizi oluşturma biçimi
    }
}