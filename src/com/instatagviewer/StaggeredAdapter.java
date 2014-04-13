package com.instatagviewer;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.annotations.SerializedName;

public class StaggeredAdapter extends ArrayAdapter<String> {

	private ImageLoader mLoader;
	
	public StaggeredAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		mLoader = new ImageLoader(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		long start = System.currentTimeMillis();
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.row_staggered_demo,null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}
		
		holder = (ViewHolder) convertView.getTag();
		mLoader.DisplayImage(getItem(position), holder.imageView);
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		Log.d("czas", "Czas wykonania: " + elapsedTimeMillis/1000F);
		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
	}
}
