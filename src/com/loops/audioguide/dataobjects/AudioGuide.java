package com.loops.audioguide.dataobjects;

import java.util.ArrayList;
import java.util.Collection;


public class AudioGuide {

	private String aid; 
	private String location_id;
	private String landmark_id;
	private String title;
	private String size;
	private String path;
	private String type;
	private String price;
	private String status;
	private String created_date;
	private String modified_date;
	private String languageid;
	
	
	public String getLanguageid() {
		return languageid;
	}

	public void setLanguageid(String languageid) {
		this.languageid = languageid;
	}

	public AudioGuide(){}
	
	public String getAid() {
		return aid;
	}
	public String getLocation_id() {
		return location_id;
	}
	public String getLandmark_id() {
		return landmark_id;
	}
	public String getTitle() {
		return title;
	}
	public String getSize() {
		return size;
	}
	public String getPath() {
		return path;
	}
	public String getType() {
		return type;
	}
	public String getPrice() {
		return price;
	}
	public String getStatus() {
		return status;
	}
	public String getCreated_date() {
		return created_date;
	}
	public String getModified_date() {
		return modified_date;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public void setLandmark_id(String landmark_id) {
		this.landmark_id = landmark_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}
	
	

}
