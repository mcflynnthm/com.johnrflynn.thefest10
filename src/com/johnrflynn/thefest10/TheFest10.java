package com.johnrflynn.thefest10;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class TheFest10 extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	Context mCtx;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mCtx = this;
        SharedPreferences prefs = getSharedPreferences("FestBand", MODE_PRIVATE);
		
        boolean runOnce = prefs.getBoolean("runOnce", false);
        MySchedule ms = new MySchedule(this);
        ms.open();
        
		if (!runOnce) ParseXML("http://johnrflynn.com/fest10/xml/schedule.xml");
		
		Button band = (Button)findViewById(R.id.mainByBand);
        Button date = (Button)findViewById(R.id.mainByDate);
        Button venue = (Button)findViewById(R.id.mainByVenue);
        Button sched = (Button)findViewById(R.id.mainMySched);
        
        band.setOnClickListener(this);
        date.setOnClickListener(this);
        venue.setOnClickListener(this);
        sched.setOnClickListener(this);
        
        
        ms.close();
        
    }
	
	public void onClick (View v){
		String text = "";
		
		switch(v.getId()){
		case R.id.mainByBand:{
	    	Intent i = new Intent(this, ByBand.class);
	    	startActivity(i);
	    	break;
		}
		case R.id.mainByDate:{
			Intent i = new Intent(this, ByDate.class);
	    	startActivity(i);
	    	break;
		}
		case R.id.mainByVenue:{
			Intent i = new Intent(this, ByVenue.class);
	    	startActivity(i);
	    	break;
		}
		case R.id.mainMySched:{
			Intent i = new Intent(this, Schedule.class);
	    	startActivity(i);
	    	break;
		}
		default: text = "wtf you doin son"; break;
		}
	
		if ( text != "") {
			Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
    
	static final String NAME = "name";
	static final String PHOTO = "photo";
	static final String MP3 = "mp3";
	static final String DESC = "description";
	static final String SONG_URL = "songURL";
	static final String SONG_NAME = "songName";
	static final String DATE = "date";
	static final String DURATION = "length";
	static final String VENUE = "venue";
	static final String ACOUSTIC = "acoustic";
	
    public void ParseXML(String XmlUrl){
    	
    	MySchedule ms = new MySchedule(this);
        ms.open();
        
    	
    	XmlResourceParser parser = getResources().getXml(R.xml.myfile2);    	
    	
    	ContentValues cv = new ContentValues();
    	ContentValues bcv = new ContentValues();
    	
    	int index = 0;
    	
    	FestDBAdapter festDB = new FestDBAdapter(this);
    	festDB.open();
    	
    	BandDbAdapter bandDB = new BandDbAdapter(this);
    	bandDB.open();
    	
    	try {
    	    int eventType = parser.getEventType();
    	    String name = null;
    	    while (eventType != XmlPullParser.END_DOCUMENT) {
    	        

    	        switch (eventType){
    	            case XmlPullParser.START_TAG:
    	                name = parser.getName().toLowerCase();
    	                break;
    	            case XmlPullParser.TEXT:
    	            	String text = parser.getText();
    	            	text = text.replace("'", "");
    	            	if (name.equals(NAME)){
    	            		cv.put(NAME, text);
    	            		bcv.put(NAME, text);
    	            	} else if (name.equals(DURATION)){
    	            		cv.put(DURATION, Integer.parseInt(parser.getText()));
    	            	} else if (name.equals(VENUE)){
    	            		cv.put(VENUE, text);
    	            	} else if (name.equals(DATE)){
    	            		cv.put(DATE, text);
    	            	}else if (name.equals(ACOUSTIC)){
    	            		if (parser.getText().equals("false")){
    	            			cv.put(ACOUSTIC, "0");
    	            		} else {
    	            			cv.put(ACOUSTIC, "1");
    	            		}
    	            	} else if (name.equals(MP3)){
    	            		bcv.put(MP3, text);
    	            	} else if (name.equals(DESC)){
    	            		if(parser.getText().equals(null)) bcv.put(DESC, "n/a"); else bcv.put(DESC, text);
    	            	} else if (name.equals(SONG_URL)){
    	            		bcv.put(SONG_URL, text);
    	            	} else if (name.equals(SONG_NAME)){
    	            		bcv.put(SONG_NAME, text);
    	            	} else if (name.equals(PHOTO)){
    	            		bcv.put(PHOTO, text);
    	            	}
    	            	break;
    	            case XmlPullParser.END_TAG:
    	                
    	            	name = parser.getName();
    	            	
    	            	if(name.equals("band")){
    	            		bandDB.addBand(bcv);
    	            		index++;
    	            		bcv = new ContentValues();
    	            	} else if (name.equals("show")){
    	            		festDB.addBand(cv);
    	            	}
    	            	if (name.equals("shows")) cv = new ContentValues();
    	                break;
    	        }

    	        eventType = parser.next();
    	    }
    	}
    	catch (XmlPullParserException e) {
    	    throw new RuntimeException("Cannot parse XML");
    	}
    	catch (IOException e) {
    	    throw new RuntimeException("Cannot parse XML");
    	}
    	finally {
    	    bandDB.close();
    	    festDB.close();
    	}
    	SharedPreferences prefs = getSharedPreferences("FestBand", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("runOnce", true);
		editor.commit();
		
    	
    }
    
}