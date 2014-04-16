package com.instatagger;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "iKFfpmgaGte6VhjFFT8ihYth2KmQbPaeR9LuxsD3", "U0zaIMXhZuS1jr5AAFGojJ17HcIYFzEbSIYmdpYc");
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}
}
