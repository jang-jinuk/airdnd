package com.dmz.airdnd.user.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dmz.airdnd.AbstractContainerBase;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.user.domain.User;

@DataJpaTest
public class UserRepositoryTest extends AbstractContainerBase {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("유저를 저장할 수 있다.")
	void testSaveUser() {
		// given
		User user = TestUserFactory.createTestUser();

		// when
		User newUser = userRepository.save(user);

		// then
		assertThat(newUser.getLoginId()).isEqualTo("testUser");
	}

	@Test
	@DisplayName("저장한 유저를 조회할 수 있다.")
	void testFindUser() {
		// given
		User user = TestUserFactory.createTestUser();

		// when
		userRepository.save(user);
		Optional<User> result = userRepository.findByLoginId("testUser");

		// then
		assertThat(result).isPresent();
		assertThat(result.get().getLoginId()).isEqualTo("testUser");
	}
}
