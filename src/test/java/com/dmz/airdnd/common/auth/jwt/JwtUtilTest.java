package com.dmz.airdnd.common.auth.jwt;

import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.security.KeyPair;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.dmz.airdnd.common.exception.JwtValidationException;
import com.dmz.airdnd.fixture.TestUserFactory;
import com.dmz.airdnd.user.domain.Role;
import com.dmz.airdnd.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

	private static final String testAccessKey = "test-access-secret-key-that-is-long-enough";

	private final JwtUtil jwtUtil = new JwtUtil(testAccessKey);

	@Test
	@DisplayName("정상적으로 JWT Access Token을 생성한다.")
	void success_generateAccessToken() {
		//given
		User user = TestUserFactory.createTestUser(1L);

		//when
		String accessToken = jwtUtil.generateAccessToken(user);

		//then
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(testAccessKey.getBytes(UTF_8)))
			.build()
			.parseClaimsJws(accessToken)
			.getBody();

		assertThat(accessToken).isNotNull();
		assertThat(claims.getSubject()).isEqualTo(String.valueOf(user.getId()));
	}

	@Test
	@DisplayName("정상적인 토큰은 유효성 검사를 통과한다.")
	void success_validateToken() {
		//given
		User user = TestUserFactory.createTestUser();
		String token = jwtUtil.generateAccessToken(user);

		//when
		Claims claims = jwtUtil.validateToken(token);

		//then
		assertThat(String.valueOf(claims.getSubject())).isEqualTo(String.valueOf(user.getId()));
		assertThat(String.valueOf(claims.get("loginId"))).isEqualTo(user.getLoginId());
		assertThat(Role.valueOf(String.valueOf(claims.get("role")))).isEqualTo(user.getRole());
		assertThat(claims.get("imageUrl")).isEqualTo(null);
	}

	@ParameterizedTest
	@MethodSource("provideInvalidJwtToken")
	@DisplayName("비정상적인 토큰은 유효성 검사를 실패한다.")
	void fail_validateToken(String invalidateToken, String expectedResult) {
		//when + then
		assertThatThrownBy(() -> jwtUtil.validateToken(invalidateToken)).isInstanceOf(JwtValidationException.class)
			.hasMessageContaining(expectedResult);
	}

	private static Stream<Arguments> provideInvalidJwtToken() {
		User user = TestUserFactory.createTestUser();

		// 잘못된 서명 토큰
		String invalidSignatureToken = Jwts.builder()
			.setSubject(String.valueOf(user.getId()))
			.signWith(Keys.hmacShaKeyFor("wrong-access-secret-key-that-is-long-enough".getBytes(UTF_8)))
			.compact();

		// 만료된 토큰
		String expiredToken = Jwts.builder()
			.setSubject(String.valueOf(user.getId()))
			.signWith(Keys.hmacShaKeyFor(testAccessKey.getBytes(UTF_8)))
			.setExpiration(new Date(System.currentTimeMillis() - 1000))
			.compact();

		// 지원하지 않는 알고리즘 토큰
		KeyPair rsaKeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
		String unsupportedAlgoToken = Jwts.builder()
			.setSubject(String.valueOf(user.getId()))
			.signWith(rsaKeyPair.getPrivate())
			.compact();

		return Stream.of(
			Arguments.of(invalidSignatureToken, "잘못된 JWT 서명입니다."),
			Arguments.of(expiredToken, "만료된 JWT 토큰입니다."),
			Arguments.of(unsupportedAlgoToken, "지원하지 않는 JWT 토큰입니다."),
			Arguments.of("", "JWT 토큰이 잘못되었습니다."),
			Arguments.of(null, "JWT 토큰이 잘못되었습니다.")
		);
	}
}
