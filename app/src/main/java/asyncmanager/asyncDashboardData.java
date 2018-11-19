package asyncmanager;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import entity.BusinessAccountdbDetail;
import entity.ClientEmployeeMaster;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 7/13/2016.
 */
public class asyncDashboardData extends Thread {
    Context ctx;

    public asyncDashboardData(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        loadDataDuringLogin();
    }

    public void loadDataDuringLogin() {
        getWelcomeText();
    }

    public void getWelcomeText() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpEngine objHttpEngine = new HttpEngine();
                final int tokenId = Helper.getIntPreference(ctx, ConstantVal.TOKEN_ID, 0);
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getWelcomeText(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id,ConstantVal.APP_REF_TYPE}, um.getParamNames(), um.isNeedToSync());
                String responseCode = objServerResponse.getResponseCode();
                String result = objServerResponse.getResponseString();
                if (result != null && !result.equals("")) {
                    try {
                        Helper.setStringPreference(ctx, ConstantVal.WELCOME_MESSAGE, new JSONObject(result).getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
