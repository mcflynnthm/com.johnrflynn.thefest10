package com.johnrflynn.thefest10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DateFriday extends ListActivity {
	
	Context mCtx;
	int[] my;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mCtx = this;
        
        FestDBAdapter festDB = new FestDBAdapter(this);
        festDB.open();
        
        MySchedule ms = new MySchedule(this);
        ms.open();
        
        Cursor c = festDB.fetchDateShows(1);
        c.moveToFirst();
        
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        String [] from = new String[] {"show"};
        int[] to = new int[] {R.id.show};
        
        my = new int[c.getCount()];
        
        for(int x = 0; x < c.getCount(); x++){
        	HashMap<String, String> map = new HashMap<String, String>();

        	String show = c.getString(0) + " @"+ c.getString(2)+"\n" + 
        			   c.getString(1).replace("2010-10-29 ", "") + " (" + c.getString(3)+"min)";
        	map.put("show", show);
        	if(ms.checkShow(c.getInt(5))) my[x] = 1; else my[x] = 0;
        	fillMaps.add(map);
        	c.moveToNext();
        }
        
        ms.close();
        festDB.close();
        
        ShowAdapter adapter = new ShowAdapter(this, fillMaps, R.layout.list_item, from, to, my);
        
        setListAdapter(adapter);
    	
    	ListView lv = getListView();
    	lv.setTextFilterEnabled(true);
    	
    	lv.setOnItemClickListener(new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
    			MySchedule ms = new MySchedule(mCtx);
    			ms.open();
    			
    			FestDBAdapter festDB = new FestDBAdapter(mCtx);
    	        festDB.open();
    	        
    	        Cursor c = festDB.fetchDateShows(1);    			
    			c.moveToPosition(position);
    			
    			if(ms.checkShow(c.getInt(5))){
    				int showId = ms.getShowId(c.getInt(5));
    				ms.removeShow(showId);
    				view.setBackgroundColor(Color.BLACK);
    				my[position] = 0;
    			} else {
    				my[position] = 1;
    				view.setBackgroundColor(Color.rgb(51,102,51));
    				ms.addShow(c.getInt(5));
    			
    			}
    			
    			festDB.close();
    			ms.close();
    		}
    	});
    }
}
