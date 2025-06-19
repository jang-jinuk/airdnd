package com.dmz.airdnd.reservation.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.common.auth.jwt.JwtUtil;
import com.dmz.airdnd.fixture.TestAccommodationFactory;
import com.dmz.airdnd.fixture.TestAddressFactory;
import com.dmz.airdnd.fixture.TestReservationFactory;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.reservation.dto.request.ReservationRequest;
import com.dmz.airdnd.reservation.dto.response.ReservationResponse;
import com.dmz.airdnd.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservationController.class)
@Import(JwtUtil.class)
class ReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ReservationService reservationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("POST /api/accommodation/{id}/reservation 성공 시 201 반환하고 서비스 호출")
	void success_booking() throws Exception {
		//given
		String token = generateToken();
		Accommodation accommodation = TestAccommodationFactory
			.createTestAccommodation(1L, TestAddressFactory.createTestAddress(1L));

		ReservationRequest reservationRequest = TestReservationFactory.createReservationRequest();
		String json = objectMapper.writeValueAsString(reservationRequest);

		ReservationResponse reservationResponse = TestReservationFactory.createReservationResponse(accommodation);
		when(reservationService.booking(any(), any())).thenReturn(reservationResponse);

		//when + then
		mockMvc.perform(post("/api/accommodations/1/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(json))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.name").value("한라산뷰다락룸개인실"))
			.andExpect(jsonPath("$.data.checkInDate").value("2025-07-21"))
			.andExpect(jsonPath("$.data.checkOutDate").value("2025-07-23"))
			.andExpect(jsonPath("$.data.numberOfGuests").value(4))
			.andExpect(jsonPath("$.data.totalPrice").value(200000))
			.andExpect(jsonPath("$.data.currency").value("KRW"))
			.andExpect(jsonPath("$.error").isEmpty());

		//service 호출 확인
		verify(reservationService).booking(any(), any(ReservationRequest.class));
	}

	private String generateToken() {
		return jwtUtil.generateAccessToken(TestUserFactory.createTestUser(1L));
	}
}
