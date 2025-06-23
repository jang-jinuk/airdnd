package com.dmz.airdnd.reservation.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationRequest {

	@NotNull(message = "체크인은 필수 입력 항목입니다.")
	private LocalDate checkInDate;

	@NotNull(message = "체크아웃은 필수 입력 항목입니다.")
	private LocalDate checkOutDate;

	@Min(value = 1, message = "투숙 인원은 1명 이상이어야 합니다.")
	private int numberOfGuests;

	@NotNull
	private String timezone;
}
