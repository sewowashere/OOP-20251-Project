package com.airline.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import com.airline.models.Baggage;
import com.airline.models.Reservation;
import com.airline.models.Seat;
import com.airline.models.Ticket;
import com.airline.persistence.ReservationDAOImpl;
import com.airline.persistence.SeatDAOImpl;
import com.airline.services.implementations.IReservationService;

public class ReservationManager implements IReservationService {
    private static ReservationManager instance;
    private final Random random = new Random();
    private ReservationDAOImpl reservationDao;
    private SeatDAOImpl seatDao;

    private ReservationManager() {
        this.reservationDao = new ReservationDAOImpl();
        this.seatDao = new SeatDAOImpl();
    }

    public static ReservationManager getInstance() {
        if (instance == null) instance = new ReservationManager();
        return instance;
    }

    /**
     * SENARYO 1: Proje gereksinimlerine göre thread yönetimi
     * 90 yolcu thread'i bu metodu çağırır.
     */
    @Override
    public void reserveRandomSeat(Seat[][] matrix, boolean isSynchronized) {
        if (isSynchronized) {
            reserveWithSync(matrix);
        } else {
            reserveWithoutSync(matrix);
        }
    }

    //THREAD-SAFE (Synchronized): 90 koltuk dolar, 90 koltuk boş kalır.
    private synchronized void reserveWithSync(Seat[][] matrix) {
        performRandomSelection(matrix);
    }

    //THREAD-SAFE OLMAYAN: Race condition nedeniyle hatalı yerleşim oluşur.
    private void reserveWithoutSync(Seat[][] matrix) {
        performRandomSelection(matrix);
    }


    //Ortak Seçim Mantığı: Yolcu rastgele boş bir koltuk bulana kadar dener.
    private void performRandomSelection(Seat[][] matrix) {
        boolean seated = false;
        while (!seated) {
            // Rastgele koltuk seçimi
            int r = random.nextInt(matrix.length);
            int c = random.nextInt(matrix[0].length);
            Seat seat = matrix[r][c];

            if (!seat.isReserved()) {
                // Senkronize olmayan modda race condition'ı tetiklemek için gecikme
                simulateDelay();

                // İki thread aynı anda buraya girerse ikisi de aynı koltuğu rezerve eder
                seat.setReserved(true);
                seated = true;
            }
        }
    }

    private void simulateDelay() {
        try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
    }


    public Reservation createReservation(String flightNum, String passengerID, String seatNum, double weight) {
        String planeID = "PLANE-" + flightNum;
        Seat seat = seatDao.findById(planeID + "-" + seatNum);

        if (seat == null || seat.isReserved()) {
            throw new IllegalStateException("Koltuk uygun değil!");
        }

        // --- DİNAMİK FİYATLANDIRMA ENTEGRASYONU ---
        com.airline.services.pricing.CalculatePrice priceCalculator = new com.airline.services.pricing.CalculatePrice();
        double finalPrice = priceCalculator.getFinalPrice(seat.getPrice(), seat.getSeatClass());
        // ------------------------------------------

        String reservationCode = generateReservationCode();
        String dateOfReservation = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Ticket nesnesine hesaplanan dinamik fiyatı veriyoruz
        Ticket ticket = new Ticket("T-" + reservationCode, finalPrice, new Baggage(weight));

        Reservation reservation = new Reservation();
        reservation.setReservationCode(reservationCode);
        reservation.setFlightNum(flightNum);
        reservation.setPassengerID(passengerID);
        reservation.setSeatNum(seatNum);
        reservation.setDateOfReservation(dateOfReservation);
        reservation.setTicket(ticket);

        seat.setReserved(true);
        seatDao.update(seat);
        reservationDao.save(reservation);

        return reservation;
    }

    public boolean cancelReservation(String reservationCode) {
        Reservation reservation = reservationDao.findById(reservationCode);
        if (reservation == null) return false;

        String planeID = "PLANE-" + reservation.getFlightNum();
        Seat seat = seatDao.findById(planeID + "-" + reservation.getSeatNum());

        if (seat != null) {
            seat.setReserved(false);
            seatDao.update(seat);
        }

        reservationDao.delete(reservationCode);
        return true;
    }

    private String generateReservationCode() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        int randomNum = random.nextInt(9999);
        return "RES" + timestamp.substring(timestamp.length() - 6) + String.format("%04d", randomNum);
    }
}