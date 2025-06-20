package com.dmz.airdnd.common.exception;

public class InvalidFilterConditionException extends BaseException {
	public InvalidFilterConditionException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidFilterConditionException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}
}
