package com.dmz.airdnd.common.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {
	@NotBlank(message = "아이디를 입력해주세요.")
	private String loginId;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
}
