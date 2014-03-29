package com.instatagviewer;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.instatagviewer.JSONmodel.Datum;
import com.instatagviewer.JSONmodel.Images;
import com.instatagviewer.JSONmodel.Pagination;
import com.instatagviewer.JSONmodel.Standard_resolution;
import com.instatagviewer.JSONmodel.Tags;
import com.instatagviewer.JSONmodel.Thumbnail;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class StaggeredFragment extends Fragment {
	//private  String urls[] = new String[10000];
	private ArrayList<String> urls = new ArrayList<String>();
	private JsonObjectRequest searchRequest;
	private RequestQueue queue;
	private Gson gson;
	private ProgressBar pBar;
	private GridView gridView;
	private StaggeredAdapter adapter;
	private AbstractHttpClient client;
	private String nextUrl;
	private static int photosCounter = 0;
	private String previousUrl;
	// HOLD THE URL TO MAKE THE API CALL TO
	private String URL;

	// STORE THE PAGING URL
	private String pagingURL;

	// FLAG FOR CURRENT PAGE
	int current_page = 1;

	// BOOLEAN TO CHECK IF NEW FEEDS ARE LOADING
	Boolean loadingMore = true;

	Boolean stopLoadingData = false;
	
	public StaggeredFragment() {
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	private class ResponseListener implements Response.Listener<JSONObject>{
			@Override
			public void onResponse(JSONObject response) {
				setUpResults(response);
			}
			
		}
		private class ErrorListener implements Response.ErrorListener{
			@Override
			public void onErrorResponse(VolleyError error) {
				pBar.setVisibility(View.GONE);
				Toast.makeText(getActivity(), 
	                    "Error, Please Try Again", Toast.LENGTH_LONG).show();
			}
		}
		private void setUpResults(JSONObject response) {
			loadingMore = true;
			gson = new Gson();
			Tags tags = gson.fromJson(response.toString(),
					Tags.class);
			Pagination pag = tags.getPagination();
			previousUrl = nextUrl;
			nextUrl = pag.getNext_url();
			//urls = new ArrayList<String>();
			if (tags.getData() != null) {
				List<Datum> data = tags.getData();
				for(Datum item : data) {
					Images images = item.getImages();
					Thumbnail res = images.getThumbnail();
					if(res.getUrl() != null) {
						urls.add(res.getUrl());
					}
					//System.out.println("Nr " + i++ + " " + res.getUrl());
				}
			}
			//pBar.setVisibility(View.GONE);
			//gridView.setVisibility(View.VISIBLE);
			int margin = getResources().getDimensionPixelSize(R.dimen.margin);
			
			//gridView.setItemMargin(margin); // set the GridView margin
			
			gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
			final int currentPosition = gridView.getLastVisiblePosition();
	        //pBar.bility(View.GONE);
				adapter = new StaggeredAdapter(getActivity(), R.id.imageView1, urls);
				gridView.invalidateViews();
				gridView.setAdapter(adapter);	
			//Parcelable state = gridView.onSaveInstanceState();
			Log.d("posi",currentPosition +" a");
			
			//gridView.onRestoreInstanceState(state);
			//gridView.setDefaultState();
			//adapter.notifyDataSetChanged();
			loadingMore = true;
			//
			gridView.post(new Runnable() {
			    @Override
			    public void run() {
			    	//gridView.clearFocus();
			    	gridView.setSelection(currentPosition-1);
			    	gridView.smoothScrollToPosition(currentPosition-1);
			    	pBar.setVisibility(View.GONE);
			    	loadingMore = false;
			    	gridView.clearFocus();
			    }
			});
			//gridView.smoothScrollToPosition(currentPosition+1);
			//gridView.loadMoreCompleated();
			//gridView.setOnLoadMoreListener(null);
			Log.d("dane", "Pobrano dane.");
		}
		
	private boolean loadAPI() {
	        String url = nextUrl != null ? nextUrl : "https://api.instagram.com/v1/tags/food/media/recent?client_id=a2f63ac8fadb4fffb5a75efb4cd6c917";
	        searchRequest = new JsonObjectRequest(Request.Method.GET, url, null, new ResponseListener(), new ErrorListener());
	        client = new DefaultHttpClient();
	        queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));
			queue.add(searchRequest);
			return true;
	    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		MyApplication app = new MyApplication();
		gridView = (GridView) getView().findViewById(R.id.staggeredGridView1);
		pBar = (ProgressBar) getView().findViewById(R.id.progressBar1);
		
		//pBar.setVisibility(View.VISIBLE);
		//gridView.setVisibility(View.GONE);
		
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
		        if ((lastInScreen == totalItemCount) && !(loadingMore) && lastInScreen != 0 && firstVisibleItem != 0 && visibleItemCount <=10) {
		            if (stopLoadingData == false) {
		                Log.d("trace","First: " + firstVisibleItem +" visible: " + visibleItemCount + "total: " + totalItemCount + " last:" +lastInScreen);
		            	loadingMore = true;
		                loadAPI();
		            }

		        }
		    }
		});
		loadAPI();
		//ridView.setOnLoadMoreListener(loadMoreListener);
	}
	 @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
	}
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.staggered_fragment, container,false);
	 }
}



