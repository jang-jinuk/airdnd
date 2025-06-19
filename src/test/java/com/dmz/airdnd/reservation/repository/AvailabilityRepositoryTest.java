package com.dmz.airdnd.reservation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.dmz.airdnd.AbstractContainerBase;
import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;
import com.dmz.airdnd.accommodation.repository.AccommodationRepository;
import com.dmz.airdnd.accommodation.repository.AddressRepository;
import com.dmz.airdnd.fixture.TestAccommodationFactory;
import com.dmz.airdnd.fixture.TestAddressFactory;
import com.dmz.airdnd.fixture.TestAvailabilityFactory;
import com.dmz.airdnd.fixture.TestReservationFactory;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.reservation.domain.Availability;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.repository.UserRepository;

@DataJpaTest
class AvailabilityRepositoryTest extends AbstractContainerBase {

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private AccommodationRepository accommodationRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	private Accommodation accommodation;

	private Reservation reservation;

	@BeforeEach
	void setup() {
		User guest = userRepository.save(TestUserFactory.createTestUser());
		Address address = addressRepository.save(TestAddressFactory.createTestAddress());
		accommodation = accommodationRepository.save(TestAccommodationFactory.createTestAccommodation(address));
		reservation = reservationRepository.save(TestReservationFactory.createTestReservation(guest, accommodation));
	}

	@Test
	@DisplayName("예약 기간 동안 각 날짜에 대한 Availability가 모두 저장된다")
	void success_saveAllAvailability() {
		//given
		List<Availability> availabilities = TestAvailabilityFactory.createTestAvailabilities(accommodation,
			reservation);
		//when
		availabilityRepository.saveAll(availabilities);

		//then
		List<Availability> newAvailabilities = availabilityRepository.findAll();

		List<LocalDate> expectedDates = reservation.getCheckInDate()
			.datesUntil(reservation.getCheckOutDate()) // 체크아웃 전날까지 반복
			.collect(Collectors.toList());

		assertThat(newAvailabilities).extracting(Availability::getDate)
			.containsExactlyInAnyOrderElementsOf(expectedDates);
	}

	@Test
	@DisplayName("예약 기간 동안 같은 날짜에 대한 Availability 중복 저장 시 예외가 발생한다")
	void fail_saveAllAvailability() {
		//given
		List<Availability> fistSave = TestAvailabilityFactory.createTestAvailabilities(accommodation, reservation);
		List<Availability> secondSave = TestAvailabilityFactory.createTestAvailabilities(accommodation, reservation);

		//when: 첫 번째 저장은 정상적으로 수행
		availabilityRepository.saveAll(fistSave);

		//then: 두 번째 저장은 같은 날짜 데이터 중복으로 예외 발생
		assertThatThrownBy(() -> availabilityRepository.saveAll(secondSave)).isInstanceOf(
			DataIntegrityViolationException.class);
	}
}
