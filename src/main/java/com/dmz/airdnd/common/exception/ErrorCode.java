package com.dmz.airdnd.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// 유저 관련
	USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),
	DUPLICATE_LOGIN_ID(409, "DUPLICATE_LOGIN_ID", "이미 존재하는 로그인 아이디입니다."),
	DUPLICATE_EMAIL(409, "DUPLICATE_EMAIL", "이미 존재하는 이메일입니다."),
	DUPLICATE_PHONE(409, "DUPLICATE_PHONE", "이미 존재하는 전화번호입니다."),

	// 인증 관련
	UNAUTHORIZED(401, "UNAUTHORIZED", "인증이 필요합니다."),
	INVALID_PASSWORD(401, "INVALID_PASSWORD", "잘못된 비밀번호입니다."),

	// JWT 토큰 관련
	INVALID_JWT_SIGNATURE(401, "INVALID_JWT_SIGNATURE", "잘못된 JWT 서명입니다."),
	EXPIRED_JWT_TOKEN(401, "EXPIRED_JWT_TOKEN", "만료된 JWT 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN(401, "UNSUPPORTED_JWT_TOKEN", "지원하지 않는 JWT 토큰입니다."),
	INVALID_JWT_TOKEN(401, "INVALID_JWT_TOKEN", "JWT 토큰이 잘못되었습니다."),
	EMPTY_JWT_TOKEN(401, "EMPTY_JWT_TOKEN", "헤더에 JWT 토큰이 없습니다."),

	// 요청 포맷 관련
	INVALID_REQUEST_FORMAT(400, "INVALID_REQUEST_FORMAT", "잘못된 요청 형식입니다."),

	// 서버 오류
	INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버에 오류가 발생했습니다."),
	// 파일 관련
	PAYLOAD_TOO_LARGE(413, "PAYLOAD_TOO_LARGE", "파일크기가 혀용된 최대 크기를 초과하였습니다.");

	private final int status;
	private final String code;
	private final String message;

	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
