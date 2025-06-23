package com.dmz.airdnd.accommodation.dto.response;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AddressResponse {
	private String country;
	private String baseAddress;
	private String detailedAddress;
	private Point location;
}
