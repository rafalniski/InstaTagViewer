package com.instatagviewer;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class ViewPagerFragment extends Fragment {
	private ViewPager mPager;
	private int position;
	private ArrayList<String> urls;
	public ViewPagerFragment() {}
	
	public ViewPagerFragment(ArrayList<String> urls, int position) {
		this.urls = urls;
		this.position = position;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mPager = (ViewPager) getView().findViewById(R.id.frame);
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(new ViewPagerAdapter(getActivity().getApplicationContext(), getActivity(), position, urls));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.view_pager, container,false);
		
	}
}
