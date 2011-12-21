package com.nz.audioguide.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nz.audioguide.R;
import com.nz.audioguide.adapters.LocationAdapter;
import com.nz.audioguide.dataobjects.LandmarkObj;
import com.nz.audioguide.dataobjects.LocationOb;
import com.nz.audioguide.model.LandmarkModel;
import com.nz.audioguide.model.LocationModel;
import com.nz.jsonparser.AudioGuideJSONParser;
import com.nz.jsonparser.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListLandmarks extends Activity {

	private ListView lstView;
	private LocationAdapter adapt;
	private static LandmarkObj locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectedguide);
		lstView = (ListView) findViewById(R.id.lstView);
		lstView.setDivider(null);
		lstView.setDividerHeight(0);
		locations = new LandmarkObj();

		int pos = getIntent()
				.getIntExtra(ListLocations.PREF_LOCATION_INDEX, -1);

		new GetLocations().execute(LocationModel.getInstance().getLocations()
				.get(pos).get_id());

		lstView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (locations != null) {
					// add purchase mechanism here
				}
			}
		});
	}

	@Override
	protected void onResume() {
		if (locations == null) {
			locations = new LandmarkObj();
			new GetLocations().execute();
		}
		super.onResume();
	}

	private class GetLocations extends AsyncTask<String, Void, LandmarkObj> {
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ListLandmarks.this, "",
					"Loading landmarks...");
			mProgressDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected LandmarkObj doInBackground(String... params) {
			Log.i("", getString(R.string.url_getlandmarks, params[0]));

			try {
				JSONObject data = new JSONObject(
						WebService.convertStreamToString(WebService
								.getData(
										getString(R.string.url_getlandmarks,
												params[0]), null)));
				
				JSONObject audioGuides = (JSONObject) data;
				LandmarkObj aud = AudioGuideJSONParser.addLandmark(audioGuides,
						new LandmarkObj());
				locations = aud;
				LandmarkModel.getInstance().setLandmark(locations);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return locations;
		}

		@Override
		protected void onPostExecute(LandmarkObj result) {
			if (result != null) {
				adapt = new LocationAdapter(ListLandmarks.this,
						result.getLandmarks());
				lstView.setAdapter(adapt);
			} else {
				Toast.makeText(ListLandmarks.this, "Error", Toast.LENGTH_LONG)
						.show();
			}
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

			super.onPostExecute(result);
		}
	}

}
