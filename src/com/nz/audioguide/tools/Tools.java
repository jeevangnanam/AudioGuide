package com.nz.audioguide.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.nz.audioguide.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Tools {

	
	public static final int BUFFER_SIZE = 1024;
	private static final String mContentType = "application/json";
	private static String mSETTRenderNativeString = null;
		

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

	

}
