package com.nz.audioguide.model;

import java.util.ArrayList;
import java.util.Collection;

public class AudioGuide {

	private String id;
	private String landmark_id;
	private String language_id;
	private String title;
	private String size;
	private String path;
	private String type;
	private String price;
	private String status;
	private String created;
	private String modified;
	private ArrayList<Landmark> landmarks;	
	private String language_name;
	private String language_charSet;
	private String language_created;
	private String language_moded;

	
	public String getId() {
		
		return id;
	}

	public String getLandmark_id() {
		return landmark_id;
	}

	public String getLanguage_id() {
		return language_id;
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

	public String getCreated() {
		return created;
	}

	public String getModified() {
		return modified;
	}

	public ArrayList<Landmark> getLandmarks() {
		return landmarks;
	}

	
	public String getLanguage_name() {
		return language_name;
	}

	public String getLanguage_charSet() {
		return language_charSet;
	}

	public String getLanguage_created() {
		return language_created;
	}

	public String getLanguage_moded() {
		return language_moded;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLandmark_id(String landmark_id) {
		this.landmark_id = landmark_id;
	}

	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
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

	public void setCreated(String created) {
		this.created = created;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setLandmarks(ArrayList<Landmark> landmarks) {
		this.landmarks = landmarks;
	}


	public void setLanguage_name(String language_name) {
		this.language_name = language_name;
	}

	public void setLanguage_charSet(String language_charSet) {
		this.language_charSet = language_charSet;
	}

	public void setLanguage_created(String language_created) {
		this.language_created = language_created;
	}

	public void setLanguage_moded(String language_moded) {
		this.language_moded = language_moded;
	}

}
