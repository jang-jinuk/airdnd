package com.dmz.airdnd.accommodation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String country;

	@Column(nullable = false, length = 50)
	private String city;

	@Column(length = 50)
	private String state;

	@Column(length = 20)
	private String postalCode;

	@Column(length = 255)
	private String addressLine1;

	@Column(length = 255)
	private String addressLine2;

	@Column(nullable = false)
	private String location;
}

