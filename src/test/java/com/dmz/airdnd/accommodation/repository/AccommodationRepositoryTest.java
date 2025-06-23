package com.dmz.airdnd.accommodation.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.dmz.airdnd.AbstractContainerBase;
import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.dto.FilterCondition;
import com.dmz.airdnd.common.config.QuerydslConfig;
import com.dmz.airdnd.fixture.TestAccommodationFactory;
import com.dmz.airdnd.fixture.TestAvailabilityFactory;
import com.dmz.airdnd.fixture.TestReservationFactory;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.reservation.domain.Availability;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.reservation.repository.AvailabilityRepository;
import com.dmz.airdnd.reservation.repository.ReservationRepository;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.repository.UserRepository;

@DataJpaTest
@Import(QuerydslConfig.class)
class AccommodationRepositoryTest extends AbstractContainerBase {

	@Autowired
	private AccommodationRepository accommodationRepository;

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@ParameterizedTest
	@MethodSource("filterConditionsProvider")
	@DisplayName("필터링 조건에 따라 가격으로 정렬된 숙소를 조회할 수 있다.")
	void success_findFilteredAccommodations(FilterCondition filterCondition, Pageable pageable, int expectedSize,
		List<String> expectedNames) {
		// given
		User user = TestUserFactory.createTestUser();
		userRepository.save(user);

		List<Accommodation> accommodationList = TestAccommodationFactory.createTestAccommodationList();
		accommodationRepository.saveAll(accommodationList);

		// todo: reservation 생성 로직 좀 더 자세히 작성하고, reservation리스트에 따라 availability를 생성하는 로직을 구현해서 fixture로 만들어야할 듯
		Reservation reservation = reservationRepository.save(TestReservationFactory.createTestReservation(user, accommodationList.get(0)));

		List<Availability> availabilityList = TestAvailabilityFactory.createTestAvailabilities(accommodationList, reservation);
		availabilityRepository.saveAll(availabilityList);

		// when
		Page<Accommodation> accommodations = accommodationRepository.findFilteredAccommodations(pageable,
			filterCondition);

		// then
		List<String> resultNames = accommodations.getContent()
			.stream()
			.map(Accommodation::getName)
			.toList();

		assertThat(accommodations.getTotalElements()).isEqualTo(expectedSize);
		assertThat(resultNames).containsExactlyElementsOf(expectedNames);
	}

	static Stream<Arguments> filterConditionsProvider() {
		return Stream.of(
			// 위치 필터링 및 가격 정렬 + 페이징 체크 (가격 순서는 3 6 5 1 2 4)
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.requestedDates(List.of())
					.build(),
				PageRequest.of(0, 3),
				6,
				List.of("서울 시내 모던룸3", "서울 시내 모던룸6", "서울 시내 모던룸5")
			),
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.requestedDates(List.of())
					.build(),
				PageRequest.of(1, 3),
				6,
				List.of("서울 시내 모던룸1", "서울 시내 모던룸2", "서울 시내 모던룸4")
			),
			// 위치 + 가격 필터링
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.minPrice(50000)
					.maxPrice(60000)
					.requestedDates(List.of())
					.build(),
				PageRequest.of(0, 3),
				1,
				List.of("서울 시내 모던룸1")
			),
			// 위치 + 가격 + 게스트 수 필터링
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.maxGuests(4)
					.requestedDates(List.of())
					.build(),
				PageRequest.of(0, 3),
				3,
				List.of("서울 시내 모던룸6", "서울 시내 모던룸5", "서울 시내 모던룸4")
			),
			Arguments.of(
				FilterCondition.builder()
					.longitude(129.1201036)
					.latitude(35.1610936)
					.minPrice(55000)
					.maxPrice(100000)
					.maxGuests(2)
					.requestedDates(List.of())
					.build(),
				PageRequest.of(0, 3),
				1,
				List.of("부산 광안리 룸")
			),
			// 위치 + 날짜 필터링 + 페이징 체크
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.requestedDates(List.of(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2)))
					.build(),
				PageRequest.of(0, 3),
				4,
				List.of("서울 시내 모던룸3", "서울 시내 모던룸6", "서울 시내 모던룸5")
			),
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.requestedDates(List.of(LocalDate.of(2026, 1, 3), LocalDate.of(2026, 1, 4)))
					.build(),
				PageRequest.of(0, 10),
				4,
				List.of("서울 시내 모던룸6", "서울 시내 모던룸5", "서울 시내 모던룸2", "서울 시내 모던룸4")
			),
			Arguments.of(
				FilterCondition.builder()
					.longitude(127.0629804)
					.latitude(37.4966645)
					.requestedDates(List.of(LocalDate.of(2026, 1, 1)))
					.build(),
				PageRequest.of(0, 10),
				4,
				List.of("서울 시내 모던룸3", "서울 시내 모던룸6", "서울 시내 모던룸5", "서울 시내 모던룸4")
			)
		);
	}
}
