package com.dmz.airdnd.common.auth.dto;

import com.dmz.airdnd.user.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
	private Long id;
	private Role role;
}
