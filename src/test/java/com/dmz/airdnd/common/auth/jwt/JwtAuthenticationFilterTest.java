package com.dmz.airdnd.common.auth.jwt;

import static org.mockito.Mockito.*;

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

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private FilterChain filterChain;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@ParameterizedTest
	@MethodSource("provideRequestUris")
	@DisplayName("JWT 토큰이 유효하면 필터를 통과하고 HttpServletRequest에 토큰 정보를 저장한다.")
	void success_doFilter(String method, String uri) throws Exception {
		//given
		when(request.getMethod()).thenReturn(method);
		when(request.getRequestURI()).thenReturn(uri);
		when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");

		Claims claims = mock(Claims.class);
		when(claims.getSubject()).thenReturn("1");
		when(claims.get("role")).thenReturn("USER");
		when(jwtUtil.validateToken("valid.token")).thenReturn(claims);

		//when
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		//then
		verify(request).setAttribute("id", "1");
		verify(request).setAttribute("role", "USER");
		verify(filterChain).doFilter(request, response);
	}

	private static Stream<Arguments> provideRequestUris() {
		return Stream.of(
			Arguments.of("GET", "/api/accommodation"),
			Arguments.of("POST", "/api/accommodation"),
			Arguments.of("PATCH", "/api/accommodation/1"),
			Arguments.of("DELETE", "/api/accommodation/1")
		);
	}

	@Test
	@DisplayName("OPTIONS 메소드는 200 반환한다.")
	void success_pass_filter() throws Exception {
		//given
		when(request.getMethod()).thenReturn("OPTIONS");

		//when + then
		jwtAuthenticationFilter.doFilter(request, response, filterChain);
		verify(response).setStatus(HttpServletResponse.SC_OK);
	}

	@ParameterizedTest
	@MethodSource("providePermissionUris")
	@DisplayName("토큰이 없어도 인증 예외 경로는 필터를 통과한다.")
	void success_doFilter_permissionUri(String method, String uri) throws Exception {
		//given
		when(request.getMethod()).thenReturn(method);
		when(request.getRequestURI()).thenReturn(uri);

		//when + then
		jwtAuthenticationFilter.doFilter(request, response, filterChain);
		verify(filterChain).doFilter(request, response);
	}

	private static Stream<Arguments> providePermissionUris() {
		return Stream.of(
			Arguments.of("POST", "/api/auth/signup"),
			Arguments.of("POST", "/api/auth/login")
		);
	}

	@ParameterizedTest
	@MethodSource("provideInvalidTokens")
	@DisplayName("Authorization 헤더가 없으면 401 반환하고, 예외 코드와 메시지를 포함한다.")
	void fail_doFilter(String invalidToken) throws Exception {
		when(request.getMethod()).thenReturn("GET");
		when(request.getRequestURI()).thenReturn("/api/accommodation");
		when(request.getHeader("Authorization")).thenReturn(invalidToken);
		java.io.PrintWriter writer = mock(java.io.PrintWriter.class);
		when(response.getWriter()).thenReturn(writer);

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}

	private static Stream<Arguments> provideInvalidTokens() {
		return Stream.of(
			Arguments.of((Object)null),
			Arguments.of("")
		);
	}
}
