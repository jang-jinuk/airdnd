package com.dmz.airdnd.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dmz.airdnd.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
