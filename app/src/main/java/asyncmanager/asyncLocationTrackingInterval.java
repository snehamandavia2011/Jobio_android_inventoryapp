package asyncmanager;

import android.content.Context;
import android.text.Html;

import org.json.JSONException;

import entity.BusinessAccountdbDetail;
import entity.ClientLocationTrackingInterval;
import parser.parsLocationTrackingInterval;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;

import service.serLocationTracker;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 1/1/2016.
 */
public class asyncLocationTrackingInterval extends Thread {
    Context ctx;
    public static String responseCode;

    public asyncLocationTrackingInterval(Context c) {
        this.ctx = c;
        start();
    }

    public void run() {
        getData();
    }

    public void getData() {
        ClientLocationTrackingInterval objLocationTrackingInterval = getDataFromServer();
        if (objLocationTrackingInterval != null) {
            int intOldIntervalTime = Helper.getIntPreference(ctx, ClientLocationTrackingInterval.Fields.LOCATION_TRACKING_INTERVAL, 0);
            if (intOldIntervalTime != objLocationTrackingInterval.getPeriod()) {
                objLocationTrackingInterval.saveFiledsToPreferences(ctx);
                serLocationTracker.isServiceRunning = false;
            }
            Helper.scheduleLocationService(ctx, objLocationTrackingInterval.getPeriod());
        }
    }


    private ClientLocationTrackingInterval getDataFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        URLMapping um = ConstantVal.getLocationTrackingIntervalURL(ctx);
        ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
        responseCode = objServerResponse.getResponseCode();
        String intervalTime = Html.fromHtml(objServerResponse.getResponseString()).toString();
        try {
            ClientLocationTrackingInterval objLocationTrackingInterval = parsLocationTrackingInterval.parseJSON(intervalTime);
            return objLocationTrackingInterval;
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static boolean isDataLoadSuccessfully() {
        try {
            if (responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return false;
        }
    }
}
