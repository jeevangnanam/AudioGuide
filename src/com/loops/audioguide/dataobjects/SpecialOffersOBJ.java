package com.loops.audioguide.dataobjects;

import java.io.Serializable;

public class SpecialOffersOBJ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7794809435655381025L;

	private String id;
	private String advertisesID;
	private String locationID;
	private String title;
	private String summary;
	private String description;
	private String pictureURL;
	private String actualURL;
	private String status;
	private String created;
	private String modified;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getAdvertisesID() {
		return advertisesID;
	}

	public String getLocationID() {
		return locationID;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getDescription() {
		return description;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public String getActualURL() {
		return actualURL;
	}

	public String getStatus() {
		return status;
	}

	public String getCreated() {
		return created;
	}

	public String getModified() {
		return modified;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAdvertisesID(String advertisesID) {
		this.advertisesID = advertisesID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public void setActualURL(String actualURL) {
		this.actualURL = actualURL;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

}
