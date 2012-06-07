package com.loops.audioguide.jsonparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebService {

	private static final int GET = 1;
	private static final int POST = 2;
	private static final int PUT = 3;
	private static final int DELETE = 4;

	private static final int TIMEOUT = 15000;

	private static InputStream makeHTTPRequest(final int type, final String url, List<NameValuePair> values) throws ServerErrorException, IOException {
	
		InputStream stream = null;
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		DefaultHttpClient httpclient = new DefaultHttpClient(params);

		HttpRequestBase httpRequest = null;

		String fullUrl = url;

		if (type == GET || type == PUT || type == DELETE) {
			if (values != null) {
				StringBuilder strBuilder = new StringBuilder(fullUrl);
				strBuilder.append("?");
				for (NameValuePair value : values) {
					strBuilder.append(value.getName()).append("=").append(value.getValue()).append("&");
				}
				fullUrl = strBuilder.toString();
			}
		}

		switch (type) {
		case GET:
			httpRequest = new HttpGet(fullUrl);
			break;
		case POST:
			HttpPost httpPost = new HttpPost(url);
			if (values != null)
				httpPost.setEntity(new UrlEncodedFormEntity(values));
			httpRequest = httpPost;
			break;
		case PUT:
			httpRequest = new HttpPut(fullUrl);
			break;
		case DELETE:
			httpRequest = new HttpDelete(fullUrl);
			break;
		}

		try {
			HttpResponse response = httpclient.execute(httpRequest);

			if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
				stream = response.getEntity().getContent();
			} else {
				throw new ServerErrorException(response.getStatusLine().getReasonPhrase());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return stream;
	}

	public static InputStream getData(final String url, List<NameValuePair> values) throws ServerErrorException, IOException {
		return makeHTTPRequest(GET, url, values);
	}

	public static InputStream postData(String url, List<NameValuePair> values) throws ServerErrorException, IOException {
		return makeHTTPRequest(POST, url, values);
	}

	public static InputStream putData(final String url, List<NameValuePair> values) throws ServerErrorException, IOException {
		return makeHTTPRequest(PUT, url, values);
	}

	public static InputStream deleteData(final String url, List<NameValuePair> values) throws ServerErrorException, IOException {
		return makeHTTPRequest(DELETE, url, values);
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		return convertStreamToString(is, HTTP.DEFAULT_CONTENT_CHARSET);
	}

	public static String convertStreamToString(InputStream is, String encoding) throws IOException {
		StringBuilder result = new StringBuilder();

		if (is != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
				String line;
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
			} finally {
				is.close();
			}
		}

		return result.toString();
	}

	public static String getMessageFromJSONError(JSONObject json) throws JSONException {
		String str = "";
		JSONArray ar = json.names();
		for (int i = 0; i < ar.length(); i++) {
			str += ar.getString(i) + " ";
			JSONArray errorArray = json.getJSONArray(ar.getString(i));
			for (int j = 0; j < errorArray.length(); j++) {
				str += errorArray.getString(j) + " ";
			}
			str += "\n";
		}
		return str;
	}

}
