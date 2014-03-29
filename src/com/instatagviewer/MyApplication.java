package com.instatagviewer;

import java.io.File;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;

public class MyApplication extends Application {
	public static ImageLoaderConfiguration config;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		File cacheDir = StorageUtils.getCacheDirectory(this);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		
		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);
		
	}
}
