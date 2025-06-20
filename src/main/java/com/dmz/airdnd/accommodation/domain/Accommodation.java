package com.dmz.airdnd.accommodation.domain;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Accommodation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private long pricePerDay;

	@Column(nullable = false, length = 50)
	private String currency;

	@Column(nullable = false)
	private int maxGuests;

	@Column(nullable = false)
	private int bedCount;

	@Column(nullable = false)
	private int bedroomCount;

	@Column(nullable = false)
	private int bathroomCount;

	@Column(nullable = false)
	private Timestamp createdAt;

	private Timestamp updatedAt;
}

