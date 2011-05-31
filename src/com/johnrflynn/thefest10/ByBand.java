package com.johnrflynn.thefest10;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ByBand extends ListActivity {

	Context mCtx;
	
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	mCtx = this;
    	
		BandDbAdapter bandDB = new BandDbAdapter(this);
    	bandDB.open();
    	
    	Cursor c = bandDB.fetchBands();

    	String [] bands = new String[c.getCount()];
    	int index = 0;
    	c.moveToFirst();
    	String name = null;
    	do{
    		name = c.getString(0);
    		if(name.contains("''")) name = name.replace("''", "'");
    		bands[index] = name;
    		c.moveToNext();
    		index++;
    	} while (index != c.getCount());
    	bandDB.close();
    	
    	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, bands));
    	
    	ListView lv = getListView();
    	lv.setTextFilterEnabled(true);
    	
    	lv.setOnItemClickListener(new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    			String bandName = (String)parent.getItemAtPosition(position);
    			//bandName = DatabaseUtils.sqlEscapeString(bandName);
    			SharedPreferences prefs = getSharedPreferences("FestBand", MODE_PRIVATE);
    			SharedPreferences.Editor editor = prefs.edit();
        	
    			Intent i = new Intent(mCtx, BandPage.class);
    			editor.putString("band", bandName);
    			editor.commit();
    			startActivity(i);
    		}
    	});
    	
	}
}
