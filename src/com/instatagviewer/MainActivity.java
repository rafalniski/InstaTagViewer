package com.instatagviewer;

import java.util.ArrayList;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.instatagviewer.InstagramAuthDialog.DialogListener;
import com.instatagviewer.slidingmenu.adapter.NavigationDrawerAdapter;
import com.instatagviewer.slidingmenu.model.NavigationDrawerItem;

public class MainActivity extends Activity implements OnNavigationListener {
	
    private ArrayList<NavigationDrawerItem> navDrawerItems;
	private NavigationDrawerAdapter adapter;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    private TypedArray navMenuIcons;
    private String[] navMenuTitles;
	private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;

	
	// nav drawer title
    private CharSequence mDrawerTitle;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        
        navDrawerItems = new ArrayList<NavigationDrawerItem>();
 
        // adding nav drawer items to array
        navDrawerItems.add(new NavigationDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        // setting the nav drawer list adapter
        adapter = new NavigationDrawerAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
        }
	}
	private class SlideMenuClickListener implements
    ListView.OnItemClickListener {
	 @Override
	 public void onItemClick(AdapterView<?> parent, View view, int position,
	         long id) {
	     // display view for selected nav drawer item
	     displayView(position);
	 }
}
	private void displayView(int position) {
        // update the main content by replacing fragments
		switch(position) {
		case 4:
		    final InstagramAuthDialog dialog = new InstagramAuthDialog(this,
		            new DialogListener() {
						
						@Override
						public void onError(DialogError error) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onComplete(Bundle values) {
							 String accessToken = values.getString("access_token");
							 Log.e("access_token","Access: " + accessToken);
							
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}
					}, getResources().getString(R.string.client_id), "basic+comments");
		    dialog.setCancelable(false);
		    dialog.show();
		    break;
		case 0:	 
			Fragment fragment  = new StaggeredFragment();
		        if (fragment != null) {
		            FragmentManager fragmentManager = getFragmentManager();
		            fragmentManager.beginTransaction()
		                    .replace(R.id.frame_container, fragment).commit();
		        }
		     break;
            
		}
		mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
	 @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
		}
		 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// toggle nav drawer on selecting action bar app icon/title
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	            return super.onOptionsItemSelected(item);
	        
		}
		/**
	     * When using the ActionBarDrawerToggle, you must call it during
	     * onPostCreate() and onConfigurationChanged()...
	     */
	 
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }
	 
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
		@Override
	    public void setTitle(CharSequence title) {
	        mTitle = title;
	        getActionBar().setTitle(mTitle);
	    }
		
		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			// if nav drawer is opened, hide the action item
	        return super.onPrepareOptionsMenu(menu);
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// TODO Auto-generated method stub
			return false;
		}

	}
