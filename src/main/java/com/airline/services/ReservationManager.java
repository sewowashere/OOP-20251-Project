package com.airline.services;

import java.util.Random;

import com.airline.models.Seat;

public class ReservationManager {
    private static ReservationManager instance;
    private final Random random = new Random();

    private ReservationManager() {}

    public static ReservationManager getInstance() {
        if (instance == null) instance = new ReservationManager();
        return instance;
    }

    /**
     * Senaryo 1: Koltuk Rezervasyonu
     * isSynchronized: GUI'deki checkbox'tan gelir.
     */
    public void reserveRandomSeat(Seat[][] matrix, boolean isSynchronized) {
        if (isSynchronized) {
            reserveWithSync(matrix);
        } else {
            reserveWithoutSync(matrix);
        }
    }

    // Thread-Safe olmayan yöntem (Hatalı sonuçlar üretir)
    private void reserveWithoutSync(Seat[][] matrix) {
        Seat seat = findRandomEmptySeat(matrix);
        if (seat != null) {
            // Yapay gecikme ekleyerek race condition ihtimalini artırıyoruz
            simulateDelay(); 
            seat.setReserved(true);
        }
    }

    // Thread-Safe yöntem (Doğru sonuç üretir)
    /*private synchronized void reserveWithSync(Seat[][] matrix) {
        Seat seat = findRandomEmptySeat(matrix);
        if (seat != null) {
            seat.setReserved(true);
        }
    }*/
    
 // ReservationManager içindeki düzeltilmiş metot
    private synchronized void reserveWithSync(Seat[][] matrix) {
        boolean seated = false;
        while (!seated) { // Boş koltuk bulana kadar vazgeçme!
            int r = random.nextInt(matrix.length);
            int c = random.nextInt(matrix[0].length);
            
            if (!matrix[r][c].isReserved()) {
                matrix[r][c].setReserved(true);
                seated = true; // Başarıyla oturdu
            }
        }
    }

    private Seat findRandomEmptySeat(Seat[][] matrix) {
        int r = random.nextInt(matrix.length);
        int c = random.nextInt(matrix[0].length);
        if (!matrix[r][c].isReserved()) {
            return matrix[r][c];
        }
        return null; // Eğer doluysa bu thread pas geçer (Senaryo gereği)
    }

    private void simulateDelay() {
        try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}

