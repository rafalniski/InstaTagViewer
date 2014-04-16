package com.instatagger.broadcastReceivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.instatagger.MainActivity;
import com.instatagger.fragments.StaggeredFragment;
import com.instatagger.utils.Utils;
import com.instatagviewer.R;
import com.mattyork.colours.Colour;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		int status = Utils.getConnectivityStatus(context);
		if(status != Utils.TYPE_NOT_CONNECTED) {
			Utils.showConnectivityToast(status, context);
		}
	}
}
