package com.nz.audioguide.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import com.nz.audioguide.R;
import com.nz.audioguide.dataobjects.AudioGuide;
import com.nz.jsonparser.AudioGuideJSONParser;
import com.nz.jsonparser.JSONElements;
import com.nz.jsonparser.WebService;

public class AudioGuideActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionsmenu);
		
	}

	public void onGPSClicked(View v) {

	}

	public void onAudioGuideClicked(View v) {
		startActivity(new Intent(this, ListLocations.class));
	}

	public void onOptionsClicked(View v) {

	}

}