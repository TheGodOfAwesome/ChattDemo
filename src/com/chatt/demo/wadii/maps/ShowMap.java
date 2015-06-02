package com.chatt.demo.wadii.maps;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chatt.demo.Chat;
import com.chatt.demo.R;
import com.chatt.demo.custom.DialogFragmentActivity;
import com.chatt.demo.wadii.database.LocationsDataSource;
import com.chatt.demo.wadii.database.MySQLiteHelper;
import com.chatt.demo.wadii.database.Locations;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;


public class ShowMap extends FragmentActivity implements
GoogleMap.OnInfoWindowClickListener, LocationListener {

	private static double lat;
	private static double lon;
	private static int zm;
	private static boolean trk;
	private static LatLng map_center;
	private GoogleMap map;
	public Context context;
	private Component c1,c2,c3;
	private Dialog list_dialog;
	ComponentAdapter array_adapter;
	private LocationManager locationManager;
	private LatLng hit = new LatLng(-17.839802, 31.008064);
	private LatLng nationalArtGallery = new LatLng(-17.825013, 31.048833);
	private LatLng joinaCity = new LatLng(-17.831836, 31.047449);
	private LatLng eastgate = new LatLng(-17.832152, 31.052406);
	private LatLng avondale = new LatLng(-17.802878, 31.038158);	
	private LatLng meikles = new LatLng(-17.830417, 31.052672);
    public  LatLng myPos;
    public  LatLng friendPos;
    private LocationsDataSource datasource;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmap);
		context = this;
		
		
		//addMapMarkers();
		// Add marker info window click listener
		//map.setOnInfoWindowClickListener(this);
		
		//creation and population of the list
        List<Component> my_list = new ArrayList<Component>();
        createComponents();
        my_list.add(c1);
        my_list.add(c2);
        my_list.add(c3);

        //adapter
        array_adapter = new ComponentAdapter(context, R.layout.component,my_list);

        
		// Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.the_map)).getMap();

		if(map != null){
			initializeMap();
		} else {
			Toast.makeText(this, getString(R.string.nomap_error), 
					Toast.LENGTH_LONG).show();
		}

	}
	
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
	
	
	private void createComponents() {
		// TODO Auto-generated method stub
		c1 = new Component("Component 1","subtitle 1");
		c2 = new Component("Component 2","subtitle 2");
		c3 = new Component("Component 3","subtitle 3");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showmap_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(map == null) {
			Toast.makeText(this, getString(R.string.nomap_error), 
					Toast.LENGTH_LONG).show();
			return false;
		}

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
			// Toggle 3D building display 
		case R.id.building:
			map.setBuildingsEnabled(!map.isBuildingsEnabled());
			// Change camera tilt to view from angle if 3D
			if(map.isBuildingsEnabled()){
				changeCamera(map, map.getCameraPosition().target, 
						map.getCameraPosition().zoom, 
						map.getCameraPosition().bearing, 45, true);
			} else {
				changeCamera(map, map.getCameraPosition().target, 
						map.getCameraPosition().zoom,
						map.getCameraPosition().bearing, 0, true);
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
		case R.id.suggestions:
			
			datasource = new LocationsDataSource(this);
			datasource.open();
			

	        final String[] planets = new String[] { "Apple", "Banana"};  
			
			List<Locations> values = datasource.getAllComments(); 
		    if (values.isEmpty() == true){
		    	 Toast.makeText(getApplicationContext(),
                         "Values is empty, Add location to " , Toast.LENGTH_LONG)
                         .show();		 
		    }else{
		    	//Toast.makeText(getApplicationContext(),
                  //      "Values is not empty" , Toast.LENGTH_LONG)
                    //    .show();
		    	// retrieving data from string list array in for loop
		    	 for (int i=0;i < values.size();i++)
		    	 {
		    	   double curlati1 = 0;
		    	   double curlongi1 = 0;
		    	   String val = "" + values.get(i);
		    	    String [] newValues = {"empty", ""};
		    		   String [] sorter = {"empty", ""};
		    		   String [] sorted = {"empty", ""};
		    		   double [] Distances = {0, 1};
		    		   String dist;
		    	   newValues[i] = val;
		    	   //Toast.makeText(getApplicationContext(),
	                 //        val , Toast.LENGTH_LONG)
	                   //      .show();
		    	   
		    	   
		    	   String TestForGps = val;
		    	   int startIndex = 0;
		    	   int endIndex = TestForGps.indexOf(":");
		    	   String replacement = "";
		    	   String toBeReplaced = TestForGps.substring(startIndex, endIndex + 1);
		    	   Toast.makeText(getApplicationContext(), TestForGps.replace(toBeReplaced, replacement), Toast.LENGTH_LONG).show();		
		    	   String Coordinates = TestForGps.replace(toBeReplaced, replacement);
				   
				   String[] result = Coordinates.split(",");
				   for (int x=0; x<result.length; x++) {
					   String curlati = result[0];
					   String curlongi= result[1];
					   if(curlati.compareTo("") != 0 && curlongi.compareTo("") != 0){
							curlati1 = Double.parseDouble(curlati);
							curlongi1 = Double.parseDouble(curlongi); 
						 }
				   }
				   //Toast.makeText(getApplicationContext(), distance(lat, lon, curlati1, curlongi1), Toast.LENGTH_LONG).show();
				   dist = "Distance = " + distance(lat, lon, curlati1, curlongi1);
				   double Distance = Double.parseDouble(distance(lat, lon, curlati1, curlongi1));
				   Distances [i] = Distance;
				   sorter[i] = newValues[i] + " " + dist;
				
				   planets[i] = newValues[i] + " " + dist;
				  
				   
		    	 }
		    	}
			
			// Use the SimpleCursorAdapter to show the
			// elements in a ListView
			
			 //String [] places = new String[values.size()];
			 //values.toArray(places);
			
			  //final String [] places = values.toArray(new String[values.size()]);
			
			
			 //String [] array = new String [values.size()];
			 //values.toArray(array); // fill the array
			
		    //for (int l=0; l < values.size();l++)
			  // {
			    //      for(int m = 0; m < values.size(); m++)
			      //    {
			        //           if(Distances [l] < Distances [m + 1])
			         //          {
			         //                      double tempVar = Distances [m + 1];
			         //                      Distances [m + 1]= Distances [l];
			         //                      Distances [l] = tempVar;
			         //          }
			         // }
			   //}
			   
			   //for(int i=0; i < values.size();i++){
			   //String check = Distances[0] + "";
			   //String checkAgain = Distances[1] + "";
			   //String strVal = newValues[i];
			   //if(strVal.contains(check)){
			   //   sorted[0] = strVal + dist;
			   //}
			   //if(strVal.contains(checkAgain)){
				//   sorted[1] = strVal + dist;
			   //}
	    	   //
	    	   
			   //planets[0] = sorted[0];
			   //planets[1] = sorted[1];
			   //}
	
			 AlertDialog.Builder builder = new AlertDialog.Builder(ShowMap.this);
			    builder.setTitle(R.string.suggestions_label)
			           .setItems(planets, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int position) {
			               // The 'which' argument contains the index position
			               // of the selected item
			            	   int itemPosition     = position;

		                       // ListView Clicked item value
		                      //String  itemValue    = (String) planets.getItemAtPosition(position);

		                        // Show Alert 
		                         Toast.makeText(getApplicationContext(),
		                          "Position :"+itemPosition+"  ListItem : "+ planets[itemPosition] , Toast.LENGTH_LONG)
		                          .show();
			            	   
			           }
			    });
			    AlertDialog alert = builder.create();
                alert.show();
                
			//list_dialog = new Dialog(context);
			//list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//list_dialog.setContentView(R.layout.list_dialog);
			
			//final ListView list = (ListView)list_dialog.findViewById(R.id.component_list);
			//list.setAdapter(array_adapter);
			 
			//list.setOnItemClickListener(new OnItemClickListener() {
			//		@Override
			//		public void onItemClick(AdapterView<?> parent, View view,
			//				int position, long id) {
			//			// TODO Auto-generated method stub
			//			
			//			int itemPosition     = position;

	                       // ListView Clicked item value
	        //               String  itemValue    = (String) list.getItemAtPosition(position);

	                        // Show Alert 
	        //                 Toast.makeText(getApplicationContext(),
	        //                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
	        //                  .show();
			//		}
		    //   } );
			
			//Button positiveButton = (Button) list_dialog.findViewById(R.id.positive_button);
			
			//positiveButton.setOnClickListener(new OnClickListener(){

			//	@Override
			//	public void onClick(View arg0) {

			//		list_dialog.dismiss();	
			//	}
			//});

			//list_dialog.show();	
			return true;
			
			
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Method to initialize the map.  Check for map != null before calling.

	private void initializeMap(){

		// Enable or disable current location
		map.setMyLocationEnabled(trk);

		// Move camera view and zoom to location
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(map_center, zm));

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

	/* Method to change properties of camera. If your GoogleMaps instance is called map, 
	 * you can use 
	 * 
	 * map.getCameraPosition().target
	 * map.getCameraPosition().zoom
	 * map.getCameraPosition().bearing
	 * map.getCameraPosition().tilt
	 * 
	 * to get the current values of the camera position (target, which is a LatLng), 
	 * zoom, bearing, and tilt, respectively.  This permits changing only a subset of
	 * the camera properties by passing the current values for all arguments you do not
	 * wish to change.
	 * 
	 * */

	private void changeCamera(GoogleMap map, LatLng center, float zoom, 
			float bearing, float tilt, boolean animate) {

		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(center)         // Sets the center of the map
		.zoom(zoom)             // Sets the zoom
		.bearing(bearing)       // Sets the bearing of the camera 
		.tilt(tilt)             // Sets the tilt of the camera relative to nadir
		.build();               // Creates a CameraPosition from the builder

		// Move (if variable animate is false) or animate (if animate is true) to new 
		// camera properties. 

		if(animate){
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		} else {
			map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	// Set these data using this static method before launching this class with an Intent:
	// for example, ShowMap.putMapData(30,150,18,true);

	public static void putMapData(double latitude, double longitude, int zoom, boolean track){
		lat = latitude;
		lon = longitude;
		zm = zoom;
		trk = track;
		map_center = new LatLng(lat,lon);
	}

	public void onLocationChanged(Locations location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	
	 private String distance(double lati, double longi, double curlati1,
		        double curlongi1) {

		    double theta = longi - curlongi1;
		    double dist = Math.sin(deg2rad(lati)) * Math.sin(deg2rad(curlati1))
		            + Math.cos(deg2rad(lati)) * Math.cos(deg2rad(curlati1))
		            * Math.cos(deg2rad(theta));
		    dist = Math.acos(dist);
		    dist = rad2deg(dist);
		    dist = dist * 60 * 1.1515;

		    dist = dist * 1.609344;

		    String result = Double.toString(dist);
		    System.out.println("dist_result :" + result);
		    return (result);
		}

		/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
		/* :: This function converts decimal degrees to radians : */
		/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
		private double deg2rad(double deg) {
		    return (deg * Math.PI / 180.0);
		}

		/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
		/* :: This function converts radians to decimal degrees : */
		/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
		private double rad2deg(double rad) {
		    return (rad * 180.0 / Math.PI);
		}


}
