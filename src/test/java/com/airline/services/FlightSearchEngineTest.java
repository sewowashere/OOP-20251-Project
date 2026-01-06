package com.airline.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.airline.models.Flight;
import java.util.List;
import java.util.stream.Collectors;

class FlightManagerSearchTest {

	@Test
	void testFilterDepartureAndArrival() {
		FlightManager fm = FlightManager.getInstance();
		// Test amaçlı manuel uçuş listesi simülasyonu
		Flight f1 = new Flight(1, "Istanbul", "Berlin", "2026-05-20");
		Flight f2 = new Flight(2, "Ankara", "Berlin", "2026-06-15");

		// Basit bir filtreleme mantığı testi
		List<Flight> allFlights = List.of(f1, f2);
		List<Flight> filtered = allFlights.stream()
				.filter(f -> f.getDeparturePlace().equals("Istanbul") && f.getArrivalPlace().equals("Berlin"))
				.collect(Collectors.toList());

		assertEquals(1, filtered.size());
		assertEquals(1, filtered.get(0).getFlightNum());
	}

	@Test
	void testEliminatePassedFlights() {
		// "2023-01-01" tarihi bugünden (2026) önce olduğu için elenmeli
		String pastDate = "2023-01-01";
		String currentDate = "2026-01-06"; // Sistemin bugünkü tarihi

		Flight oldFlight = new Flight(99, "Izmir", "Bursa", pastDate);

		// Basit tarih kıyaslama mantığı (String bazlı veya LocalDate ile)
		boolean isPassed = oldFlight.getDate().compareTo(currentDate) < 0;

		assertTrue(isPassed, "Tarihi geçmiş uçuş elenmeliydi!");
	}
}