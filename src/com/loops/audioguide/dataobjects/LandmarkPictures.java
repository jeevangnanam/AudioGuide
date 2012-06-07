package com.loops.audioguide.dataobjects;

public class LandmarkPictures {
	
	private String locationID;	
	private String landmarkID;
	private String imageURL;
	
	public LandmarkPictures(){
		
	}
	
	public String getLandmarkID() {
		return landmarkID;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setLandmarkID(String landmarkID) {
		this.landmarkID = landmarkID;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
}
