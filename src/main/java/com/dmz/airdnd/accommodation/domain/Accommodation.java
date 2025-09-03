package com.dmz.airdnd.accommodation.domain;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accommodation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	@BatchSize(size = 30)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "accommodation_label",
		joinColumns = @JoinColumn(name = "accommodation_id"),
		inverseJoinColumns = @JoinColumn(
			name = "label_id",
			columnDefinition = "VARCHAR(50) NOT NULL")
	)
	private List<Label> labels;

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

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Timestamp createdAt;

	private Timestamp updatedAt;
}
