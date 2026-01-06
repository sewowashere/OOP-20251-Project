package main;

import FlightManagementModule.*;
import managers.FlightManager;
import managers.SeatManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Havayolu Yönetim Sistemi Test Başlatıldı ===\n");

        // 1. Singleton Instance'ların Alınması
        FlightManager flightManager = FlightManager.getInstance();
        SeatManager seatManager = SeatManager.getInstance();

        // ---------------------------------------------------------
        // TEST 1: FlightManager & DAO (Dosya İşlemleri)
        // ---------------------------------------------------------
        System.out.println("--- Test 1: Uçuş İşlemleri ---");
       
        Flight f1 = new Flight(747, "Istanbul", "Tokyo", "2026-05-20");
        Flight f2 = new Flight(320, "Ankara", "Berlin", "2026-06-15");

        // Dosyaya kaydetme
        flightManager.createNewFlight(f1);
        flightManager.createNewFlight(f2);

        // Listeleme
        List<Flight> allFlights = flightManager.getAllFlights();
        System.out.println("Kayıtlı Uçuş Sayısı: " + allFlights.size());
        allFlights.forEach(f -> System.out.println("Uçuş Bilgisi: " + f));

        // Güncelleme (Tokyo uçuşunu Osaka yapalım)
        Flight updatedF1 = new Flight(747, "Istanbul", "Osaka", "2026-05-20");
        flightManager.updateFlight(updatedF1);
        
        Flight checkF1 = flightManager.findFlight(747);
        System.out.println("Güncelleme Sonrası Hedef: " + checkF1.getArrivalPlace());
        System.out.println();

        // ---------------------------------------------------------
        // TEST 2: SeatManager (Koltuk Matrisi ve Mantık)
        // ---------------------------------------------------------
        System.out.println("--- Test 2: Koltuk ve Kapasite İşlemleri ---");
        
        String testPlaneID = "TR-VEO-01";
        int capacity = 20; // 4 sıra x 5 koltuk
        
        // Matris Oluşturma
        Seat[][] matrix = seatManager.createSeatingArrangement(testPlaneID, capacity);
        
        System.out.println("Uçak ID: " + testPlaneID + " için koltuklar oluşturuldu.");
        System.out.println("İlk Koltuk: " + matrix[0][0].getSeatNum() + " (" + matrix[0][0].getSeatClass() + ")");
        System.out.println("Son Koltuk: " + matrix[3][4].getSeatNum() + " (" + matrix[3][4].getSeatClass() + ")");

        // Reserve islemi
        matrix[0][0].setReserved(true); 
        
        // Müsaitlik Hesaplama
        int available = seatManager.countAvailableSeats(matrix);
        System.out.println("Toplam Kapasite: " + capacity);
        System.out.println("Müsait Koltuk Sayısı: " + available + " (1 koltuk rezerve edildi)");

        // ---------------------------------------------------------
        // TEST 3: Flight ve Seat Entegrasyonu
        // ---------------------------------------------------------
        System.out.println("\n--- Test 3: Entegrasyon Kontrolü ---");
        // Uçuş nesnesine uçağı ve koltukları bağlayalım (Simülasyon)
        Plane plane = new Plane(); // Boş bir plane objesi varsayalım
        plane.setSeatMatrix(matrix);
        
        // Flight sınıfındaki getAvailableSeatsCount() metodunu test etme
        // Not: Flight sınıfında 'plane' alanının dolu olması gerekir.
        f1.setPlane(plane); 
        System.out.println("Flight nesnesi üzerinden müsaitlik: " + f1.getAvailableSeatsCount());

        System.out.println("\n=== Test Başarıyla Tamamlandı ===");
    }
}
