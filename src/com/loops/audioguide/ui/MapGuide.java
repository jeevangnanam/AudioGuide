package com.loops.audioguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.loops.audioguide.R;
import com.loops.audioguide.adapters.LocationAdapter;
import com.loops.audioguide.callback.AudioGuideClickListener;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.mapoverlay.CustomOverlayItem;
import com.loops.audioguide.mapoverlay.CustomOverlayObject;
import com.loops.audioguide.model.LandmarkModel;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.tasks.DownloadTask;
import com.loops.audioguide.tasks.LandmarkTask;
import com.loops.audioguide.tools.ImageDownloader;
import com.loops.audioguide.tools.Tools;
import com.loops.audioguide.ui.ListLandmarks.AudioGuideDownloader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MapGuide extends MapActivity implements OnClickListener {

	private double mlongti;
	private double mlatiti;
	private ProgressDialog mProgressDialog;
	private static boolean isRunning = false;
	TelephonyManager tm;
	private MapView map;
	private Drawable drawable;
	private CustomOverlayItem locations;
	private CustomOverlayItem landmarks;
	private List<Overlay> mapOverlays;
	private MapController mc;
	public static final int ENABLE_GPS = 0;
	public ImageDownloader imageLoader;

	private Drawable currLocationDrawable;

	private LocationManager manager;
	private ConnectivityManager connec;

	private AtomicBoolean isThreadRunning = new AtomicBoolean(false);
	private AtomicBoolean hasLocation = new AtomicBoolean(false);
	private AtomicBoolean mAnimateLocation = new AtomicBoolean(true);
	private static boolean gpsListneredRemoved = false;
	private final Handler handler = new Handler();
	private Boolean MyListenerIsRegistered = false;
	private String barCouponId = null;
	private boolean animatePushBar = false;
	private static ArrayList<LocationOb> allLocations;
	private static int mode = LocationAdapter.MODE_LOCATION;

	private WifiLock mWifiLock;
	private boolean isDownloading = false;
	private Database dh;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		imageLoader = new ImageDownloader(this);
		dh = new Database(this);
		tm = (TelephonyManager) getBaseContext().getSystemService(
				Context.TELEPHONY_SERVICE);
		map = (MapView) findViewById(R.id.mapView);
		map.setBuiltInZoomControls(true);
		map.displayZoomControls(true);
		mc = map.getController();
		mapOverlays = map.getOverlays();
		allLocations = new ArrayList<LocationOb>();
		Drawable greenPin = this.getResources().getDrawable(
				R.drawable.pin_green);
		locations = new CustomOverlayItem(greenPin, this, ie);
		landmarks = new CustomOverlayItem(greenPin, this, ie);

		/* if (LocationModel.getInstance().getLocations().size() == 0) { */
		boolean readSuccess = false;
		try {
			ArrayList<LocationOb> locationObs = (ArrayList<LocationOb>) Tools
					.ReadSerializeOb(ListLocations.LOCATION_SERIALIZABLE_OB,
							MapGuide.this);
			allLocations = locationObs;
			readSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!readSuccess) {
				new GetLocations().execute();
			} else {
				if (allLocations != null) {
					addLocationsToMap(allLocations,
							LocationAdapter.MODE_LOCATION);
					LocationModel.getInstance().setLocation(allLocations);
				}
			}
		}
		/*
		 * } else { allLocations = (ArrayList<LocationOb>)
		 * LocationModel.getInstance() .getLocations();
		 * addLocationsToMap(allLocations, LocationAdapter.MODE_LOCATION); }
		 */

	}

	public void getLocation() {

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
			return;
		}

		if (connec.getNetworkInfo(0).isConnectedOrConnecting()
				|| connec.getNetworkInfo(1).isConnectedOrConnecting()) {

			mProgressDialog = ProgressDialog.show(MapGuide.this, "",
					"Locating...");
			mProgressDialog.setCancelable(false);

		} else {
			AlertDialog.Builder ab = new AlertDialog.Builder(MapGuide.this);
			ab.setMessage(Html.fromHtml("Please check internet connectivity"));
			ab.setPositiveButton("ok", null);
			ab.show();
		}
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent, ENABLE_GPS);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();

					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ENABLE_GPS) {
			getLocation();
		}
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
	protected void onResume() {
		super.onResume();
		if (dh == null) {
			dh = new Database(this);
		}
	}

	@Override
	protected void onDestroy() {
		if (mWifiLock != null) {
			if (mWifiLock.isHeld())
				mWifiLock.release();
		}
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public void addLocationsToMap(List<LocationOb> result, int type) {
		for (int i = 0; i < result.size(); i++) {

			try {
				LocationOb loc = result.get(i);
				double lat = Tools.convertHourToDecimal(loc.getLat());

				double longi = Tools.convertHourToDecimal(loc.getLong());

				GeoPoint point1 = new GeoPoint((int) (lat * 1E6),
						(int) (longi * 1E6));

				CustomOverlayObject overlayitem2 = new CustomOverlayObject(
						point1, "", "", i);

				if (LocationAdapter.MODE_LOCATION == type) {
					locations.addOverlay(overlayitem2);
					if (i == 0) {
						mc.animateTo(point1);
						mc.setZoom(9);
					}
				} else {
					landmarks.addOverlay(overlayitem2);
					if (i == 0) {
						mc.animateTo(point1);
						mc.setZoom(17);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mapOverlays.clear();
		if (LocationAdapter.MODE_LOCATION == type) {
			mode = LocationAdapter.MODE_LOCATION;
			mapOverlays.add(locations);
		} else {
			mode = LocationAdapter.MODE_LANDMARK;
			mapOverlays.add(landmarks);
		}
	}

	// AsyncTask for Landmarks

	/*
	 * private class GetLandMarks extends AsyncTask<String, Void, LandmarkObj> {
	 * private ProgressDialog mProgressDialog;
	 * 
	 * @Override protected void onPreExecute() { mProgressDialog =
	 * ProgressDialog.show(MapGuide.this, "", "Loading landmarks...");
	 * mProgressDialog.setCancelable(false); super.onPreExecute(); }
	 * 
	 * @Override protected LandmarkObj doInBackground(String... params) {
	 * Log.i("", getString(R.string.url_getlandmarks, params[0]));
	 * 
	 * try { JSONObject data = new JSONObject(
	 * WebService.convertStreamToString(WebService .getData(
	 * getString(R.string.url_getlandmarks, params[0]), null)));
	 * 
	 * JSONObject audioGuides = (JSONObject) data; LandmarkObj aud =
	 * AudioGuideJSONParser.addLandmark(audioGuides, new LandmarkObj());
	 * LandmarkModel.getInstance().setLandmark(aud);
	 * 
	 * return aud; } catch (Exception e) { return null; } }
	 * 
	 * @Override protected void onPostExecute(LandmarkObj result) { if (result
	 * != null) { if (result != null) { addLocationsToMap(result.getLandmarks(),
	 * LocationAdapter.MODE_LANDMARK);
	 * 
	 * Tools.performWriteToCache( ListLocations.LANDMARK_SERIALIZABLE_PREFIX +
	 * result.get_id(), result, MapGuide.this);
	 * 
	 * } } else { Toast.makeText(MapGuide.this, "Error", Toast.LENGTH_LONG)
	 * .show(); } if (mProgressDialog != null) { mProgressDialog.dismiss(); }
	 * 
	 * super.onPostExecute(result); } }
	 */

	private class GetLandMarks extends LandmarkTask {

		public GetLandMarks(Context c, Database dh) {
			super(c, dh);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(LandmarkObj result) {

			if (result != null) {
				if (result != null) {
					addLocationsToMap(result.getLandmarks(),
							LocationAdapter.MODE_LANDMARK);

					Tools.performWriteToCache(
							ListLocations.LANDMARK_SERIALIZABLE_PREFIX
									+ result.get_id(), result, MapGuide.this);

				}
			} else {
				Toast.makeText(MapGuide.this, "Error", Toast.LENGTH_LONG)
						.show();
			}

			if (result != null) {
				Tools.performWriteToCache(
						ListLocations.LANDMARK_SERIALIZABLE_PREFIX
								+ result.get_id(), result, MapGuide.this);

			} else {
				Toast.makeText(MapGuide.this, "Error", Toast.LENGTH_LONG)
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
			Tools.ShowYesNoDialog(MapGuide.this, yes, no,
					"AudioBooks - Download",
					"Do you want to download the Audio Books? Please make sure you are on WiFi");
		}
	}

	DialogInterface.OnClickListener yes = new DialogInterface.OnClickListener() {
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
						MapGuide.this);
				task.execute(id);
			}
		}
	};

	DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {
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
				Toast.makeText(MapGuide.this, "Error with download",
						Toast.LENGTH_LONG).show();
			} else {
				if (dh == null) {
					dh = new Database(MapGuide.this);
				}
				if (result.equals("1")) {
					try {
						dh.insertDownloaded(id, "2");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (result.equals("0")) {

					Toast.makeText(
							MapGuide.this,
							"Please make sure you have valid external storage such as an SD Card",
							Toast.LENGTH_LONG).show();

				} else if (result.equals("2")) {

					Toast.makeText(
							MapGuide.this,
							"You do not have enough space on your external storage",
							Toast.LENGTH_LONG).show();
				}
			}
			isDownloading = false;
			super.onPostExecute(result);
		}
	}

	// AsyncTask for Locations
	private class GetLocations extends AsyncTask<Void, Void, List<LocationOb>> {
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(MapGuide.this, "",
					"Loading locations...");
			mProgressDialog.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected List<LocationOb> doInBackground(Void... params) {
			allLocations.clear();
			try {
				JSONArray data = new JSONArray(
						WebService.convertStreamToString(WebService.getData(
								getString(R.string.url_getlocations), null)));

				for (int i = 0; i < data.length(); i++) {
					JSONObject audioGuides = (JSONObject) data.get(i);
					LocationOb aud = AudioGuideJSONParser.addlocation(
							audioGuides, new LocationOb());
					allLocations.add(aud);
				}

				LocationModel.getInstance().setLocation(allLocations);
				return allLocations;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<LocationOb> result) {

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			if (result != null) {
				addLocationsToMap(result, LocationAdapter.MODE_LOCATION);
				if (result != null) {
					Tools.performWriteToCache(
							ListLocations.LOCATION_SERIALIZABLE_OB, result,
							MapGuide.this);
				}
			}
			super.onPostExecute(result);
		}
	}

	private AudioGuideClickListener ie = new AudioGuideClickListener() {

		@Override
		public void displayInfo(int i) {

			if (LocationAdapter.MODE_LOCATION == mode) {

				id = LocationModel.getInstance().getLocations().get(i).get_id();
				LandmarkObj locationObs = null;
				boolean readSuccess = false;
				try {
					locationObs = (LandmarkObj) Tools.ReadSerializeOb(
							ListLocations.LANDMARK_SERIALIZABLE_PREFIX + id,
							MapGuide.this);
					// locations = locationObs;
					readSuccess = true;

				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (!readSuccess) {
						new GetLandMarks(MapGuide.this, dh).execute(id);
					} else {
						if (locationObs != null) {
							LandmarkModel.getInstance()
									.setLandmark(locationObs);
							addLocationsToMap(locationObs.getLandmarks(),
									LocationAdapter.MODE_LANDMARK);
						}
					}
				}

			} else {
				Intent intent = new Intent(MapGuide.this, LandmarkDesc.class);
				intent.putExtra(ListLocations.PREF_LANDMARK_INDEX, i);
				startActivity(intent);
			}
		}
	};

}
