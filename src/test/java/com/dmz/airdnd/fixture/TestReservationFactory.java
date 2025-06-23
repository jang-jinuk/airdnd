package com.dmz.airdnd.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.domain.ReservationStatus;
import com.dmz.airdnd.reservation.dto.request.ReservationRequest;
import com.dmz.airdnd.reservation.dto.response.ReservationResponse;
import com.dmz.airdnd.user.domain.User;

public class TestReservationFactory {
	public static Reservation createTestReservation(User guest, Accommodation accommodation) {
		return Reservation.builder()
			.guest(guest)
			.accommodation(accommodation)
			.checkInDate(LocalDate.of(2025, 7, 21))
			.checkOutDate(LocalDate.of(2025, 7, 23))
			.numberOfGuests(4)
			.totalPrice(200000)
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
