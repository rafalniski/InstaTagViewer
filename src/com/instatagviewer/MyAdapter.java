package com.instatagviewer;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


public class MyAdapter extends FragmentStatePagerAdapter {

	private ArrayList<String> urls;
	private ArrayList<HashMap<String, String>> imageInfo;
	private int positionImage;
	public MyAdapter(android.support.v4.app.FragmentManager fm, ArrayList<String> urls, int position,  ArrayList<HashMap<String, String>> imageInfo) {
		super(fm);
		this.urls = urls;
		this.imageInfo = imageInfo;
		this.positionImage = position;
	}
	@Override
	public Fragment getItem(int position) {
		Log.d("size","Posiiton z funk: " + position +"Pos started: "+ positionImage + " urlssize: "  + urls.size());
		int finalPos = position + positionImage;
		if(finalPos >= imageInfo.size()) finalPos = imageInfo.size()-1;
		// Scroll until we reached all the available images.
		Log.d("size","imageinfosize: " + imageInfo.size() + " urlssize: " +urls.size());
		return FullImageFragment.newInstance(urls.get(finalPos), imageInfo.get(finalPos));
	}

	@Override
	public int getCount() {
		return imageInfo.size();
	}

}
