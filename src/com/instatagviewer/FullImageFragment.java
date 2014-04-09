package com.instatagviewer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FullImageFragment extends Fragment {

	private String imageUrl;
	private ImageLoader mLoader;
	private HashMap<String, String> imageInfo;
	private TextView location;
	static FullImageFragment newInstance(String url, HashMap<String, String> imageInfo) {
        FullImageFragment f = new FullImageFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putSerializable("imageInfo", imageInfo);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments() != null ? getArguments().getString("url") : "";
        imageInfo = (HashMap<String, String>) (getArguments() != null ? getArguments().getSerializable("imageInfo") : null);
        mLoader = new ImageLoader(getActivity());
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpager_image, container, false);
        ImageView image = (ImageView) v.findViewById(R.id.imageViewPager);
        ImageView user_image = (ImageView) v.findViewById(R.id.user_photo);
        TextView user_nickname = (TextView) v.findViewById(R.id.username);
        location = (TextView) v.findViewById(R.id.location);
        TextView user_full_name = (TextView) v.findViewById(R.id.full_name);
        user_nickname.setText("@" + imageInfo.get("username"));
        user_full_name.setText(imageInfo.get("full_name"));
        Log.d("info", "Info: Likes: " + imageInfo.get("likes") + " Username: "+ imageInfo.get("username"));
        
        mLoader.DisplayImage(imageUrl,image);
        mLoader.DisplayImage(imageInfo.get("profile_picture"),user_image);
        String location = imageInfo.get("location");
        Log.d("loca","Raw location: " + location);
        if(location.length() > 0 && location.length() < 50) {
        	Log.d("loca",location);
	        location = location.substring(1,location.length()-1);
	        Location mLocation = new Location("");
	        String[] loc = location.split(",");
	        
	        mLocation.setLongitude((Double.parseDouble(loc[0].substring(10,loc[0].length()))));//your coords of course
	        Double latitude;
	        Double longitude = Double.parseDouble(loc[0].substring(10,loc[0].length()));
	        if(loc[1].indexOf(0) == '=') {
	        	latitude = Double.parseDouble(loc[1].substring(10));
	        } else {
	        	latitude = Double.parseDouble(loc[1].substring(10));
	        }
	        Log.d("loca","Lon:'" + longitude + "'Lat:'" + latitude + "'");
	        mLocation.setLatitude(latitude);
	        mLocation.setLongitude(longitude);
	        new GetAddressTask(getActivity()).execute(mLocation);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    private class GetAddressTask extends AsyncTask<Location, Void, String> {
		
    	Context mContext;
		public GetAddressTask(Context context) {
		    super();
		    mContext = context;
		}
		/**
		 * Get a Geocoder instance, get the latitude and longitude
		 * look up the address, and return it
		 *
		 * @params params One or more Location objects
		 * @return A string containing the address of the current
		 * location, or an empty string if no address can be found,
		 * or an error message
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
		        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
		    } catch (IOException e1) {
		    Log.e("LocationSampleActivity",
		            "IO Exception in getFromLocation()");
		    e1.printStackTrace();
		    return ("IO Exception trying to get address");
		    } catch (IllegalArgumentException e2) {
		    // Error message to post in the log
		    String errorString = "Illegal arguments " +
		            Double.toString(loc.getLatitude()) +
		            " , " +
		            Double.toString(loc.getLongitude()) +
		            " passed to address service";
		    Log.e("LocationSampleActivity", errorString);
		    e2.printStackTrace();
		    return errorString;
		    }
		    // If the reverse geocode returned an address
		    if (addresses != null && addresses.size() > 0) {
		        // Get the first address
		        Address address = addresses.get(0);
		        /*
		         * Format the first line of address (if available),
		         * city, and country name.
		         */
		        String addressText = String.format(
		                "%s %s %s",
		                // If there's a street address, add it
		                address.getMaxAddressLineIndex() > 0 ?
		                        address.getAddressLine(0) + "," : "",
		                // Locality is usually a city
		                address.getLocality() != null ? address.getLocality() +"," : "",
		                // The country of the address
		                address.getCountryName() != null ? address.getCountryName() : "");
		        // Return the text
		        return addressText;
		    } else {
		        return "No address found";
		    }
		}
		@Override
        protected void onPostExecute(String address) {
            // Set activity indicator visibility to "gone"
            // Display the results of the lookup.
            location.setText(address);
        }
    }
    
}
