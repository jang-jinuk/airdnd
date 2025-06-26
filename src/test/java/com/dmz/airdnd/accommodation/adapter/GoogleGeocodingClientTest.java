package com.dmz.airdnd.accommodation.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dmz.airdnd.accommodation.dto.response.CoordinatesDto;
import com.dmz.airdnd.common.config.RestTemplateConfig;
import com.dmz.airdnd.common.exception.GeocodingException;

@ImportAutoConfiguration(RestTemplateAutoConfiguration.class)
@SpringJUnitConfig(classes = {
	RestTemplateConfig.class,
	GoogleGeocodingClient.class})
@ActiveProfiles("dev")
class GoogleGeocodingClientTest {

	@Autowired
	private GoogleGeocodingClient geocodingClient;

	@Test
	@DisplayName("주소를 입력하면 위도, 경도를 정상적으로 반환한다")
	void geocodingTest() {
		// given
		final String BASE_ADDRESS = "서울특별시 강남구 강남대로 62길 23";
		final double LATITUDE = 37.490898;
		final double LONGITUDE = 127.0334296;

		// when
		CoordinatesDto coordinates = geocodingClient.lookupCoordinates(BASE_ADDRESS);

		// then
		assertThat(LATITUDE).isEqualTo(coordinates.latitude());
		assertThat(LONGITUDE).isEqualTo(coordinates.longitude());
	}

	@Test
	@DisplayName("Google API 호출이 실패하면 GeocodingException을 던진다")
	void geocodingFailTest() {
		// given
		final String INVALID_ADDRESS = "주소가존재하지않는장소12345";

		// when + then
		assertThrows(GeocodingException.class, () -> {
			geocodingClient.lookupCoordinates(INVALID_ADDRESS);
		});
	}
}
