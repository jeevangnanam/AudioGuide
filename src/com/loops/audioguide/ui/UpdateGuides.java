package com.loops.audioguide.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loops.audioguide.R;
import com.loops.audioguide.dataobjects.LocationOb;
import com.loops.audioguide.model.LocationModel;
import com.loops.audioguide.tasks.LocationLoader;
import com.loops.audioguide.tools.Tools;

public class UpdateGuides extends Activity {
	ArrayList<LocationOb> locationObs;
	ArrayList<LocationOb> UpdatedlocationObs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateaudioguides);
		boolean readSuccess = false;
		try {
			locationObs = (ArrayList<LocationOb>) Tools.ReadSerializeOb(
					ListLocations.LOCATION_SERIALIZABLE_OB, UpdateGuides.this);
			readSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!readSuccess) {
				new DownloadLocation(this).execute();
			} else {
				if (locationObs != null) {
					LocationModel.getInstance().setLocation(locationObs);
				}
			}
		}

	}

	public class DownloadLocation extends LocationLoader {

		public DownloadLocation(Context c) {
			super(c);
		}

		@Override
		protected void onPostExecute(List<LocationOb> result) {
			Log.i("", "asdsadasdsadsa");
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			UpdatedlocationObs = (ArrayList<LocationOb>) result;

			if (locationObs == null) {
				Toast.makeText(UpdateGuides.this,
						"Updated " + UpdatedlocationObs.size(),
						Toast.LENGTH_LONG).show();
			} else {

				int updated = (locationObs.size() - UpdatedlocationObs.size());
				if (updated > 0)
					Toast.makeText(UpdateGuides.this, "Updated " + updated,
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(UpdateGuides.this, "No updates ",
							Toast.LENGTH_LONG).show();
			}

			if (result != null) {
				Tools.performWriteToCache(
						ListLocations.LOCATION_SERIALIZABLE_OB, result, c);
			}

			super.onPostExecute(result);
		}
	}

	public void onUpdateClicked(View v) {
		new DownloadLocation(this).execute();
	}

}
