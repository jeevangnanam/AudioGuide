package com.loops.audioguide.ui;

import java.io.File;
import java.util.ArrayList;

import com.loops.audioguide.R;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LandmarkPictures;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.model.LandmarkModel;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.player.MusicService;
import com.loops.audioguide.tasks.DownloadTask;
import com.loops.audioguide.tools.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LandmarkDesc extends Activity implements View.OnClickListener,
		OnLongClickListener {

	private WebView mWebView;
	private static final int SWIPE_MIN_DISTANCE = 250;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 50;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	public static final String MIME_TYPE = "text/html";
	public static final String ENCODING = "utf-8";
	private LocationOb landm;
	private LocationOb locaOb;
	String locationID;
	String landmarkID;
	int pos;
	private static boolean playing = false;
	private static boolean isPaused = false;
	public static final String KEY_URL = "com.audioguide.audiokey";
	private Database dh;
	private Button btnRewind;
	private Button btnFF;
	private ToggleButton btnPlay;
	private AudioGuideReceiver listener = null;
	private Boolean MyListenerIsRegistered = false;
	public static final String BROADCAST_RECEIVER_ONCOMPLETE = "com.nz.audioguide.stop";
	private boolean isUpdatingPlayButton = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.landmarkdesc);

		mWebView = (WebView) findViewById(R.id.webview);
		btnRewind = (Button) findViewById(R.id.btnRewind);
		btnFF = (Button) findViewById(R.id.btnFF);
		btnPlay = (ToggleButton) findViewById(R.id.btnPlay);
		btnRewind.setOnClickListener(this);
		btnFF.setOnClickListener(this);
		btnRewind.setOnLongClickListener(this);
		btnFF.setOnLongClickListener(this);

		dh = new Database(this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.setWebViewClient(new ArticleWebViewClient());

		pos = getIntent().getIntExtra(ListLocations.PREF_LANDMARK_INDEX, -1);

		landm = LandmarkModel.getInstance().getLandmarks().getLandmarks()
				.get(pos);

		locaOb = LocationModel.getInstance().getLocations()
				.get(LocationModel.getInstance().getLastAccessedLocationId());

		locationID = locaOb.get_id();
		landmarkID = landm.get_id();
		String image = "";
		ArrayList<LandmarkPictures> ss = dh.getLandmarkPictures(locationID,
				landmarkID);

		if (!ss.isEmpty()) {
			image = getString(R.string.base_url) + ss.get(0).getImageURL();
		}
		setArticleData(landm, image);

		btnPlay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isUpdatingPlayButton)
					return;
				String fileName = getAudioGuideFileName();

				File f = Tools.isFileExists(fileName);

				if (f == null) {
					Toast.makeText(LandmarkDesc.this, "File does not exist",
							Toast.LENGTH_LONG).show();
					btnPlay.setChecked(false);
					return;
				}

				if (isChecked) {
					if (!isPaused) {

						Intent i = new Intent(MusicService.ACTION_URL);
						Log.i("", fileName);
						i.putExtra(KEY_URL, fileName);
						startService(i);
						playing = true;
					} else {
						startService(new Intent(MusicService.ACTION_PLAY));
						isPaused = false;
					}
				} else {
					startService(new Intent(MusicService.ACTION_PAUSE));
					playing = false;
					isPaused = true;
				}
			}
		});
		listener = new AudioGuideReceiver();

	}

	@Override
	protected void onResume() {
		if (!MyListenerIsRegistered) {
			registerReceiver(listener, new IntentFilter(
					BROADCAST_RECEIVER_ONCOMPLETE));
			MyListenerIsRegistered = true;
		}

		if (MusicService.currentlyPlayingTrack != null) {
			String name = getAudioGuideFileName();
			if (!MusicService.currentlyPlayingTrack.equals(name)) {
				startService(new Intent(MusicService.ACTION_STOP));
			}

		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		if (MyListenerIsRegistered) {
			unregisterReceiver(listener);
			MyListenerIsRegistered = false;
		}
		super.onPause();
	}

	public void moveNext() {
		int p = pos + 1;

		if (p < LandmarkModel.getInstance().getLandmarks().getLandmarks()
				.size()) {
			pos = pos + 1;
			landm = LandmarkModel.getInstance().getLandmarks().getLandmarks()
					.get(pos);
			landmarkID = landm.get_id();
			dh.getLandmarkPictures(locationID, landmarkID);
			ArrayList<LandmarkPictures> ss = dh.getLandmarkPictures(locationID,
					landmarkID);
			String image = "";
			if (!ss.isEmpty()) {
				image = getString(R.string.base_url) + ss.get(0).getImageURL();
			}
			setArticleData(landm, image);
		}

	}

	public void movePrevious() {
		int p = pos - 1;

		if (p >= 0) {
			pos = pos - 1;
			landm = LandmarkModel.getInstance().getLandmarks().getLandmarks()
					.get(pos);
			landmarkID = landm.get_id();
			dh.getLandmarkPictures(locationID, landmarkID);
			ArrayList<LandmarkPictures> ss = dh.getLandmarkPictures(locationID,
					landmarkID);
			String image = "";
			if (!ss.isEmpty()) {
				image = getString(R.string.base_url) + ss.get(0).getImageURL();
			}
			setArticleData(landm, image);
		}
	}

	private class ArticleWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("location://")) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

		}
	}

	public void setArticleData(LocationOb landmark, String image) {
		if (!image.equals("")) {
			mWebView.loadDataWithBaseURL(
					"http://audioguide.loooops.com",
					getString(R.string.html_code, landmark.getName(), image,
							landmark.getDesc()), MIME_TYPE, ENCODING,
					"http://audioguide.loooops.com");
		} else {
			mWebView.loadDataWithBaseURL(
					"http://audioguide.loooops.com",
					getString(R.string.html_code_noimage, landmark.getName(),
							landmark.getDesc()), MIME_TYPE, ENCODING,
					"http://audioguide.loooops.com");
		}

		mWebView.scrollTo(0, 0);
	}

	public void onPreviousClick(View v) {
		startService(new Intent(MusicService.ACTION_STOP));
	}

	public void onPlayClick(View v) {

		if (!playing) {
			String extStorageDirectory = Tools.getStoragePath();
			String fileName = extStorageDirectory + "/" + locationID + "_"
					+ landmarkID + "_" + "2" + DownloadTask.post_fix;

			Intent i = new Intent(MusicService.ACTION_URL);
			i.putExtra(KEY_URL, fileName);
			startService(i);
			playing = true;
		} else {
			startService(new Intent(MusicService.ACTION_STOP));
			playing = false;
		}
	}

	public void OnAboutAuthorClicked(View v) {
		startActivity(new Intent(this, AboutAuthor.class));
	}

	public void onShareClicked(View v) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Audio Traveller");
		sharingIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						landm.getName()
								+ ", I am using AudioGuide to check out all these amazing places");

		startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}

	@Override
	public boolean onLongClick(View v) {
		if (btnRewind == v) {
			Intent i = new Intent(MusicService.ACTION_REWIND);
			startService(i);
			return true;
		} else if (btnFF == v) {
			Intent i = new Intent(MusicService.ACTION_SKIP);
			startService(i);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (btnRewind == v) {
			movePrevious();
			playing = false;
			isPaused = false;
			updatePauseButt();
			startService(new Intent(MusicService.ACTION_STOP));

		} else if (btnFF == v) {
			moveNext();
			playing = false;
			isPaused = false;
			updatePauseButt();
			startService(new Intent(MusicService.ACTION_STOP));

		}
	}

	public void updatePauseButt() {
		isUpdatingPlayButton = true;
		if (playing) {
			btnPlay.setChecked(true);
		} else {
			btnPlay.setChecked(false);
		}
		isUpdatingPlayButton = false;
	}

	protected class AudioGuideReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(BROADCAST_RECEIVER_ONCOMPLETE)) {
				playing = false;
				isPaused = false;
				updatePauseButt();
				startService(new Intent(MusicService.ACTION_STOP));
			}
		}
	}

	public String getAudioGuideFileName() {
		String extStorageDirectory = Tools.getStoragePath();
		String fileName = extStorageDirectory + "/" + locationID + "_"
				+ landmarkID + "_" + "2" + DownloadTask.post_fix;
		return fileName;
	}

}
