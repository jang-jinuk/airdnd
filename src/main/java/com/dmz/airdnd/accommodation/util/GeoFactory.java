package com.dmz.airdnd.accommodation.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GeoFactory {

	private static final org.locationtech.jts.geom.GeometryFactory geometryFactory =
		new org.locationtech.jts.geom.GeometryFactory(
			new org.locationtech.jts.geom.PrecisionModel(), 4326
		);

	// longitude : 경도
	// latitude : 위도
	public static Point createPoint(double longitude, double latitude) {
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}

	public static Polygon createBoundingBox(double longitude, double latitude) {
		double radiusKm = 5.0;

		// 위도 1도당 약 111km
		double lonDiff = radiusKm / (111.045 * Math.cos(Math.toRadians(longitude)));
		double latDiff = radiusKm / 111.045;

		double minLon = longitude - latDiff;
		double maxLon = longitude + latDiff;
		double minLat = latitude - lonDiff;
		double maxLat = latitude + lonDiff;

		Coordinate[] coordinates = new Coordinate[] {
			new Coordinate(minLon, minLat),
			new Coordinate(maxLon, minLat),
			new Coordinate(maxLon, maxLat),
			new Coordinate(minLon, maxLat),
			new Coordinate(minLon, minLat)
		};

		return geometryFactory.createPolygon(coordinates);
	}
}
