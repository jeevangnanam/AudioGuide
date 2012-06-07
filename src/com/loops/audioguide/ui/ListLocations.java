package com.loops.audioguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loops.audioguide.R;
import com.loops.audioguide.adapters.LocationAdapter;
import com.loops.audioguide.calculations.Distance;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.locationlistener.CustomLocationListener;
import com.loops.audioguide.locationlistener.CustomLocationListener.LocationResult;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.payment.PaymentBilling;
import com.loops.audioguide.tools.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ListLocations extends PaymentBilling {

	private ListView lstView;
	private LocationAdapter adapt;
	private static ArrayList<LocationOb> locations;
	private static ArrayList<LocationOb> closeLocations;
	public static final String PREF_LOCATION_INDEX = "com.nz.audioguide.locationindex";
	public static final String PREF_LANDMARK_INDEX = "com.nz.audioguide.landmarkindex";
	public static final String LOCATION_SERIALIZABLE_OB = "com.nz.audioguide.obname";
	public static final String LANDMARK_SERIALIZABLE_PREFIX = "com.nz.audioguide.landmarkobname";

	private static boolean gpsListneredRemoved = false;
	private static boolean isRunningLocationCal = false;
	private ToggleButton locationFeature;
	private Database dh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseguide);
		lstView = (ListView) findViewById(R.id.lstView);
		lstView.setDivider(null);
		lstView.setDividerHeight(0);
		locationFeature = (ToggleButton) findViewById(R.id.btnToggleLocationNearby);

		locations = new ArrayList<LocationOb>();
		closeLocations = new ArrayList<LocationOb>();

		dh = new Database(getApplicationContext());

		/* if (LocationModel.getInstance().getLocations().size() == 0) { */
		boolean readSuccess = false;
		try {
			ArrayList<LocationOb> locationObs = (ArrayList<LocationOb>) Tools
					.ReadSerializeOb(LOCATION_SERIALIZABLE_OB,
							ListLocations.this);
			locations = locationObs;
			readSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!readSuccess) {
				new GetLocations().execute();
			} else {
				if (locations != null) {
					adapt = new LocationAdapter(ListLocations.this, locations,
							LocationAdapter.MODE_LOCATION, BtnClicked);
					lstView.setAdapter(adapt);
					LocationModel.getInstance().setLocation(locations);
				}
			}
		}

		/*
		 * } else { locations = (ArrayList<LocationOb>)
		 * LocationModel.getInstance() .getLocations(); adapt = new
		 * LocationAdapter(ListLocations.this, locations,
		 * LocationAdapter.MODE_LOCATION); lstView.setAdapter(adapt); }
		 */

		locationFeature
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							locationClick();
						} else {
							if (myLocation != null) {
								try {
									myLocation.removeListeners();
									gpsListneredRemoved = true;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							adapt = new LocationAdapter(ListLocations.this,
									locations, LocationAdapter.MODE_LOCATION,
									BtnClicked);
							lstView.setAdapter(adapt);
							LocationModel.getInstance().setLocation(locations);
						}
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (locations == null) {
			locations = new ArrayList<LocationOb>();
			new GetLocations().execute();
		}
		if (dh == null) {
			dh = new Database(getApplicationContext());
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (dh != null) {
			dh.dbClose();
		}
		if (myLocation != null) {
			try {
				myLocation.removeListeners();
				gpsListneredRemoved = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
				return null;
			}
			LocationModel.getInstance().setLocation(locations);
			return locations;
		}

		@Override
		protected void onPostExecute(List<LocationOb> result) {

			if (result != null) {
				Tools.performWriteToCache(LOCATION_SERIALIZABLE_OB, result,
						ListLocations.this);
				adapt = new LocationAdapter(ListLocations.this, result,
						LocationAdapter.MODE_LOCATION, BtnClicked);
				lstView.setAdapter(adapt);
			} else {
				Toast.makeText(ListLocations.this,
						"Please check internet connectivity", Toast.LENGTH_LONG)
						.show();
			}

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

			super.onPostExecute(result);
		}
	}

	CustomLocationListener myLocation = new CustomLocationListener();

	private void locationClick() {
		if (myLocation != null)
			myLocation.getLocation(getApplicationContext(), locationResult);
		else {
			myLocation = new CustomLocationListener();
			myLocation.getLocation(getApplicationContext(), locationResult);
		}
	}

	public LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {

			double mlongti = location.getLongitude();
			double mlatiti = location.getLatitude();
			Log.i("", mlongti + " " + mlatiti);
			if (!isRunningLocationCal)
				new GetNearbyLocations().execute(mlongti, mlatiti);
		}
	};

	private class GetNearbyLocations extends
			AsyncTask<Double, Void, ArrayList<LocationOb>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isRunningLocationCal = true;
		}

		@Override
		protected ArrayList<LocationOb> doInBackground(Double... params) {
			double longi = params[0];
			double lattitde = params[1];
			closeLocations.clear();
			for (int i = 0; i < locations.size(); i++) {

				double distance = Distance.getDistance(
						Tools.convertHourToDecimal(locations.get(i).getLat()),
						Tools.convertHourToDecimal(locations.get(i).getLong()),
						lattitde, longi);
				if (distance < 25) {
					closeLocations.add(locations.get(i));
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<LocationOb> result) {
			isRunningLocationCal = false;
			adapt = new LocationAdapter(ListLocations.this, closeLocations,
					LocationAdapter.MODE_LOCATION, BtnClicked);
			lstView.setAdapter(adapt);
			LocationModel.getInstance().setLocation(closeLocations);

			super.onPostExecute(result);
		}
	}

	OnClickListener BtnClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {

			// check if purchased
			// processpayment();
			int pos = (Integer) v.getTag();
			Intent i = new Intent(ListLocations.this, ListLandmarks.class);
			LocationModel.getInstance().setLastAccessedLocationId(pos);
			i.putExtra(ListLocations.PREF_LOCATION_INDEX, pos);
			startActivity(i);

		}
	};

	public void onFaqClick(View v) {
		startActivity(new Intent(this, Faq.class));
	}

}
