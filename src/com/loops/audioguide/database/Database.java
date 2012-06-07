package com.loops.audioguide.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.dataobjects.EmergencyDetail;
import com.loops.audioguide.dataobjects.EmergencyGroup;
import com.loops.audioguide.dataobjects.FAQDetail;
import com.loops.audioguide.dataobjects.FaqGroup;
import com.loops.audioguide.dataobjects.LandmarkPictures;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class Database {

	private static final String DATABASE_NAME = "audioguide.db";

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_AUDIOGUIDE = "AudioGuide";
	private static final String TABLE_AUDIOGUIDEDOWNLOAD = "AudioGuideDownload";
	private static final String TABLE_PICTURES = "Picutres";

	private static final String TABLE_EMERGENCY = "EMERGENCY";
	private static final String TABLE_FAQ = "FAQ";

	private static final String TABLE_AUDIO_GUIDE_CREATE = "create table "
			+ TABLE_AUDIOGUIDE
			+ " (_id integer primary key autoincrement, "
			+ "aid text not null, location_id text not null, landmark_id text not null, "
			+ "title text , "
			+ "size text, path text, type text, price text, status text, created_date text, modified_date text,lang_id text );";

	private static final String TABLE_GUIDE_DOWNLOADED_CREATE = "create table "
			+ TABLE_AUDIOGUIDEDOWNLOAD
			+ " (_id integer primary key autoincrement, "
			+ "locationid text not null unique, languageid text);";

	private static final String TABLE_PICTURES_CREATE = "create table "
			+ TABLE_PICTURES + " (_id integer primary key autoincrement, "
			+ "pictures text not null unique, "
			+ "landmarkid text not null , location_id text not null);";

	private static final String TABLE_EMERGENCY_CREATE = "create table "
			+ TABLE_EMERGENCY
			+ " (_id integer primary key autoincrement, "
			+ "catid text not null, "
			+ "catname text not null , title text, phone1 text, phone2 text, phone3 text, description text);";
	
	private static final String TABLE_FAQ_CREATE = "create table "
		+ TABLE_FAQ
		+ " (_id integer primary key autoincrement, "
		+ "catid text not null, "
		+ "catname text not null , question text, answer text);";

	private static final String INSERT_SECTION = "insert into "
			+ TABLE_AUDIOGUIDE
			+ "(aid, location_id, landmark_id, title, size, path,type,price,status,created_date,modified_date,lang_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String INSERT_DOWNLOAD = "insert into "
			+ TABLE_AUDIOGUIDEDOWNLOAD
			+ "(locationid, languageid) values (?,?)";

	private static final String INSERT_PCITURES = "insert into "
			+ TABLE_PICTURES
			+ "(pictures, landmarkid,location_id) values (?,?,?)";

	private static final String INSERT_EMERGENCY = "insert into "
			+ TABLE_EMERGENCY
			+ " (catid, catname,title,phone1,phone2,phone3,description) values (?,?,?,?,?,?,?)";
	
	private static final String INSERT_FAQ = "insert into "
		+ TABLE_FAQ
		+ " (catid, catname,question,answer) values (?,?,?,?)";

	private SQLiteDatabase db;
	private SQLiteStatement insertStmtSection;
	private SQLiteStatement insertStmtDownload;
	private SQLiteStatement insertStmtPictures;
	private SQLiteStatement insertStmtEmergency;
	private SQLiteStatement insertStmtFAQ;
	private static OpenHelper openHelper;

	public Database(Context context) {

		if (openHelper == null) {
			openHelper = new OpenHelper(context);
		}

		db = openHelper.getWritableDatabase();
		this.insertStmtSection = this.db.compileStatement(INSERT_SECTION);
		this.insertStmtDownload = this.db.compileStatement(INSERT_DOWNLOAD);
		this.insertStmtPictures = this.db.compileStatement(INSERT_PCITURES);
		this.insertStmtEmergency = this.db.compileStatement(INSERT_EMERGENCY);
		this.insertStmtFAQ = this.db.compileStatement(INSERT_FAQ);
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public ArrayList<AudioGuide> getAudioGuides(String locationID) {

		ArrayList<AudioGuide> menu = new ArrayList<AudioGuide>();

		Cursor cursor = this.db.query(true, TABLE_AUDIOGUIDE, new String[] {
				"location_id", "landmark_id", "path", "price", "status",
				"lang_id" }, "location_id ='" + locationID
				+ "' AND lang_id='2'", null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				AudioGuide mnuitem = new AudioGuide();
				mnuitem.setLocation_id(cursor.getString(0));
				mnuitem.setLandmark_id(cursor.getString(1));
				mnuitem.setPath(cursor.getString(2));
				mnuitem.setPrice(cursor.getString(3));
				mnuitem.setStatus(cursor.getString(4));
				mnuitem.setLanguageid(cursor.getString(5));
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}
	
	
	public ArrayList<EmergencyGroup> getEmergencyGroups() {

		ArrayList<EmergencyGroup> menu = new ArrayList<EmergencyGroup>();

		Cursor cursor = this.db.query(true, TABLE_EMERGENCY, new String[] {
				"catid", "catname" }, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				EmergencyGroup mnuitem = new EmergencyGroup();
				mnuitem.setCatID(cursor.getString(0));
				mnuitem.setCatName(cursor.getString(1));
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}

	public ArrayList<EmergencyDetail> getEmergencyDetail(String locationID) {

		ArrayList<EmergencyDetail> menu = new ArrayList<EmergencyDetail>();

		Cursor cursor = this.db.query(true, TABLE_EMERGENCY, new String[] {
				"catid", "catname" ,"title","phone1","phone2","phone3","description" }, 
				"catid ='" + locationID + "'", null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				EmergencyDetail mnuitem = new EmergencyDetail();
				mnuitem.setCatID(cursor.getString(0));
				mnuitem.setCatName(cursor.getString(1));
				mnuitem.setTitle(cursor.getString(2));
				mnuitem.setPhone1(cursor.getString(3));
				mnuitem.setPhone2(cursor.getString(4));
				mnuitem.setPhone3(cursor.getString(5));
				mnuitem.setDescription(cursor.getString(6));
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}
	
	public ArrayList<FaqGroup> getFAQGroups() {

		ArrayList<FaqGroup> menu = new ArrayList<FaqGroup>();

		Cursor cursor = this.db.query(true, TABLE_FAQ, new String[] {
				"catid", "catname" }, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				FaqGroup mnuitem = new FaqGroup();
				mnuitem.setCatID(cursor.getString(0));
				mnuitem.setCatName(cursor.getString(1));
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}

	public ArrayList<FAQDetail> getFAQDetail(String locationID) {

		ArrayList<FAQDetail> menu = new ArrayList<FAQDetail>();

		Cursor cursor = this.db.query(true, TABLE_FAQ, new String[] {
				"catid", "catname" ,"question", "answer" }, 
				"catid ='" + locationID + "'", null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				FAQDetail mnuitem = new FAQDetail();
				mnuitem.setCatID(cursor.getString(0));
				mnuitem.setCatName(cursor.getString(1));
				mnuitem.setQuestion(cursor.getString(2));
				mnuitem.setAnswer(cursor.getString(3));				
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}

	public long insertSection(String aid, String location_id,
			String landmark_id, String title, String size, String path,
			String type, String price, String status, String created_date,
			String modified_date, String languageid) {

		this.insertStmtSection.bindString(1, aid);
		this.insertStmtSection.bindString(2, location_id);
		this.insertStmtSection.bindString(3, landmark_id);
		this.insertStmtSection.bindString(4, title);
		this.insertStmtSection.bindString(5, size);
		this.insertStmtSection.bindString(6, path);
		this.insertStmtSection.bindString(7, type);
		this.insertStmtSection.bindString(8, price);
		this.insertStmtSection.bindString(9, status);
		this.insertStmtSection.bindString(10, created_date);
		this.insertStmtSection.bindString(11, modified_date);
		this.insertStmtSection.bindString(12, languageid);

		return this.insertStmtSection.executeInsert();
	}

	public ArrayList<LandmarkPictures> getLandmarkPictures(String locationID,
			String LandmarkID) {

		ArrayList<LandmarkPictures> menu = new ArrayList<LandmarkPictures>();

		Cursor cursor = this.db.query(true, TABLE_PICTURES, new String[] {
				"landmarkid", "location_id", "pictures", }, "location_id ='"
				+ locationID + "' AND landmarkid='" + LandmarkID + "'", null,
				null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				LandmarkPictures mnuitem = new LandmarkPictures();
				mnuitem.setLandmarkID(cursor.getString(0));
				mnuitem.setLocationID(cursor.getString(1));
				mnuitem.setImageURL(cursor.getString(2));
				menu.add(mnuitem);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return menu;
	}

	public boolean isAudioGuideDownloaded(String locationid) {

		Cursor cursor = this.db.rawQuery("select 1 from "
				+ TABLE_AUDIOGUIDEDOWNLOAD + " where locationid=? ",
				new String[] { locationid });

		boolean exists = (cursor.getCount() > 0);

		cursor.close();

		return exists;
	}

	public long insertDownloaded(String location_id, String languageid)
			throws SQLException {

		this.insertStmtDownload.bindString(1, location_id);
		this.insertStmtDownload.bindString(2, languageid);

		return this.insertStmtDownload.executeInsert();
	}

	public long insertPicture(String path, String landmarkID, String location_id) {

		this.insertStmtPictures.bindString(1, path);
		this.insertStmtPictures.bindString(2, landmarkID);
		this.insertStmtPictures.bindString(3, location_id);

		return this.insertStmtPictures.executeInsert();
	}

	public long insertEmergencyNo(String catid, String catname, String title,
			String phone1, String phone2, String phone3, String desc) {
		// catid, catname,title,phone1,phone2,phone3,description
		this.insertStmtEmergency.bindString(1, catid);
		this.insertStmtEmergency.bindString(2, catname);
		this.insertStmtEmergency.bindString(3, title);
		this.insertStmtEmergency.bindString(4, phone1);
		this.insertStmtEmergency.bindString(5, phone2);
		this.insertStmtEmergency.bindString(6, phone3);
		this.insertStmtEmergency.bindString(7, desc);

		return this.insertStmtEmergency.executeInsert();
	}
	
	public long insertFAQ(String catid, String catname, String question,
			String answer) {
		// catid, catname,title,phone1,phone2,phone3,description
		this.insertStmtFAQ.bindString(1, catid);
		this.insertStmtFAQ.bindString(2, catname);
		this.insertStmtFAQ.bindString(3, question);
		this.insertStmtFAQ.bindString(4, answer);		

		return this.insertStmtFAQ.executeInsert();
	}

	public void dbClose() {
		openHelper.close();
	}

	public void deleteTables() {
		this.db.delete(TABLE_AUDIOGUIDE, null, null);
	}
	
	public void deleteEmergencyDetails() {
		this.db.delete(TABLE_EMERGENCY, null, null);
	}
	
	public void deleteFAQ() {
		this.db.delete(TABLE_FAQ, null, null);
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_AUDIO_GUIDE_CREATE);
			db.execSQL(TABLE_GUIDE_DOWNLOADED_CREATE);
			db.execSQL(TABLE_PICTURES_CREATE);
			db.execSQL(TABLE_EMERGENCY_CREATE);
			db.execSQL(TABLE_FAQ_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example",
					"Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIOGUIDE);
			onCreate(db);
		}
	}
}
