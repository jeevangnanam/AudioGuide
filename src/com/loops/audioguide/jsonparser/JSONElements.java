package com.loops.audioguide.jsonparser;

public class JSONElements {

	public final static String AUDIOGUIDE = "Audioguide";
	public final static String AUDIOGUIDEID = "id";
	public final static String LANDMARKID = "landmark_id";
	public final static String LANUGAGEID = "language_id";
	public final static String TITLE = "title";
	public final static String SIZE = "size";
	public final static String PATH = "path";
	public final static String TYPE = "type";
	public final static String PRICE = "price";
	public final static String STATUS = "status";
	public final static String CREATED = "created";
	public final static String MODIFIED = "modified";

	public final static String LANDMARK = "Landmark";
	public final static String LOCATION_ID = "location_id";
	public final static String LOCATION_NAME = "name";
	public final static String LOCATION_DESC = "description";
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";
	public final static String AREA = "area";

	public final static String LANGUAGE = "Language";
	public final static String CHARSET = "charset";

	public final static String LOCATION = "Location";
	public final static String PICTURE = "picture";

	public final static String LANDMARK_PIC = "Landmarkpicture";

	public final static String CONTENT = "Content";
	public final static String CONTENT_AUTHOR = "content";

	private static final String ABOUT_AUTHOR_FILE_NAME = "about_author.ser";
	private static final String ABOUT_APP_FILE_NAME = "about_app.ser";
	private static final String ABOUT_LOOPS_FILE_NAME = "about_loops.ser";

	public static final String SPECIAL_OFFERS = "Specialoffer";
	public static final String ADVERTISERS_ID = "advertiser_id";
	public static final String SUMMARY = "summery";
	public static final String URL = "url";
	public static final String STAUS = "status";

	public static String getAboutAppFileName() {
		return ABOUT_APP_FILE_NAME;
	}

	public static String getAboutLoopsFileName() {
		return ABOUT_LOOPS_FILE_NAME;
	}

	public static String getAboutAuthorFileName() {
		return ABOUT_AUTHOR_FILE_NAME;
	}
}
