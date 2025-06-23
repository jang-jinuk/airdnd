package com.dmz.airdnd.fixture;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.dmz.airdnd.accommodation.domain.Address;

public class TestAddressFactory {

	private static final GeometryFactory geometryFactory = new GeometryFactory();

	public static Address createTestAddress() {
		Point location = geometryFactory.createPoint(new Coordinate(127.123456, 37.123456)); // 경도, 위도 순서
		location.setSRID(4326);

		return Address.builder()
			.country("대한민국")
			.baseAddress("서울특별시 강남구 테헤란로 123")
			.detailedAddress("ABC 오피스텔 101호")
			.location(location)
			.build();
	}

	public static Address createTestAddress(Long id) {
		Point location = geometryFactory.createPoint(new Coordinate(127.123456, 37.123456));
		location.setSRID(4326);

		return Address.builder()
			.id(id)
			.country("대한민국")
			.baseAddress("서울특별시 강남구 테헤란로 123")
			.detailedAddress("ABC 오피스텔 101호")
			.location(location)
			.build();
	}
}
