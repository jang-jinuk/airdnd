package com.dmz.airdnd.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.reservation.domain.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
