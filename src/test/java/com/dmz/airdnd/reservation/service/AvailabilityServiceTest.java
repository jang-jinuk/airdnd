package com.dmz.airdnd.reservation.service;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.repository.AvailabilityRepository;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

	@InjectMocks
	private AvailabilityService availabilityService;

	@Mock
	private AvailabilityRepository availabilityRepository;

	@Test
	@DisplayName("예약 정보를 기반으로 Availability 목록을 저장한다")
	void success_saveReservationDates() {
		//given
		Accommodation accommodation = Mockito.mock(Accommodation.class);
		Reservation reservation = Mockito.mock(Reservation.class);
		when(reservation.getCheckInDate()).thenReturn(LocalDate.of(2025, 7, 21));
		when(reservation.getCheckOutDate()).thenReturn(LocalDate.of(2025, 7, 23));
		when(availabilityRepository.saveAll(anyList())).thenReturn(List.of());

		//when
		availabilityService.saveReservationDates(accommodation, reservation);

		//then
		verify(availabilityRepository).saveAll(anyList());
	}
}
