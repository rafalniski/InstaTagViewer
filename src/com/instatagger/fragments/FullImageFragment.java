package com.instatagger.fragments;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.instatagger.R;
import com.instatagger.utils.ImageLoader;
import com.mattyork.colours.Colour;

public class FullImageFragment extends Fragment {

	private class GetAddressTask extends AsyncTask<Location, Void, String> {

		Context mContext;

		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 * 
		 * @params params One or more Location objects
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 */
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				e1.printStackTrace();
				return ("IO Exception trying to get address");
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(loc.getLatitude()) + " , "
						+ Double.toString(loc.getLongitude())
						+ " passed to address service";
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = String.format(
						"%s %s %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) + "," : "",
						// Locality is usually a city
						address.getLocality() != null ? address.getLocality()
								+ "," : "",
						// The country of the address
						address.getCountryName() != null ? address
								.getCountryName() : "");
				// Return the text
				return addressText;
			} else {
				return "";
			}
		}

		@Override
		protected void onPostExecute(String address) {
			// Set activity indicator visibility to "gone"
			// Display the results of the lookup.
			location.setText(address);
		}
	}
	public static FullImageFragment newInstance(String url,
			HashMap<String, String> imageInfo, String title) {
		FullImageFragment f = new FullImageFragment();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("url", url);
		args.putString("title", title);
		args.putSerializable("imageInfo", imageInfo);
		f.setArguments(args);

		return f;
	}
	private HashMap<String, String> imageInfo;
	private String imageUrl;
	private TextView location;
	private ImageLoader mLoader;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageUrl = getArguments() != null ? getArguments().getString("url")
				: "";
		getActivity().setTitle(
				"#" + getArguments() != null ? getArguments()
						.getString("title") : getResources().getString(
						R.string.app_name));
		
		this.imageInfo = (HashMap<String, String>) (getArguments() != null ? getArguments()
				.getSerializable("imageInfo") : null);
		getActivity().getActionBar().setBackgroundDrawable(
				new ColorDrawable(Colour.crimsonColor()));
		mLoader = new ImageLoader(getActivity());
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_viewpager_image, container,
				false);
		ImageView image = (ImageView) v.findViewById(R.id.imageViewPager);
		ImageView user_image = (ImageView) v.findViewById(R.id.user_photo);
		TextView imageText = (TextView) v.findViewById(R.id.text);
		TextView likesText = (TextView) v.findViewById(R.id.likes);
		TextView user_nickname = (TextView) v.findViewById(R.id.username);
		TextView image_date = (TextView) v.findViewById(R.id.time);
		TextView image_tags = (TextView) v.findViewById(R.id.tags);
		location = (TextView) v.findViewById(R.id.location);
		TextView user_full_name = (TextView) v.findViewById(R.id.full_name);
		user_nickname.setText("@" + imageInfo.get("username"));
		String tags = imageInfo.get("tags");
		if(image_tags != null) {
			image_tags.setText(tags);
		}
		user_full_name.setText(imageInfo.get("full_name"));
		String creation_time = imageInfo.get("creation_date");
		if (!creation_time.isEmpty()) {
			likesText.setText("Likes: " + imageInfo.get("likes"));
			Date currentDate = new Date(Long.parseLong(imageInfo
					.get("creation_date")) * 1000);
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault());
			image_date.setText(df.format(currentDate));
			imageText.setMovementMethod(new ScrollingMovementMethod());
			imageText.setText(imageInfo.get("text"));
		}

		mLoader.DisplayImage(imageUrl, image);
		mLoader.DisplayImage(imageInfo.get("profile_picture"), user_image);
		String location = imageInfo.get("location");
		if (location.length() > 20 && location.length() < 50) {
			location = location.substring(1, location.length() - 1);
			Location mLocation = new Location("");
			String[] loc = location.split(",");

			mLocation.setLongitude((Double.parseDouble(loc[0].substring(10,
					loc[0].length()))));// your coords of course
			Double latitude;
			Double longitude = Double.parseDouble(loc[0].substring(10,
					loc[0].length()));
			if (loc[1].indexOf(0) == '=') {
				latitude = Double.parseDouble(loc[1].substring(10));
			} else {
				latitude = Double.parseDouble(loc[1].substring(10));
			}
			mLocation.setLatitude(latitude);
			mLocation.setLongitude(longitude);
			new GetAddressTask(getActivity()).execute(mLocation);
		} else {
			this.location.setVisibility(View.GONE);
		}
		return v;
	}

}
