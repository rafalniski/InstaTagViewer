package com.instatagger.slidingmenu.model;

public class NavigationDrawerItem {
	private String title;
	private int icon;
	private String count = "0";
	private int id;
	private boolean isCounterVisible = false;

	public NavigationDrawerItem() {
	}

	public NavigationDrawerItem(String title, int icon, int id) {
		this.title = title;
		this.icon = icon;
		this.setId(id);
	}

	public NavigationDrawerItem(String title, int icon,
			boolean isCounterVisible, String count) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle() {
		return this.title;
	}

	public int getIcon() {
		return this.icon;
	}

	public String getCount() {
		return this.count;
	}

	public boolean getCounterVisibility() {
		return this.isCounterVisible;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setCounterVisibility(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
