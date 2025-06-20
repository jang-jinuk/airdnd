package com.dmz.airdnd.reservation.domain;

import java.sql.Date;
import java.time.LocalDateTime;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "guest_id", nullable = false)
	private User guest;

	@ManyToOne(optional = false)
	@JoinColumn(name = "accommodation_id", nullable = false)
	private Accommodation accommodation;

	@Column(nullable = false)
	private Date checkInDate;

	@Column(nullable = false)
	private Date checkOutDate;

	@Column(nullable = false)
	private int numberOfGuests;

	@Column(nullable = false)
	private long totalPrice;

	@Column(nullable = false, length = 20)
	private ReservationStatus status;

	@Column(nullable = false, length = 50)
	private String timezone;

	@Column(nullable = false, length = 50)
	private String currency;

	@Column(nullable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
