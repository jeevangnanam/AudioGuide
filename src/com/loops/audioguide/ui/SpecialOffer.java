package com.loops.audioguide.ui;

import java.util.ArrayList;

import com.loops.audioguide.R;
import com.loops.audioguide.dataobjects.EmergencyDetail;
import com.loops.audioguide.dataobjects.EmergencyGroup;
import com.loops.audioguide.dataobjects.LandmarkObj;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.dataobjects.SpecialOffersOBJ;
import com.loops.audioguide.model.LandmarkModel;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.tools.ImageDownloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SpecialOffer extends Activity implements OnItemSelectedListener {

	ArrayList<SpecialOffersOBJ> specialOffers;
	private TextView txtTitle;
	private TextView txtDescription;
	private ImageView imgSpecial;
	public ImageDownloader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialoffers);
		txtTitle = (TextView) findViewById(R.id.SOTitle);
		txtDescription = (TextView) findViewById(R.id.SODescription);
		imgSpecial = (ImageView) findViewById(R.id.SOImage);

		LandmarkObj landMarks = LandmarkModel.getInstance().getLandmarks();
		specialOffers = landMarks.getSpecialOffers();
		imageLoader = new ImageDownloader(this);
		Spinner spin = (Spinner) findViewById(R.id.spinner);

		String[] categories = new String[specialOffers.size()];

		for (int i = 0; i < specialOffers.size(); i++) {
			SpecialOffersOBJ els = specialOffers.get(i);
			categories[i] = els.getTitle();
		}

		spin.setOnItemSelectedListener(this);

		ArrayAdapter aa = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, categories);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);

		if (!specialOffers.isEmpty()) {
			SpecialOffersOBJ els = specialOffers.get(0);
			txtTitle.setText(els.getTitle());
			txtDescription.setText(els.getSummary());
			try {
				imgSpecial.setTag(els.getPictureURL());
				imageLoader.DisplayImage(els.getPictureURL(), imgSpecial);
			} catch (Exception e) {

			}

		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!specialOffers.isEmpty()) {
			SpecialOffersOBJ els = specialOffers.get(arg2);
			txtTitle.setText(els.getTitle());
			txtDescription.setText(els.getDescription());
			try {
				imgSpecial.setTag(els.getPictureURL());
				imageLoader.DisplayImage(els.getPictureURL(), imgSpecial);
			} catch (Exception e) {

			}

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
