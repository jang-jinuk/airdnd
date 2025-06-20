package com.dmz.airdnd.common.exception;

public class JwtValidationException extends BaseException {
	public JwtValidationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
