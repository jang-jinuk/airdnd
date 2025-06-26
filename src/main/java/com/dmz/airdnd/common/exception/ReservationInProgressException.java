package com.dmz.airdnd.common.exception;

public class ReservationInProgressException extends BaseException {
	public ReservationInProgressException(ErrorCode errorCode) {
		super(errorCode);
	}
}
