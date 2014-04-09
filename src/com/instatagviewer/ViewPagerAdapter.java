package com.instatagviewer;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class ViewPagerAdapter extends PagerAdapter {

	 Activity activity;
	 private ImageLoader mLoader;
	 private  int pos;
	 private ArrayList<String> imageArray;

	    public ViewPagerAdapter(Context context, Activity act, int startPosition, ArrayList<String> imageArray) {
	        activity = act;
	        this.imageArray = imageArray;
	        this.pos = startPosition;
	        mLoader = new ImageLoader(context);
	    }

	    public int getCount() {
	        return Integer.MAX_VALUE;
	    }


	    public Object instantiateItem(View collection, int position) {

	        ImageView mwebView = new ImageView(activity);
	        ((ViewPager) collection).addView(mwebView, 0);
	        //mwebView.setScaleType(ScaleType.CENTER);
	       // mwebView.setImageResource(imageArray[pos]);
	        mLoader.DisplayImage(imageArray.get(pos), mwebView);
	        Log.d("pos","Pos: " + pos + "Position z funkcji: " + position);
	        if (pos >= imageArray.size() - 1)
	           pos = 0;
	        else
	           ++pos;

	        return mwebView;
	    }

	    @Override
	    public void destroyItem(View arg0, int arg1, Object arg2) {
	        ((ViewPager) arg0).removeView((View) arg2);
	    }

	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == ((View) arg1);
	    }

	    @Override
	    public Parcelable saveState() {
	        return null;
	    }

}
