package com.dmz.airdnd.accommodation.adapter;

public interface GeocodingClient {

	double[] lookupCoordinates(String baseAddress);
}
