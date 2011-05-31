package com.johnrflynn.thefest10;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BandAdapter extends BaseAdapter {

	Context mContext;
	String[] Bands;
	
	public BandAdapter(Context c, String[] fest){
		mContext = c;
		Bands = fest;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Bands.length;
	}

	@Override
	public String getItem(int x) {
		// TODO Auto-generated method stub
		return Bands[x];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(mContext);
		tv.setTextSize(14);
		tv.setTextColor(Color.BLACK);
		tv.setText(Bands[pos]);
		return tv;
	}

}
