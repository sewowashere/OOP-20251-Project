package managers;



import java.util.ArrayList;
import java.util.List;

import FlightManagementModule.Seat;

public class SeatManager {
    private static SeatManager instance;

    private SeatManager() {}

    public static SeatManager getInstance() {
        if (instance == null) {
            instance = new SeatManager();
        }
        return instance;
    }

    /**
     * Verilen uçağın kapasitesine göre koltuk haritasını oluşturur.
     * Senin Seat sınıfındaki constructor'a uygun parametreler gönderir.
     */
    public Seat[][] createSeatingArrangement(String planeID, int capacity) {
        int rows = capacity / 5; // Her sırada 5 koltuk (A,B,C,D,E)
        Seat[][] seatMatrix = new Seat[rows][5];

        for (int i = 0; i < rows; i++) {
            char[] columns = {'A', 'B', 'C', 'D', 'E'};
            for (int j = 0; j < 5; j++) {
                String seatNum = (i + 1) + String.valueOf(columns[j]);
                
                // İlk 2 sırayı BUSINESS, kalanları ECONOMY yapar
                Seat.SeatClass sClass = (i < 2) ? Seat.SeatClass.BUSINESS : Seat.SeatClass.ECONOMY;
                double initialPrice = (sClass == Seat.SeatClass.BUSINESS) ? 2000.0 : 800.0;
                
                seatMatrix[i][j] = new Seat(seatNum, planeID, sClass, initialPrice, false);
            }
        }
        return seatMatrix;
    }

    /**
     * Müsait koltuk sayısını hesaplar (Flight sınıfındaki yardımcı metodu destekler)
     */
    public int countAvailableSeats(Seat[][] matrix) {
        int count = 0;
        for (Seat[] row : matrix) {
            for (Seat seat : row) {
                if (!seat.isReserved()) count++;
            }
        }
        return count;
    }
}