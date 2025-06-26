package com.dmz.airdnd.reservation.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.ReservationInProgressException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisLockService {

	private final RedissonClient redis;

	private static final int WAIT_TIME = 0; //0초
	private static final int AUTO_LEASE_TIME = 10000; // 10초

	public void executeWithMultiLock(List<String> keys, Runnable task) throws InterruptedException {
		List<RLock> locks = keys.stream()
			.map(redis::getLock)
			.toList();

		RedissonMultiLock multiLock = new RedissonMultiLock(locks.toArray(new RLock[0]));

		boolean locked = multiLock.tryLock(WAIT_TIME, AUTO_LEASE_TIME, TimeUnit.MILLISECONDS);

		if (!locked) {
			throw new ReservationInProgressException(ErrorCode.RESERVATION_IN_PROGRESS);
		}

		try {
			task.run();
		} finally {
			if (multiLock.isHeldByCurrentThread()) {
				multiLock.unlock();
			}
		}
	}
}
