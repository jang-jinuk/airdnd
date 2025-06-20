package com.dmz.airdnd.reservation.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.dmz.airdnd.AbstractContainerBase;
import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.Address;
import com.dmz.airdnd.accommodation.repository.AccommodationRepository;
import com.dmz.airdnd.accommodation.repository.AddressRepository;
import com.dmz.airdnd.common.config.QuerydslConfig;
import com.dmz.airdnd.fixture.TestAccommodationFactory;
import com.dmz.airdnd.fixture.TestAddressFactory;
import com.dmz.airdnd.fixture.TestReservationFactory;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.reservation.domain.Reservation;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.repository.UserRepository;

@DataJpaTest
@Import(QuerydslConfig.class)
class ReservationRepositoryTest extends AbstractContainerBase {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccommodationRepository accommodationRepository;

	@Autowired
	private AddressRepository addressRepository;

	private User guest;

	private Accommodation accommodation;

	@BeforeEach
	void setup() {
		guest = userRepository.save(TestUserFactory.createTestUser());
		Address address = addressRepository.save(TestAddressFactory.createTestAddress());
		accommodation = accommodationRepository.save(TestAccommodationFactory.createTestAccommodation(address));
	}

	@Test
	@DisplayName("예약을 등록할 수 있다.")
	void success_saveReservation() {
		//given
		Reservation reservation = TestReservationFactory.createTestReservation(guest, accommodation);

		//when
		Reservation newReservation = reservationRepository.save(reservation);

		//then
		assertThat(newReservation.getId()).isNotNull();
		assertThat(newReservation.getCheckInDate()).isEqualTo(reservation.getCheckInDate());
		assertThat(newReservation.getCheckOutDate()).isEqualTo(reservation.getCheckOutDate());
		assertThat(newReservation.getStatus()).isEqualTo(reservation.getStatus());
		assertThat(newReservation.getCurrency()).isEqualTo(reservation.getCurrency());
		assertThat(newReservation.getTotalPrice()).isEqualTo(reservation.getTotalPrice());
	}
}
