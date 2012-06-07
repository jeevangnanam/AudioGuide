package com.loops.audioguide.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.loops.audioguide.R;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LandmarkPictures;
import com.loops.audioguide.jsonparser.AudioGuideJSONParser;
import com.loops.audioguide.jsonparser.JSONElements;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.model.LandmarkModel;
import com.loops.audioguide.ui.ListLandmarks;

public class LandmarkTask extends AsyncTask<String, Void, LandmarkObj> {

	protected ProgressDialog mProgressDialog;
	private Context c;
	private Database dh;

	public LandmarkTask(Context c, Database dh) {
		this.c = c;
		this.dh = dh;
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = ProgressDialog.show(c, "", "Loading landmarks...");
		mProgressDialog.setCancelable(false);
		super.onPreExecute();
	}

	@Override
	protected LandmarkObj doInBackground(String... params) {

		LandmarkObj aud = null;

		try {
			JSONObject data = new JSONObject(
					WebService.convertStreamToString(WebService.getData(
							c.getString(R.string.url_getlandmarks, params[0]),
							null)));

			JSONObject audioGuides = (JSONObject) data;
		
			aud = AudioGuideJSONParser.addLandmark(audioGuides,
					new LandmarkObj());
			
			ListLandmarks.setLocations(aud);
			LandmarkModel.getInstance().setLandmark(aud);			

			// gets each landmark

			for (int i = 0; i < aud.getLandmarks().size(); i++) {
				String id = aud.getLandmarks().get(i).get_id();
				
				data = new JSONObject(
						WebService.convertStreamToString(WebService.getData(
								c.getString(R.string.url_getlandmark, id), null)));
				
				downloadAudioGuide(params[0],
						data.getJSONArray(JSONElements.AUDIOGUIDE));

				downloadImages(params[0],
						data.getJSONArray(JSONElements.LANDMARK_PIC));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return aud;
	}

	public void downloadAudioGuide(String locationID, JSONArray audG) {
		ArrayList<AudioGuide> audioGuides;
		try {
			audioGuides = AudioGuideJSONParser.addAudioGuide(locationID, audG,
					new ArrayList<AudioGuide>());
		} catch (Exception e) {
			return;
		}
		if (audioGuides != null) {
			for (int a = 0; a < audioGuides.size(); a++) {
				AudioGuide obs = audioGuides.get(a);
				if (dh != null)
					dh.insertSection(obs.getAid(), obs.getLocation_id(),
							obs.getLandmark_id(), obs.getTitle(),
							obs.getSize(), obs.getPath(), obs.getType(),
							obs.getPrice(), obs.getStatus(),
							obs.getCreated_date(), obs.getModified_date(),
							obs.getLanguageid());
			}
		}
	}

	public void downloadImages(String locationID, JSONArray Lpics) {
		ArrayList<LandmarkPictures> pics = AudioGuideJSONParser
				.AddLandmarkPictures(locationID, Lpics,
						new ArrayList<LandmarkPictures>());

		for (int i = 0; i < pics.size(); i++) {
			LandmarkPictures p = pics.get(i);

			try {
				if (dh != null)
					dh.insertPicture(p.getImageURL(), p.getLandmarkID(),
							p.getLocationID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void downloadSpecialOffers(String locationID, JSONArray specialOffers) {
		ArrayList<AudioGuide> audioGuides;
		try {
			audioGuides = AudioGuideJSONParser.addAudioGuide(locationID, specialOffers,
					new ArrayList<AudioGuide>());
		} catch (Exception e) {
			return;
		}
		if (audioGuides != null) {
			for (int a = 0; a < audioGuides.size(); a++) {
				AudioGuide obs = audioGuides.get(a);
				if (dh != null)
					dh.insertSection(obs.getAid(), obs.getLocation_id(),
							obs.getLandmark_id(), obs.getTitle(),
							obs.getSize(), obs.getPath(), obs.getType(),
							obs.getPrice(), obs.getStatus(),
							obs.getCreated_date(), obs.getModified_date(),
							obs.getLanguageid());
			}
		}
	}

}
