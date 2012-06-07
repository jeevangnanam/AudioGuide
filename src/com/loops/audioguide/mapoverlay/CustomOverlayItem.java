package com.loops.audioguide.mapoverlay;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.loops.audioguide.callback.AudioGuideClickListener;



public class CustomOverlayItem extends ItemizedOverlay {

	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private AudioGuideClickListener ie;
	
	
	
	public CustomOverlayItem(Drawable defaultMarker) {
		 super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}	

	public CustomOverlayItem(Drawable defaultMarker, Context context, AudioGuideClickListener ie) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.ie = ie;		
	}
	

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public ArrayList<OverlayItem> getmOverlays() {
		return mOverlays;
	}
	
	@Override
	protected boolean onTap(int index) {
		try {
			CustomOverlayObject item = (CustomOverlayObject) mOverlays
					.get(index);
			
			ie.displayInfo(item.getPosition());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public void clearOverlays(){
		mOverlays.clear();
		populate();
	}
		

}
