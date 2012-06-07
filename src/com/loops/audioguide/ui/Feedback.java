package com.loops.audioguide.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loops.audioguide.R;

public class Feedback extends Activity {
	
	private EditText txtName;
	private EditText txtEmailAddress;
	private EditText txtDesc;
	private Button btnSubmit;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmailAddress = (EditText) findViewById(R.id.txtAddress);
		txtDesc = (EditText) findViewById(R.id.txtBody);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(txtEmailAddress.getText().toString().trim().equals("")  || txtDesc.getText().toString().trim().equals("") ){
					Toast.makeText(Feedback.this, "Please fill in the form appropriately", Toast.LENGTH_LONG).show();
				}else{
					
					final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

					
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"cheran@looops.com"});
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "From -" + txtName.getText() );
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, txtDesc.getText() + "\n" + txtEmailAddress.getText());					
					startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				}
					
					
				
			}
		});
		
		
	}

}
