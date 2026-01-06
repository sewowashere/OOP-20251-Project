package com.airline.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.airline.models.Seat;
import com.airline.services.pricing.CalculatePrice;

public class CalculatePriceTest {
    
    private CalculatePrice calculator;

    @BeforeEach
    void setUp() {
        // Her testten önce yeni bir calculator nesnesi oluşturulur
        calculator = new CalculatePrice();
    }

    @Test
    @DisplayName("Ekonomi sınıfı fiyatı base fiyata eşit olmalı")
    void testEconomyPrice() {
        double basePrice = 500.0;
        double finalPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.ECONOMY);
        
        assertEquals(500.0, finalPrice, "Ekonomi fiyatı hesaplamasında hata var!");
    }

    @Test
    @DisplayName("Business sınıfı fiyatı base fiyatın 2.5 katı olmalı")
    void testBusinessPrice() {
        double basePrice = 1000.0;
        double expectedPrice = 2500.0; // 1000 * 2.5
        double finalPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.BUSINESS);
        
        assertEquals(expectedPrice, finalPrice, "Business fiyatı hesaplamasında hata var!");
    }

    @Test
    @DisplayName("Ekstra bagaj ücreti doğru hesaplanmalı")
    void testBaggageSurcharge() {
        double allowance = 20.0;
        double actualWeight = 25.0; // 5kg fazla
        double expectedExtra = 100.0; // 5kg * 20.0
        
        double result = calculator.calculateBaggageSurcharge(actualWeight, allowance);
        
        assertEquals(expectedExtra, result, "Bagaj ücreti yanlış hesaplandı!");
    }

    @Test
    @DisplayName("Limit altındaki bagaj için ücret sıfır olmalı")
    void testBaggageNoSurcharge() {
        double result = calculator.calculateBaggageSurcharge(15.0, 20.0);
        assertEquals(0.0, result, "Limit altındaki bagaja ücret kesilmemeli!");
    }
}
