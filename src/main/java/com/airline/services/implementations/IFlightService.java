package com.airline.services.implementations;

import com.airline.models.Flight;
import com.airline.services.pricing.PricingStrategy;

//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.util.List;

public interface IFlightService {
    public List<Flight> getAvailableFlights(String departure, String arrival);
    public Flight findFlight(int flightNum);
    public List<Flight> getAllFlights();
    public void createAndSaveFlight(int num, String dep, String arr, String date, float hour, float dur);
    public void deleteFlightWithIntegrity(int flightNum);
    public String[] getUniqueCities();
}
