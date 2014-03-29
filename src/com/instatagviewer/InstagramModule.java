package com.instatagviewer;

import android.os.Bundle;

import com.instatagviewer.InstagramAuthDialog.DialogListener;

public class InstagramModule implements
	DialogListener {
	@Override
	public void onComplete(final Bundle values) {
	    String accessToken = values.getString("access_token");
	}
	
	@Override
	public void onError(final DialogError error) {
	    // Log error
	}
	
	@Override
	public void onCancel() {
	    // User canceled.
	}
}
