package com.dmz.airdnd.accommodation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.accommodation.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByBaseAddressAndDetailedAddress(String baseAddress, String detailedAddress);
}
