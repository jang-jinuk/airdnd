package com.dmz.airdnd.accommodation.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeocodeResponse {

	private String status;
	private List<Result> results;

	@Getter
	@Setter
	public static class Result {
		private Geometry geometry;
	}

	@Getter
	@Setter
	public static class Geometry {
		private Location location;
	}

	@Getter
	@Setter
	public static class Location {
		private double latitude;    // 위도
		private double longitude;	// 경도
	}
}
