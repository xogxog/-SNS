package com.ssafy.homesns.dto;

public class LocationDto {
	
	private int locationId;
	private int groupId;
	private String locationName;
	private float lat;
	private float lng;
	
	private boolean isFavorite;

	
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	@Override
	public String toString() {
		return "LocationDto [locationId=" + locationId + ", groupId=" + groupId + ", locationName=" + locationName
				+ ", lat=" + lat + ", lng=" + lng + ", isFavorite=" + isFavorite + "]";
	}
	
	
}
