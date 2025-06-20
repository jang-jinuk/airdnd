package com.dmz.airdnd.fixture;

import com.dmz.airdnd.common.auth.dto.LoginRequest;

public class TestLoginRequestFactory {
	public static LoginRequest createLoginRequest() {
		return LoginRequest.builder()
			.loginId("testUser")
			.password("password123")
			.build();
	}

	public static LoginRequest createLoginRequest(String invalidPassword) {
		return LoginRequest.builder()
			.loginId("testUser")
			.password(invalidPassword)
			.build();
	}
}
