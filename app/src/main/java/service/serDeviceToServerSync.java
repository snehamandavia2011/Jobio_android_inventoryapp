package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.crashlytics.android.Crashlytics;

import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import io.fabric.sdk.android.Fabric;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;

public class serDeviceToServerSync extends Service {
    Context mContext;
    public static boolean isServiceRunning = false;
    boolean isSessionExists;

    public serDeviceToServerSync() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //service execute at every 3 minutes
    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
        Logger.debug(".....................in serDeviceToServerSync:" + new Date() + " isServiceRunning:" + isServiceRunning + " isSessionExists:" + isSessionExists + " flag:" + flags + " startid:" + startId);
        if (!isServiceRunning && !isSessionExists) {
            return super.onStartCommand(intent, flags, startId);
        }
        //check internet in every poll and poll would be 3 minutes
        new Thread() {
            public void run() {
                HttpEngine objHttpEngine = new HttpEngine();
                if (objHttpEngine.isNetworkAvailable(mContext)) {
                    //check, is there any pending unsync data or not, by checking firing qyery on sync table with filtering status as 0
                    String currentAdminUserId = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                    String currentAccountId = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    String currentToken = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    DataBase db = new DataBase(mContext);
                    db.open();
                    Cursor cutUnSyncData = db.fetch(DataBase.device_to_db_sync_table, DataBase.device_to_db_sync_int, "isSync=0 and admin_user_id='" + currentAdminUserId + "'and account_id='" + currentAccountId + "'");
                    boolean isInternetFound = false;
                    if (cutUnSyncData != null && cutUnSyncData.getCount() > 0) {
                        Logger.debug("Data need to sync serDeviceToServerSync:" + cutUnSyncData.getCount());
                        cutUnSyncData.moveToFirst();
                        do {
                            //send one by one request back to server, VERY IMP but do not store entry in sync table
                            int id = cutUnSyncData.getInt(0);
                            String url = cutUnSyncData.getString(1);
                            StringBuffer data = new StringBuffer(cutUnSyncData.getString(2));
                            try {
                                //replace token with current token
                                int startIndex = data.indexOf("token_id=") + 9;
                                int endIndex = data.indexOf("&", startIndex);
                                String oldToken = data.substring(startIndex, endIndex);
                                //Logger.debug(data + " " + startIndex + " " + endIndex + " " + oldToken);
                                if (oldToken.length() <= 0) {
                                    data = data.insert(startIndex, currentToken);
                                } else {
                                    data = data.replace(startIndex, endIndex, currentToken);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Logger.debug(data.toString());
                            ServerResponse objServerResponse = objHttpEngine.makeHttpRequestCall(mContext, url, data.toString());

                            if (!objServerResponse.getResponseCode().equals(ConstantVal.ServerResponseCode.NO_INTERNET))
                                isInternetFound = true;
                            boolean isUpdated = objHttpEngine.updateSyncTable(mContext, id, url, objServerResponse);
                            Logger.debug("After Device to server sync:" + objServerResponse.getResponseCode() + " isInternetFound:" + isInternetFound);
                        } while (cutUnSyncData.moveToNext());
                    }
                    cutUnSyncData.close();
                    db.close();

                    //Though service fot server to device sync is running every 5 minutes, but as internet is available start the
                    //service for "Server to device sync". And this "Server to device sync" make the sync if current time - last sync time
                    // is greater than 15 minutes, else do not make any sync.
                    if (isInternetFound) {
                        Logger.debug("serServerToDeviceSync is calling by serDeviceToServerSync as internet connectivity is found");
                        Intent i = new Intent(mContext, serServerToDeviceSync.class);
                        startService(i);
                    }
                }
            }
        }.start();
/*c- If Internet is available then,
A - check, is there any pending unsync data or not, by checking firing qyery on sync table with filtering status as 0
B - by checking index value send one by one request back to server, VERY IMP but do not store entry in sync table
C - Finally if you get result code = 5, 6, 8 then turn the status as 1 for the transaction, if you do not get 5, 6 ,7 still do not make entry in sync
D - If one or more than one data sync successfully with server than store lastInternetConnectivity  and do once Server to device sync.*/

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Helper.startFabric(mContext);
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
