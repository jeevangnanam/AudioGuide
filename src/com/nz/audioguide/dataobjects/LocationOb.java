package com.nz.audioguide.dataobjects;

import java.util.ArrayList;

public class LocationOb {
	
	private String _id;
	private String mName;
	private String mDesc;
	private String mLat;
	private String mLong;
	private String mArea;
	private String mImage;
	private String mStatus;
	private String mCreated;
	private String mModified;
	
	
	public LocationOb(){}
		
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public String getDesc() {
		return mDesc;
	}
	public void setDesc(String mDesc) {
		this.mDesc = mDesc;
	}
	public String getLat() {
		return mLat;
	}
	public void setLat(String mLat) {
		this.mLat = mLat;
	}
	public String getLong() {
		return mLong;
	}
	public void setLong(String mLong) {
		this.mLong = mLong;
	}
	public String getArea() {
		return mArea;
	}
	public void setArea(String mArea) {
		this.mArea = mArea;
	}
	public String getImage() {
		return mImage;
	}
	public void setImage(String mImage) {
		this.mImage = mImage;
	}
	public String getStatus() {
		return mStatus;
	}
	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public String getCreated() {
		return mCreated;
	}
	public void setCreated(String mCreated) {
		this.mCreated = mCreated;
	}
	public String getModified() {
		return mModified;
	}
	public void setModified(String mModified) {
		this.mModified = mModified;
	}
	
}
