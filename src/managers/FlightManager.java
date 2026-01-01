package managers;

import FlightManagementModule.Flight;
import service.IFlightService;
import java.util.ArrayList;
import java.util.List;

public class FlightManager implements IFlightService {
    private List<Flight> flights = new ArrayList<>();

    @Override
    public void showDestinationCityFlights(String currentCity, String destinationCity) {
        for(Flight f : flights) {
            if(f.getArrivalPlace().equalsIgnoreCase(destinationCity) && f.getDeparturePlace().equalsIgnoreCase(currentCity)) {

            }
        }
    }
    @Override
    public void addFlight(Flight flight) {
        flights.add(flight);
    }
    @Override
    public void deleteFlight(int flightNum) {
        flights.removeIf(f -> f.getFlightNum() == flightNum);
    }
    @Override
    public boolean saveFlightsToFile() {
        for(Flight f : flights) {

        }
    }
}
