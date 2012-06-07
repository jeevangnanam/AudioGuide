package com.loops.audioguide.jsonparser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AboutPO;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.EmergencyDetail;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LandmarkPictures;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.dataobjects.SpecialOffersOBJ;

public class AudioGuideJSONParser {

	public static ArrayList<AudioGuide> addAudioGuide(String locationID,
			JSONArray audioGuides, ArrayList<AudioGuide> aud)
			throws JSONException {

		for (int i = 0; i < audioGuides.length(); i++) {

			AudioGuide ob = new AudioGuide();
			JSONObject audioGuide = (JSONObject) audioGuides.get(i);
			ob = addAudioGuideDetails(audioGuide, ob);
			ob.setLocation_id(locationID);
			aud.add(ob);
		}

		return aud;
	}

	private static AudioGuide addAudioGuideDetails(JSONObject audioGuide,
			AudioGuide aud) {

		aud.setAid(getJsonValue(audioGuide, JSONElements.AUDIOGUIDEID));
		aud.setTitle(getJsonValue(audioGuide, JSONElements.TITLE));
		aud.setType(getJsonValue(audioGuide, JSONElements.TYPE));
		aud.setStatus(getJsonValue(audioGuide, JSONElements.STATUS));
		aud.setSize(getJsonValue(audioGuide, JSONElements.SIZE));
		aud.setPrice(getJsonValue(audioGuide, JSONElements.PRICE));
		aud.setPath(getJsonValue(audioGuide, JSONElements.PATH));
		aud.setModified_date(getJsonValue(audioGuide, JSONElements.MODIFIED));
		aud.setCreated_date(getJsonValue(audioGuide, JSONElements.CREATED));
		aud.setLandmark_id(getJsonValue(audioGuide, JSONElements.LANDMARKID));
		aud.setLanguageid(getJsonValue(audioGuide, JSONElements.LANUGAGEID));

		return aud;
	}

	/*
	 * private static AudioGuide addAudioGuideLanguageDetails(JSONObject
	 * audioGuide, AudioGuide aud) {
	 * 
	 * aud.setLanguage_charSet(getJsonValue(audioGuide, JSONElements.CHARSET));
	 * aud.setLanguage_created(getJsonValue(audioGuide, JSONElements.CREATED));
	 * aud.setLanguage_id(getJsonValue(audioGuide, JSONElements.AUDIOGUIDEID));
	 * aud.setLanguage_moded(getJsonValue(audioGuide, JSONElements.MODIFIED));
	 * aud.setLanguage_name(getJsonValue(audioGuide,
	 * JSONElements.LOCATION_NAME));
	 * 
	 * return aud; }
	 */

	private static String getJsonValue(JSONObject audioGuide, String id) {
		try {
			return audioGuide.getString(id);
		} catch (JSONException e) {
			Log.i("error with", id);
			return null;
		}
	}

