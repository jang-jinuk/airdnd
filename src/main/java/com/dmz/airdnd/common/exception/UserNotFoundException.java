package com.dmz.airdnd.common.exception;

public class UserNotFoundException extends BaseException {
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
