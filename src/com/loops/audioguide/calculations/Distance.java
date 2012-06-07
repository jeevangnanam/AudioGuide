package com.loops.audioguide.calculations;

import android.location.Location;
import android.util.Log;

public class Distance {

	
	public static final double PI = 3.14159265;
    public static final double deg2radians = PI/180.0;
    public static final double miles = 0.000621371192;
    public static final double kms = 0.001;
    
	public static double getDistance(double latitude1, double longitude1, double latitude2,double longitude2) {

		 double distance;  
		  
		 Location locationA = new Location("point A");  
		   
		 locationA.setLatitude(latitude1);  
		 locationA.setLongitude(longitude1);  
		   
		 Location locationB = new Location("point B");  
		   
		 locationB.setLatitude(latitude2);  
		 locationB.setLongitude(longitude2);  
		   
		 distance = locationA.distanceTo(locationB); 
		 double radd = distance * kms;
	
		 return radd;
	}
	
	
}
