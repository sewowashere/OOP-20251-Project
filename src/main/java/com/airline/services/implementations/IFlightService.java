package com.airline.services.implementations;

import com.airline.models.Flight;
import com.airline.services.pricing.PricingStrategy;

import java.util.List;

public interface IFlightService {
    public void addFlight(Flight flight);
    public void deleteFlight(int flightNum);
    public boolean saveFlightsToFile();

    public List<Flight> getDestinationCityFlights(String currentCity, String destinationCity, String date);
}
