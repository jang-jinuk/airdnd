package com.dmz.airdnd.fixture;

import java.sql.Timestamp;
import java.util.List;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;
import com.dmz.airdnd.accommodation.util.GeometryFactory;

public class TestAccommodationFactory {
	public static Accommodation createTestAccommodation(Long id) {
		return Accommodation.builder()
			.id(id)
			.address(Address.builder()
				.country("KR")
				.baseAddress("서울특별시 강남구")
				.location(GeometryFactory.createPoint(127.0629804, 37.4966645))
				.build())
			.labels(List.of())
			.name("서울 시내 모던룸1")
			.description("조용하고 따뜻한 다락방")
			.pricePerDay(55000)
			.currency("KRW")
			.maxGuests(2)
			.bedCount(1)
			.bedroomCount(1)
			.bathroomCount(1)
			.createdAt(now())
			.build();
	}

	public static Accommodation createTestAccommodation(Address address) {
		return Accommodation.builder()
			.address(address)
			.labels(List.of())
			.name("서울 시내 모던룸1")
			.description("조용하고 따뜻한 다락방")
			.pricePerDay(55000)
			.currency("KRW")
			.maxGuests(2)
			.bedCount(1)
			.bedroomCount(1)
			.bathroomCount(1)
			.createdAt(now())
			.build();
	}

	public static Accommodation createTestAccommodation1() { // TODO : 숙소에 라벨 추가하기, 라벨 enum으로 관리
		return Accommodation.builder()
			.address(Address.builder()
				.country("KR")
				.baseAddress("서울특별시 강남구")
				.location(GeometryFactory.createPoint(127.0629804, 37.4966645))
				.build())
			.labels(List.of())
			.name("서울 시내 모던룸1")
			.description("조용하고 따뜻한 다락방")
			.pricePerDay(55000)
			.currency("KRW")
			.maxGuests(2)
			.bedCount(1)
			.bedroomCount(1)
			.bathroomCount(1)
			.createdAt(now())
			.build();
	}

	public static List<Accommodation> createTestAccommodationList() {
		return List.of(
			Accommodation.builder()
				.address(Address.builder()
					.country("KR")
					.baseAddress("서울특별시 강남구")
					.location(GeometryFactory.createPoint(127.0629804, 37.4966645))
					.build())
				.name("서울 시내 모던룸1")
				.description("조용하고 따뜻한 다락방")
				.pricePerDay(55000)
				.currency("KRW")
				.maxGuests(1)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("서울특별시 강남구")
					.country("KR")
					.location(GeometryFactory.createPoint(127.0629805, 37.4966646))
					.build())
				.name("서울 시내 모던룸2")
				.description("지하철과 가까운 깔끔한 원룸")
				.pricePerDay(73000)
				.currency("KRW")
				.maxGuests(2)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("서울특별시 강남구")
					.country("KR")
					.location(GeometryFactory.createPoint(127.0629806, 37.4966644))
					.build())
				.name("서울 시내 모던룸3")
				.description("지하철과 가까운 깔끔한 원룸")
				.pricePerDay(7000)
				.currency("KRW")
				.maxGuests(3)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("서울특별시 강남구")
					.country("KR")
					.location(GeometryFactory.createPoint(127.0629805, 37.4966636))
					.build())
				.name("서울 시내 모던룸4")
				.description("지하철과 가까운 깔끔한 원룸")
				.pricePerDay(730000)
				.currency("KRW")
				.maxGuests(4)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("서울특별시 강남구")
					.country("KR")
					.location(GeometryFactory.createPoint(127.0629805, 37.4966626))
					.build())
				.name("서울 시내 모던룸5")
				.description("지하철과 멉니다")
				.pricePerDay(44000)
				.currency("KRW")
				.maxGuests(5)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("서울특별시 강남구")
					.country("KR")
					.location(GeometryFactory.createPoint(127.0629804, 37.4966646))
					.build())
				.name("서울 시내 모던룸6")
				.description("지하철 버스 모두 가까운 깔끔한 원룸")
				.pricePerDay(13000)
				.currency("KRW")
				.maxGuests(6)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("강원도 강릉시")
					.country("KR")
					.location(GeometryFactory.createPoint(129.1741038, 35.1719465))
					.build())
				.name("강릉 오션뷰 하우스")
				.description("바다가 보이는 테라스가 특징인 숙소")
				.pricePerDay(98000)
				.currency("KRW")
				.maxGuests(4)
				.bedCount(2)
				.bedroomCount(2)
				.bathroomCount(2)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("경상북도 경주시")
					.country("KR")
					.location(GeometryFactory.createPoint(129.206998, 35.8447433))
					.build())
				.name("경주 전통 한옥")
				.description("고즈넉한 분위기의 한옥 숙소")
				.pricePerDay(89000)
				.currency("KRW")
				.maxGuests(3)
				.bedCount(2)
				.bedroomCount(2)
				.bathroomCount(1)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("제주특별자치도 서귀포시")
					.country("KR")
					.location(GeometryFactory.createPoint(126.5609945, 33.2532177))
					.build())
				.name("제주 중문 풀빌라")
				.description("야외 수영장과 정원이 있는 풀빌라")
				.pricePerDay(210000)
				.currency("KRW")
				.maxGuests(6)
				.bedCount(3)
				.bedroomCount(3)
				.bathroomCount(2)
				.createdAt(now())
				.build(),

			Accommodation.builder()
				.address(Address.builder()
					.baseAddress("부산광역시 수영구")
					.country("KR")
					.location(GeometryFactory.createPoint(129.1201036, 35.1610936))
					.build())
				.name("부산 광안리 룸")
				.description("광안대교 야경이 멋진 숙소")
				.pricePerDay(67000)
				.currency("KRW")
				.maxGuests(2)
				.bedCount(1)
				.bedroomCount(1)
				.bathroomCount(1)
				.createdAt(now())
				.build()
		);
	}

	private static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
}
