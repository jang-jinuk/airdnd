package com.dmz.airdnd.accommodation.adapter;

import com.dmz.airdnd.accommodation.dto.response.CoordinateResponse;

public interface GeocodingClient {

	CoordinateResponse lookupCoordinates(String baseAddress);
}
