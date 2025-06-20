package com.dmz.airdnd.common.auth;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.dmz.airdnd.common.auth.dto.LoginRequest;
import com.dmz.airdnd.common.auth.jwt.JwtUtil;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.user.dto.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /api/auth/signup 성공 시 201 반환하고 서비스 호출")
  void success_signup() throws Exception {
    // given
    UserRequest validRequest = UserRequest.builder()
        .loginId("user123")
        .password("pass!234")
        .email("user@example.com")
        .phone("01012345678")
        .build();
    String json = objectMapper.writeValueAsString(validRequest);

		when(authService.signup(any())).thenReturn(TestUserFactory.createTestUser());

		// when & then
		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").isEmpty())
			.andExpect(jsonPath("$.error").isEmpty());

		// service 호출 확인
		verify(authService).signup(any(UserRequest.class));
	}

  @ParameterizedTest
  @MethodSource("invalidSignUpRequests")
  @DisplayName("POST /api/auth/signup 유효성 검사 실패 시 400 반환하고, 예외 코드와 메시지를 포함한다.")
  void fail_signup(UserRequest invalidRequest, String message) throws Exception {
    // given
    String invalidJson = objectMapper.writeValueAsString(invalidRequest);

		mockMvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.data").isEmpty())
			.andExpect(jsonPath("$.error").isNotEmpty())
			.andExpect(jsonPath("$.error.code").value("INVALID_REQUEST_FORMAT"))
			.andExpect(jsonPath("$.error.message").value(message));

		// 서비스 호출 여부 확인
		verify(authService, never()).signup(any(UserRequest.class));
	}

  static Stream<Arguments> invalidSignUpRequests() {
    return Stream.of(
        // 1) loginId 빈 값
        Arguments.of(
            UserRequest.builder()
                .loginId("     ")
                .password("validPass123")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "로그인 아이디는 필수 입력 항목입니다."
        ),

        // 2) loginId 너무 짧음
        Arguments.of(
            UserRequest.builder()
                .loginId("abc")
                .password("validPass123")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "로그인 아이디는 5~25자 이내여야 합니다."
        ),

        // 3) loginId 너무 김
        Arguments.of(
            UserRequest.builder()
                .loginId("abcdefghijklmnpqrsabcdefghijklmnpqrsabcdefghijklmnpqrs")
                .password("validPass123")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "로그인 아이디는 5~25자 이내여야 합니다."
        ),

        // 4) password 빈 값
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("     ")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "비밀번호는 필수 입력 항목입니다."
        ),

        // 5) password 너무 짧음
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("123")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "비밀번호는 5~25자 이내여야 합니다."
        ),

        // 6) password 너무 김
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("1234567890123456789012345678901")
                .email("test@example.com")
                .phone("01012345678")
                .build(),
            "비밀번호는 5~25자 이내여야 합니다."
        ),

        // 7) email null 값
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email(null)
                .phone("01012345678")
                .build(),
            "이메일은 필수 입력 항목입니다."
        ),

        // 8) email 형식 오류
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("invalid-email")
                .phone("01012345678")
                .build(),
            "올바른 이메일 형식이어야 합니다."
        ),

        // 9) email 너무 짧음
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("a@b.c")
                .phone("01012345678")
                .build(),
            "이메일은 6~25자 이내여야 합니다."
        ),

        // 10) email 너무 김
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("abcdabcdabcdabcdabcdabcd@example.com")
                .phone("01012345678")
                .build(),
            "이메일은 6~25자 이내여야 합니다."
        ),

        // 11) phone 빈 값
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("test@example.com")
                .phone("        ")
                .build(),
            "전화번호는 필수 입력 항목입니다."
        ),

        // 12) phone 너무 짧음
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("test@example.com")
                .phone("0101234")
                .build(),
            "전화번호는 8~25자 이내여야 합니다."
        ),

        // 13) phone 너무 김
        Arguments.of(
            UserRequest.builder()
                .loginId("validLogin")
                .password("validPass123")
                .email("test@example.com")
                .phone("123456123456123456123456123456")
                .build(),
            "전화번호는 8~25자 이내여야 합니다."
        )
    );
  }

  @Test
  @DisplayName("POST /api/auth/login 성공 시 201 반환하고 서비스 호출")
  void success_login() throws Exception {
    // given
    LoginRequest validRequest = LoginRequest.builder()
        .loginId("user123")
        .password("pass!234")
        .build();
    String json = objectMapper.writeValueAsString(validRequest);

    String token = "Mock-JWT-Token";
    when(authService.login(any())).thenReturn(token);

		// when & then
		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").value(token))
			.andExpect(jsonPath("$.error").isEmpty());

		// service 호출 확인
		verify(authService).login(any(LoginRequest.class));
	}

  @ParameterizedTest
  @MethodSource("provideInvalidLoginRequests")
  @DisplayName("POST /api/auth/login 유효성 검사 실패 시 400 반환")
  void fail_login_validation(LoginRequest invalidRequest, String message) throws Exception {
    // given
    String invalidJson = objectMapper.writeValueAsString(invalidRequest);

    mockMvc.perform(
            post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.error").isNotEmpty())
        .andExpect(jsonPath("$.error.code").value("INVALID_REQUEST_FORMAT"))
        .andExpect(jsonPath("$.error.message").value(message));

		verify(authService, never()).login(any(LoginRequest.class));
	}

  private static Stream<Arguments> provideInvalidLoginRequests() {
    return Stream.of(
        Arguments.of(LoginRequest.builder()
                .loginId("")
                .password("validPass123")
                .build(),
            "아이디를 입력해주세요."),
        Arguments.of(LoginRequest.builder()
                .loginId("validLogin")
                .password("")
                .build(),
            "비밀번호를 입력해주세요.")
    );
  }
}
