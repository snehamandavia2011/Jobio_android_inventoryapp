package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.lab360io.jobio.inventoryapp.acMessageList;

import java.util.Date;

import asyncmanager.asyncAsset;
import asyncmanager.asyncEmployeeList;
import asyncmanager.asyncLoadCommonData;
import asyncmanager.asyncLocationTrackingInterval;
import asyncmanager.asyncMessageList;
import asyncmanager.asyncUserData;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;

public class serServerToDeviceSync extends Service {
    Context mContext;
    public static boolean isServiceRunning = false;
    boolean isSessionExists;
    Handler mHandler = new Handler();

    public serServerToDeviceSync() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //service execute at every 5 minutes
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        Helper.startFabric(mContext);
        isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
        Logger.debug(".....................in serServerToDeviceSync:" + new Date() + " isServiceRunning:" + isServiceRunning + " isSessionExists:" + isSessionExists + " flag:" + flags + " startid:" + startId);
        if (!isServiceRunning && !isSessionExists) {
            return super.onStartCommand(intent, flags, startId);
        }
        //When device was not connected from last 15 minutes, Get the difference between lastInternetConnectivity and  cuurent timeout
        Date dtCurrentTime = new Date();
        long lngServerToDeviceSyncTime = Helper.getLongPreference(mContext, ConstantVal.LAST_SERVER_TO_DEVICE_SYNC_TIME, 0);
        int minutes = Helper.getDifferentInMinute(dtCurrentTime.getTime(), lngServerToDeviceSyncTime);
        Logger.debug("serServerToDeviceSyc: Current time" + dtCurrentTime.toString() + " last server to device sync time:" + new Date(lngServerToDeviceSyncTime).toString() + " minute:" + minutes);
        if (minutes >= 15) {//replace 3 with 15
            Logger.debug("Server to device going to sync as minutes:" + minutes);
            if (!new HttpEngine().isNetworkAvailable(mContext)) {
                Logger.debug("network is not available");
                return super.onStartCommand(intent, flags, startId);
            }
            try {
                new asyncUserData(mContext).join();
                new asyncAsset(mContext).getAllData().join();
                new asyncLoadCommonData(mContext).startSync().join();
                new asyncLocationTrackingInterval(mContext);
                new asyncEmployeeList(mContext);
                if (!acMessageList.IS_MESSAGE_LOADING_RUN) {
                    Logger.debug("Message is going to sync via service");
                    new asyncMessageList(mContext);
                } else {
                    Logger.debug("Message is not going to sync via service");
                }
                Logger.debug("asyncUserData.responseCode:" + asyncUserData.responseCode+" asyncAsset:"+asyncAsset.responseCode);
                if ((asyncUserData.responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) ||//008
                        asyncUserData.responseCode.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED) ||//005
                        asyncUserData.responseCode.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) ||//006
                        (asyncAsset.responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) ||
                                asyncAsset.responseCode.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED) ||
                                asyncAsset.responseCode.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN))) {
                    Date dt = new Date();
                    Logger.debug("in serServerToDeviceSync Last internet connectivity time is going to save:" + dt.toString());
                    Helper.setLongPreference(mContext, ConstantVal.LAST_SERVER_TO_DEVICE_SYNC_TIME, dt.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Logger.debug("Server to device is not going to sync as minutes:" + minutes);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
        if (isSessionExists)
            isServiceRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }
}
