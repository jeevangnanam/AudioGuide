package com.loops.audioguide.adapters;

import java.util.ArrayList;
import java.util.List;


import com.loops.audioguide.R;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.ui.LandmarkDesc;
import com.loops.audioguide.ui.ListLandmarks;
import com.loops.audioguide.ui.ListLocations;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {

	private List<LocationOb> locationList;
	private Context context;
	Resources res;
	LayoutInflater mLayoutInflater;
	public static final int MODE_LOCATION = 0;
	public static final int MODE_LANDMARK = 1;
	private int mode = MODE_LOCATION;
	private OnClickListener BtnClicked;

	public LocationAdapter(Context c, List<LocationOb> locations, int mode,
			OnClickListener BtnClicked) {
		this.locationList = locations;
		context = c;
		this.mode = mode;
		res = c.getResources();
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.BtnClicked = BtnClicked;
	}

	@Override
	public int getCount() {
		return locationList.size();
	}

	@Override
	public Object getItem(int position) {
		return locationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.listsublayout, null);
		}
		LocationOb location = locationList.get(position);
		Button btn1 = (Button) convertView.findViewById(R.id.btnLocation);
		Button btn2 = (Button) convertView.findViewById(R.id.btnLocation2);
		if (location.getName() != null) {
			btn1.setText(location.getName().trim());
		}
		btn1.setOnClickListener(BtnClicked);
		btn2.setOnClickListener(BtnClicked);
		btn1.setTag(position);
		btn2.setTag(position);

		return convertView;
	}

	public void setData(ArrayList<LocationOb> b) {
		locationList = b;
	}

}