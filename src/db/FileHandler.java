package db;

import FlightManagementModule.Flight;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FileHandler {

    public void saveFlightsToFile(List<Flight> flightList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("flights.txt"))) {
            for (Flight flight : flightList) {
                writer.write(flight.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFlightsFromFile() {

    }

    public void getReservationsFromFile() {

    }

    public void saveReservationsToFile() {

    }


}
