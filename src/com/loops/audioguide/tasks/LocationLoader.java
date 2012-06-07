package com.loops.audioguide.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loops.audioguide.R;
import com.loops.audioguide.adapters.LocationAdapter;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.tools.Tools;
import com.loops.audioguide.ui.ListLocations;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LocationLoader extends AsyncTask<Void, Void, List<LocationOb>> {
	protected ProgressDialog mProgressDialog;
	protected Context c;
	private List<LocationOb> locations;

	public LocationLoader(Context c) {
		this.c = c;
		locations = new ArrayList<LocationOb>();
	}

	public LocationLoader(Context c, List<LocationOb> locations) {
		this.c = c;
		this.locations = locations;
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = ProgressDialog.show(c, "", "Loading locations...");
		mProgressDialog.setCancelable(false);
		super.onPreExecute();
	}

	@Override
	protected List<LocationOb> doInBackground(Void... params) {

		try {
			JSONArray data = new JSONArray(
					WebService.convertStreamToString(WebService.getData(
							c.getString(R.string.url_getlocations), null)));

			for (int i = 0; i < data.length(); i++) {
				JSONObject audioGuides = (JSONObject) data.get(i);
				LocationOb aud = AudioGuideJSONParser.addlocation(audioGuides,
						new LocationOb());
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
			Tools.performWriteToCache(ListLocations.LOCATION_SERIALIZABLE_OB,
					result, c);
		}

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		super.onPostExecute(result);
	}
}