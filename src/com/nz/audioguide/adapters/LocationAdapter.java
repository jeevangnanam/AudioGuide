package com.nz.audioguide.adapters;

import java.util.ArrayList;
import java.util.List;

import com.nz.audioguide.R;
import com.nz.audioguide.dataobjects.AudioGuide;
import com.nz.audioguide.dataobjects.LocationOb;
import com.nz.audioguide.ui.ListLandmarks;
import com.nz.audioguide.ui.ListLocations;

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

	public LocationAdapter(Context c, List<LocationOb> locations) {
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
		LocationOb location = locationList.get(position);
		Button btn1 = (Button) convertView.findViewById(R.id.btnLocation);
		Log.i("outside", position +"");
		if (location.getName() != null) {
			btn1.setText(location.getName().trim());
				
		}
		btn1.setOnClickListener(BtnClicked);
		btn1.setTag(position);
		return convertView;
	}
	
	OnClickListener BtnClicked = new OnClickListener() {		
		@Override
		public void onClick(View v) {		
			int pos = (Integer) v.getTag();
			Intent i = new Intent(context, ListLandmarks.class);
			i.putExtra(ListLocations.PREF_LOCATION_INDEX, pos);
			context.startActivity(i);
		}
	};

	public void setData(ArrayList<LocationOb> b) {
		locationList = b;
	}

}