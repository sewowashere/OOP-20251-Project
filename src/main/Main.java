package main;

import FlightManagementModule.Flight;
import dao.FlightDAOImpl;

public class Main {
    public static void main(String[] args) {
        FlightDAOImpl flightDao = new FlightDAOImpl();

        // 1. Yeni Uçuş Oluştur ve Kaydet
        Flight f1 = new Flight(101, "Istanbul", "London", "2024-10-15");
        Flight f2 = new Flight(102, "Ankara", "Berlin", "2024-10-16");
        
        flightDao.save(f1);
        flightDao.save(f2);
        System.out.println("Uçuşlar kaydedildi.");

        // 2. Tüm Uçuşları Oku
        System.out.println("\n--- Mevcut Uçuşlar ---");
        flightDao.getAll().forEach(System.out::println);

        // 3. Güncelleme Testi (ID 101 olan uçuşun varış yerini değiştir)
        f1 = new Flight(101, "Istanbul", "Paris", "2024-10-20");
        flightDao.update(f1);
        System.out.println("\nID 101 güncellendi (Paris yapıldı).");

        // 4. Silme Testi (ID 102'yi sil)
        flightDao.delete(102);
        System.out.println("ID 102 silindi.");

        // Final Liste
        System.out.println("\n--- Son Durum ---");
        flightDao.getAll().forEach(System.out::println);
    }
}
