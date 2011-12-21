package com.nz.audioguide.ui;

import com.nz.audioguide.R;
import com.nz.audioguide.dataobjects.LandmarkObj;
import com.nz.audioguide.dataobjects.LocationOb;
import com.nz.audioguide.model.LandmarkModel;
import com.nz.audioguide.model.LocationModel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LandmarkDesc extends Activity {

	private WebView mWebView;
	private static final int SWIPE_MIN_DISTANCE = 250;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 50;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	public static final String MIME_TYPE = "text/html";
	public static final String ENCODING = "utf-8";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landmarkdesc);
		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.setWebViewClient(new ArticleWebViewClient());		
		setArticleData(LandmarkModel.getInstance().getLandmarks().getLandmarks().get(0));
	}

	private class ArticleWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("location://")) {
				return true;
			} else {
				return false;
			}

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

		}
	}

	public void setArticleData(LocationOb landmark) {

		mWebView.loadDataWithBaseURL(
				"http://audioguide.loooops.com",
				getString(R.string.html_code, landmark.getName(),
						landmark.getImage(), landmark.getDesc()), MIME_TYPE,
				ENCODING, "http://audioguide.loooops.com");

		mWebView.scrollTo(0, 0);

	}

}
