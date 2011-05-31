package com.johnrflynn.thefest10;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class BandPage extends Activity{

	String name;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.bandpage);
        
        // Open the two databases: FestDB for shows
        // and BandDB for the band info.
        BandDbAdapter bandDb = new BandDbAdapter(this);
        bandDb.open();
        
        FestDBAdapter festDb = new FestDBAdapter(this);
        festDb.open();
        
        // Now who were we talking about? Oh, right.
        SharedPreferences prefs = getSharedPreferences("FestBand", MODE_PRIVATE);
        name = prefs.getString("band", "Red City Radioz");
        
        if(name.contains("' ")) name = name.replace("' ", "'' ");
        
        Cursor c = bandDb.fetchBand(name);
        c.moveToFirst();
        
        Cursor b = festDb.fetchShows(name);
        b.moveToFirst();
        
        // Set it up
        String bname, info, pic;
        bname = c.getString(0);
        pic = "http://www.thefestfl.com/"+c.getString(1);
        info = c.getString(2);
        
        // Alter the page all dynamic-like
        TextView tv = (TextView)findViewById(R.id.bandName);
        tv.setText(bname);
        
        TextView infov = (TextView)findViewById(R.id.bandInfo);
        infov.setText(info);
        
        ImageView imgV = (ImageView)findViewById(R.id.bandPic);
        imgV.setImageBitmap(getPic(pic));
        
        // Set up the GridView for the shows this band will be playing
        
        String[] shows = null;
        shows = new String[b.getCount()];
        for (int x = 0; x<b.getCount(); x++){
        	shows[x] = b.getString(0)+"\n"+b.getString(1);
        	b.moveToNext();
        }
        
        GridView gv = (GridView)findViewById(R.id.bandShows);
        gv.setAdapter(new BandAdapter(this, shows));
        gv.setVerticalSpacing(1);
        gv.setBackgroundColor(Color.WHITE);
        
        

//        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, shows));
	}
	
	/*
	 *  Retrieves the band's picture from the Fest website
	 *  Happily, they're already there, and formatted to a
	 *  standard size, 111px x 200px!
	 */
	private Bitmap getPic(String srcUrl){
		Bitmap bitmap = null;
		InputStream in = null;
		
		try{
			in = OpenHttpConnection(srcUrl);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return bitmap;
		
	}
	
	/*
	 * Opens that HTTP connection for the picture above.
	 */
	private InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
}
