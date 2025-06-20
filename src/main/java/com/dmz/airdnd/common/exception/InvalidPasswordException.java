package com.dmz.airdnd.common.exception;

public class InvalidPasswordException extends BaseException {
	public InvalidPasswordException(ErrorCode errorCode) {
		super(errorCode);
	}
}
