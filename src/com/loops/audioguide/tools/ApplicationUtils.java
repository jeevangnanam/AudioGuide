/*
 * SETT Browser
 * Sinhala-Englsih-Tamil trilingual rendering supported
 * mobile web-browser for Android.
 * 
 * Copyright (C) 2010 Dhanika Kaushalya Perera.
 * 
 * Based on the open-source project:
 * Zirco Browser for Android
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.loops.audioguide.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

/**
 * Application utilities.
 */
public class ApplicationUtils {
	
	private static String mAdSweepString = null;
	private static String mSETTRenderNativeString = null;
	private static String mSETTRenderLatinString = null;
	
	/**
	 * Truncate a string to a given maximum width, relative to its paint size.
	 * @param paintObject The object the text will be painted on.
	 * @param text The text to truncate.
	 * @param maxWidth The maximum width of the truncated string.
	 * @return The truncated string.
	 */
	public static String getTruncatedString(Paint paintObject, String text, int maxWidth) {
		
		boolean modified = false;
		
		while ((paintObject.measureText(text) > maxWidth) &&
				(text.length() > 0)) {
			text = text.substring(0, text.length() - 1);
			modified = true;		
		}
		
		if (modified) {
			text += "...";
		}
		
		return text;
	}

	



}
