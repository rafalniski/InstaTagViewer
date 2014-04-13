package com.instatagviewer;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.instatagviewer.TagsContract.TagEntry;

public class TagsManagerFragment extends ListFragment {

	private DatabaseDataModel db;
	private ActionMode mActionMode;
	public TagsManagerFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tags_manager_fragment, null);
		db = new DatabaseDataModel(getActivity());
    	db.open();
		setList();
		getActivity().invalidateOptionsMenu();
		return view;
	}
	private void setList() {
		Cursor mCursor = db.getAllTasksInCursor();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1,
				mCursor,
				new String[] {TagEntry.COLUMN_NAME_TITLE},
				new int[] {android.R.id.text1},1);
		adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
				if(aColumnIndex == 1) {
					String tagTitle = aCursor.getString(aColumnIndex);
	                TextView textView = (TextView) aView;
	                textView.setText("#" + tagTitle);
	                return true;

				}
				return false;
			}
		});
		setListAdapter(adapter);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_add:
			Log.d("lol","Klliknieto add.");
			QuantityDialogFragment dialog = new QuantityDialogFragment();
	        dialog.show(getFragmentManager(), "Dialog");
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_add).setVisible(true);
		getActivity().invalidateOptionsMenu();
	}
	public void onDialogClicked(String value) {
		if(value == null) {
			// inform user that he's naughty 
		} else {
			// add to database
			db.addTag(value);
			setList();
			
		}
		
	}
	@Override
	public void onListItemClick(ListView item, View v, int position, long id) {
		
		super.onListItemClick(item, v, position, id);
		mActionMode = getActivity().startActionMode(new ActionModeCallback(id));
	}
	private class ActionModeCallback implements Callback {
	    	
	    	private long dbID;
	        public ActionModeCallback(long id) {
				this.dbID = id;
			}

			@Override
	        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	            // inflate contextual menu
	            mode.getMenuInflater().inflate(R.menu.tag_modify, menu);
	            return true;
	        }
	 
	        @Override
	        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        	return true;
	        }
	 
	        @Override
	        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        	
	            switch (item.getItemId()) {
	            case R.id.menu_delete:
	            	db.deleteTag(this.dbID);
	            	setList();
	            	
	            	mode.finish();
	            	break;
	            default:
	                return false;
	            }
				return true;
	 
	        }
	 
	        @Override
	        public void onDestroyActionMode(ActionMode mode) {
	            // remove selection
	            mActionMode = null;
	        }
	    }
	@SuppressLint("ValidFragment")
	private class QuantityDialogFragment extends DialogFragment implements OnClickListener {

	    private EditText editQuantity;

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        editQuantity = new EditText(getActivity());
	        editQuantity.setInputType(InputType.TYPE_CLASS_TEXT);
	        editQuantity.setText("#");
	        editQuantity.setSelection(1);

	        return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage("Please Enter tag name")
	                .setPositiveButton("ADD", this).setNegativeButton("CANCEL", null).setView(editQuantity).create();

	    }

	    @Override
	    public void onClick(DialogInterface dialog, int position) {

	        String value = editQuantity.getText().toString();
	        Log.d("Quantity: ", value);
	        if(value.indexOf("#") == 0) {
	        	value = value.substring(1);
	        }
	        onDialogClicked(value);
	        dialog.dismiss();
	    }
	}
}