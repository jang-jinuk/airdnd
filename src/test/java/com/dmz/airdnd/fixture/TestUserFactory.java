package com.dmz.airdnd.fixture;

import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.dto.request.UserRequest;

public class TestUserFactory {
	public static User createTestUser() {
		return User.builder()
			.loginId("testUser")
			.password("password123")
			.email("test@test.com")
			.role(Role.USER)
			.phone("010-1234-5678")
			.build();
	}

	public static User createTestUser(Long id) {
		return User.builder()
			.id(id)
			.loginId("testUser")
			.password("password123")
			.email("test@test.com")
			.role(Role.USER)
			.phone("010-1234-5678")
			.build();
	}

	public static UserRequest createUserRequestFrom(User user) {
		return UserRequest.builder()
			.loginId(user.getLoginId())
			.password(user.getPassword())
			.email(user.getEmail())
			.phone(user.getPhone())
			.build();
	}
}
