package com.loops.audioguide.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import com.google.android.c2dm.C2DMessaging;
import com.loops.audioguide.R;
import com.loops.audioguide.data.SerializeData;
import com.loops.audioguide.jsonparser.ServerErrorException;
import com.loops.audioguide.jsonparser.WebService;
import com.loops.audioguide.services.C2DMRegistrationService;
import com.loops.audioguide.tasks.DownloadTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Tools {

	public static final int BUFFER_SIZE = 1024;
	private static final String mContentType = "application/json";
	private static final String METHOD_TYPE = "_method";
	private static final String PUSH_ID_KEY = "data[Pushnotification][pushid]";
	private static final String PUSH_DEVICEID_KEY = "data[Pushnotification][deviceid]";

	public static void CopyStream(InputStream is, OutputStream os) {
		byte[] bytes = new byte[BUFFER_SIZE];
		int count;
		try {
			while ((count = is.read(bytes, 0, BUFFER_SIZE)) != -1) {
				os.write(bytes, 0, count);
			}
		} catch (IOException ex) {
		}
	}

	public static Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static boolean isOnline(Context cc) {
		ConnectivityManager cm = (ConnectivityManager) cc
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}

	public static AlertDialog showConnectivityErrorDialog(Context mContext,
			DialogInterface.OnClickListener onClickListener) {
		return new AlertDialog.Builder(mContext)
				.setTitle(R.string.connectivity_title_dialog)
				.setMessage(
						mContext.getResources().getText(
								R.string.connectivity_message_dialog))
				.setCancelable(false)
				.setPositiveButton(R.string.connectivity_retry_button,
						onClickListener)
				.setNegativeButton(R.string.connectivity_cancel_button,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}

	public static String sendRequest(String url) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", mContentType);

			HttpResponse response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				return EntityUtils.toString(resEntity);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static String tag = "Tools";

	public static boolean performWriteToCache(String name, Object obs, Context c) {

		try {
			WriteSerializeOb(c.getCacheDir() + name, obs);
			return true;
		} catch (IOException e) {
			Log.e(tag, "Could not write cache" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	private static void WriteSerializeOb(String filename, Object obj)
			throws IOException {
		SerializeData.writeObs(filename, obj);
	}

	public static Object ReadSerializeOb(String filename, Context c)
			throws IOException, ClassNotFoundException {

		return SerializeData.getObjs(c.getCacheDir() + filename);
	}

	public static void ShowYesNoDialog(Context c, OnClickListener yes,
			OnClickListener no, String title, String message) {
		final AlertDialog.Builder b = new AlertDialog.Builder(c);
		b.setIcon(android.R.drawable.ic_dialog_alert);
		b.setTitle(title);
		b.setMessage(message);
		b.setPositiveButton(android.R.string.yes, yes);
		b.setNegativeButton(android.R.string.no, no);
		b.show();
	}

	public static String getStoragePath() {

		String extStorageDirectory;
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;

			File f = new File(android.os.Environment
					.getExternalStorageDirectory().getAbsoluteFile()
					+ DownloadTask.directory_fix, DownloadTask.pre_fix);

			if (!f.exists()) {
				f.mkdirs();
			}

			extStorageDirectory = f.getAbsolutePath();

			return extStorageDirectory;

		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			return null;
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			return null;
		}
	}

	public static File isFileExists(String filename) {
		try {
			File f = new File(filename);
			if (f.exists())
				return f;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static double convertHourToDecimal(String degree) {

		degree = degree.split("\"")[0].replaceAll("\\s+", "");
		Log.i("", degree);
		String[] strArray = degree.split("[°']");

		if (!degree.matches("-?\\d+\\°\\d{1,2}\\'\\d{1,2}.?\\d*"))
			throw new IllegalArgumentException();

		return Double.parseDouble(strArray[0])
				+ Double.parseDouble(strArray[1]) / 60
				+ Double.parseDouble(strArray[2]) / 3600;
	}

	public static String Register_Push_onServer(String uid, String APID,
			String timestamp) throws IOException, ParserConfigurationException,
			SAXException, ServerErrorException {

		List<NameValuePair> nvps = new ArrayList<NameValuePair>(3);

		nvps.add(new BasicNameValuePair(METHOD_TYPE, "post"));
		nvps.add(new BasicNameValuePair(PUSH_ID_KEY, APID));
		nvps.add(new BasicNameValuePair(PUSH_DEVICEID_KEY, uid));

		String returnVal = WebService
				.convertStreamToString(WebService
						.postData(
								"http://audioguide.loooops.com/admin/pushnotifications/add?url=admin%2Fpushnotifications%2Fadd",
								nvps));

		return returnVal;
	}

	public static String getDeviceId(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice;
		tmDevice = "" + tm.getDeviceId();

		return tmDevice;
	}

	public static void registerPushNotifications(Context c) {

		SharedPreferences myPrefs = PreferenceManager
				.getDefaultSharedPreferences(c);

		boolean regP2 = myPrefs.getBoolean(
				C2DMRegistrationService.KEY_P2REGISTRATION, false);
		if (!regP2) {
			C2DMessaging.register(c, "nzanoon@gmail.com");
		}

		if (!myPrefs.getBoolean(
				C2DMRegistrationService.KEY_P2REGISTRATION_UPDATED_ONSERVER,
				false)
				&& regP2) {
			c.startService(new Intent(c, C2DMRegistrationService.class));
		}

	}

}
