package com.dmz.airdnd.common.exception;

public class DuplicateResourceException extends BaseException {
	public DuplicateResourceException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DuplicateResourceException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
