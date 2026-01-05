package service;

import FlightManagementModule.Flight;

import java.util.List;

public interface IFlightService {
    public void addFlight(Flight flight);
    public void deleteFlight(int flightNum);
    public boolean saveFlightsToFile();

    public List<Flight> getDestinationCityFlights(String currentCity, String destinationCity, String date);
}
