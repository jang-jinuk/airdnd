package com.dmz.airdnd.accommodation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.dmz.airdnd.accommodation.domain.Accommodation;
import com.dmz.airdnd.accommodation.dto.FilterCondition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccommodationRepositoryImpl implements AccommodationRepositoryCustom {
	private final EntityManager entityManager;
	private final int RADIUS_METERS = 5000;

	@Override
	public Page<Accommodation> findFilteredAccommodations(Pageable pageable, FilterCondition filterCondition) {
		double lon = filterCondition.longitude();
		double lat = filterCondition.latitude();

		// Bounding Box 계산
		double latDiff = RADIUS_METERS / 111045.0;
		double lonDiff = RADIUS_METERS / (111045.0 * Math.cos(Math.toRadians(lat)));
		double minLat = lat - latDiff;
		double maxLat = lat + latDiff;
		double minLon = lon - lonDiff;
		double maxLon = lon + lonDiff;

		String boundingBox = String.format(
			"POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
			minLat, minLon,
			minLat, maxLon,
			maxLat, maxLon,
			maxLat, minLon,
			minLat, minLon
		);

		String userLocation = String.format("POINT(%f %f)", lon, lat);

		// 데이터 조회 쿼리
		String dataSql = """
			SELECT a.*
			FROM accommodation a
			JOIN address ad ON ad.id = a.address_id
			WHERE MBRContains(ST_GeomFromText(:boundingBox, 4326), ad.location)
			AND a.price_per_day >= :minPrice
			AND a.price_per_day <= :maxPrice
			AND a.max_guests >= :maxGuests
			AND NOT EXISTS (
			    SELECT 1 FROM availability av
			    WHERE av.accommodation_id = a.id
			    AND av.date IN :dates
			)
			AND ST_Distance_Sphere(ad.location, ST_SRID(ST_GeomFromText(:userLocation), 4326)) <= :radius
			ORDER BY a.price_per_day
			LIMIT :limit OFFSET :offset
			""";

		Query dataQuery = entityManager.createNativeQuery(dataSql, Accommodation.class)
			.setParameter("boundingBox", boundingBox)
			.setParameter("userLocation", userLocation)
			.setParameter("minPrice", filterCondition.minPrice())
			.setParameter("maxPrice", filterCondition.maxPrice())
			.setParameter("maxGuests", filterCondition.maxGuests())
			.setParameter("dates", filterCondition.requestedDates())
			.setParameter("radius", RADIUS_METERS)
			.setParameter("limit", pageable.getPageSize())
			.setParameter("offset", pageable.getOffset());

		@SuppressWarnings("unchecked")
		List<Accommodation> accommodations = dataQuery.getResultList();

		String countSql = """
			SELECT COUNT(a.id)
			FROM accommodation a
			JOIN address ad ON ad.id = a.address_id
			WHERE MBRContains(ST_GeomFromText(:boundingBox, 4326), ad.location)
			AND a.price_per_day >= :minPrice
			AND a.price_per_day <= :maxPrice
			AND a.max_guests >= :maxGuests
			AND NOT EXISTS (
			    SELECT 1 FROM availability av
			    WHERE av.accommodation_id = a.id
			    AND av.date IN :dates
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
