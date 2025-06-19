package com.dmz.airdnd.accommodation.service;

import org.springframework.stereotype.Service;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.repository.AccommodationRepository;
import com.dmz.airdnd.common.exception.AccommodationNotFound;
import com.dmz.airdnd.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	public Accommodation getAccommodationById(Long accommodationId) {
		return accommodationRepository.findById(accommodationId)
			.orElseThrow(() -> new AccommodationNotFound(ErrorCode.ACCOMMODATION_NOT_FOUND));
	}
}
