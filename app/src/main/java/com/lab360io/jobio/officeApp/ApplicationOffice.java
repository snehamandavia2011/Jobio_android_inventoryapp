package com.lab360io.jobio.officeApp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import notification.Notification;

/**
 * Created by SAI on 11/12/2016.
 */
public class ApplicationOffice extends MultiDexApplication {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Notification.initNotification(this);
        /*OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceiveHandler())
                .setNotificationOpenedHandler(new NotificationOpenHandler()).init();*/
    }

    public static Context getContext() {
        return appContext;
    }

}
