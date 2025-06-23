package com.dmz.airdnd.accommodation.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dmz.airdnd.accommodation.dto.response.CoordinateResponse;
import com.dmz.airdnd.accommodation.dto.response.GeocodeResponse;
import com.dmz.airdnd.common.exception.GeocodingException;

@ExtendWith(MockitoExtension.class)
class GoogleGeocodingClientTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private GoogleGeocodingClient geocodingService;

	private final String BASE_ADDRESS = "서울특별시 강남구 강남대로 62길 23";
	private final double LATITUDE = 37.490898;
	private final double LONGITUDE = 127.0334296;

	@Test
	@DisplayName("주소를 입력하면 위도, 경도를 정상적으로 반환한다")
	void geocodingTest() {
		// given
		GeocodeResponse.Location location = new GeocodeResponse.Location();
		location.setLatitude(LATITUDE);
		location.setLongitude(LONGITUDE);

		GeocodeResponse.Geometry geometry = new GeocodeResponse.Geometry();
		geometry.setLocation(location);

		GeocodeResponse.Result result = new GeocodeResponse.Result();
		result.setGeometry(geometry);

		GeocodeResponse mockResponse = new GeocodeResponse();
		mockResponse.setStatus("OK");
		mockResponse.setResults(List.of(result));

		when(restTemplate.getForObject(anyString(), eq(GeocodeResponse.class)))
			.thenReturn(mockResponse);

		// when
		CoordinateResponse coordinates = geocodingService.lookupCoordinates(BASE_ADDRESS);

		// then
		assertThat(location.getLatitude()).isEqualTo(coordinates.latitude());
		assertThat(location.getLongitude()).isEqualTo(coordinates.longitude());
	}

	@Test
	@DisplayName("Google API 호출이 실패하면 GeocodingException을 던진다")
	void geocodingFailTest() {
		when(restTemplate.getForObject(anyString(), eq(GeocodeResponse.class)))
			.thenThrow(new RestClientException("네트워크 오류"));

		assertThrows(GeocodingException.class, () -> {
			geocodingService.lookupCoordinates(BASE_ADDRESS);
		});
	}
}
