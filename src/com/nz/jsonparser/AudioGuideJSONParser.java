package com.nz.jsonparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.nz.audioguide.model.AudioGuide;

public class AudioGuideJSONParser {

	public static AudioGuide addlocation(JSONObject audioGuides, AudioGuide aud ) throws JSONException {

		
		JSONObject audioGuide = (JSONObject) audioGuides
				.get(JSONElements.AUDIOGUIDE);

		aud = addAudioGuideDetails(audioGuide, aud);

		JSONObject audioGuideLandMarks = (JSONObject) audioGuides
				.get(JSONElements.LANDMARK);
		aud = addAudioLankmarkDetails(audioGuideLandMarks, aud);

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

	private static AudioGuide addAudioLankmarkDetails(JSONObject audioGuide,
			AudioGuide aud) {
		/*
		 * audioGuide.get aud.setLandmark_id(getJsonValue(audioGuide,
		 * JSONElements.LOCATION_ID));
		 * aud.setLandmarks(landmarks)(getJsonValue(audioGuide,
		 * JSONElements.LOCATION_ID));
		 */

		return aud;
	}

	private static String getJsonValue(JSONObject audioGuide, String id) {
		try {
			return audioGuide.getString(id);
		} catch (JSONException e) {
			return null;
		}
	}

}
