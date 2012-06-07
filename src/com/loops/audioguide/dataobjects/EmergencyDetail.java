package com.loops.audioguide.dataobjects;

public class EmergencyDetail {

	private String catID;
	private String catName;

	private String title;
	private String phone1;
	private String phone2;
	private String phone3;
	private String description;

	public EmergencyDetail() {
	}

	public String getCatID() {
		return catID;
	}

	public String getTitle() {
		return title;
	}

	public String getPhone1() {
		return phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public String getDescription() {
		return description;
	}

	public void setCatID(String catID) {
		this.catID = catID;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

}
