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

import com.dmz.airdnd.common.auth.jwt.JwtUtil;
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

		ReservationRequest request = TestReservationFactory.createReservationRequest();
		String json = objectMapper.writeValueAsString(request);

		ReservationResponse response = ReservationResponse.builder()
			.name("Test Accommodation")
			.checkInDate(request.getCheckInDate())
			.checkOutDate(request.getCheckOutDate())
			.numberOfGuests(request.getNumberOfGuests())
			.totalPrice(200000L)
			.currency("KRW")
			.build();
		when(reservationService.booking(any(), any())).thenReturn(response);

		//when + then
		mockMvc.perform(post("/api/accommodations/1/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(json))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.name").value(response.getName()))
			.andExpect(jsonPath("$.data.checkInDate").value(response.getCheckInDate().toString()))
			.andExpect(jsonPath("$.data.checkOutDate").value(response.getCheckOutDate().toString()))
			.andExpect(jsonPath("$.data.numberOfGuests").value(response.getNumberOfGuests()))
			.andExpect(jsonPath("$.data.totalPrice").value(response.getTotalPrice()))
			.andExpect(jsonPath("$.data.currency").value(response.getCurrency()))
			.andExpect(jsonPath("$.error").isEmpty());

		//service 호출 확인
		verify(reservationService).booking(any(), any(ReservationRequest.class));
	}

	private String generateToken() {
		return jwtUtil.generateAccessToken(TestUserFactory.createTestUser(1L));
	}
}
