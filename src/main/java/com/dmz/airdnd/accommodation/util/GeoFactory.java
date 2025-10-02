package com.dmz.airdnd.accommodation.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

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
}
