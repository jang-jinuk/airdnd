package com.dmz.airdnd.accommodation.adapter;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dmz.airdnd.accommodation.dto.response.CoordinateResponse;
import com.dmz.airdnd.accommodation.dto.response.GeocodeResponse;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.GeocodingException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleGeocodingClient implements GeocodingClient {

	private final String GOOGLE_GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";
	private final RestTemplate restTemplate;

	@Value("${GOOGLE_MAPS_API_KEY}")
	private String apiKey;

	@Override
	public CoordinateResponse lookupCoordinates(String baseAddress) {
		String coordinateRequestUri = generateUri(baseAddress);
		GeocodeResponse geocodeResponse;

		try {
			geocodeResponse = restTemplate.getForObject(coordinateRequestUri, GeocodeResponse.class);
		} catch (RestClientException e) {
			throw new GeocodingException(ErrorCode.GEOCODING_FAILED);
		}

		if (geocodeResponse == null || !"OK".equals(geocodeResponse.getStatus()) || geocodeResponse.getResults()
			.isEmpty()) {
			throw new GeocodingException(ErrorCode.GEOCODING_FAILED);
		}

		GeocodeResponse.Location location = geocodeResponse.getResults().get(0).getGeometry().getLocation();

		return new CoordinateResponse(location.getLatitude(), location.getLongitude());
	}

	private String generateUri(String baseAddress) {
		UriComponentsBuilder builder = UriComponentsBuilder
			.fromUri(URI.create(GOOGLE_GEOCODING_URL))
			.queryParam("address", baseAddress)
			.queryParam("key", apiKey);
		return builder.toUriString();
	}
}
