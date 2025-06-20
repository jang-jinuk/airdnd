package com.dmz.airdnd.accommodation.dto.response;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccommodationResponse {
	private Long id;
	private AddressResponse addressResponse;
	private List<LabelResponse> labelResponses;
	private String name;
	private String description;
	private long pricePerDay;
	private String currency;
	private int maxGuests;
	private int bedCount;
	private int bedroomCount;
	private int bathroomCount;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
