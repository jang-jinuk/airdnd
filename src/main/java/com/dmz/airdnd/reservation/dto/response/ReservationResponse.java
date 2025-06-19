package com.dmz.airdnd.reservation.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationResponse {
	private String name;

	private LocalDate checkInDate;

	private LocalDate checkOutDate;

	private int numberOfGuests;

	private Long totalPrice;

	private String currency;

}
