package com.dmz.airdnd.common.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmz.airdnd.common.auth.dto.LoginRequest;
import com.dmz.airdnd.common.auth.jwt.JwtUtil;
import com.dmz.airdnd.common.exception.DuplicateResourceException;
import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.InvalidPasswordException;
import com.dmz.airdnd.common.exception.UserNotFoundException;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.dto.request.UserRequest;
import com.dmz.airdnd.user.mapper.UserMapper;
import com.dmz.airdnd.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public User signup(UserRequest userRequest) {
		if (userRepository.existsByLoginId(userRequest.getLoginId())) {
			throw new DuplicateResourceException(ErrorCode.DUPLICATE_LOGIN_ID);
		}
		if (userRepository.existsByEmail(userRequest.getEmail())) {
			throw new DuplicateResourceException(ErrorCode.DUPLICATE_EMAIL);
		}
		if (userRepository.existsByPhone(userRequest.getPhone())) {
			throw new DuplicateResourceException(ErrorCode.DUPLICATE_PHONE);
		}

		User user = UserMapper.toEntity(userRequest);

		return userRepository.save(user);
	}

	public String login(LoginRequest loginRequest) {
		User user = userRepository.findByLoginId(loginRequest.getLoginId())
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

		validatePassword(user, loginRequest.getPassword());

		return jwtUtil.generateAccessToken(user);
	}

	private void validatePassword(User user, String rawPassword) {
		if (!user.getPassword().equals(rawPassword)) {
			throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);
		}
	}
}
