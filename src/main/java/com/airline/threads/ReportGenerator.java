package com.airline.threads;

import java.util.function.Consumer;

public class ReportGenerator extends Thread {
    private final Consumer<String> onComplete;

    public ReportGenerator(Consumer<String> onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        try {
            // Rapor hazırlama simülasyonu (Uzun süren işlem)
            Thread.sleep(3000); 
            
            double occupancyRate = 75.5; // Örnek hesaplama sonucu
            String result = "Rapor Hazır! Doluluk Oranı: %" + occupancyRate;
            
            // Sonucu GUI'ye geri gönder
            onComplete.accept(result);
            
        } catch (InterruptedException e) {
            onComplete.accept("Rapor hatası oluştu!");
        }
    }
}