package com.instatagger.utils;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.instatagviewer.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static final int TYPE_WIFI = 1;
	public static final int TYPE_MOBILE = 2;
	public static final int TYPE_NOT_CONNECTED = 0;

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = getConnectivityStatus(context);
		String status = null;
		if (conn == Utils.TYPE_WIFI) {
			status = "Successfully connected to the internet";
		} else if (conn == Utils.TYPE_MOBILE) {
			status = "Successfully connected to the internet";
		} else if (conn == Utils.TYPE_NOT_CONNECTED) {
			status = "Not connected to Internet";
		}
		return status;
	}
	public static void showConnectivityToast(int status, Context context) {
	
		switch(status) {
		case Utils.TYPE_NOT_CONNECTED:
			String message = context.getResources().getString(R.string.no_internet_connection);
			SuperToast.create(context, message , SuperToast.Duration.SHORT, 
				    Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
			break;
		default:
			message = context.getResources().getString(R.string.valid_internet_connection);
			SuperToast.create(context, message , SuperToast.Duration.SHORT, 
				    Style.getStyle(Style.GREEN, SuperToast.Animations.FLYIN)).show();
			break;
		}
			
	}
	
	public static void showBadTagToast(Context context) {
		
			String message = context.getResources().getString(R.string.bad_tag);
			SuperToast.create(context, message , SuperToast.Duration.SHORT, 
				    Style.getStyle(Style.ORANGE, SuperToast.Animations.FLYIN)).show();
			
	}
}