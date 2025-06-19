package com.dmz.airdnd.fixture;

import java.sql.Timestamp;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;

public class TestAccommodationFactory {
	public static Accommodation createTestAccommodation(Address address) {
		return Accommodation.builder()
			.address(address)
			.name("한라산뷰다락룸개인실")
			.description("건물이 목조주택이라 방이 아늑한 느낌입니다.")
			.pricePerDay(63900)
			.currency("KRW")
			.maxGuests(4)
			.bedCount(3)
			.bedroomCount(3)
			.bathroomCount(2)
			.createdAt(new Timestamp(System.currentTimeMillis()))
			.build();
	}

	public static Accommodation createTestAccommodation(Long id, Address address) {
		return Accommodation.builder()
			.id(id)
			.address(address)
			.name("한라산뷰다락룸개인실")
			.description("건물이 목조주택이라 방이 아늑한 느낌입니다.")
			.pricePerDay(63900)
			.currency("KRW")
			.maxGuests(4)
			.bedCount(3)
			.bedroomCount(3)
			.bathroomCount(2)
			.createdAt(new Timestamp(System.currentTimeMillis()))
			.build();
	}
}
