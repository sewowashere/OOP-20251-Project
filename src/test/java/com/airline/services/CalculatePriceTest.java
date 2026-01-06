package com.airline.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.airline.models.Seat;
import com.airline.services.pricing.CalculatePrice;

class CalculatePriceTest {

	@Test
	void testEconomyAndBusinessPricing() {
		CalculatePrice calculator = new CalculatePrice();
		double basePrice = 1000.0;

		// Senaryo 1: Economy Testi (Stratejine göre 1x olmalı)
		double economyPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.ECONOMY);
		assertEquals(1000.0, economyPrice, "Ekonomi fiyatı hatalı!");

		// Senaryo 2: Business Testi (Stratejine göre 2.5x olmalı)
		double businessPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.BUSINESS);
		assertEquals(2500.0, businessPrice, "Business fiyatı hatalı!");
	}
}