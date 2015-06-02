package com.chatt.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatt.demo.custom.CustomActivity;
import com.chatt.demo.model.Conversation;
import com.chatt.demo.utils.Const;
import com.chatt.demo.wadii.maps.MapsActivity;
import com.chatt.demo.wadii.maps.ShowMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * The Class Chat is the Activity class that holds main chat screen. It shows
 * all the conversation messages between two users and also allows the user to
 * send and receive messages.
 */
public class Chat extends CustomActivity
{
	public String Longitude;
	
	public String Latitude;
	
    public String latitude;
	  
    public String longitude;
	
	public double longitudeDouble;
	
	public double latitudeDouble;
	
	private double lon;
	
	private double lat;

	/** The Conversation list. */
	private ArrayList<Conversation> convList;

	/** The chat adapter. */
	private ChatAdapter adp;

	/** The Editext to compose the message. */
	private EditText txt;

	/** The user name of buddy. */
	private String buddy;

	/** The date of last message in conversation. */
	private Date lastMsgDate;

	/** Flag to hold if the activity is running or not. */
	private boolean isRunning;

	/** The handler. */
	private static Handler handler;
	
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);

		convList = new ArrayList<Conversation>();
		ListView list = (ListView) findViewById(R.id.list);
		adp = new ChatAdapter();
		list.setAdapter(adp);
		list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		list.setStackFromBottom(true);

		txt = (EditText) findViewById(R.id.txt);
		txt.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

		setTouchNClick(R.id.btnSend);
		setTouchNClick(R.id.btnSendLocation);
		
		final Button SendLocation = (Button) findViewById(R.id.btnSendLocation);
		
		 new CountDownTimer(30000, 1000) {
          
		     public void onTick(long millisUntilFinished) {
		    	 SendLocation.setEnabled(false); 
		         
		     }

		     public void onFinish() {
		    	 SendLocation.setEnabled(true); 
		     }
		  }.start();

		
		

		buddy = getIntent().getStringExtra(Const.EXTRA_DATA);
		getActionBar().setTitle(buddy);

		handler = new Handler();
		
		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}
	
	public void openAlert(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
	      alertDialogBuilder.setMessage(R.string.decision);
	      alertDialogBuilder.setPositiveButton(R.string.positive_button, 
	      new DialogInterface.OnClickListener() {
			
	         @Override
	         public void onClick(DialogInterface arg0, int arg1) {
	           
	        	 if(latitude.compareTo("") != 0 && longitude.compareTo("") != 0){
						lat = Double.parseDouble(latitude);
						lon = Double.parseDouble(longitude); 
						ShowMap.putMapData(lat, lon, 13, true);
						Intent MapFriend = new Intent(Chat.this, ShowMap.class);
			            startActivity(MapFriend);
					 }
	         }
	      });
	      alertDialogBuilder.setNegativeButton(R.string.negative_button, 
	      new DialogInterface.OnClickListener() {
				
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	 dialog.dismiss();
			 }
	      });
		    
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
		    
	   }
 
	
	/* Class My Location Listener */

	public class MyLocationListener implements LocationListener

	{

	@Override

	public void onLocationChanged(Location loc){
	latitudeDouble = loc.getLatitude();
	longitudeDouble= loc.getLongitude();
	
	Latitude = String.valueOf(latitudeDouble);
	Longitude = String.valueOf(longitudeDouble);
	
	//String Text = "My current location is: "+ "Latitude = " + loc.getLatitude() + "Longitude = " + loc.getLongitude();
	//Toast.makeText( getApplicationContext(),Text,Toast.LENGTH_SHORT).show();
	}


	@Override

	public void onProviderDisabled(String provider){
	//SendLocation.setEnabled(false); 
	Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
    }


	@Override
	public void onProviderEnabled(String provider){
	Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
	// new CountDownTimer(30000, 1000) {
         
	  //   public void onTick(long millisUntilFinished) {
	  //  	 SendLocation.setEnabled(false); 
	         
	  //   }

	 //    public void onFinish() {
	 //   	 SendLocation.setEnabled(true); 
	 //    }
	 // }.start();
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){

	}

	}/* End of Class MyLocationListener */
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		isRunning = true;
		loadConversationList();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		isRunning = false;
	}

	/* (non-Javadoc)
	 * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnSend)
		{
			sendMessage();
		}
		if (v.getId() == R.id.btnSendLocation)
		{
			
			sendLocation();
			//Intent mapintent = new Intent(Chat.this, MapsActivity.class);
            //Intent mapintent = new Intent("com.chatt.demo.wadii.maps.MapsActivity");
            //startActivity(mapintent);
		}


	}
	
	/**
	 * Call this method to Send Location to opponent. It does nothing if the location
	 * is empty otherwise it creates a Parse object for user location and sends it
	 * to Parse server.
	 */
	private void sendLocation()
	{
		if (Latitude.length() == 0 && Longitude.length() == 0){
			
			Toast.makeText(getApplicationContext(), "Please Wait Location Is Loading", Toast.LENGTH_LONG).show();
			
		}else{
			

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

		String s = "GPS:" + Latitude + "," + Longitude;
		final Conversation c = new Conversation(s, new Date(),
				UserList.user.getUsername());
		c.setStatus(Conversation.STATUS_SENDING);
		convList.add(c);
		adp.notifyDataSetChanged();
		txt.setText(null);

		ParseObject po = new ParseObject("Chat");
		po.put("sender", UserList.user.getUsername());
		po.put("receiver", buddy);
		// po.put("createdAt", "");
		po.put("message", s);
		po.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e)
			{
				if (e == null)
					c.setStatus(Conversation.STATUS_SENT);
				else
					c.setStatus(Conversation.STATUS_FAILED);
				adp.notifyDataSetChanged();
			}
		});
		}
	}

	/**
	 * Call this method to Send message to opponent. It does nothing if the text
	 * is empty otherwise it creates a Parse object for Chat message and sends it
	 * to Parse server.
	 */
	private void sendMessage()
	{
		if (txt.length() == 0)
			return;

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

		String s = txt.getText().toString();
		final Conversation c = new Conversation(s, new Date(),
				UserList.user.getUsername());
		c.setStatus(Conversation.STATUS_SENDING);
		convList.add(c);
		adp.notifyDataSetChanged();
		txt.setText(null);

		ParseObject po = new ParseObject("Chat");
		po.put("sender", UserList.user.getUsername());
		po.put("receiver", buddy);
		// po.put("createdAt", "");
		po.put("message", s);
		po.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e)
			{
				if (e == null)
					c.setStatus(Conversation.STATUS_SENT);
				else
					c.setStatus(Conversation.STATUS_FAILED);
				adp.notifyDataSetChanged();
			}
		});
	}

	/**
	 * Load the conversation list from Parse server and save the date of last
	 * message that will be used to load only recent new messages
	 */
	private void loadConversationList()
	{
		ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
		if (convList.size() == 0)
		{
			// load all messages...
			ArrayList<String> al = new ArrayList<String>();
			al.add(buddy);
			al.add(UserList.user.getUsername());
			q.whereContainedIn("sender", al);
			q.whereContainedIn("receiver", al);
		}
		else
		{
			// load only newly received message..
			if (lastMsgDate != null)
				q.whereGreaterThan("createdAt", lastMsgDate);
			q.whereEqualTo("sender", buddy);
			q.whereEqualTo("receiver", UserList.user.getUsername());
			
		}
		q.orderByDescending("createdAt");
		q.setLimit(30);
		q.findInBackground(new FindCallback<ParseObject>() {

			//this function then loads the list for both 
			@Override
			public void done(List<ParseObject> li, ParseException e)
			{
				if (li != null && li.size() > 0)
				{
					for (int i = li.size() - 1; i >= 0; i--)
					{
						ParseObject po = li.get(i);
						Conversation c = new Conversation(po
								.getString("message"), po.getCreatedAt(), po
								.getString("sender"));
						convList.add(c);
						
						if (i == 0){
						   String TestForGps = po.getString("message");
						   TestForGps = TestForGps.toLowerCase();
						   String GPS = "GPS:";
						   GPS = GPS.toLowerCase();
						   //Toast.makeText( getApplicationContext(),TestForGps,Toast.LENGTH_SHORT ).show();
						   if (TestForGps.contains(GPS)){
							   String Coordinates = TestForGps.replace("gps:","");
							   //Toast.makeText( getApplicationContext(),Coordinates,Toast.LENGTH_SHORT ).show();
							   
							  
							   
							   String[] result = Coordinates.split(",");
							   for (int x=0; x<result.length; x++) {
								  //Toast.makeText( getApplicationContext(),result[x],Toast.LENGTH_SHORT ).show();
								   latitude = result[0];
								   longitude= result[1];
								   //Toast.makeText( getApplicationContext(),"lat"+latitude,Toast.LENGTH_SHORT ).show();
								   //Toast.makeText( getApplicationContext(),"lon"+longitude,Toast.LENGTH_SHORT ).show();
							   }
							   
							   openAlert();
							    
						   }
						}
						
						if (lastMsgDate == null
								|| lastMsgDate.before(c.getDate()))
							lastMsgDate = c.getDate();
						adp.notifyDataSetChanged();
						
						if (lastMsgDate == null
								|| lastMsgDate.before(c.getDate())){
							
							
						}
						
					}
				}
				handler.postDelayed(new Runnable() {

					@Override
					public void run()
					{
						if (isRunning)
							loadConversationList();
					}
				}, 1000);
			}
		});

	}

	/**
	 * The Class ChatAdapter is the adapter class for Chat ListView. This
	 * adapter shows the Sent or Receieved Chat message in each list item.
	 */
	private class ChatAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return convList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Conversation getItem(int arg0)
		{
			return convList.get(arg0);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			Conversation c = getItem(pos);
			if (c.isSent())
				v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
			else
				v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

			TextView lbl = (TextView) v.findViewById(R.id.lbl1);
			lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
					.getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
					DateUtils.DAY_IN_MILLIS, 0));

			lbl = (TextView) v.findViewById(R.id.lbl2);
			lbl.setText(c.getMsg());

			lbl = (TextView) v.findViewById(R.id.lbl3);
			if (c.isSent())
			{
				if (c.getStatus() == Conversation.STATUS_SENT)
					lbl.setText("Delivered");
				else if (c.getStatus() == Conversation.STATUS_SENDING)
					lbl.setText("Sending...");
				else
					lbl.setText("Failed");
			}
			else
				lbl.setText("");

			return v;
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
