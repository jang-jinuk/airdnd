package com.dmz.airdnd.common.exception;

public class DuplicateReservationException extends BaseException {

	public DuplicateReservationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
