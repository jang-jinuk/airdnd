package com.dmz.airdnd.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByLoginId(String loginId);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	Optional<User> findByLoginId(String loginId);
}
