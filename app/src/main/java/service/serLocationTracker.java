package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.crashlytics.android.Crashlytics;

import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientAdminUserAppsRel;
import entity.ClientFieldLocation;
import entity.MyLocation;
import io.fabric.sdk.android.Fabric;
import utility.ConstantVal;
import utility.GPSTracker;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.URLMapping;

public class serLocationTracker extends Service {
    Context mContext;
    public static boolean isServiceRunning = false;

    public serLocationTracker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        Helper.startFabric(mContext);
        boolean isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
        boolean is_gps_tracking = (Helper.getStringPreference(mContext, ClientAdminUserAppsRel.Fields.IS_GPS_TRACKING, "").equals("Y") ? true : false);
        Logger.debug(".....................in serLocationTracker:" + new Date() + " isServiceRunning:" + isServiceRunning + " isSessionExists:" + isSessionExists + " is_gps_trackinh:" + is_gps_tracking);
        if (!isServiceRunning && !isSessionExists && !is_gps_tracking) {
            return super.onStartCommand(intent, flags, startId);
        }
        GPSTracker gps = new GPSTracker(mContext);
        final MyLocation objMyLocation = gps.getLocation();
        final ClientFieldLocation objClientFieldLocation = new ClientFieldLocation(objMyLocation);

        objClientFieldLocation.setUserId(Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, ""));
        objClientFieldLocation.setAppType("F");
        objClientFieldLocation.setJobId(0);
        if (objMyLocation != null) {
            objClientFieldLocation.setReverseGeoCodeName(objMyLocation.getAddressFromLocation(mContext));
        } else {
            objClientFieldLocation.setReverseGeoCodeName("Unable to get last location.");
        }
        //objClientFieldLocation.display();
        new Thread() {
            public void run() {
                if (objMyLocation != null) {
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    URLMapping um = ConstantVal.getSaveUserLocationURL(mContext);
                    final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    HttpEngine objHttpEngine = new HttpEngine();
                    Date dt = new Date();
                    String date = Helper.convertDateToString(dt, ConstantVal.DATE_FORMAT);
                    String time = Helper.convertDateToString(dt, ConstantVal.TIME_FORMAT);
                    objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                            new String[]{String.valueOf(objClientFieldLocation.getObjMyLocation().getLatitude()),
                                    String.valueOf(objClientFieldLocation.getObjMyLocation().getLongitude()),
                                    objClientFieldLocation.getObjMyLocation().getGpsType(),
                                    objClientFieldLocation.getReverseGeoCodeName(),
                                    String.valueOf(objClientFieldLocation.getUserId()),
                                    objClientFieldLocation.getAppType(),
                                    String.valueOf(objClientFieldLocation.getJobId()), String.valueOf(tokenId), date, time, account_id}
                            , um.getParamNames(), um.isNeedToSync());
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }
}

