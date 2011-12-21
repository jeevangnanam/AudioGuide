package com.nz.audioguide.dataobjects;

import java.util.ArrayList;

public class LandmarkObj extends LocationOb{
	private ArrayList<LocationOb> landmarks;
	
	public ArrayList<LocationOb> getLandmarks() {
		return landmarks;
	}

	public void setLandmarks(ArrayList<LocationOb> landmarks) {
		this.landmarks = landmarks;
	}

	public LandmarkObj(){
		landmarks = new ArrayList<LocationOb>();
	}
}
