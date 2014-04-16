package com.instatagger.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.instatagger.utils.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		int status = Utils.getConnectivityStatus(context);
		if(status != Utils.TYPE_NOT_CONNECTED) {
			Utils.showConnectivityToast(status, context);
		}
	}
}
