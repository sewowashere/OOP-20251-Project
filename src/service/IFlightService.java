package service;

import FlightManagementModule.Flight;

public interface IFlightService {
    public void addFlight(Flight flight);
    public void deleteFlight(int flightNum);
    public boolean saveFlightsToFile();

    public void showDestinationCityFlights(String currentCity, String destinationCity);
}
