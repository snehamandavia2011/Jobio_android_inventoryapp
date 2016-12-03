package notification;

import android.content.Context;
import android.os.AsyncTask;

import com.onesignal.OneSignal;

import org.json.JSONObject;

import asyncmanager.asyncMessageList;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 11/19/2016.
 */
public class Notification {
    public static String responseCode = "";

    public Thread savePlayerId(final Context mContext) {
        Thread t = new Thread() {
            public void run() {
                OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                    @Override
                    public void idsAvailable(String userId, String registrationId) {
                        //Logger.debug("idsAvailable: " + userId);
                        //Logger.debug(registrationId != null ? "Reg:" + registrationId : "Reg is null");
                        //call web API to save userID to mysql server
                        HttpEngine objHttpEngine = new HttpEngine();
                        final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                        String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                        String adminUser_id = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                        URLMapping um = ConstantVal.savePlayerId(mContext);
                        String[] data = {adminUser_id, userId, "Mobile", tokenId, account_id, ConstantVal.APP_REF_TYPE};
                        ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                        responseCode = objServerResponse.getResponseCode();
                    }
                });
            }
        };
        t.start();
        return t;
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
            return false;
        }
    }

    public static void initNotification(Context mContext) {
        OneSignal.startInit(mContext).init();
        OneSignal.startInit(mContext).setNotificationReceivedHandler(new NotificationReceiveHandler()).init();
        OneSignal.startInit(mContext).setNotificationOpenedHandler(new NotificationOpenHandler(mContext)).init();
    }

    public static void performActionWhileNotificationReceive(final JSONObject data, final Context mContext) {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String action;
                if (data != null) {
                    try {
                        action = data.optString("action", null);
                        if (action != null) {
                            if (action.equals(ConstantVal.NotificationType.ADD_SERVICE_TRANSACTION)) {//data:account_id,employee_id

                            } else if (action.equals(ConstantVal.NotificationType.ADD_INSPECTION_TRANSACTION)) {//data:account_id

                            } else if (action.equals(ConstantVal.NotificationType.MESSAGE_RECEIVED)) {//data:account_id
                                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                                String admin_user_id = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                                String acId = data.optString("account_id", null);
                                String to_id = data.optString("to_id", null);
                                Logger.debug(acId + " " + account_id + " " + to_id + " " + admin_user_id);
                                if (account_id.equals(acId) && admin_user_id.equals(to_id))
                                    new asyncMessageList(mContext);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }
}
