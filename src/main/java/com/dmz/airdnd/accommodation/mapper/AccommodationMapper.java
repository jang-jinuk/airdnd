package com.dmz.airdnd.accommodation.mapper;

import java.util.List;

import org.locationtech.jts.geom.Point;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;
import com.dmz.airdnd.accommodation.domain.Label;
import com.dmz.airdnd.accommodation.dto.request.AccommodationCreateRequest;
import com.dmz.airdnd.accommodation.domain.Label;
import com.dmz.airdnd.accommodation.dto.response.AccommodationResponse;
import com.dmz.airdnd.accommodation.dto.response.AddressResponse;
import com.dmz.airdnd.accommodation.dto.response.LabelResponse;
import com.dmz.airdnd.accommodation.dto.response.AccommodationCreateResponse;

public class AccommodationMapper {
	public static Accommodation toEntity(AccommodationCreateRequest request, Address address, List<Label> labels) {
		return Accommodation.builder()
			.name(request.getName())
			.description(request.getDescription())
			.pricePerDay(request.getPricePerDay())
			.currency(request.getCurrency())
			.maxGuests(request.getMaxGuests())
			.bedCount(request.getBedCount())
			.bedroomCount(request.getBedroomCount())
			.bathroomCount(request.getBathroomCount())
			.address(address)
			.labels(labels)
			.build();
	}

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
		Point location = address.getLocation();
		return AddressResponse.builder()
			.country(address.getCountry())
			.baseAddress(address.getBaseAddress())
			.detailedAddress(address.getDetailedAddress())
			.latitude(location.getX())
			.longitude(location.getY())
			.build();
	}

	public static List<LabelResponse> toResponses(List<Label> labels) {
		return labels.stream().map(label -> new LabelResponse(label.getId(), label.getName())).toList();
	}

	public static AccommodationCreateResponse fromEntity(Accommodation accommodation) {
		return AccommodationCreateResponse.builder()
			.id(accommodation.getId())
			.address(formatFullAddress(accommodation.getAddress()))
			.labels(accommodation.getLabels())
			.name(accommodation.getName())
			.description(accommodation.getDescription())
			.pricePerDay(accommodation.getPricePerDay())
			.currency(accommodation.getCurrency())
			.maxGuests(accommodation.getMaxGuests())
			.bedCount(accommodation.getBedCount())
			.bedroomCount(accommodation.getBedroomCount())
			.bathroomCount(accommodation.getBathroomCount())
			.createdAt(accommodation.getCreatedAt())
			.build();
	}

	private static String formatFullAddress(Address address) {
		String base = address.getBaseAddress();
		String detail = address.getDetailedAddress();
		if (detail != null && !detail.isEmpty()) {
			return base + " " + detail;
		}
		return base;
	}
}
