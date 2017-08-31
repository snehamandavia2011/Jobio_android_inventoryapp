package com.stackio.jobio.officeApp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import notification.Notification;

/**
 * Created by SAI on 11/12/2016.
 */
public class ApplicationOffice extends MultiDexApplication {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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
