package com.nz.audioguide.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import com.nz.audioguide.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


public class ImageDownloader {

	//the cache now uses SoftReferences and is unified in order to prevent outOfMemoryExceptions

	private static HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

	private static final String DIRECTORY_NAME = "lankadeepa";

	public final static String mode_profile = "profile";

	public static String mode = mode_profile;

	int stub_id = R.drawable.temploadimage;

	private File cacheDir;
	
	

	public ImageDownloader(Context context) {
		// Make the background thead low priority.
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// dir to save cached images
		/*
		 * if
		 * (android.os.Environment.getExternalStorageState().equals(android.os
		 * .Environment.MEDIA_MOUNTED)) cacheDir = new
		 * File(android.os.Environment
		 * .getExternalStorageDirectory(),DIRECTORY_NAME); else
		 */
		cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public ImageDownloader(Context context, int stub_id) {
		// Make the background thead low priority.
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// dir to save cached images
		/*
		 * if
		 * (android.os.Environment.getExternalStorageState().equals(android.os
		 * .Environment.MEDIA_MOUNTED)) cacheDir = new
		 * File(android.os.Environment
		 * .getExternalStorageDirectory(),DIRECTORY_NAME); else
		 */
		cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();

		this.stub_id = stub_id;
	}

	public void DisplayImage(String url, ImageView imageView) {

		Bitmap bmp;
		if (cache.containsKey(url) && (bmp = cache.get(url).get()) != null ) {
				imageView.setImageBitmap(bmp);
		} else {

			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	protected void queuePhoto(String url, ImageView imageView) {
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}
		// start thread
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	private Bitmap getBitmap(String url) {
		String filename = String.valueOf(url.hashCode());

		File f = new File(cacheDir, filename);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null) {
			return b;
		}
		// from web
		try {
			Bitmap bitmap = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			Tools.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 50;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {

						PhotoToLoad photoToLoad;

						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}

						Bitmap bmp = getBitmap(photoToLoad.url);

						cache.put(photoToLoad.url, new SoftReference<Bitmap>(bmp));

						if (((String) photoToLoad.imageView.getTag()).equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
			else
				imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}
