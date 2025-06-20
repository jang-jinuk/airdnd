package com.dmz.airdnd.common.auth;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dmz.airdnd.common.auth.dto.LoginRequest;
import com.dmz.airdnd.common.auth.jwt.JwtUtil;
import com.dmz.airdnd.common.exception.DuplicateResourceException;
import com.dmz.airdnd.common.exception.InvalidPasswordException;
import com.dmz.airdnd.common.exception.UserNotFoundException;
import com.dmz.airdnd.fixture.TestLoginRequestFactory;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.dto.request.UserRequest;
import com.dmz.airdnd.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("중복된 유저가 없으면 회원가입을 성공한다.")
	void success_signup() {
		//given
		User user = TestUserFactory.createTestUser(1L);
		UserRequest userRequest = TestUserFactory.createUserRequestFrom(user);
		when(userRepository.existsByLoginId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(userRepository.existsByPhone(anyString())).thenReturn(false);
		when(userRepository.save(any())).thenReturn(user);
		//when
		User newUser = authService.signup(userRequest);
		//then
		verify(userRepository).existsByLoginId(user.getLoginId());
		verify(userRepository).existsByEmail(user.getEmail());
		verify(userRepository).existsByPhone(user.getPhone());
		assertThat(newUser.getLoginId()).isEqualTo(user.getLoginId());
		assertThat(newUser.getEmail()).isEqualTo(user.getEmail());
		assertThat(newUser.getPhone()).isEqualTo(user.getPhone());
		assertThat(newUser.getRole()).isEqualTo(Role.USER);
	}

	@ParameterizedTest
	@MethodSource("provideDuplicateRequests")
	@DisplayName("중복된 필드가 있을 경우 회원가입에 실패한다.")
	void fail_signup(UserRequest request, boolean loginIdResult, boolean emailResult, boolean phoneResult,
		String expectedResult) {
		//given
		// 미리 예외가 발생하면 호출 안할 가능성 있음
		lenient().when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(loginIdResult);
		lenient().when(userRepository.existsByEmail(request.getEmail())).thenReturn(emailResult);
		lenient().when(userRepository.existsByPhone(request.getPhone())).thenReturn(phoneResult);

		//when + then
		assertDuplicateError(() -> authService.signup(request), expectedResult);
	}

	private void assertDuplicateError(Runnable executable, String expectedMessage) {
		assertThatThrownBy(executable::run).isInstanceOf(DuplicateResourceException.class)
			.hasMessageContaining(expectedMessage);
	}

	private static Stream<Arguments> provideDuplicateRequests() {
		return Stream.of(
			Arguments.of(new UserRequest("test123", "pw", "new@example.com", "010-9999-8888"), true, false, false,
				"이미 존재하는 로그인 아이디입니다."), // loginId 중복
			Arguments.of(new UserRequest("newId", "pw", "test@example.com", "010-9999-8888"), false, true, false,
				"이미 존재하는 이메일입니다."),   // email 중복
			Arguments.of(new UserRequest("newId", "pw", "new@example.com", "010-1234-5678"), false, false, true,
				"이미 존재하는 전화번호입니다.")    // phone 중복
		);
	}

	@Test
	@DisplayName("올바른 로그인 정보로 로그인하면 JWT 토큰을 반환한다.")
	void success_login() {
		//given
		LoginRequest loginRequest = TestLoginRequestFactory.createLoginRequest();
		User user = TestUserFactory.createTestUser();
		when(userRepository.findByLoginId(loginRequest.getLoginId())).thenReturn(Optional.of(user));
		when(jwtUtil.generateAccessToken(user)).thenReturn("mock-jwt-token");

		//when
		String token = authService.login(loginRequest);

		//then
		assertThat(token).isEqualTo("mock-jwt-token");
	}

	@Test
	@DisplayName("존재하지 않은 아이디로 로그인하면 로그인에 실패한다.")
	void fail_login_user_not_found() {
		//given
		LoginRequest loginRequest = TestLoginRequestFactory.createLoginRequest();
		when(userRepository.findByLoginId(loginRequest.getLoginId())).thenReturn(Optional.empty());

		//when + then
		assertThatThrownBy(() -> authService.login(loginRequest))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("해당 유저를 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("등록된 비밀번호와 불일치하면 로그인에 실패한다.")
	void fail_login_invalidPassword() {
		//given
		LoginRequest loginRequest = TestLoginRequestFactory.createLoginRequest("invalidPassword1234");
		User user = TestUserFactory.createTestUser();
		when(userRepository.findByLoginId(loginRequest.getLoginId())).thenReturn(Optional.of(user));

		//when + then
		assertThatThrownBy(() -> authService.login(loginRequest))
			.isInstanceOf(InvalidPasswordException.class)
			.hasMessageContaining("잘못된 비밀번호입니다.");
	}
}
