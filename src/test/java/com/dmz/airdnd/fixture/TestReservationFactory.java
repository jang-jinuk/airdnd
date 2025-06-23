package com.dmz.airdnd.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.domain.ReservationStatus;
import com.dmz.airdnd.reservation.dto.request.ReservationRequest;
import com.dmz.airdnd.user.domain.User;

public class TestReservationFactory {
	public static Reservation createTestReservation(User guest, Accommodation accommodation) {
		LocalDate checkIn = LocalDate.of(2025, 7, 21);
		LocalDate checkOut = LocalDate.of(2025, 7, 23);
		return Reservation.builder()
			.guest(guest)
			.accommodation(accommodation)
			.checkInDate(checkIn)
			.checkOutDate(checkOut)
			.numberOfGuests(4)
			.totalPrice(accommodation.getPricePerDay() * ChronoUnit.DAYS.between(checkIn, checkOut))
			.status(ReservationStatus.PENDING)
			.timezone("Asia/Seoul")
			.currency("KRW")
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ReservationRequest createReservationRequest() {
		return ReservationRequest.builder()
			.checkInDate(LocalDate.of(2025, 7, 21))
			.checkOutDate(LocalDate.of(2025, 7, 23))
			.numberOfGuests(4)
			.timezone("Asia/Seoul")
			.build();
	}
}
