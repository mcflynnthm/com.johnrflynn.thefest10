package com.johnrflynn.thefest10;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ShowAdapter extends SimpleAdapter {

	Context mCtx;
	int[] my;
	
	public ShowAdapter(Context c, List<HashMap<String, String>> items, int res, String[] from, int[] to){
		super(c, items, res, from, to);
		mCtx = c;
	}
	
	public ShowAdapter(Context c, List<HashMap<String, String>> items, int res, String[] from, int[] to, int[] myshows){
		super(c, items, res, from, to);
		mCtx = c;
		my = myshows;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent){
		View view = super.getView(pos, convertView, parent);
		
		if(my[pos] == 1) view.setBackgroundColor(Color.rgb(51, 102, 51)); else view.setBackgroundColor(Color.BLACK);
		
		
		return view;
	}
}
