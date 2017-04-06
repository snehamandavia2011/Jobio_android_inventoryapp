package asyncmanager;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientEmployeeMaster;
import entity.ClientStockTransactionReason;
import entity.ClientStockTransactionStatusMaster;
import entity.ClientUIControl;
import entity.ClientUIControlType;
import entity.ClientUIFormStatus;
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
    public static String welcomeResponseCode, STSResponseCode, STRResponseCode, UIFormStatusResponseCode, UIControlResponseCode, UIControlTypeResponseCode;

    public asyncDashboardData(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        loadDataDuringLogin();
    }

    public void loadDataDuringLogin() {
        try {
            getWelcomeText().join();
            getStockTranactionStatus().join();
            getStockTranactionReason().join();
            getUIFormStatus().join();
            getUIControl().join();
            getUIControlType().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Thread getStockTranactionStatus() {
        Thread t = new Thread() {
            public void run() {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getStockTransactionStatus(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                STSResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientStockTransactionStatusMaster> arr = ClientStockTransactionStatusMaster.parseList(result);
                        if (arr != null)
                            ClientStockTransactionStatusMaster.saveDataToDatabase(ctx, arr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return t;
    }

    public Thread getStockTranactionReason() {
        Thread t = new Thread() {
            public void run() {
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
            }
        };
        t.start();
        return t;
    }

    public Thread getWelcomeText() {
        Thread t = new Thread() {
            public void run() {
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
            }
        };
        t.start();
        return t;
    }


    public static boolean isDataLoadSuccessfully() {
        try {
            if ((welcomeResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) || welcomeResponseCode.equals(ConstantVal.ServerResponseCode.BLANK_RESPONSE)) &&
                    STSResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    STRResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    UIFormStatusResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    UIControlResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    UIControlTypeResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Thread getUIFormStatus() {
        Thread t = new Thread() {
            public void run() {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getUIFormStatus(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                        new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                UIFormStatusResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientUIFormStatus> arrClientUIFormStatus = ClientUIFormStatus.parseData(result);
                        if (arrClientUIFormStatus != null)
                            ClientUIFormStatus.saveDataToDatabase(ctx, arrClientUIFormStatus);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return t;
    }

    public Thread getUIControl() {
        Thread t = new Thread() {
            public void run() {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getUIControl(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                        new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                UIControlResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientUIControl> arrClientUIControl = ClientUIControl.parseData(result);
                        if (arrClientUIControl != null)
                            ClientUIControl.saveDataToDatabase(ctx, arrClientUIControl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return t;
    }

    public Thread getUIControlType() {
        Thread t = new Thread() {
            public void run() {
                HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getUIControlType(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                        new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
                String result = objServerResponse.getResponseString();
                UIControlTypeResponseCode = objServerResponse.getResponseCode();
                if (result != null && !result.equals("")) {
                    try {
                        ArrayList<ClientUIControlType> arrClientUIControlType = ClientUIControlType.parseData(result);
                        if (arrClientUIControlType != null)
                            ClientUIControlType.saveDataToDatabase(ctx, arrClientUIControlType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return t;
    }
}
