package com.instatagger.fragments.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.instatagger.fragments.FullImageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FullImageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<HashMap<String, String>> imageInfo;
	private int positionImage;
	private String title;
	private ArrayList<String> urls;

	public FullImageAdapter(android.support.v4.app.FragmentManager fm,
			ArrayList<String> urls, int position,
			ArrayList<HashMap<String, String>> imageInfo, String title) {
		super(fm);
		this.urls = urls;
		this.imageInfo = imageInfo;
		this.positionImage = position;
		this.title = title;
	}

	@Override
	public int getCount() {
		return urls.size() - positionImage;
	}

	@Override
	public Fragment getItem(int position) {
		int finalPos = position + positionImage;
		if (finalPos >= imageInfo.size()) {
			finalPos = imageInfo.size() - 1;
		}
		// Scroll until we reached all the available images.
		return FullImageFragment.newInstance(urls.get(finalPos),
				imageInfo.get(finalPos), this.title);
	}

}
