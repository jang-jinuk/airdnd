package com.dmz.airdnd.accommodation.mapper;

import java.util.List;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;
import com.dmz.airdnd.accommodation.domain.Label;
import com.dmz.airdnd.accommodation.dto.response.AccommodationResponse;
import com.dmz.airdnd.accommodation.dto.response.AddressResponse;
import com.dmz.airdnd.accommodation.dto.response.LabelResponse;

public class AccommodationMapper {
	public static AccommodationResponse toResponse(Accommodation accommodation) {
		return AccommodationResponse.builder()
			.id(accommodation.getId())
			.addressResponse(toResponse(accommodation.getAddress()))
			.labelResponses(toResponses(accommodation.getLabels()))
			.name(accommodation.getName())
			.description(accommodation.getDescription())
			.pricePerDay(accommodation.getPricePerDay())
			.currency(accommodation.getCurrency())
			.maxGuests(accommodation.getMaxGuests())
			.bedCount(accommodation.getBedCount())
			.bedroomCount(accommodation.getBedroomCount())
			.bathroomCount(accommodation.getBathroomCount())
			.createdAt(accommodation.getCreatedAt())
			.updatedAt(accommodation.getUpdatedAt())
			.build();
	}

	public static AddressResponse toResponse(Address address) {
		return AddressResponse.builder()
			.country(address.getCountry())
			.baseAddress(address.getBaseAddress())
			.detailedAddress(address.getDetailedAddress())
			.location(address.getLocation())
			.build();
	}

	public static List<LabelResponse> toResponses(List<Label> labels) {
		return labels.stream().map(label -> new LabelResponse(label.getId(), label.getName())).toList();
	}
}
