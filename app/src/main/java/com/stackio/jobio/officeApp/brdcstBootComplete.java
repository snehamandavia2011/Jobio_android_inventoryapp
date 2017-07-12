package com.stackio.jobio.officeApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import entity.ClientLocationTrackingInterval;
import utility.Helper;
import utility.Logger;

public class brdcstBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Helper.startFabric(context);
        int intIntervalTime = Helper.getIntPreference(context, ClientLocationTrackingInterval.Fields.LOCATION_TRACKING_INTERVAL, 0);
        Logger.debug("........................in brdcstBootComplete:" + intIntervalTime);
        Helper.startBackgroundService(context);
        Helper.scheduleLocationService(context, intIntervalTime);
    }
}
