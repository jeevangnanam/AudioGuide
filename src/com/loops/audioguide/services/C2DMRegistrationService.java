package com.loops.audioguide.services;

import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.loops.audioguide.tools.Tools;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class C2DMRegistrationService extends IntentService {

	public static final String KEY_P2REGISTRATION_ID = "com.audioguide.pushid";
	public static final String KEY_P2REGISTRATION_UPDATED_ONSERVER = "com.audioguide.pushidUpdated";
	public static final String KEY_P2REGISTRATION = "com.audioguide.pushidregistration";

	public C2DMRegistrationService() {
		super("C2DM");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);

			String c2dmid = prefs.getString(KEY_P2REGISTRATION_ID, null);

			if (c2dmid != null) {
				long epoch = System.currentTimeMillis() / 1000;

				Tools.Register_Push_onServer(
						Tools.getDeviceId(C2DMRegistrationService.this),
						c2dmid, Long.toString(epoch));

				SharedPreferences myPrefs = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				Editor edit = myPrefs.edit();
				edit.putBoolean(KEY_P2REGISTRATION_UPDATED_ONSERVER, true);
				edit.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
