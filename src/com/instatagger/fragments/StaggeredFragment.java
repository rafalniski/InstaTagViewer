package com.instatagger.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.instatagger.JSONmodel.Datum;
import com.instatagger.JSONmodel.Pagination;
import com.instatagger.JSONmodel.Standard_resolution;
import com.instatagger.JSONmodel.Tags;
import com.instatagger.fragments.adapters.StaggeredAdapter;
import com.instatagviewer.R;

public class StaggeredFragment extends Fragment {
	private class ErrorListener implements Response.ErrorListener {
		@Override
		public void onErrorResponse(VolleyError error) {
			if (isVisible()) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				if (attemptCount++ < 4) {
					getActivity().setProgressBarIndeterminateVisibility(true);

					loadAPI();
				}

				Toast.makeText(getActivity(),
						"Error: " + error.getMessage() + ", Please Try Again",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	private class ResponseListener implements Response.Listener<JSONObject> {
		@Override
		public void onResponse(JSONObject response) {
			new SetUpResults().execute(response);
		}

	}
	private class SetUpResults extends AsyncTask<JSONObject, Void, Void> {
		@Override
		protected Void doInBackground(JSONObject... params) {
			JSONObject response = params[0];
			loadingMore = true;
			gson = new Gson();
			Tags tags = gson.fromJson(response.toString(), Tags.class);
			Pagination pag = tags.getPagination();
			nextUrl = pag.getNext_url();
			urls = new ArrayList<String>();
			if (tags.getData() != null) {
				List<Datum> data = tags.getData();
				for (Datum item : data) {
					HashMap<String, String> info = new HashMap<String, String>();
					Standard_resolution res = item.getImages()
							.getStandard_resolution();
					if (res.getUrl() != null) {
						String likes = item.getLikes() != null ? item
								.getLikes().getCount().toString() : "";
						String creation_date = item.getCaption() != null ? item
								.getCaption().getCreated_time() : "";
						String location = item.getLocation() != null ? item
								.getLocation().toString() : "";
						String username = item.getUser() != null ? item
								.getUser().getUsername() : "";
						String full_name = item.getUser() != null ? item
								.getUser().getFull_name() : "";
						String profile_picture = item.getUser() != null ? item
								.getUser().getProfile_picture() : "";
						String text = item.getCaption() != null ? item
								.getCaption().getText() : "";
						info.put("likes", likes);
						info.put("creation_date", creation_date);
						info.put("location", location);
						info.put("username", username);
						info.put("full_name", full_name);
						info.put("profile_picture", profile_picture);
						info.put("text", text);
						urls.add(res.getUrl());
						imageInfo.add(info);
						AllUrls.add(res.getUrl());
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isVisible() && getActivity() != null) {
				if (adapter == null) {
					adapter = new StaggeredAdapter(getActivity(),
							R.id.imageView1, urls);
					gridView.setAdapter(adapter);
				} else {
					if (urls != null && gridView != null
							&& gridView.getAdapter() != null) {
						// TODO handle when urls is null - probably problem with
						// connections
						((StaggeredAdapter) gridView.getAdapter()).addAll(urls);
						((StaggeredAdapter) gridView.getAdapter())
								.notifyDataSetChanged();
					} else if (gridView.getAdapter() == null) {
						Log.d("grdiviewgetadapternull",
								"Wywolano to co nie powinno sie wywolac:)");
						getActivity().setProgressBarIndeterminateVisibility(
								true);
						adapter = new StaggeredAdapter(getActivity(),
								R.id.imageView1, urls);
						gridView.setAdapter(adapter);

					}
				}
				loadingMore = false;
				getActivity().setProgressBarIndeterminateVisibility(false);
				getActivity().invalidateOptionsMenu();
			}

		}

	}
	private static StaggeredAdapter adapter;
	// HOLD THE URL TO MAKE THE API CALL TO
	private static int attemptCount = 0;
	private ArrayList<String> AllUrls = new ArrayList<String>();
	private AbstractHttpClient client;
	// FLAG FOR CURRENT PAGE
	int current_page = 1;
	private GridView gridView;
	private Gson gson;
	private ArrayList<HashMap<String, String>> imageInfo = new ArrayList<HashMap<String, String>>();
	// BOOLEAN TO CHECK IF NEW FEEDS ARE LOADING
	Boolean loadingMore = true;
	private String nextUrl;

	private RequestQueue queue;

	private JsonObjectRequest searchRequest;

	Boolean stopLoadingData = false;

	private String tagName = "food";

	// private String urls[] = new String[10000];
	private ArrayList<String> urls = new ArrayList<String>();

	public StaggeredFragment() {

	}

	private boolean loadAPI() {
		String url = nextUrl != null ? nextUrl
				: "https://api.instagram.com/v1/tags/"
						+ this.tagName
						+ "/media/recent?client_id=a2f63ac8fadb4fffb5a75efb4cd6c917";
		searchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new ResponseListener(), new ErrorListener());
		client = new DefaultHttpClient();
		queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(
				client));
		queue.add(searchRequest);
		return true;
	}

	public Fragment newInstance(String tagName) {
		Fragment f = new StaggeredFragment();
		Bundle args = new Bundle();
		args.putString("tagName", tagName);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gridView = (GridView) getView().findViewById(R.id.staggeredGridView1);

		// pBar.setVisibility(View.VISIBLE);
		// gridView.setVisibility(View.GONE);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("click", "Position: " + position + " ID: " + id
						+ "Item: " + adapter.getItem(position));
				Intent intent = new Intent(getActivity(),
						ViewPagerFragment.class);
				intent.putStringArrayListExtra("urls", AllUrls);
				intent.putExtra("imageInfo", imageInfo);
				intent.putExtra("title", tagName);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == (totalItemCount - 12)) && !(loadingMore)
						&& lastInScreen > 0) {
					if (stopLoadingData == false) {
						Log.d("trace", "First: " + firstVisibleItem
								+ " visible: " + visibleItemCount + "total: "
								+ totalItemCount + " last:" + lastInScreen);
						loadingMore = true;
						getActivity().setProgressBarIndeterminateVisibility(
								true);
						loadAPI();
					}

				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}
		});

		getActivity().setProgressBarIndeterminateVisibility(true);
		loadAPI();

		// ridView.setOnLoadMoreListener(loadMoreListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			tagName = getArguments().getString("tagName");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.staggered_fragment, container, false);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (loadingMore == false)
			menu.findItem(R.id.action_refresh).setVisible(true);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

	}

}
