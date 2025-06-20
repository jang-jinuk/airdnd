package com.dmz.airdnd.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRequest {
	@NotBlank(message = "로그인 아이디는 필수 입력 항목입니다.")
	@Size(min = 5, max = 25, message = "로그인 아이디는 5~25자 이내여야 합니다.")
	private String loginId;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 5, max = 25, message = "비밀번호는 5~25자 이내여야 합니다.")
	private String password;

	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	@Size(min = 6, max = 25, message = "이메일은 6~25자 이내여야 합니다.")
	private String email;

	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
	@Size(min = 8, max = 25, message = "전화번호는 8~25자 이내여야 합니다.")
	private String phone;
}
