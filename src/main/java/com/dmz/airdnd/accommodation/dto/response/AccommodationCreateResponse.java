package com.dmz.airdnd.accommodation.dto.response;

import java.sql.Timestamp;
import java.util.List;

import com.dmz.airdnd.accommodation.domain.Label;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationCreateResponse {
	private final Long id;
	private final String address;
	private final List<Label> labels;
	private final String name;
	private final String description;
	private final Long pricePerDay;
	private final String currency;
	private final Integer maxGuests;
	private final Integer bedCount;
	private final Integer bedroomCount;
	private final Integer bathroomCount;
	private final Timestamp createdAt;
}
