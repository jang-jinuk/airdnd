package com.dmz.airdnd.reservation.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.repository.AccommodationRepository;
import com.dmz.airdnd.common.aop.RoleCheck;
import com.dmz.airdnd.common.auth.UserContext;
import com.dmz.airdnd.common.auth.dto.UserInfo;
import com.dmz.airdnd.common.exception.AccommodationNotFound;
import com.dmz.airdnd.common.exception.DuplicateReservationException;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.UserNotFoundException;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.dto.request.ReservationRequest;
import com.dmz.airdnd.reservation.dto.response.ReservationResponse;
import com.dmz.airdnd.reservation.mapper.ReservationMapper;
import com.dmz.airdnd.reservation.repository.ReservationRepository;
import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;

	private final AccommodationRepository accommodationRepository;

	private final UserRepository userRepository;

	private final AvailabilityService availabilityService;

	@Transactional
	@RoleCheck(Role.USER)
	public ReservationResponse booking(Long accommodationId, ReservationRequest reservationRequest) {
		Accommodation accommodation = getAccommodation(accommodationId);

		User guest = getCurrentUser();

		Reservation reservation = ReservationMapper.toEntity(reservationRequest, guest, accommodation);

		try {
			Reservation saved = reservationRepository.save(reservation);
			availabilityService.saveReservationDates(accommodation, saved);
			return ReservationMapper.toResponse(saved, accommodation);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateReservationException(ErrorCode.DUPLICATE_RESERVATION);
		}
	}

	private Accommodation getAccommodation(Long accommodationId) {
		return accommodationRepository.findById(accommodationId)
			.orElseThrow(() -> new AccommodationNotFound(ErrorCode.ACCOMMODATION_NOT_FOUND));
	}

	private User getCurrentUser() {
		UserInfo currentUser = UserContext.get();
		return userRepository.findById(currentUser.getId())
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
	}
}
