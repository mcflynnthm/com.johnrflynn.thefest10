package com.johnrflynn.thefest10;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Schedule extends ListActivity {
	
	Context mCtx;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		MySchedule ms = new MySchedule(this);
		ms.open();
		
		mCtx = this;
		
		FestDBAdapter fest = new FestDBAdapter(this);
		fest.open();
		
		Cursor c = ms.getSched();
		c.moveToFirst();
		String[] shows = new String[c.getCount()];
		
		for(int x = 0; x< c.getCount(); x++){
			Cursor f = fest.fetchShowById(c.getInt(0));
			f.moveToFirst();
			shows[x] = f.getString(0);
			if(f.getString(4).equals("1")) shows[x] = shows[x]+ " (acoustic)";
			shows[x] = shows [x] +"\n"+f.getString(1)+" @"+f.getString(2)+ " ("+f.getInt(3)+"min)"; 
			c.moveToNext();
		}
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, shows));
    	
    	ListView lv = getListView();
    	lv.setTextFilterEnabled(true);
    	
    	lv.setOnItemClickListener(new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//    			String bandName = (String)parent.getItemAtPosition(position);
    			MySchedule ms = new MySchedule(mCtx);
    			ms.open();
    			
    			ms.removeShow(position+1);
    			
    			ms.close();
    			
    			
    		}
    	});
		
	}

}
