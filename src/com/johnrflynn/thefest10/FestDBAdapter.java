package com.johnrflynn.thefest10;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FestDBAdapter {
	
	private static final String TAG = "FestDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase FestDb;

	static final String KEY_ID = "_id";
	static final String KEY_NAME = "name";
	static final String KEY_DATE = "date";
	static final String KEY_DURATION = "length";
	static final String KEY_VENUE = "venue";
	static final String KEY_ACOUSTIC = "acoustic";
	
	private static final String FESTDB_CREATE =
        "create table if not exists fest (" + 
        "_id integer primary key autoincrement, " +
        "name text not null, " + 
        "date text not null, " +
        "length integer not null, " +
        "venue text not null, " +
        "acoustic text not null" +
        ");";
	
	private static final String DATABASE_NAME = "data";
    private static final String FESTDB_TABLE = "fest";
    private static final int DATABASE_VERSION = 2;
    
    private final  Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);    
        }
    	
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(FESTDB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		public void onOpen(SQLiteDatabase db){
			db.execSQL(FESTDB_CREATE);
		}
    }
    
    public FestDBAdapter(Context c){
    	this.mCtx = c;
    }
    
    public FestDBAdapter open() throws SQLException{
    	mDbHelper = new DatabaseHelper(mCtx);
    	FestDb = mDbHelper.getWritableDatabase();
//    	FestDb.execSQL(FESTDB_CREATE);
    	return this;
    }
    
    public void close(){
    	mDbHelper.close();
    }
    
    public long addBand(String name, Date date, int length, String venue, boolean acoustic){
    	ContentValues init = new ContentValues();
    	init.put(KEY_NAME, name);
    	init.put(KEY_DATE, date.toString());
    	init.put(KEY_DURATION, length);
    	init.put(KEY_VENUE, venue);
    	if(acoustic) { init.put(KEY_ACOUSTIC, 1); } else {init.put(KEY_ACOUSTIC, 0);}
    	
    	return FestDb.insert(FESTDB_TABLE, null, init);
    }
    
    public long addBand(ContentValues cv){
    	
    	
    	return FestDb.insert(FESTDB_TABLE, null, cv);
    }
    
    public Cursor fetchBands(){
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_NAME}, null, null, null, null, KEY_DATE);
    }
    
    public Cursor fetchAllShows(){
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_NAME, KEY_DATE, KEY_VENUE, KEY_DURATION, KEY_ACOUSTIC}, null, null, null, null, null);
    }
    
    public Cursor fetchDateShows(int day){
    	String date = null;
    	switch(day){
    	case 1: date = "2010-10-29"; break;
    	case 2: date = "2010-10-30"; break;
    	case 3: date = "2010-10-31"; break;
    	}
    	
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_NAME, KEY_DATE, KEY_VENUE, KEY_DURATION, KEY_ACOUSTIC, KEY_ID}, KEY_DATE+" like '"+date+"%'", null, null, null, KEY_DATE+" ASC");
    }
    
    public Cursor fetchShows(String name){
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_DATE, KEY_VENUE, KEY_ACOUSTIC}, KEY_NAME+"='"+name+"'", null, null, null, null);
    }
    
    public Cursor fetchVenueShows(String venue){
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_NAME, KEY_DATE, KEY_DURATION, KEY_ACOUSTIC, KEY_ID}, KEY_VENUE+"='"+venue+"'", null, null, null, KEY_DATE+" ASC");
    }
    
    public Cursor fetchShowById(int id){
    	return FestDb.query(FESTDB_TABLE, new String[] {KEY_NAME, KEY_DATE, KEY_VENUE, KEY_DURATION, KEY_ACOUSTIC, KEY_ID}, KEY_ID+"='"+id+"'", null, null, null, null);
    }
    
    public void destroyDB(){
    	FestDb.execSQL("DROP TABLE fest;");
    }
}
