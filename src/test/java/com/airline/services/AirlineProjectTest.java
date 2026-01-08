package com.airline.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.airline.models.Seat;
import com.airline.models.Flight;
import com.airline.services.pricing.CalculatePrice;
import java.time.LocalDateTime;
import java.util.List;

public class AirlineProjectTest {

    private CalculatePrice calculator;
    private SeatManager seatManager;
    private FlightManager flightManager;

    @BeforeEach
    void setUp() {
        calculator = new CalculatePrice();
        seatManager = SeatManager.getInstance();
        flightManager = FlightManager.getInstance();
    }

    // PRICE CALCULATION TESTS

    @Test
    @DisplayName("Verify Economy price equals base price")
    void testEconomyPrice() {
        double basePrice = 500.0;
        double finalPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.ECONOMY); // [cite: 9, 10]
        assertEquals(500.0, finalPrice, "Economy pricing should not apply multipliers [cite: 9]");
    }

    @Test
    @DisplayName("Verify Business price applies 2.5x multiplier")
    void testBusinessPrice() {
        double basePrice = 1000.0;
        double expectedPrice = 2500.0; // 1000 * 2.5 [cite: 11]
        double finalPrice = calculator.getFinalPrice(basePrice, Seat.SeatClass.BUSINESS); // [cite: 11]
        assertEquals(expectedPrice, finalPrice, "Business pricing should be exactly 2.5x base price [cite: 11]");
    }

    @Test
    @DisplayName("Verify pricing strategy ratio using assertAll")
    void testPriceStrategyRatio() {
        double base = 1000.0;
        double eco = calculator.getFinalPrice(base, Seat.SeatClass.ECONOMY);
        double bus = calculator.getFinalPrice(base, Seat.SeatClass.BUSINESS);

        assertAll("Pricing Strategy Validation",
                () -> assertTrue(bus > eco, "Business must be more expensive than Economy"),
                () -> assertEquals(2.5, bus / eco, 0.001, "Business-to-Economy ratio must be 2.5 [cite: 11]")
        );
    }

    // BAGGAGE SURCHARGE TESTS

    @Test
    @DisplayName("Verify correct baggage allowance for each class")
    void testBaggageAllowancePerClass() {
        // Business: 30kg, Economy: 15kg kuralı
        assertAll("Class Specific Allowance",
                () -> assertEquals(30.0, calculator.getAllowance(Seat.SeatClass.BUSINESS), "Business allowance should be 30kg"),
                () -> assertEquals(15.0, calculator.getAllowance(Seat.SeatClass.ECONOMY), "Economy allowance should be 15kg")
        );
    }

    @Test
    @DisplayName("Verify surcharge for Economy exceeding 15kg limit")
    void testEconomyBaggageSurcharge() {
        double weight = 20.0; // 5kg extra for Economy
        double expectedExtra = 250.0; // 5kg * 50.0 per kg

        double result = calculator.calculateBaggageSurcharge(weight, Seat.SeatClass.ECONOMY);
        assertEquals(expectedExtra, result, "Economy baggage surcharge calculation failed");
    }

    @Test
    @DisplayName("Verify no surcharge for Business within 30kg limit")
    void testBusinessBaggageNoSurcharge() {
        double weight = 25.0; // Within 30kg limit for Business
        double result = calculator.calculateBaggageSurcharge(weight, Seat.SeatClass.BUSINESS);
        assertEquals(0.0, result, "No surcharge should be applied for Business under 30kg");
    }

    // FLIGHT SEARCH ENGINE TESTS

    @Test
    @DisplayName("Verify flight filtering by departure and arrival cities")
    void testFlightSearchByCity() {
        flightManager.createAndSaveFlight(101, "Istanbul", "Berlin", "2026-05-15", 14, 3); // [cite: 4]
        List<Flight> results = flightManager.getAvailableFlights("Istanbul", "Berlin"); // [cite: 4]

        assertFalse(results.isEmpty(), "Flight search should return matching routes");
        assertEquals("Istanbul", results.get(0).getDeparturePlace());
        assertEquals("Berlin", results.get(0).getArrivalPlace());
    }

    @Test
    @DisplayName("Verify past flights are excluded from search results")
    void testPastFlightElimination() {
        String yesterday = LocalDateTime.now().minusDays(1).toLocalDate().toString();
        flightManager.createAndSaveFlight(102, "Ankara", "Izmir", yesterday, 10, 1); // [cite: 4]

        List<Flight> results = flightManager.getAvailableFlights("Ankara", "Izmir"); // [cite: 4]
        assertTrue(results.isEmpty(), "Expired flights should be eliminated from results [cite: 4]");
    }

    // SEAT MANAGEMENT TESTS

    @Test
    @DisplayName("Verify empty seat count decreases after reservation")
    void testSeatCountDecrease() {
        Seat[][] matrix = seatManager.createSeatingArrangement("TEST-001", 180); // [cite: 13]
        int initialCount = seatManager.emptySeatsCount(matrix); // [cite: 13]

        seatManager.reserveSeat(matrix, "1A"); // [cite: 13]
        assertEquals(initialCount - 1, seatManager.emptySeatsCount(matrix), "Seat count should decrease by exactly 1 [cite: 13]");
    }

    @Test
    @DisplayName("Verify exception when booking non-existent seat")
    void testInvalidSeatException() {
        Seat[][] matrix = seatManager.createSeatingArrangement("TEST-001", 10);
        assertThrows(IllegalArgumentException.class, () -> {
            seatManager.reserveSeat(matrix, "99Z"); // [cite: 13]
        }, "Should throw IllegalArgumentException for non-existent seat numbers");
    }
}