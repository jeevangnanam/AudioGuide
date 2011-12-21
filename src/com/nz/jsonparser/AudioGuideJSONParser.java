package com.nz.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.nz.audioguide.dataobjects.AudioGuide;
import com.nz.audioguide.dataobjects.LandmarkObj;
import com.nz.audioguide.dataobjects.LocationOb;

public class AudioGuideJSONParser {

	public static AudioGuide addAudioGuide(JSONObject audioGuides, AudioGuide aud ) throws JSONException {
		
		JSONObject audioGuide = (JSONObject) audioGuides
				.get(JSONElements.AUDIOGUIDE);

		aud = addAudioGuideDetails(audioGuide, aud);

		JSONObject audioGuideLandMarks = (JSONObject) audioGuides
				.get(JSONElements.LANDMARK);
		//aud = addAudioLankmarkDetails(audioGuideLandMarks, aud);

		JSONObject audioGuideLanguageInfo = (JSONObject) audioGuides
				.get(JSONElements.LANGUAGE);

		aud = addAudioGuideLanguageDetails(audioGuideLanguageInfo, aud);

		return aud;
	}
	


	private static AudioGuide addAudioGuideDetails(JSONObject audioGuide,
			AudioGuide aud) {

		aud.setId(getJsonValue(audioGuide, JSONElements.AUDIOGUIDEID));
		aud.setTitle(getJsonValue(audioGuide, JSONElements.TITLE));
		aud.setType(getJsonValue(audioGuide, JSONElements.TYPE));
		aud.setStatus(getJsonValue(audioGuide, JSONElements.STATUS));
		aud.setSize(getJsonValue(audioGuide, JSONElements.SIZE));
		aud.setPrice(getJsonValue(audioGuide, JSONElements.PRICE));
		aud.setPath(getJsonValue(audioGuide, JSONElements.PATH));
		aud.setModified(getJsonValue(audioGuide, JSONElements.MODIFIED));
		aud.setCreated(getJsonValue(audioGuide, JSONElements.CREATED));

		return aud;
	}

	private static AudioGuide addAudioGuideLanguageDetails(JSONObject audioGuide,
			AudioGuide aud) {

		aud.setLanguage_charSet(getJsonValue(audioGuide, JSONElements.CHARSET));
		aud.setLanguage_created(getJsonValue(audioGuide, JSONElements.CREATED));
		aud.setLanguage_id(getJsonValue(audioGuide, JSONElements.AUDIOGUIDEID));
		aud.setLanguage_moded(getJsonValue(audioGuide, JSONElements.MODIFIED));
		aud.setLanguage_name(getJsonValue(audioGuide,
				JSONElements.LOCATION_NAME));

		return aud;
	}

	

	private static String getJsonValue(JSONObject audioGuide, String id) {
		try {
			return audioGuide.getString(id);
		} catch (JSONException e) {
			return null;
		}
	}


	public static LocationOb addlocation(JSONObject locationJSON,
			LocationOb locations) throws JSONException {
		
		JSONObject location = (JSONObject) locationJSON
				.get(JSONElements.LOCATION);
		
		locations.set_id(getJsonValue(location, JSONElements.AUDIOGUIDEID));
		locations.setName(getJsonValue(location, JSONElements.LOCATION_NAME));
		locations.setDesc(getJsonValue(location, JSONElements.LOCATION_DESC));
		locations.setLat(getJsonValue(location, JSONElements.LATITUDE));
		locations.setLong(getJsonValue(location, JSONElements.LONGITUDE));
		locations.setArea(getJsonValue(location, JSONElements.AREA));
		locations.setImage(getJsonValue(location, JSONElements.PICTURE));
		locations.setStatus(getJsonValue(location, JSONElements.STATUS));
		locations.setCreated(getJsonValue(location, JSONElements.CREATED));
		locations.setModified(getJsonValue(location, JSONElements.MODIFIED));
		
		return locations;
	}
	
	public static LandmarkObj addLandmark(JSONObject locationJSON,
			LandmarkObj locations) throws JSONException {
		
		JSONObject location = (JSONObject) locationJSON
				.get(JSONElements.LOCATION);

		locations.set_id(getJsonValue(location, JSONElements.AUDIOGUIDEID));

		locations.setName(getJsonValue(location, JSONElements.LOCATION_NAME));

		locations.setDesc(getJsonValue(location, JSONElements.LOCATION_DESC));

		locations.setLat(getJsonValue(location, JSONElements.LATITUDE));
		locations.setLong(getJsonValue(location, JSONElements.LONGITUDE));
		locations.setArea(getJsonValue(location, JSONElements.AREA));
		locations.setImage(getJsonValue(location, JSONElements.PICTURE));
		locations.setStatus(getJsonValue(location, JSONElements.STATUS));
		locations.setCreated(getJsonValue(location, JSONElements.CREATED));
		locations.setModified(getJsonValue(location, JSONElements.MODIFIED));
	
		locations = addAudioLankmarkDetails(locationJSON,locations);
		
		return locations;
	}
	
	private static LandmarkObj addAudioLankmarkDetails(JSONObject audioGuide,
			LandmarkObj aud) throws JSONException {
						
		Log.i("", audioGuide.toString());
		
		JSONArray landmarks = (JSONArray) audioGuide
				.get(JSONElements.LANDMARK);
		Log.i("", "2");
		
		for(int i=0;i<landmarks.length();i++){
			LocationOb obs = new LocationOb();
			JSONObject lm = (JSONObject) landmarks.get(i);
			obs.set_id(getJsonValue(lm, JSONElements.AUDIOGUIDEID));
			obs.setName(getJsonValue(lm, JSONElements.LOCATION_NAME));
			obs.setDesc(getJsonValue(lm, JSONElements.LOCATION_DESC));
			obs.setLat(getJsonValue(lm, JSONElements.LATITUDE));
			obs.setLong(getJsonValue(lm, JSONElements.LONGITUDE));
			obs.setArea(getJsonValue(lm, JSONElements.AREA));
			obs.setCreated(getJsonValue(lm, JSONElements.CREATED));
			obs.setModified(getJsonValue(lm, JSONElements.MODIFIED));			
			aud.getLandmarks().add(obs);
		}

		return aud;
	}

}
