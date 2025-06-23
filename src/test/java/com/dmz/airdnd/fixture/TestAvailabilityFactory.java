package com.dmz.airdnd.fixture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Availability;
import com.dmz.airdnd.reservation.domain.Reservation;

public class TestAvailabilityFactory {
	public static List<Availability> createTestAvailabilities(Accommodation accommodation, Reservation reservation) {
		List<Availability> availabilities = new ArrayList<>();

		for (LocalDate date = reservation.getCheckInDate(); date.isBefore(
			reservation.getCheckOutDate()); date = date.plusDays(1)) {
			Availability availability = Availability.builder()
				.date(date)
				.accommodation(accommodation)
				.reservation(reservation)
				.build();
			availabilities.add(availability);
		}
		return availabilities;
	}

	public static List<Availability> createTestAvailabilities(List<Accommodation> accommodations, Reservation reservation) {
		// 0: 2026-01-01 ~ 2026-01-03
		// 1: 2026-01-01 ~ 2026-01-02
		// 2: 2026-01-04 ~ 2026-01-06
		// 3: 2026-01-06 예약됨.
		return List.of(
			Availability.builder()
				.accommodation(accommodations.get(0))
				.date(LocalDate.of(2026, 1, 1))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(0))
				.date(LocalDate.of(2026, 1, 2))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(0))
				.date(LocalDate.of(2026, 1, 3))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(1))
				.date(LocalDate.of(2026, 1, 1))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(1))
				.date(LocalDate.of(2026, 1, 2))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(2))
				.date(LocalDate.of(2026, 1, 4))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(2))
				.date(LocalDate.of(2026, 1, 5))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(2))
				.date(LocalDate.of(2026, 1, 6))
				.reservation(reservation)
				.build(),
			Availability.builder()
				.accommodation(accommodations.get(3))
				.date(LocalDate.of(2026, 1, 6))
				.reservation(reservation)
				.build()
		);
	}
}
