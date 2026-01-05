package managers;

import FlightManagementModule.Flight;
import service.IFlightService;
import java.util.ArrayList;
import java.util.List;

public class FlightManager implements IFlightService {
    private List<Flight> flights = new ArrayList<>();

    @Override
    public List<Flight> getDestinationCityFlights(String currentCity, String destinationCity, String date) {
        List<Flight> foundFlights = new ArrayList<>();
        for (Flight f : flights) {
            // Şehirler ve Tarih aynı anda eşleşmeli
            if (f.getArrivalPlace().equalsIgnoreCase(destinationCity) &&
                    f.getDeparturePlace().equalsIgnoreCase(currentCity) &&
                    f.getDate().equals(date)) { // String tarih karşılaştırması

                foundFlights.add(f);
            }
        }
        return foundFlights;
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
