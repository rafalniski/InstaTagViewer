package com.instatagger.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.instatagger.fragments.adapters.FullImageAdapter;
import com.instatagger.utils.ZoomOutPageTransformer;
import com.instatagviewer.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

@SuppressLint("ValidFragment")
public class ViewPagerFragment extends FragmentActivity {
	private ViewPager mPager;
	private int position;
	private ArrayList<String> urls;
	private ArrayList<HashMap<String, String>> imageInfo;
	public ViewPagerFragment() {
	}

	FullImageAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		urls = getIntent().getExtras().getStringArrayList("urls");
		position = getIntent().getExtras().getInt("position");
		imageInfo = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("imageInfo");
		mAdapter = new FullImageAdapter(getSupportFragmentManager(), urls, position,
				imageInfo, getIntent().getExtras().getString("title"));

		mPager = (ViewPager) findViewById(R.id.frame);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPager.setAdapter(mAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
