package com.dmz.airdnd.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.reservation.domain.Availability;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.repository.AvailabilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

	private final AvailabilityRepository availabilityRepository;

	@Transactional
	public void saveReservationDates(Accommodation accommodation, Reservation reservation) {
		List<Availability> reservationDates = buildReservationDates(accommodation, reservation);
		availabilityRepository.saveAll(reservationDates);
	}

	private List<Availability> buildReservationDates(Accommodation accommodation, Reservation reservation) {
		List<Availability> dates = new ArrayList<>();
		for (LocalDate date = reservation.getCheckInDate(); date.isBefore(
			reservation.getCheckOutDate()); date = date.plusDays(1)) {
			dates.add(Availability.builder()
				.accommodation(accommodation)
				.reservation(reservation)
				.date(date)
				.build());
		}
		return dates;
	}
}
