package com.dmz.airdnd.accommodation.dto.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeocodeResponse {

	private String status;
	private List<Result> results;

	@Data
	public static class Result {
		private Geometry geometry;
	}

	@Data
	public static class Geometry {
		private Location location;
	}

	@Data
	public static class Location {
		private double latitude;    // 위도
		private double longitude;	// 경도
	}
}
