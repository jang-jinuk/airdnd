package com.dmz.airdnd.reservation.domain;

import java.time.LocalDate;

import com.dmz.airdnd.accommodation.domain.Accommodation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "availability", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"accommodation_id", "date"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@ManyToOne(optional = false)
	@JoinColumn(name = "accommodation_id", nullable = false)
	private Accommodation accommodation;

	@ManyToOne(optional = false)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;
}
