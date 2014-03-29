package com.instatagviewer;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class InstagramAuth extends Activity {
	private static final String AUTHURL="https://api.instagram.com/oauth/authorize/";
	private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
	public static final String APIURL = "https://api.instagram.com/v1";
	public static String CALLBACKURL = "instagram://connect";
	private String authURLString = AUTHURL + "?client_id=" + getResources().getString(R.string.client_id) + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient()); 
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authURLString);
	}
}
