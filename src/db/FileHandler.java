package db;

import FlightManagementModule.*;
import ReservationTicketingModule.Passenger;
import ReservationTicketingModule.Reservation;

import java.io.*;
import java.nio.Buffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {

    public List<Plane> getPlanesFromFile() throws IOException, NumberFormatException {
        List<Plane> planeList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader((new FileReader("planes.csv")))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String data[] = line.split(",");

                String planeID = data[0];
                String modelName = data[1];
                int rows = Integer.parseInt(data[2]);
                int cols = Integer.parseInt(data[3]);
                float regPrice = Float.parseFloat(data[4]);

                PlaneModel model = new PlaneModel(modelName, rows, cols);
                Plane plane = new Plane(model, planeID, regPrice);
                planeList.add(plane);
            }
        }
        return planeList;
    }

    public void savePlanesToFile(List<Plane> planeList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter("planes.csv")))) {
            for (Plane p : planeList) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        }
        // Hata oluşursa IOException fırlatılır ve üst katmana iletilir.
    }


    public void saveFlightsToFile(List<Flight> flightList) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("flights.csv"))) {
            for (Flight flight : flightList) {
                writer.write(flight.toCSV());
                writer.newLine();
            }
        }
    }

    public List<Flight> getFlightsFromFile(List<Plane> availablePlanes) throws IOException, NumberFormatException {
        List<Flight> fligthList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("flights.csv"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Verileri parse ederek listeye yerleştirdik:
                String[] data = line.split(",");

                int fNum = Integer.parseInt(data[0]);
                String dep = data[1];
                String arr = data[2];
                String date = data[3];
                float hour = Float.parseFloat(data[4]);
                float dur = Float.parseFloat(data[5]);
                String planeID = data[6];

                Plane flightPlane = null;
                int i = 0;
                while (i < availablePlanes.size() && flightPlane == null) {
                    if (availablePlanes.get(i).getPlaneID().equalsIgnoreCase(planeID)){
                        flightPlane = availablePlanes.get(i);
                    }
                    i++;
                }

                if (flightPlane != null) {
                    Flight f = new Flight(fNum, dep, arr, date, hour, dur, flightPlane, "Direct");
                    fligthList.add(f);
                }
            }
        }

        /**
         * yukarıda herhangi bir catch bloğu yok.
         * çünkü bu terminal dostu olsa da gui dostu değildir.
         * Bunun yerine yukarıdaki yapıyı aşağıdaki şekilde kullanacağız.
         *
         * Terminal tarafında:
         * try {
         *     List<Flight> flights = fileHandler.readFlightsFromFile(planes);
         *     System.out.println("Uçuşlar başarıyla yüklendi.");
         * } catch (Exception e) {
         *     System.out.println("HATA: Dosya okunamadı! -> " + e.getMessage());
         * }
         *
         *  UI tarafında:
         *  try {
         *     List<Flight> flights = fileHandler.readFlightsFromFile(planes);
         * } catch (Exception e) {
         *     // Kullanıcıya görsel bir hata penceresi gösterir
         *     JOptionPane.showMessageDialog(null, "Veriler yüklenirken hata oluştu: " + e.getMessage(),
         *                                  "Dosya Hatası", JOptionPane.ERROR_MESSAGE);
         * }
         */

        return fligthList;
    }


    public void saveReservationsToFile(List<Reservation> reservationList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter("reservations.csv")))) {
            for (Reservation r : reservationList) {
                writer.write(r.toCSV());
                writer.newLine();
            }
        }
    }

    public List<Reservation> getReservationsFromFile(List<Flight> availableFlights, List<Passenger> passengers) throws IOException {
        List<Reservation> reservationList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("reservations.csv"))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                String reservationCode = data[0];
                String passID = data[1];
                int flightNum = Integer.parseInt(data[2]);
                String seatNum = data[3];
                String date = data[4];

                Passenger foundPassenger = null;
                int i = 0;
                while (i < passengers.size() && foundPassenger == null) {
                    if (passengers.get(i).getPassengerID().equals(passID)) {
                        foundPassenger = passengers.get(i);
                        i++;
                    }
                }

                Flight foundFlight = null;
                int ii = 0;
                while (ii < availableFlights.size() && foundFlight == null) {
                    if (availableFlights.get(i).getFlightNum() == flightNum) {
                        foundFlight = availableFlights.get(i);
                        ii++;
                    }
                }

                if (foundPassenger != null && foundFlight != null) {
                    Seat foundSeat = null;
                    Seat[][] matrix = foundFlight.getPlane().getSeatMatrix();
                    for (int r = 0; r <matrix.length && foundSeat == null; r++) {
                       for (int c = 0; c < matrix.length && foundSeat ==null; c++) {
                           if (matrix[r][c].getSeatNum().equalsIgnoreCase(seatNum)) {
                               foundSeat = matrix[r][c];
                               foundSeat.setReserveStatus(true);
                           }
                       }
                    }

                    if (foundSeat != null) {
                        Reservation r = new Reservation(reservationCode, foundFlight, foundPassenger, foundSeat, date);
                        reservationList.add(r);
                    }

                }
            }
        }
        return reservationList;
    }

}
