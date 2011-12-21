package com.nz.audioguide.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nz.audioguide.R;
import com.nz.audioguide.adapters.LocationAdapter;
import com.nz.audioguide.dataobjects.AudioGuide;
import com.nz.audioguide.dataobjects.LocationOb;
import com.nz.audioguide.model.LocationModel;
import com.nz.jsonparser.AudioGuideJSONParser;
import com.nz.jsonparser.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListLocations extends Activity {

	private ListView lstView;
	private LocationAdapter adapt;
	private static ArrayList<LocationOb> locations;
	public static final String PREF_LOCATION_INDEX = "com.nz.audioguide.locationindex";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseguide);
		lstView = (ListView) findViewById(R.id.lstView);
		lstView.setDivider(null);
		lstView.setDividerHeight(0);
		locations = new ArrayList<LocationOb>();
		new GetLocations().execute();

	/*	lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("", "hmm");
				if(locations != null){
					Intent i = new Intent(ListLocations.this, ListLandmarks.class);
					i.putExtra(PREF_LOCATION_INDEX, arg2);
					LocationModel.getInstance().setLocation(locations);
					startActivity(i);				
				}
			}
		});*/
	}

	@Override
	protected void onResume() {
		if (locations == null) {
			locations = new ArrayList<LocationOb>();
			new GetLocations().execute();
		}
		super.onResume();
	}

	private class GetLocations extends AsyncTask<Void, Void, List<LocationOb>> {
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ListLocations.this, "",
					"Loading locations...");
			mProgressDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected List<LocationOb> doInBackground(Void... params) {
			locations.clear();
			try {
				JSONArray data = new JSONArray(
						WebService.convertStreamToString(WebService.getData(
								getString(R.string.url_getlocations), null)));

				for (int i = 0; i < data.length(); i++) {
					JSONObject audioGuides = (JSONObject) data.get(i);
					LocationOb aud = AudioGuideJSONParser.addlocation(
							audioGuides, new LocationOb());
					locations.add(aud);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LocationModel.getInstance().setLocation(locations);
			return locations;
		}

		@Override
		protected void onPostExecute(List<LocationOb> result) {

			adapt = new LocationAdapter(ListLocations.this, result);
			lstView.setAdapter(adapt);

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

			super.onPostExecute(result);
		}
	}

}
