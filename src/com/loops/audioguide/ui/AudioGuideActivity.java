package com.loops.audioguide.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.loops.audioguide.R;
import com.loops.audioguide.payment.Consts.PurchaseState;
import com.loops.audioguide.payment.Consts.ResponseCode;

public class AudioGuideActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionsmenu);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}
	/**
	 * Called when this activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public void onGPSClicked(View v) {
		startActivity(new Intent(this, MapGuide.class));
	}

	public void onAudioGuideClicked(View v) {
		startActivity(new Intent(this, ListLocations.class));
	}

	public void onOptionsClicked(View v) {
		startActivity(new Intent(this, UpdateGuides.class));
	}

	public void HelpViewClicked(View v) {
		startActivity(new Intent(this, Help.class));
	}

}