package asyncmanager;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientEmployeeMaster;
import entity.ClientStockTransactionReason;
import entity.ClientStockTransactionStatusMaster;
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
    public static String welcomeResponseCode, STSResponseCode, STRResponseCode;

    public asyncDashboardData(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        loadDataDuringLogin();
    }

    public void loadDataDuringLogin() {
        getWelcomeText();
        getStockTranactionStatus();
        getStockTranactionReason();
    }

    public void getStockTranactionStatus() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getStockTransactionStatus(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id, ConstantVal.APP_REF_TYPE}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                STSResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientStockTransactionStatusMaster> arr = ClientStockTransactionStatusMaster.parseList(result);
                        ClientStockTransactionStatusMaster.saveDataToDatabase(ctx, arr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getStockTranactionReason() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getStockTransactionReason(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id, ConstantVal.APP_REF_TYPE}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                STRResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientStockTransactionReason> arr = ClientStockTransactionReason.parseList(result);
                        ClientStockTransactionReason.saveDataToDatabase(ctx, arr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getWelcomeText() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getWelcomeText(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id, ConstantVal.APP_REF_TYPE}, um.getParamNames(), um.isNeedToSync());
                welcomeResponseCode = objServerResponse.getResponseCode();
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


    public static boolean isDataLoadSuccessfully() {
        if ((welcomeResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) || welcomeResponseCode.equals(ConstantVal.ServerResponseCode.BLANK_RESPONSE)) &&
                STSResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                STRResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
            return true;
        } else {
            return false;
        }
    }
}
