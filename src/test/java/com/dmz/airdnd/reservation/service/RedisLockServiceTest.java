package com.dmz.airdnd.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.dmz.airdnd.common.exception.ReservationInProgressException;

@Testcontainers
class RedisLockServiceTest {

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2").withExposedPorts(6379);

	private RedisLockService redisLockService;

	private static final int THREAD_COUNT = 100;

	private List<String> keys;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		RedissonClient redissonClient = Redisson.create(config);
		redisLockService = new RedisLockService(redissonClient);

		keys = List.of(
			"lock:reservation:1:2025-06-01",
			"lock:reservation:1:2025-06-02"
		);
	}

	@Test
	@DisplayName("동시에 100개의 요청이 들어오면 하나만 락을 획득한다.")
	void success_locking() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
		AtomicInteger successCount = new AtomicInteger();

		for (int i = 0; i < THREAD_COUNT; i++) {
			executor.submit(() -> {
				try {
					redisLockService.executeWithMultiLock(keys, () -> {
						successCount.incrementAndGet();
						try { // 작업 시간이 너무 짧아 지연 로직 추가
							Thread.sleep(50);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					});
				} catch (Exception ignored) {
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executor.shutdown();

		assertThat(successCount.get()).isEqualTo(1);
	}

	@Test
	@DisplayName("이미 락이 걸려있으면 ReservationInProgressException 발생한다.")
	void fail_locking_if_already_locked() throws InterruptedException {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.submit(() -> {
			try {
				redisLockService.executeWithMultiLock(keys, () -> {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		Thread.sleep(10);

		assertThatThrownBy(() -> {
			redisLockService.executeWithMultiLock(keys, () -> {
			});
		}).isInstanceOf(ReservationInProgressException.class);

		executor.shutdown();
	}
}
