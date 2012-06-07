/***
  Copyright (c) 2008-2011 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.loops.audioguide.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.c2dm.C2DMBaseReceiver;
import com.loops.audioguide.R;

public class C2DMReceiver extends C2DMBaseReceiver {

	private static final int NOTIFY_ME_ID = 1337;
	public static String PUSH_APP_ID = "com.appminister.pushid_intent";

	public C2DMReceiver() {
		super("test@noone.com");
	}

	@Override
	public void onRegistered(Context context, String registrationId) {

		Log.w("C2DMReceiver-onRegistered", registrationId);

		/*
		 * SharedPreferences myPrefs = PreferenceManager
		 * .getDefaultSharedPreferences(getApplicationContext()); Editor e =
		 * myPrefs.edit();
		 * e.putBoolean(C2DMRegistrationService.KEY_P2REGISTRATION, true);
		 * e.putString(C2DMRegistrationService.KEY_P2REGISTRATION_ID,
		 * registrationId); e.commit();
		 * 
		 * if (!myPrefs.getBoolean(
		 * C2DMRegistrationService.KEY_P2REGISTRATION_UPDATED_ONSERVER, false))
		 * { startService(new Intent(this, C2DMRegistrationService.class)); }
		 */
	}

	@Override
	public void onUnregistered(Context context) {
		Log.w("C2DMReceiver-onUnregistered", "got here!");
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.w("C2DMReceiver-onError", errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		Log.w("C2DMReceiver", "is over here" + intent.getStringExtra("payload"));
		String id = intent.getStringExtra("payload");

		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Notification note = new Notification(R.drawable.notification,
				"appminister!", System.currentTimeMillis());

		Intent splash = new Intent(this, Splashscreen.class);
		splash.putExtra(PUSH_APP_ID, id);

		PendingIntent i = PendingIntent.getActivity(this, 0, splash, 0);

		note.setLatestEventInfo(this, "appminister app of the day",
				"Check out .....", i);

		note.vibrate = new long[] { 500L, 200L, 200L, 500L };
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		mgr.notify(NOTIFY_ME_ID, note);
	}

}