package com.loops.audioguide.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loops.audioguide.R;
import com.loops.audioguide.dataobjects.AboutPO;
import com.loops.audioguide.jsonparser.JSONElements;
import com.loops.audioguide.tools.Tools;

public class Help extends Activity {

	TextView txtBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.help);
		txtBody = (TextView) findViewById(R.id.rptTxtBody);

		try {
			AboutPO aboutLoops = (AboutPO) Tools.ReadSerializeOb(
					JSONElements.getAboutLoopsFileName(), this);
			txtBody.setText(aboutLoops.getmContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onFeedbackClick(View v) {
		startActivity(new Intent(this, Feedback.class));
	}

	public void onTermsOfService(View v) {
		startActivity(new Intent(this, TermsofService.class));
	}

	public void onInformationProvidedby(View v) {
		startActivity(new Intent(this, AboutAuthor.class));
	}

	public void onEmergencyClicked(View v) {
		startActivity(new Intent(this, EmergencyContact.class));
	}

}
