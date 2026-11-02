package com.dmz.airdnd.accommodation.repository;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.dto.FilterCondition;

import com.dmz.airdnd.accommodation.util.GeoFactory;
import com.dmz.airdnd.reservation.domain.QAvailability;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {
	private final EntityManager entityManager;
	private final int RADIUS_METERS = 5000;

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
				Expressions.booleanTemplate("mbrcontains({0}, {1})", boundingBox, accommodation.address.location),
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
				Expressions.booleanTemplate("mbrcontains({0}, {1})", boundingBox, accommodation.address.location),
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
			AND ST_Distance_Sphere(ad.location, ST_SRID(ST_GeomFromText(:userLocation), 4326)) <= :radius
			""";

		Query countQuery = entityManager.createNativeQuery(countSql)
			.setParameter("boundingBox", boundingBox)
			.setParameter("userLocation", userLocation)
			.setParameter("minPrice", filterCondition.minPrice())
			.setParameter("maxPrice", filterCondition.maxPrice())
			.setParameter("maxGuests", filterCondition.maxGuests())
			.setParameter("dates", filterCondition.requestedDates())
			.setParameter("radius", RADIUS_METERS);

		long total = ((Number)countQuery.getSingleResult()).longValue();

		return new PageImpl<>(accommodations, pageable, total);
	}
}
