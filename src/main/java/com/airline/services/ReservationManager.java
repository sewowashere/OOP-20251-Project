package com.airline.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.List;
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
     * SENARYO 1: Thread Yönetimi ve Concurrency
     * AdminGUI'deki simülasyon bu metodu çağırır.
     */
    @Override
    public void reserveRandomSeat(Seat[][] matrix, boolean isSynchronized) {
        if (isSynchronized) {
            reserveWithSync(matrix);
        } else {
            reserveWithoutSync(matrix);
        }
    }

    // THREAD-SAFE (Synchronized): Yarış durumunu (Race Condition) önler.
    private synchronized void reserveWithSync(Seat[][] matrix) {
        performRandomSelection(matrix);
    }

    // THREAD-SAFE OLMAYAN: Race condition oluşmasına izin verir.
    private void reserveWithoutSync(Seat[][] matrix) {
        performRandomSelection(matrix);
    }

    private void performRandomSelection(Seat[][] matrix) {
        boolean seated = false;
        while (!seated) {
            int r = random.nextInt(matrix.length);
            int c = random.nextInt(matrix[0].length);
            Seat seat = matrix[r][c];

            if (!seat.isReserved()) {
                simulateDelay(); // Race condition'ı gözle görülür kılmak için
                seat.setReserved(true);
                seated = true;
            }
        }
    }

    private void simulateDelay() {
        try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * Admin bir uçuşu sildiğinde çağrılır.
     * Veritabanındaki tüm ilişkili rezervasyonları temizler.
     */
    public void cancelAllReservationsByFlight(String flightNum) {
        // DAO üzerinden hem bellekten hem dosyadan (CSV) siler
        reservationDao.deleteByFlightNum(flightNum);
    }

    public Reservation createReservation(String flightNum, String passengerID, String seatNum, double weight) {
        String planeID = "PLANE-" + flightNum;
        Seat seat = seatDao.findById(planeID + "-" + seatNum);

        if (seat == null || seat.isReserved()) {
            throw new IllegalStateException("Seat is not available!");
        }

        // --- DİNAMİK FİYATLANDIRMA ---
        com.airline.services.pricing.CalculatePrice priceCalculator = new com.airline.services.pricing.CalculatePrice();
        double finalPrice = priceCalculator.getFinalPrice(seat.getPrice(), seat.getSeatClass());

        String reservationCode = generateReservationCode();
        String dateOfReservation = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Ticket ticket = new Ticket("T-" + reservationCode, finalPrice, new Baggage(weight));

        Reservation reservation = new Reservation();
        reservation.setReservationCode(reservationCode);
        reservation.setFlightNum(flightNum);
        reservation.setPassengerID(passengerID);
        reservation.setSeatNum(seatNum);
        reservation.setDateOfReservation(dateOfReservation);
        reservation.setTicket(ticket);

        seat.setReserved(true);
        seatDao.update(seat); // Koltuğu rezerve et
        reservationDao.save(reservation); // Rezervasyonu kaydet

        return reservation;
    }

    public boolean cancelReservation(String reservationCode) {
        Reservation reservation = reservationDao.findById(reservationCode);
        if (reservation == null) return false;

        // Koltuğu tekrar boşa çıkar
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