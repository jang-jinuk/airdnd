package com.dmz.airdnd.reservation.mapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.domain.ReservationStatus;
import com.dmz.airdnd.reservation.dto.request.ReservationRequest;
import com.dmz.airdnd.reservation.dto.response.ReservationResponse;
import com.dmz.airdnd.user.domain.User;

public class ReservationMapper {
	public static Reservation toEntity(ReservationRequest reservationRequest, User guest, Accommodation accommodation) {
		long nights = ChronoUnit.DAYS.between(
			reservationRequest.getCheckInDate(),
			reservationRequest.getCheckOutDate()
		);

		return Reservation.builder()
			.guest(guest)
			.accommodation(accommodation)
			.checkInDate(reservationRequest.getCheckInDate())
			.checkOutDate(reservationRequest.getCheckOutDate())
			.numberOfGuests(reservationRequest.getNumberOfGuests())
			.totalPrice(accommodation.getPricePerDay() * nights)
			.status(ReservationStatus.PENDING)
			.timezone(reservationRequest.getTimezone())
			.currency(accommodation.getCurrency())
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ReservationResponse toResponse(Reservation reservation, Accommodation accommodation) {
		return ReservationResponse.builder()
			.name(accommodation.getName())
			.checkInDate(reservation.getCheckInDate())
			.checkOutDate(reservation.getCheckOutDate())
			.numberOfGuests(reservation.getNumberOfGuests())
			.totalPrice(reservation.getTotalPrice())
			.currency(reservation.getCurrency())
			.build();
	}
}
