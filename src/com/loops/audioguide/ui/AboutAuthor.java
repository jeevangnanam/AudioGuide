package com.loops.audioguide.ui;

import com.loops.audioguide.R;
import com.loops.audioguide.dataobjects.AboutPO;
import com.loops.audioguide.jsonparser.JSONElements;
import com.loops.audioguide.tools.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutAuthor extends Activity {
	
	
	private TextView txtTitle;
	private TextView txtContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_author);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtContent = (TextView) findViewById(R.id.txtDesc);
		try{
			AboutPO aboutAuthor = (AboutPO) Tools.ReadSerializeOb(JSONElements.getAboutAuthorFileName(), this);			
			txtTitle.setText(aboutAuthor.getmTitle());
			txtContent.setText(aboutAuthor.getmContent());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
