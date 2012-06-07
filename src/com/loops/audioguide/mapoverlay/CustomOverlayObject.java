package com.loops.audioguide.mapoverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayObject extends OverlayItem {
	
	private int position;
	
	public CustomOverlayObject(GeoPoint p, String title, String name, int position){
		super(p, title, name);		
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	
}
