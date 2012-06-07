package com.loops.audioguide.model;

import java.util.ArrayList;
import java.util.List;

import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LocationOb;

public class LandmarkModel {
	
	private static LandmarkModel instance = new LandmarkModel();
	
	private LandmarkObj Locations;

	public LandmarkObj getLandmarks() {
		return Locations;
	}

	public void setLandmark(LandmarkObj locations) {
		this.Locations = locations;
	}

	public LandmarkModel(){	
		Locations = new LandmarkObj();
	}

	public static LandmarkModel getInstance() {
		return instance;
	}
		
	
}
