package com.instatagger.database;

import android.provider.BaseColumns;

public class TagsContract {
	/*
	 * This is an inner class representing single table. By implementing
	 * BaseColumns we gain consistency by having _ID and _COUNT in every row
	 */
	public static abstract class TagEntry implements BaseColumns {
		public static final String COLUMN_NAME_IS_FAV = "is_fav";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String TABLE_NAME = "tags";
		
	}
	public static final String COMMA_SEP = ",";
	public static final String TEXT_TYPE = " TEXT ";
	public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ TagEntry.TABLE_NAME + " ( " + TagEntry._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ TagEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP
			+ TagEntry.COLUMN_NAME_IS_FAV + TEXT_TYPE + ")";

	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TagEntry.TABLE_NAME;

	
	private String _id;

	private String is_fav;

	private String title;

	public TagsContract() {
	}

	public String get_id() {
		return _id;
	}

	public String getIs_fav() {
		return is_fav;
	}

	public String getTitle() {
		return title;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public void setIs_fav(String is_fav) {
		this.is_fav = is_fav;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
