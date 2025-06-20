package com.dmz.airdnd.common.auth.jwt;

import static com.dmz.airdnd.common.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dmz.airdnd.common.exception.JwtValidationException;
import com.dmz.airdnd.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	private final String accessSecretKey;
	private static final String CLAIM_LOGIN_ID = "loginId";
	private static final String CLAIM_ROLE = "role";
	private static final String CLAIM_IMAGE_URL = "imageUrl";

	public static final long ACCESS_EXPIRATION_TIME = 86400000;

	public JwtUtil(@Value("${spring.jwt.access-key}") String accessSecretKey) {
		this.accessSecretKey = accessSecretKey;
	}

	public String generateAccessToken(User loginUser) {
		return Jwts.builder()
			.setSubject(String.valueOf(loginUser.getId()))
			.claim(CLAIM_LOGIN_ID, loginUser.getLoginId())
			.claim(CLAIM_ROLE, loginUser.getRole())
			.claim(CLAIM_IMAGE_URL, loginUser.getImageUrl())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
			.signWith(Keys.hmacShaKeyFor(accessSecretKey.getBytes(UTF_8)), SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims validateToken(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(accessSecretKey.getBytes(UTF_8)))
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			throw new JwtValidationException(INVALID_JWT_SIGNATURE);
		} catch (ExpiredJwtException e) {
			throw new JwtValidationException(EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new JwtValidationException(UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new JwtValidationException(INVALID_JWT_TOKEN);
		}
	}
}
