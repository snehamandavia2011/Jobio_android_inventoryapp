package notification;

import android.content.Context;

import com.onesignal.OneSignal;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 11/19/2016.
 */
public class Notification {
    public static String responseCode;

    public Thread savePlayerId(final Context mContext) {
        Thread t = new Thread() {
            public void run() {
                OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                    @Override
                    public void idsAvailable(String userId, String registrationId) {
                        //call web API to save userID to mysql server
                        HttpEngine objHttpEngine = new HttpEngine();
                        final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                        String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                        String adminUser_id = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                        URLMapping um = ConstantVal.savePlayerId(mContext);
                        String[] data = {adminUser_id, userId, "Mobile", tokenId, account_id};
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
        if (responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
            return true;
        } else {
            return false;
        }
    }
}
