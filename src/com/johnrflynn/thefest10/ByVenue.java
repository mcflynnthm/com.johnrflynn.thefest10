package com.johnrflynn.thefest10;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ByVenue extends ListActivity {
	
	Context mCtx;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.byvenue);

	    mCtx = this;
	    
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, venues));
    	
    	ListView lv = getListView();
    	lv.setTextFilterEnabled(true);
    	
    	lv.setOnItemClickListener(new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    			String venueName = (String)parent.getItemAtPosition(position);
    			SharedPreferences prefs = getSharedPreferences("FestBand", MODE_PRIVATE);
    			SharedPreferences.Editor editor = prefs.edit();
        	
    			Intent i = new Intent(mCtx, VenuePage.class);
    			editor.putString("venue", venueName);
    			editor.commit();
    			startActivity(i);
    			
    		}
    	});
	}
	
	private String[] venues = new String[] {
		"1982",
		"8 Seconds",
		"Boca Fiesta",
		"Civic Media Center",
		"Common Grounds",
		"Holiday Inn Poolside",
		"No Idea",
		"Rum Runners",
		"Spin Cycle",
		"The Atlantic",
		"The Lunch Box",
		"The Top",
		"The Venue",
		"The Venue Side Hatch"
	};
    		
}
