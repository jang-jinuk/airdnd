package com.dmz.airdnd.accommodation.adapter;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dmz.airdnd.accommodation.dto.response.CoordinatesDto;
import com.dmz.airdnd.accommodation.dto.response.GeocodeResponse;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.GeocodingException;

import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleGeocodingClient implements GeocodingClient {

	private final String GOOGLE_GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json";
	private final String STATUS_OK = "OK";
	private final String URI_ADDRESS = "address";
	private final String URI_KEY = "key";
	private final String URI_LANGUAGE = "language";
	private final String LANGUAGE_VALUE_KO = "ko";

	private final RestTemplate restTemplate;

	@Value("${GOOGLE_MAPS_API_KEY}")
	private String apiKey;

	@Override
	public CoordinatesDto lookupCoordinates(String baseAddress) {
		URI coordinateRequestUri = generateUri(baseAddress);
		GeocodeResponse geocodeResponse;

		try {
			geocodeResponse = restTemplate.getForObject(coordinateRequestUri, GeocodeResponse.class);
		} catch (RestClientException e) {
			throw new GeocodingException(ErrorCode.GEOCODING_FAILED);
		}

		if (geocodeResponse == null || !geocodeResponse.getStatus().equals(STATUS_OK) || geocodeResponse.getResults()
			.isEmpty()) {
			log.warn("유효하지 않은 GeocodeResponse: {}", geocodeResponse);
			throw new GeocodingException(ErrorCode.GEOCODING_FAILED);
		}

		GeocodeResponse.Location location = geocodeResponse.getResults().get(0).getGeometry().getLocation();

		return new CoordinatesDto(location.getLatitude(), location.getLongitude());
	}

	private URI generateUri(String baseAddress) {
		return UriComponentsBuilder
			.fromHttpUrl(GOOGLE_GEOCODING_URL)
			.queryParam(URI_ADDRESS, baseAddress)
			.queryParam(URI_KEY, apiKey)
			.queryParam(URI_LANGUAGE, LANGUAGE_VALUE_KO)
			.encode(StandardCharsets.UTF_8)
			.build()
			.toUri();
	}
}
