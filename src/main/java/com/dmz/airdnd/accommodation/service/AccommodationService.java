package com.dmz.airdnd.accommodation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.dto.FilterCondition;
import com.dmz.airdnd.accommodation.dto.request.AccommodationSearchRequest;
import com.dmz.airdnd.accommodation.dto.response.AccommodationPageResponse;
import com.dmz.airdnd.accommodation.mapper.AccommodationMapper;
import com.dmz.airdnd.accommodation.repository.AccommodationRepository;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.InvalidFilterConditionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	@Transactional(readOnly = true)
	public AccommodationPageResponse findFilteredAccommodations(AccommodationSearchRequest request) {
		FilterCondition filterCondition = toCondition(request);
		Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());

		Page<Accommodation> accommodationPage = accommodationRepository.findFilteredAccommodations(pageable,
			filterCondition);
		return toResponse(accommodationPage);
	}

	private FilterCondition toCondition(AccommodationSearchRequest request) {
		LocalDate checkIn = request.getCheckIn();
		LocalDate checkOut = request.getCheckOut();

		if (checkIn != null && checkOut != null) {
			if (!checkIn.isBefore(checkOut)) {
				throw new InvalidFilterConditionException(ErrorCode.INVALID_DATE_RANGE);
			}
		}

		if (request.getMinPrice() != null && request.getMaxPrice() != null) {
			if (request.getMinPrice() > request.getMaxPrice()) {
				throw new InvalidFilterConditionException(ErrorCode.INVALID_MIN_MAX_PRICE);
			}
		}

		List<LocalDate> requestedDates =
			(checkIn != null && checkOut != null) ? checkIn.datesUntil(checkOut).toList() : List.of();

		return FilterCondition.builder()
			.longitude(request.getLongitude())
			.latitude(request.getLatitude())
			.minPrice(request.getMinPrice())
			.maxPrice(request.getMaxPrice())
			.maxGuests(request.getMaxGuests())
			.requestedDates(requestedDates)
			.build();
	}

	private AccommodationPageResponse toResponse(Page<Accommodation> accommodationPage) {
		return AccommodationPageResponse.builder()
			.page(accommodationPage.getNumber() + 1)
			.pageSize(accommodationPage.getSize())
			.totalElements(accommodationPage.getTotalElements())
			.totalPages(accommodationPage.getTotalPages())
			.accommodationResponses(accommodationPage.getContent()
				.stream()
				.map(AccommodationMapper::toResponse)
				.collect(Collectors.toList()))
			.build();
	}
}

