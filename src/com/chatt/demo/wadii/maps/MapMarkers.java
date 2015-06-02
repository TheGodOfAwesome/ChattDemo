package com.chatt.demo.wadii.maps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.chatt.demo.R;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapMarkers extends FragmentActivity implements
GoogleMap.OnInfoWindowClickListener, LocationListener {

	private GoogleMap map;
	private LocationManager locationManager;
	private LatLng hit = new LatLng(-17.839802, 31.008064);
	private LatLng nationalArtGallery = new LatLng(-17.825013, 31.048833);
	private LatLng joinaCity = new LatLng(-17.831836, 31.047449);
	private LatLng eastgate = new LatLng(-17.832152, 31.052406);
	private LatLng avondale = new LatLng(-17.802878, 31.038158);	
	private LatLng meikles = new LatLng(-17.830417, 31.052672);
    public  LatLng myPos;
    public  LatLng friendPos;
    private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapmarkers);  

		// Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.markers_map)).getMap();

		if(map !=null){
			
			drawMap();

		
		} else {
			Toast.makeText(this, getString(R.string.nomap_error), 
					Toast.LENGTH_LONG).show();
		}
		
		/********** get Gps location service LocationManager object ***********/
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		/*
		  Parameters :
		     First(provider)    :  the name of the provider with which to register 
		     Second(minTime)    :  the minimum time interval for notifications, in milliseconds. This field is only used as a hint to conserve power, and actual time between location updates may be greater or lesser than this value. 
		     Third(minDistance) :  the minimum distance interval for notifications, in meters 
		     Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location) method will be called for each location update 
        */
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000,   // 3 sec
				10, this);
		
		/********* After registration onLocationChanged method called periodically after each 3 sec ***********/

		
		// Define the criteria how to select the locatioin provider -> use
	    // default
	   
	}

	
	public void drawMap(){
		
		initializeMap();

		addMapMarkers();
		
		// Add marker info window click listener
		map.setOnInfoWindowClickListener(this);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showmap_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		// Toggle traffic overlay
		case R.id.traffic:
			map.setTrafficEnabled(!map.isTrafficEnabled());  
			return true;
			// Toggle satellite overlay
		case R.id.satellite:
			int mt = map.getMapType();
			if(mt == GoogleMap.MAP_TYPE_NORMAL){
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			} else {
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
			return true;
			// Toggle 3D building display (best when showing map instead of satellite)
		case R.id.building:
			map.setBuildingsEnabled(!map.isBuildingsEnabled());
			// Change camera tilt to view from angle if 3D
			if(map.isBuildingsEnabled()){
				changeCamera(map, map.getCameraPosition().target, 
						map.getCameraPosition().zoom, 
						map.getCameraPosition().bearing, 45);
			} else {
				changeCamera(map, map.getCameraPosition().target, 
						map.getCameraPosition().zoom,
						map.getCameraPosition().bearing, 0);
			}
			return true;
			// Toggle whether indoor maps displayed
		case R.id.indoor:
			map.setIndoorEnabled(!map.isIndoorEnabled());
			return true;
			// Settings page	
		case R.id.action_settings:
			// Actions for settings page
			Intent j = new Intent(this, Settings.class);
			startActivity(j);
			return true;  	
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Method to animate camera properties change

	private void changeCamera(GoogleMap map, LatLng center, float zoom, 
			float bearing, float tilt) {

		// Change properties of camera
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(center)         // Sets the center of the map
		.zoom(zoom)             // Sets the zoom
		.bearing(bearing)       // Sets the orientation of the camera 
		.tilt(tilt)             // Sets the tilt of the camera relative to nadir
		.build();               // Creates a CameraPosition from the builder
		if(map != null){
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		} else {
			Toast.makeText(this, getString(R.string.nomap_error), 
					Toast.LENGTH_LONG).show();
		}
	}

	// Method to add map markers. See
	//     http://developer.android.com/reference/com/google/android/gms/maps/model
	//      /BitmapDescriptorFactory.html
	// for additional marker color options.

	private void addMapMarkers(){	

		// Add some location markers
		map.addMarker(new MarkerOptions()
		.title("Harare Institue of Technology")
		.snippet("Higher Education Institute")
		.position(hit)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
		);

		map.addMarker(new MarkerOptions()
		.title("Joina City")
		.snippet("Shopping Mall")
		.position(joinaCity)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		);

		map.addMarker(new MarkerOptions()
		.title("National Art Gallery")
		.snippet("A gallery dedicated to Zimbabwean arts")
		.position(nationalArtGallery)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
		);
		
		map.addMarker(new MarkerOptions()
		.title("Eastgate")
		.snippet("Shopping Mall")
		.position(eastgate)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		);
		
		map.addMarker(new MarkerOptions()
		.title("Meikles")
		.snippet("Department Store")
		.position(meikles)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		);
		
		map.addMarker(new MarkerOptions()
		.title("Avondale")
		.snippet("Shopping Mall")
		.position(avondale)
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
		);

	}
	
	private void addMyPosition(Location location){	

		myPos = new LatLng(location.getLatitude(), location.getLongitude());
		
		// Add some location markers
		map.addMarker(new MarkerOptions()
		.title("You are here")
		.snippet("My Current Position")
		.position(myPos)
		).setDraggable(true);

	}
	
	private void addFriendPosition(Location location){	

		
		// Add some location markers
		map.addMarker(new MarkerOptions()
		.title("You are here")
		.snippet("My Current Position")
		.position(friendPos)
		).setDraggable(true);

	}


	// Method to initialize the map

	private void initializeMap(){

		// Enable or disable current location
		map.setMyLocationEnabled(true);

		// Move camera view and zoom to location
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(eastgate, 13));

		// Initialize type of map
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Initialize 3D buildings enabled for map view
		map.setBuildingsEnabled(false);

		// Initialize whether indoor maps are shown if available
		map.setIndoorEnabled(false);

		// Initialize traffic overlay
		map.setTrafficEnabled(false);

		// Enable rotation gestures
		map.getUiSettings().setRotateGesturesEnabled(true);

	}


	@Override
	public void onInfoWindowClick(Marker marker) {

		String address = null;
		String title = marker.getTitle();
		if(title.equals("Honolulu")){
			address = "http://www.honolulu.gov/government/";		
		} else if (title.equals("Waikiki")) {
			address = "http://en.wikipedia.org/wiki/Waikiki";
		} else if (title.equals("Diamond Head")) {
			address = "http://en.wikipedia.org/wiki/Diamond_Head,_Hawaii";
		}

		marker.hideInfoWindow();

		Intent link = new Intent(Intent.ACTION_VIEW);
		link.setData(Uri.parse(address));
		startActivity(link);

	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String str = "Latitude: "+location.getLatitude()+" \nLongitude: "+location.getLongitude();
		Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
		drawMap();
		addMyPosition(location);
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		
		/******** Called when User off Gps *********/
		
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		
		/******** Called when User on Gps  *********/
		
		Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
