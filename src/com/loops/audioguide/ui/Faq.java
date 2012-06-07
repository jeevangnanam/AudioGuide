package com.loops.audioguide.ui;

import java.util.ArrayList;

import com.loops.audioguide.R;
import com.loops.audioguide.database.Database;
import com.loops.audioguide.dataobjects.EmergencyDetail;
import com.loops.audioguide.dataobjects.EmergencyGroup;
import com.loops.audioguide.dataobjects.FAQDetail;
import com.loops.audioguide.dataobjects.FaqGroup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Faq extends Activity implements OnItemSelectedListener {

	private Database dh;
	private ListView lstView;
	ArrayList<FaqGroup> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq);
		Spinner spin = (Spinner) findViewById(R.id.spinner);
		lstView = (ListView) findViewById(R.id.lstView);

		dh = new Database(this);

		groups = dh.getFAQGroups();

		String[] categories = new String[groups.size()];

		for (int i = 0; i < groups.size(); i++) {
			FaqGroup els = groups.get(i);
			categories[i] = els.getCatName();
		}

		spin.setOnItemSelectedListener(this);

		ArrayAdapter aa = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, categories);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);

		if (!groups.isEmpty()) {
			ArrayList<FAQDetail> emergencyCotnacts = dh.getFAQDetail(groups
					.get(0).getCatID());
			lstView.setAdapter(new OrderAdapter(this, emergencyCotnacts));
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!groups.isEmpty()) {
			ArrayList<FAQDetail> emergencyCotnacts = dh.getFAQDetail(groups
					.get(arg2).getCatID());
			lstView.setAdapter(new OrderAdapter(this, emergencyCotnacts));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		if (dh != null) {
			dh.dbClose();
			dh = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (dh == null) {
			dh = new Database(this);
		}
		super.onResume();
	}

	private class OrderAdapter extends BaseAdapter {

		private ArrayList<FAQDetail> items;

		public OrderAdapter(Context context, ArrayList<FAQDetail> items) {

			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			FAQDetail o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.txtTitle);
				TextView bt = (TextView) v.findViewById(R.id.txtNumber);
				if (tt != null) {
					tt.setText(o.getQuestion());
				}
				if (bt != null) {
					bt.setText(o.getAnswer());
				}
			}
			return v;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

}
