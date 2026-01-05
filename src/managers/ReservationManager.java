package managers;

import service.IReservationService;
import FlightManagementModule.*;
import FlightManagementModule.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager implements IReservationService {
    public class ReservationManager {
        private List<Reservation> allReservations = new ArrayList<>();

        // SYNCHRONIZED: Bu metota aynı anda sadece 1 thread girebilir.
        // Bu sayede koltuk kapma yarışı güvenli hale gelir.
        public synchronized Reservation makeReservation(Flight flight, Passenger passenger, String seatNum) {
            // 1. Uçuşun koltuk matrisini al
            Seat[][] seats = flight.getPlane().getSeatMatrix();

            // 2. İstenen koltuğu bul ve durumuna bak
            for (Seat[] row : seats) {
                for (Seat seat : row) {
                    if (seat.getSeatNum().equals(seatNum) && !seat.isReserved()) {
                        // 3. Koltuk boşsa rezerve et
                        seat.setReserved(true);

                        // 4. Rezervasyon nesnesini oluştur
                        String pnr = "PNR" + (allReservations.size() + 1000);
                        Reservation newRes = new Reservation(pnr, flight, passenger, seat);

                        allReservations.add(newRes);
                        return newRes; // Başarılı!
                    }
                }
            }
            return null; // Koltuk doluysa veya bulunamadıysa null döner
        }
    }
}
