package com.loops.audioguide.ui;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loops.audioguide.R;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AboutPO;
import com.loops.audioguide.dataobjects.EmergencyDetail;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.JSONElements;
import com.loops.audioguide.jsonparser.ServerErrorException;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.services.C2DMRegistrationService;
import com.loops.audioguide.tools.ErrorDialog;
import com.loops.audioguide.tools.Tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.IntentFilter.AuthorityEntry;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

public class Splashscreen extends Activity {

	private ProgressBar b1;
	private static final int WAIT_TIME = 2000;
	private Database dh;

	public static final String KEY_PREFS_ABOUTAUTHOR = "com.nz.audioguide.aboutauthor";
	public static final String KEY_PREFS_ABOUTAPP = "com.nz.audioguide.aboutapp";
	public static final String KEY_PREFS_ABOUTLOOPS = "com.nz.audioguide.aboutloops";
	public static final String KEY_PREFS_EMERGENCY = "com.nz.audioguide.aboutemergency";
	public static final String KEY_PREFS_FAQ = "com.nz.audioguide.faq";
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		dh = new Database(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Tools.registerPushNotifications(this);
	}

	@Override
	protected void onResume() {
		if (dh == null) {
			dh = new Database(this);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (dh != null) {
			dh.dbClose();
			dh = null;
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (dh != null) {
			dh.dbClose();
			dh = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStart() {

		super.onStart();

		if (Tools.isOnline(this)) {
			if (prefs.getBoolean(KEY_PREFS_ABOUTAUTHOR, false)
					&& prefs.getBoolean(KEY_PREFS_ABOUTAPP, false)
					&& prefs.getBoolean(KEY_PREFS_ABOUTLOOPS, false)
					&& prefs.getBoolean(KEY_PREFS_EMERGENCY, false)
					&& prefs.getBoolean(KEY_PREFS_EMERGENCY, false))
				StartThread();
			else
				new InitialDownload().execute();
		} else {
			StartThread();
		}
	}

	public void StartThread() {

		new Handler().postDelayed(new Runnable() {
			public void run() {
				finish();
				startActivity(new Intent(Splashscreen.this,
						AudioGuideActivity.class));
			}
		}, WAIT_TIME);
	}

	public void onStop() {
		super.onStop();
	}

	private class InitialDownload extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			AboutPO ss;

			Editor editor = prefs.edit();
			boolean download = prefs.getBoolean(KEY_PREFS_ABOUTAUTHOR, false);

			if (!download) {
				try {
					ss = AudioGuideJSONParser
							.getAboutDetails(new JSONObject(
									WebService.convertStreamToString(WebService
											.getData(
													getString(R.string.url_about_author),
													null))));
					Tools.performWriteToCache(
							JSONElements.getAboutAuthorFileName(), ss,
							Splashscreen.this);
					editor.putBoolean(KEY_PREFS_ABOUTAUTHOR, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			download = prefs.getBoolean(KEY_PREFS_ABOUTAPP, false);

			if (!download) {
				try {
					ss = AudioGuideJSONParser.getAboutDetails(new JSONObject(
							WebService.convertStreamToString(WebService
									.getData(getString(R.string.url_about_app),
											null))));
					Tools.performWriteToCache(
							JSONElements.getAboutAppFileName(), ss,
							Splashscreen.this);
					editor.putBoolean(KEY_PREFS_ABOUTAPP, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			download = prefs.getBoolean(KEY_PREFS_ABOUTLOOPS, false);
			if (!download) {
				try {
					ss = AudioGuideJSONParser
							.getAboutDetails(new JSONObject(
									WebService.convertStreamToString(WebService
											.getData(
													getString(R.string.url_about_loops),
													null))));
					Tools.performWriteToCache(
							JSONElements.getAboutLoopsFileName(), ss,
							Splashscreen.this);
					editor.putBoolean(KEY_PREFS_ABOUTLOOPS, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			download = prefs.getBoolean(KEY_PREFS_EMERGENCY, false);
			if (!download) {
				try {
					AudioGuideJSONParser
							.getEmergencyContactDetails(
									new JSONArray(
											WebService
													.convertStreamToString(WebService
															.getData(
																	getString(R.string.url_emergency_cat),
																	null))), dh);
					editor.putBoolean(KEY_PREFS_EMERGENCY, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			download = prefs.getBoolean(KEY_PREFS_FAQ, false);
			if (!download) {
				try {
					AudioGuideJSONParser.getFAQDetails(
							new JSONArray(WebService
									.convertStreamToString(WebService.getData(
											getString(R.string.url_faq_cat),
											null))), dh);
					editor.putBoolean(KEY_PREFS_FAQ, true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			editor.commit();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			startActivity(new Intent(Splashscreen.this,
					AudioGuideActivity.class));
			finish();
			super.onPostExecute(result);
		}
	}

}
