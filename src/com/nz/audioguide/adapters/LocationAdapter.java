package com.nz.audioguide.adapters;

import java.util.ArrayList;
import java.util.List;


import com.loops.audioguide.R;
import com.nz.audioguide.model.AudioGuide;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {

	private List<AudioGuide> locationList;
	private Context context;
	Resources res;
	LayoutInflater mLayoutInflater;

	public LocationAdapter(Context c, List<AudioGuide> locations) {
		this.locationList = locations;

		context = c;
		res = c.getResources();
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
		AudioGuide location = locationList.get(position);
		Button btn1 = (Button) convertView.findViewById(R.id.btnLocation);
		Log.i("outside", position +"");
		if (location.getTitle() != null) {
			btn1.setText(location.getTitle().trim());
			
			
		}

		return convertView;
	}

	public void setData(ArrayList<AudioGuide> b) {
		locationList = b;
	}

}