	private static String getJsonValueWithString(JSONObject audioGuide,
			String id) {
		try {
			return audioGuide.getString(id);
		} catch (JSONException e) {
			Log.i("error with", id);
			return "";
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

		// sets location info
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

		// sets all the landmarks
		locations = addAudioLankmarkDetails(locationJSON, locations);

		// sets all the special offers
		locations = addSpecialOffers(locationJSON, locations);

		return locations;
	}

	private static LandmarkObj addAudioLankmarkDetails(JSONObject audioGuide,
			LandmarkObj aud) throws JSONException {

		JSONArray landmarks = (JSONArray) audioGuide.get(JSONElements.LANDMARK);

		for (int i = 0; i < landmarks.length(); i++) {
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

	private static LandmarkObj addSpecialOffers(JSONObject audioGuide,
			LandmarkObj aud) throws JSONException {

		JSONArray specialOffers = (JSONArray) audioGuide
				.get(JSONElements.SPECIAL_OFFERS);

		for (int i = 0; i < specialOffers.length(); i++) {

			SpecialOffersOBJ obs = new SpecialOffersOBJ();

			JSONObject lm = (JSONObject) specialOffers.get(i);

			obs.setId(getJsonValue(lm, JSONElements.AUDIOGUIDEID));
			obs.setLocationID(getJsonValue(lm, JSONElements.LOCATION_NAME));
			obs.setTitle(getJsonValue(lm, JSONElements.TITLE));
			obs.setSummary(getJsonValue(lm, JSONElements.SUMMARY));
			obs.setDescription(getJsonValue(lm, JSONElements.LOCATION_DESC));
			obs.setPictureURL(getJsonValue(lm, JSONElements.PICTURE));
			obs.setActualURL(getJsonValue(lm, JSONElements.URL));
			obs.setCreated(getJsonValue(lm, JSONElements.CREATED));
			obs.setModified(getJsonValue(lm, JSONElements.MODIFIED));
			aud.getSpecialOffers().add(obs);
		}

		return aud;
	}

	public static ArrayList<LandmarkPictures> AddLandmarkPictures(
			String locationID, JSONArray landmarkPictures,
			ArrayList<LandmarkPictures> landmarkPics) {

		for (int i = 0; i < landmarkPictures.length(); i++) {

			LandmarkPictures ob = new LandmarkPictures();
			JSONObject audioGuide;
			try {
				audioGuide = (JSONObject) landmarkPictures.get(i);
				ob = addLandmarkPictureDetails(audioGuide, ob);
				ob.setLocationID(locationID);
				landmarkPics.add(ob);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return landmarkPics;
	}

	private static LandmarkPictures addLandmarkPictureDetails(
			JSONObject lpictures, LandmarkPictures pic) {
		if (getJsonValue(lpictures, JSONElements.LOCATION_NAME) != null)
			Log.i("", getJsonValue(lpictures, JSONElements.LOCATION_NAME));
		pic.setImageURL(getJsonValue(lpictures, JSONElements.LOCATION_NAME));
		pic.setLandmarkID(getJsonValue(lpictures, JSONElements.LANDMARKID));

		return pic;
	}

	public static AboutPO getAboutDetails(JSONObject authorDetails)
			throws JSONException {
		JSONObject obs = (JSONObject) authorDetails.get(JSONElements.CONTENT);
		AboutPO author = new AboutPO();
		author.setmTitle(getJsonValue(obs, JSONElements.TITLE));
		author.setmContent(getJsonValue(obs, JSONElements.CONTENT_AUTHOR));
		author.setmStatus(getJsonValue(obs, JSONElements.STATUS));
		author.setmCreated(getJsonValue(obs, JSONElements.CREATED));
		author.setmModified(getJsonValue(obs, JSONElements.MODIFIED));

		return author;
	}

	public static boolean getEmergencyContactDetails(
			JSONArray emergencyDetails, Database dh) throws JSONException {

		dh.deleteEmergencyDetails();

		for (int i = 0; i < emergencyDetails.length(); i++) {

			JSONObject mainObs = (JSONObject) emergencyDetails.get(i);
			JSONObject det = mainObs.getJSONObject("Emergencycontact");
			JSONObject cat = mainObs.getJSONObject("Emergencycontactcategory");
			Log.i("", cat.toString());
			dh.insertEmergencyNo(getJsonValue(cat, "id"),
					getJsonValueWithString(cat, "name"),
					getJsonValueWithString(det, JSONElements.TITLE),
					getJsonValueWithString(det, "phone1"),
					getJsonValueWithString(det, "phone2"),
					getJsonValueWithString(det, "phone3"),
					getJsonValueWithString(det, "description"));
		}

		return true;
	}

	public static boolean getFAQDetails(JSONArray faq, Database dh)
			throws JSONException {
		dh.deleteFAQ();
		for (int i = 0; i < faq.length(); i++) {

			JSONObject mainObs = (JSONObject) faq.get(i);
			JSONObject det = mainObs.getJSONObject("Faq");
			JSONObject cat = mainObs.getJSONObject("Faqcategory");

			dh.insertFAQ(getJsonValue(cat, "id"),
					getJsonValueWithString(cat, "name"),
					getJsonValueWithString(det, "question"),
					getJsonValueWithString(det, "answer"));
		}

		return true;
	}
}
