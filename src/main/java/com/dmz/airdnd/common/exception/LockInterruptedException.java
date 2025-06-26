package com.dmz.airdnd.common.exception;

public class LockInterruptedException extends BaseException {
	public LockInterruptedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
