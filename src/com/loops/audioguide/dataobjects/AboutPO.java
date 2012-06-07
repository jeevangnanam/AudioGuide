package com.loops.audioguide.dataobjects;

import java.io.Serializable;

public class AboutPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6282991195385798444L;
	

	private String mTitle;
	private String mContent;
	private String mStatus;
	private String mCreated;
	private String mModified;

	public AboutPO() {
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}

	public String getmStatus() {
		return mStatus;
	}

	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getmCreated() {
		return mCreated;
	}

	public void setmCreated(String mCreated) {
		this.mCreated = mCreated;
	}

	public String getmModified() {
		return mModified;
	}

	public void setmModified(String mModified) {
		this.mModified = mModified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
