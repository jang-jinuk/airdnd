package com.dmz.airdnd.user.mapper;

import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.dto.request.UserRequest;

public class UserMapper {
	public static User toEntity(UserRequest userRequest) {
		return User.builder()
			.loginId(userRequest.getLoginId())
			.password(userRequest.getPassword())
			.email(userRequest.getEmail())
			.phone(userRequest.getPhone())
			.role(Role.USER)
			.build();
	}
}
