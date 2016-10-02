package asyncmanager;

import android.content.Context;

import org.json.JSONException;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.UserData;
import parser.parsUserData;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 1/1/2016.
 */
public class asyncUserData extends Thread {
    Context ctx;
    public static String responseCode;

    public asyncUserData(Context ctx) {
        this.ctx = ctx;
        start();
    }


    public void run() {
        getData();
    }

    public void getData() {
        UserData objUserData = new UserData();
        HttpEngine objHttpEngine = new HttpEngine();
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String userName = Helper.getStringPreference(ctx, ClientAdminUser.Fields.USER_NAME, "");
        String password = Helper.getStringPreference(ctx, ClientAdminUser.Fields.PASSWORD, "");
        String qrcode = Helper.getStringPreference(ctx, ConstantVal.QRCODE_VALUE, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        URLMapping um = ConstantVal.getUserDataUrl(ctx);
        ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{userName, password, qrcode, String.valueOf(tokenId), account_id,ConstantVal.APP_REF_TYPE}, um.getParamNames(), um.isNeedToSync());
        responseCode = objServerResponse.getResponseCode();
        String result = objServerResponse.getResponseString();
        if (result != null && !result.equals("")) {
            try {
                objUserData = parsUserData.parseJSON(result);
                objUserData.getObjClientAdminUser().setPassword(password);
                objUserData.getObjBusinessAccountdbDetail().saveFiledsToPreferences(ctx);
                objUserData.getObjBusinessAccountMaster().saveFiledsToPreferences(ctx);
                objUserData.getObjClientAdminUser().saveFiledsToPreferences(ctx);
                objUserData.getObjClientAdminUserAppsRel().saveFiledsToPreferences(ctx);
                objUserData.getObjClientEmployeeMaster().saveFiledsToPreferences(ctx);
                objUserData.getObjClientStockSelection().saveFiledsToPreferences(ctx);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
