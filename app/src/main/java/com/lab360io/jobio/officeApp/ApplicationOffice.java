package com.lab360io.jobio.officeApp;

import android.support.multidex.MultiDexApplication;

import com.onesignal.OneSignal;

import notification.NotificationReceiveHandler;
import utility.Logger;

/**
 * Created by SAI on 11/12/2016.
 */
public class ApplicationOffice extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceiveHandler())
                .init();
    }
}
