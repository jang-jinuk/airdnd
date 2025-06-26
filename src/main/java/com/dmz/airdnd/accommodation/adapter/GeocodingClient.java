package com.dmz.airdnd.accommodation.adapter;

import com.dmz.airdnd.accommodation.dto.response.CoordinatesDto;

public interface GeocodingClient {

	CoordinatesDto lookupCoordinates(String baseAddress);
}
