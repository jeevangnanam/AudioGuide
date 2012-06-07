package com.loops.audioguide.model;

import java.util.ArrayList;
import java.util.List;

import com.loops.audioguide.dataobjects.LocationOb;

public class LocationModel {
	
	private static LocationModel instance = new LocationModel();
	
	private List<LocationOb> Locations;
	private int lastAccessedLocationId;
		

	public int getLastAccessedLocationId() {
		return lastAccessedLocationId;
	}

	public void setLastAccessedLocationId(int lastAccessedLocationId) {
		this.lastAccessedLocationId = lastAccessedLocationId;
	}

	public List<LocationOb> getLocations() {
		return Locations;
	}

	public void setLocation(List<LocationOb> locations) {
		this.Locations = locations;
	}

	public LocationModel(){	
		Locations = new ArrayList<LocationOb>();
	}

	public static LocationModel getInstance() {
		return instance;
	}
		
	
}
