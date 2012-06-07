package com.loops.audioguide.tools;

import android.app.AlertDialog;
import android.content.Context;

public class ErrorDialog extends AlertDialog {
	
	public ErrorDialog(Context context, String message) {
		super(context);
		setMessage(message);
		setButton("OK", (OnClickListener) null);
	}
	
	public static ErrorDialog show(Context context, String message) {
		ErrorDialog d = new ErrorDialog(context, message);
		d.show();
		return d;
	}
	
	public static ErrorDialog show(Context context, String message, OnDismissListener dis) {
		ErrorDialog d = new ErrorDialog(context, message);
		d.setOnDismissListener(dis);
		d.show();
		return d;
	}
			
	
}