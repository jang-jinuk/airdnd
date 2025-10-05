package com.dmz.airdnd.accommodation.repository;

import java.time.LocalDate;
import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.domain.QAccommodation;
import com.dmz.airdnd.accommodation.dto.FilterCondition;
import com.dmz.airdnd.accommodation.util.GeoFactory;
import com.dmz.airdnd.reservation.domain.QAvailability;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	private final int RADIUS_METERS = 5000; // 5km radius

	@Override
	public Page<Accommodation> findFilteredAccommodations(Pageable pageable, FilterCondition filterCondition) {
		QAccommodation accommodation = QAccommodation.accommodation;
		QAvailability availability = QAvailability.availability;

		double lon = filterCondition.longitude();
		double lat = filterCondition.latitude();

		Integer minPrice = filterCondition.minPrice();
		Integer maxPrice = filterCondition.maxPrice();
		Integer maxGuests = filterCondition.maxGuests();
		List<LocalDate> requestedDates = filterCondition.requestedDates();

		Point userLocation = GeoFactory.createPoint(lon, lat);
		Polygon boundingBox = GeoFactory.createBoundingBox(lon, lat);

		List<Accommodation> accommodations = queryFactory
			.selectFrom(accommodation)
			.join(accommodation.address).fetchJoin()
			.where(
				Expressions.numberTemplate(
					Integer.class,
					"MBRContains({0}, {1})",
					Expressions.constant(boundingBox),
					accommodation.address.location
				).eq(1),
				minPrice != null ? accommodation.pricePerDay.goe(minPrice) : null,
				maxPrice != null ? accommodation.pricePerDay.loe(maxPrice) : null,
				maxGuests != null ? accommodation.maxGuests.goe(maxGuests) : null,
				JPAExpressions.selectOne()
					.from(availability)
					.where(
						availability.accommodation.eq(accommodation),
						availability.date.in(requestedDates)
					)
					.notExists(),
				Expressions.booleanTemplate(
					"ST_Distance_Sphere({0}, {1}) <= {2}",
					accommodation.address.location,
					Expressions.constant(userLocation),
					RADIUS_METERS
				)
			)
			.orderBy(accommodation.pricePerDay.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(accommodation.count())
			.from(accommodation)
			.where(
				Expressions.numberTemplate(
					Integer.class,
					"MBRContains({0}, {1})",
					Expressions.constant(boundingBox),
					accommodation.address.location
				).eq(1),
				minPrice != null ? accommodation.pricePerDay.goe(minPrice) : null,
				maxPrice != null ? accommodation.pricePerDay.loe(maxPrice) : null,
				maxGuests != null ? accommodation.maxGuests.goe(maxGuests) : null,
				JPAExpressions.selectOne()
					.from(availability)
					.where(
						availability.accommodation.eq(accommodation),
						availability.date.in(requestedDates)
					)
					.notExists(),
				Expressions.booleanTemplate(
					"ST_Distance_Sphere({0}, {1}) <= {2}",
					accommodation.address.location,
					Expressions.constant(userLocation),
					RADIUS_METERS
				)
			)
			.fetchOne();

		long safeTotal = total != null ? total : 0L;

		return new PageImpl<>(accommodations, pageable, safeTotal);
	}
}
