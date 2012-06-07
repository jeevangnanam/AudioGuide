package com.loops.audioguide.dataobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class LandmarkObj extends LocationOb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5640193638673376835L;

	private String languageId;

	private ArrayList<LocationOb> landmarks;
	private ArrayList<SpecialOffersOBJ> specialOffers;

	public ArrayList<LocationOb> getLandmarks() {
		return landmarks;
	}

	public void setLandmarks(ArrayList<LocationOb> landmarks) {
		this.landmarks = landmarks;
	}

	public LandmarkObj() {
		landmarks = new ArrayList<LocationOb>();
		specialOffers = new ArrayList<SpecialOffersOBJ>();
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public ArrayList<SpecialOffersOBJ> getSpecialOffers() {
		return specialOffers;
	}

	public void setSpecialOffers(ArrayList<SpecialOffersOBJ> specialOffers) {
		this.specialOffers = specialOffers;
	}
}
