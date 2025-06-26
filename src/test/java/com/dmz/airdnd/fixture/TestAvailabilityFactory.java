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

	public static List<Availability> createTestAvailabilities(List<Reservation> reservations) {
		List<Availability> availabilities = new ArrayList<>();

		for (Reservation reservation : reservations) {
			availabilities.addAll(createTestAvailabilities(reservation.getAccommodation(), reservation));
		}
		return availabilities;
	}
}
