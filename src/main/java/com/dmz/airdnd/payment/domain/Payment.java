package com.dmz.airdnd.payment.domain;

import java.time.LocalDateTime;

import com.dmz.airdnd.reservation.domain.Reservation;
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
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;

	@Column(nullable = false)
	private long amount;

	@Column(nullable = false, length = 100)
	private String paymentKey;

	@Column(nullable = false, length = 50)
	private String pgProvider;

	@Column(nullable = false, length = 30)
	private String method;

	@Column(nullable = false, length = 50)
	private String currency;

	@Column(nullable = false, length = 20)
	private PaymentStatus status;

	private LocalDateTime paidAt;

	@Column(length = 255)
	private String failReason;

	@Column(length = 255)
	private String receiptUrl;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}

