package com.nz.audioguide.model;

import java.util.ArrayList;
import java.util.List;
import com.nz.audioguide.dataobjects.LocationOb;

public class LocationModel {
	
	private static LocationModel instance = new LocationModel();
	
	private List<LocationOb> Locations;

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
