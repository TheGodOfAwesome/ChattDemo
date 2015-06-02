package com.chatt.demo.wadii.database;

import java.util.List;
import java.util.Random;

import com.chatt.demo.R;
import com.chatt.demo.wadii.maps.ShowMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class LocationDatabaseActivity extends ListActivity {
	private LocationsDataSource datasource;
	private double lon;
	private double lat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_database_layout);

		datasource = new LocationsDataSource(this);
		datasource.open();

		List<Locations> values = datasource.getAllComments();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Locations> adapter = new ArrayAdapter<Locations>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Locations> adapter = (ArrayAdapter<Locations>) getListAdapter();
		Locations comment = null;
		switch (view.getId()) {
		case R.id.add:
			// Read the latitude and longitude from the input fields
						EditText nameText = (EditText) findViewById(R.id.loc_name);
						EditText latText = (EditText) findViewById(R.id.lat_input);
						EditText lonText = (EditText) findViewById(R.id.lon_input);	
						String latString = latText.getText().toString();
						String lonString = lonText.getText().toString();
						String nameString = nameText.getText().toString();

						// Only execute if user has put entries in both lat and long fields.
						if(latString.compareTo("") != 0 && lonString.compareTo("") != 0 && nameString.compareTo("") != 0){
							
							String newLocation = nameString + ": " + latString + " , " +lonString;
							
							// Save the new comment to the database
							comment = datasource.createComment(newLocation);
							adapter.add(comment);
							
							lat = Double.parseDouble(latString);
							lon = Double.parseDouble(lonString); 
							ShowMap.putMapData(lat, lon, 13, true);
							Intent k = new Intent(this, ShowMap.class);
							startActivity(k);
								
						}else{
							Toast.makeText( getApplicationContext(),"Please Fill In All The Required Fields!",Toast.LENGTH_SHORT).show();
						}
							
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				comment = (Locations) getListAdapter().getItem(0);
				datasource.deleteComment(comment);
				adapter.remove(comment);
			}
			break;
		case R.id.deleteall:
			if (getListAdapter().getCount() > 0) {
				datasource.deleteAllComments();
				adapter.clear();
			}
			break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}