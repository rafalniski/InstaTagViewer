package com.instatagger;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.instatagger.database.DatabaseDataModel;
import com.instatagger.database.TagsContract;
import com.instatagger.fragments.StaggeredFragment;
import com.instatagger.fragments.TagsManagerFragment;
import com.instatagger.slidingmenu.adapter.NavigationDrawerAdapter;
import com.instatagger.slidingmenu.model.NavigationDrawerItem;
import com.instatagger.utils.Utils;
import com.instatagviewer.R;
import com.mattyork.colours.Colour;

public class MainActivity extends Activity implements OnNavigationListener {

	private NavigationDrawerAdapter adapter;
	private DatabaseDataModel db;
	private int lastClickedPosition = 0;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	// nav drawer title
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private ArrayList<NavigationDrawerItem> navDrawerItems;

	private ArrayList<String> navMenuTitles;
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position, id);
		}
	}
	private void displayView(int position, long id) {
		if(Utils.getConnectivityStatus(this) != Utils.TYPE_NOT_CONNECTED) {
			switch (position) {
			case 0:
				TagsManagerFragment fragment = new TagsManagerFragment();
				if (fragment != null) {
					android.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
				}
				break;
			default:
				Fragment fragmentSecond = new StaggeredFragment().newInstance(db
						.getTagName(id));
				if (fragmentSecond != null) {
					android.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragmentSecond).commit();
	
				}
				break;
	
			}
		} else {
			Utils.showConnectivityToast(Utils.TYPE_NOT_CONNECTED, this);
		}
		lastClickedPosition = position;
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles.get(position));
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void initDrawer() {
		mTitle = getTitle();
		navMenuTitles = new ArrayList<String>();
		db = new DatabaseDataModel(this);
		db.open();
		List<TagsContract> tags = db.getAllTasks();
		navDrawerItems = new ArrayList<NavigationDrawerItem>();
		navMenuTitles.add("Manage Tags");
		navDrawerItems.add(new NavigationDrawerItem("Manage Tags",
				R.drawable.edittags, 0));
		for (TagsContract tag : tags) {
			navMenuTitles.add(tag.getTitle());
			navDrawerItems.add(new NavigationDrawerItem(tag.getTitle(),
					R.drawable.hash, Integer.parseInt(tag.get_id())));
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		adapter = new NavigationDrawerAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDrawer();

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Colour.crimsonColor()));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.app_name,
				R.string.app_name) {
			@Override
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				initDrawer();
				mDrawerList.setItemChecked(lastClickedPosition, true);
				mDrawerList.setSelection(lastClickedPosition);
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		if (savedInstanceState == null) {
			// on first time display manage tags view, because there no tags
			if (navDrawerItems.size() > 1) {
				displayView(1, navDrawerItems.get(1).getId());
				mDrawerList.setItemChecked(1, true);
				mDrawerList.setSelection(1);
			} else {

				displayView(0, navDrawerItems.get(0).getId());
				mDrawerList.setItemChecked(0, true);
				mDrawerList.setSelection(0);
			}
		} else {
			int savedPosition = savedInstanceState.getInt("lastPosition");
			lastClickedPosition = savedPosition;
			displayView(savedPosition, navDrawerItems.get(savedPosition).getId());
			mDrawerList.setItemChecked(savedPosition, true);
			mDrawerList.setSelection(savedPosition);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_refresh:
			if(Utils.getConnectivityStatus(this) != Utils.TYPE_NOT_CONNECTED) {
				item.setVisible(false);
				displayView(lastClickedPosition,
				navDrawerItems.get(lastClickedPosition).getId());
			} else {
				Utils.showConnectivityToast(Utils.TYPE_NOT_CONNECTED, this);
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
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
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("lastPosition", lastClickedPosition);
		super.onSaveInstanceState(outState);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action item
		menu.findItem(R.id.action_add).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	

}
