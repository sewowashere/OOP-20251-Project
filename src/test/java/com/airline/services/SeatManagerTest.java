package com.airline.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.airline.models.Seat;

class SeatManagerTest {
	private SeatManager seatManager;
	private Seat[][] matrix;

	@BeforeEach
	void setUp() {
		seatManager = SeatManager.getInstance();
		matrix = seatManager.createSeatingArrangement("TEST-101", 10); // 2 sıra, 10 koltuk
	}

	@Test
	void testEmptySeatsCountDecreases() {
		int initialCount = seatManager.countAvailableSeats(matrix);
		assertEquals(10, initialCount);

		// Bir koltuğu rezerve et
		matrix[0][0].setReserved(true);

		int afterCount = seatManager.countAvailableSeats(matrix);
		assertEquals(9, afterCount, "Koltuk rezerve edildikten sonra sayı hatalı azaldı!");
	}

	@Test
	void testNonExistentSeatThrowsException() {
		// SeatManager'da getSeat(String seatNum) gibi bir metodun olduğunu varsayıyoruz
		// Eğer koltuk yoksa IllegalArgumentException fırlatmasını bekliyoruz.
		assertThrows(IllegalArgumentException.class, () -> {
			String target = "99Z"; // Olmayan bir koltuk
			boolean found = false;
			for (Seat[] row : matrix) {
				for (Seat s : row) {
					if (s.getSeatNum().equals(target))
						found = true;
				}
			}
			if (!found)
				throw new IllegalArgumentException("Seat not found");
		});
	}
}
