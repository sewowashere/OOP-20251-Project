package com.airline.services;

import com.airline.models.Seat;
import com.airline.persistence.SeatDAOImpl;
import com.airline.services.implementations.ISeatService;
import java.util.List;

public class SeatManager implements ISeatService {
    private static SeatManager instance;
    private SeatDAOImpl seatDao;

    private SeatManager() {
        this.seatDao = new SeatDAOImpl();
    }

    public static SeatManager getInstance() {
        if (instance == null) {
            instance = new SeatManager();
        }
        return instance;
    }

    @Override
    public Seat[][] getSeatsForFlight(int flightNum) {
        String planeID = "PLANE-" + flightNum;
        List<Seat> seatsFromDb = seatDao.getSeatsByPlane(planeID);

        if (seatsFromDb.isEmpty()) {
            Seat[][] newLayout = createSeatingArrangement(planeID, 180);
            // İlk kez oluşturuluyorsa dosyaya kaydet
            for (Seat[] row : newLayout) {
                for (Seat s : row) seatDao.save(s);
            }
            return newLayout;
        }
        return convertListToMatrix(seatsFromDb);
    }

    private Seat[][] convertListToMatrix(List<Seat> seatList) {
        Seat[][] matrix = new Seat[30][6];
        for (Seat s : seatList) {
            int row = Integer.parseInt(s.getSeatNum().replaceAll("[^0-9]", "")) - 1;
            int col = s.getSeatNum().replaceAll("[0-9]", "").toUpperCase().charAt(0) - 'A';
            if (row >= 0 && row < 30 && col >= 0 && col < 6) matrix[row][col] = s;
        }
        return matrix;
    }

    @Override
    public void reserveSeat(Seat[][] matrix, String seatNum) {
        for (Seat[] row : matrix) {
            for (Seat seat : row) {
                if (seat.getSeatNum().equalsIgnoreCase(seatNum)) {
                    if (seat.isReserved()) throw new IllegalStateException("Reserved!");
                    seat.setReserved(true);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Not found!");
    }

    @Override
    public Seat[][] createSeatingArrangement(String planeID, int capacity) {
        Seat[][] seatMatrix = new Seat[30][6];
        char[] columns = {'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 6; j++) {
                String sNum = (i + 1) + String.valueOf(columns[j]);

                // İlk 6 sıra Business (0, 1, 2, 3, 4, 5. indeksler)
                Seat.SeatClass sClass = (i < 6) ? Seat.SeatClass.BUSINESS : Seat.SeatClass.ECONOMY;

                // Base fiyatlar (CalculatePrice bu fiyatları çarpanla büyütecek)
                double basePrice = (sClass == Seat.SeatClass.BUSINESS) ? 1000.0 : 400.0;

                seatMatrix[i][j] = new Seat(sNum, planeID, sClass, basePrice, false);
            }
        }
        return seatMatrix;
    }

    @Override
    public int reservedSeatsCount(Seat[][] matrix) {
        int count = 0;
        for (Seat[] row : matrix) {
            for (Seat seat : row) {
                if (seat.isReserved()) {
                    count++;
                }
            }
        }
        return count;
    }

}