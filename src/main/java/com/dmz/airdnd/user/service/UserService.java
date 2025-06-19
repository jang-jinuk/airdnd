package com.dmz.airdnd.user.service;

import org.springframework.stereotype.Service;

import com.dmz.airdnd.common.exception.ErrorCode;
import com.dmz.airdnd.common.exception.UserNotFoundException;
import com.dmz.airdnd.user.domain.User;
import com.dmz.airdnd.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User getUserFindById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
	}
}
