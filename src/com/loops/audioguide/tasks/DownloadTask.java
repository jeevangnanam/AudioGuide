package com.loops.audioguide.tasks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.AudioGuide;
import com.loops.audioguide.tools.Tools;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public abstract class DownloadTask extends AsyncTask<String, Integer, String> {

	protected ProgressDialog mProgressDialog;
	private Context c;
	private Database dh;
	protected int size;
	protected int curr;
	protected boolean isUpdated = false;

	public static final String post_fix = ".m4a";
	public static final String pre_fix = "audiofiles";
	public static final String directory_fix = "/Android/data/com.nz.audioguide/cache/";
	protected boolean error = false;
	protected String id;

	public DownloadTask(Context c) {
		this.c = c;
		dh = new Database(c);

	}

	protected void onPreExecute() {
		mProgressDialog = new ProgressDialog(c);
		mProgressDialog.setMessage("Downloading audio guide ");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.show();
	};

	@Override
	protected String doInBackground(String... params) {
		String extStorageDirectory;
		extStorageDirectory = Tools.getStoragePath();

		if (extStorageDirectory == null)
			return "0";

		id = params[0];

		ArrayList<AudioGuide> guides = dh.getAudioGuides(id);

		size = guides.size();

		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getBlockCount();

		for (int i = 0; i < guides.size(); i++) {
			try {
				String nativeurl = "http://audioguide.loooops.com"
						+ guides.get(i).getPath();
				nativeurl = nativeurl.substring(0,
						nativeurl.lastIndexOf("/") + 1)
						+ encode(nativeurl
								.substring(nativeurl.lastIndexOf("/") + 1));

				URL url = new URL(nativeurl);

				URLConnection conexion = url.openConnection();
				conexion.connect();

				curr = i;
				updateDownloaderProgress();

				int lenghtOfFile = conexion.getContentLength();
				if (i != 0)
					bytesAvailable = (bytesAvailable - lenghtOfFile);

				if (bytesAvailable < lenghtOfFile) {
					return "2";
				}

				String fileName = extStorageDirectory + "/"
						+ guides.get(i).getLocation_id() + "_"
						+ guides.get(i).getLandmark_id() + "_"
						+ guides.get(i).getLanguageid() + post_fix;

				File f = Tools.isFileExists(fileName);

				if (f != null) {

					if (!(f.length() == lenghtOfFile)) {
						downloadAndWriteFile(fileName, url, lenghtOfFile);
					} else {
						updateDownloaderProgress("Skipping");
					}
				} else {
					downloadAndWriteFile(fileName, url, lenghtOfFile);
				}
			} catch (Exception e) {
				error = true;
				e.printStackTrace();
			}
		}
		return "1";
	}

	public void downloadAndWriteFile(String fileName, URL url, int lenghtOfFile)
			throws IOException {
		int count;
		// download the file
		InputStream input = new BufferedInputStream(url.openStream());
		OutputStream output = new FileOutputStream(fileName);

		byte data[] = new byte[1024];

		long total = 0;

		while ((count = input.read(data)) != -1) {
			total += count;
			// publishing the progress....
			output.write(data, 0, count);
			publishProgress((int) (total * 100 / lenghtOfFile));
		}
		output.flush();
		output.close();
		input.close();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

		mProgressDialog.setProgress(values[0]);
		super.onProgressUpdate(values);
	}

	/*
	 * @Override protected void onPostExecute(String result) { if
	 * (mProgressDialog != null) { mProgressDialog.dismiss(); } if (error) {
	 * Toast.makeText(c, "Error with download", Toast.LENGTH_LONG).show(); }
	 * else { dh.insertDownloaded(id, "2"); }
	 * 
	 * if (dh != null) { dh.dbClose(); } super.onPostExecute(result); }
	 */

	public abstract void updateDownloaderProgress();

	public abstract void updateDownloaderProgress(String s);

	public static String encode(String input) {
		StringBuilder resultStr = new StringBuilder();
		for (char ch : input.toCharArray()) {
			if (isUnsafe(ch)) {
				resultStr.append('%');
				resultStr.append(toHex(ch / 16));
				resultStr.append(toHex(ch % 16));
			} else {
				resultStr.append(ch);
			}
		}
		return resultStr.toString();
	}

	private static char toHex(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	private static boolean isUnsafe(char ch) {
		if (ch > 128 || ch < 0)
			return true;
		return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
	}

}
