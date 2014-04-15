package com.instatagger.database;

import java.util.ArrayList;
import java.util.List;

import com.instatagger.database.TagsContract.TagEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DatabaseDataModel {
	private SQLiteDatabase database;
	private TagsEntryDbHelper dbHelper;

	private String[] tagColumns = { TagEntry._ID, TagEntry.COLUMN_NAME_TITLE,
			TagEntry.COLUMN_NAME_IS_FAV };

	public DatabaseDataModel(Context context) {
		dbHelper = new TagsEntryDbHelper(context);
	}

	public boolean addTag(String title) {
		ContentValues values = new ContentValues();
		values.put(TagEntry.COLUMN_NAME_TITLE, title);
		long insertId = database.insert(TagEntry.TABLE_NAME, null, values);
		Cursor cursor = database.query(TagEntry.TABLE_NAME, tagColumns,
				BaseColumns._ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		cursor.close();
		return true;
	}

	public void close() {
		dbHelper.close();
	}

	private TagsContract cursorToTag(Cursor cursor) {
		if (cursor != null) {
			TagsContract tags = new TagsContract();
			tags.setTitle(cursor.getString(cursor
					.getColumnIndex(TagEntry.COLUMN_NAME_TITLE)));
			tags.set_id(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)));
			tags.setIs_fav(cursor.getString(2));
			return tags;
		}
		return null;

	}

	public void deleteTag(long tagID) {
		database.delete(TagEntry.TABLE_NAME, BaseColumns._ID + " = " + "\""
				+ tagID + "\"", null);

	}

	public List<TagsContract> getAllTasks() {
		List<TagsContract> tags = new ArrayList<TagsContract>();
		Cursor cursor = database.query(TagEntry.TABLE_NAME, tagColumns, null,
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TagsContract task = cursorToTag(cursor);
			tags.add(task);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return tags;
	}

	public Cursor getAllTasksInCursor() {
		Cursor cursor = database.query(TagEntry.TABLE_NAME, tagColumns, null,
				null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public String getTagName(long id) {
		Cursor cursor = database.query(TagEntry.TABLE_NAME, tagColumns,
				BaseColumns._ID + " = " + String.valueOf(id), null, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			TagsContract task = cursorToTag(cursor);
			cursor.close();
			return task.getTitle();
		} else {
			return null;
		}

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
}
