package com.loops.audioguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loops.audioguide.R;
import com.loops.audioguide.adapters.LocationAdapter;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LandmarkPictures;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.JSONElements;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.model.LandmarkModel;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.player.MusicService;
import com.loops.audioguide.tasks.DownloadTask;
import com.loops.audioguide.tasks.LandmarkTask;
import com.loops.audioguide.tools.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListLandmarks extends Activity {

	private ListView lstView;
	private LocationAdapter adapt;
	private static LandmarkObj locations;

	public static void setLocations(LandmarkObj locations) {
		ListLandmarks.locations = locations;
	}

	public static LandmarkObj getLocations() {
		return locations;
	}

	private String id;
	private Database dh;
	private TextView txtTitle;
	private WifiLock mWifiLock;
	private boolean isDownloading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectedguide);
		lstView = (ListView) findViewById(R.id.lstView);
		lstView.setDivider(null);
		lstView.setDividerHeight(0);
		if (locations == null)
			locations = new LandmarkObj();
		txtTitle = (TextView) findViewById(R.id.txtTitle);

		dh = new Database(this);
		int pos = getIntent()
				.getIntExtra(ListLocations.PREF_LOCATION_INDEX, -1);

		id = LocationModel.getInstance().getLocations().get(pos).get_id();

		txtTitle.setText(LocationModel.getInstance().getLocations().get(pos)
				.getName());

		boolean readSuccess = false;


		try {
			LandmarkObj locationObs = (LandmarkObj) Tools.ReadSerializeOb(
					ListLocations.LANDMARK_SERIALIZABLE_PREFIX + id,
					ListLandmarks.this);
			locations = locationObs;
			readSuccess = true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!readSuccess) {
				new GetLocations(ListLandmarks.this).execute(id);
			} else {
				if (locations != null) {
					adapt = new LocationAdapter(ListLandmarks.this,
							locations.getLandmarks(),
							LocationAdapter.MODE_LANDMARK, BtnClicked);
					lstView.setAdapter(adapt);
					LandmarkModel.getInstance().setLandmark(locations);
					DownloadAudioGuides();
				}
			}
		}

	}

	@Override
	protected void onResume() {
		if (locations == null) {
			locations = new LandmarkObj();
			new GetLocations(ListLandmarks.this).execute();
		}
		if (dh == null) {
			dh = new Database(this);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (!isDownloading) {
			if (dh != null) {
				dh.dbClose();
				dh = null;
			}
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (dh != null) {
			dh.dbClose();
			dh = null;
		}
		if (mWifiLock != null) {
			if (mWifiLock.isHeld())
				mWifiLock.release();
		}

		super.onDestroy();
	}

	private class GetLocations extends LandmarkTask {

		GetLocations(Context c) {
			super(c, dh);
		}

		@Override
		protected void onPostExecute(LandmarkObj result) {
			if (result != null) {
				adapt = new LocationAdapter(ListLandmarks.this,
						result.getLandmarks(), LocationAdapter.MODE_LANDMARK,
						BtnClicked);
				lstView.setAdapter(adapt);

				Tools.performWriteToCache(
						ListLocations.LANDMARK_SERIALIZABLE_PREFIX
								+ result.get_id(), result, ListLandmarks.this);

			} else {
				Toast.makeText(ListLandmarks.this, "Error", Toast.LENGTH_LONG)
						.show();
			}
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			DownloadAudioGuides();
			super.onPostExecute(result);
		}
	}

	public void DownloadAudioGuides() {
		if (!dh.isAudioGuideDownloaded(id)) {
			Tools.ShowYesNoDialog(ListLandmarks.this, yes, no,
					"AudioBooks - Download",
					"Do you want to download the Audio Books? Please make sure you are on WiFi");
		}
	}

	OnClickListener yes = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// download logic
			if (mWifiLock == null) {
				mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
						.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
				mWifiLock.acquire();
			}
			isDownloading = true;
			if (id != null) {
				AudioGuideDownloader task = new AudioGuideDownloader(
						ListLandmarks.this);
				task.execute(id);
			}
		}
	};

	OnClickListener no = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

	public class AudioGuideDownloader extends DownloadTask {

		public AudioGuideDownloader(Context c) {
			super(c);
		}

		@Override
		public void updateDownloaderProgress() {
			runOnUiThread(new Runnable() {
				public void run() {
					mProgressDialog.setMessage("Downloading audio guide "
							+ (curr + 1) + " of " + size);
				}
			});
		}

		@Override
		public void updateDownloaderProgress(final String s) {
			runOnUiThread(new Runnable() {
				public void run() {
					mProgressDialog.setMessage(s);
				}
			});
		}

		@Override
		protected void onPostExecute(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			if (mWifiLock != null) {
				if (mWifiLock.isHeld())
					mWifiLock.release();
			}
			if (error) {
				Toast.makeText(ListLandmarks.this, "Error with download",
						Toast.LENGTH_LONG).show();
			} else {
				if (dh == null) {
					dh = new Database(ListLandmarks.this);
				}
				if (result.equals("1")) {
					try {
						dh.insertDownloaded(id, "2");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (result.equals("0")) {

					Toast.makeText(
							ListLandmarks.this,
							"Please make sure you have valid external storage such as an SD Card",
							Toast.LENGTH_LONG).show();

				} else if (result.equals("2")) {

					Toast.makeText(
							ListLandmarks.this,
							"You do not have enough space on your external storage",
							Toast.LENGTH_LONG).show();
				}
			}
			isDownloading = false;
			super.onPostExecute(result);
		}
	}

	View.OnClickListener BtnClicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			int pos = (Integer) v.getTag();
			Intent i = new Intent(ListLandmarks.this, LandmarkDesc.class);
			i.putExtra(ListLocations.PREF_LANDMARK_INDEX, pos);
			startActivity(i);

		}
	};

	public void onFaqClick(View v) {
		startActivity(new Intent(this, SpecialOffer.class));
	}
}
