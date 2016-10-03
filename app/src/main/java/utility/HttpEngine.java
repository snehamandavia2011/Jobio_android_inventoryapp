package utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.core.CrashlyticsCore;
import com.lab360io.jobio.inventoryapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import asyncmanager.asyncMessageList;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;

/**
 * Created by SAI on 12/7/2015.
 */
public class HttpEngine {
    public ServerResponse getDataFromWebAPI(Context mContext, String strURL, String[] paramValues, String[] paramNames, boolean isRequireToSync) {
        //Logger.debug("URL:" + strURL);
        ServerResponse objServerResponse = null;
        String data = "";
        try {
            for (int i = 0; i < paramNames.length; i++) {
                data += "&" + URLEncoder.encode(paramNames[i], "UTF-8")
                        + "=" + URLEncoder.encode(paramValues[i], "UTF-8");
            }
            if (!data.equals(""))
                data = data.substring(1, data.length());
            //Logger.debug("data:" + data);

            long intSync = 0;
            if (isRequireToSync) {
                intSync = saveDataToSyncTable(mContext, strURL, data, "0", "");//save data to sync table
            }
            objServerResponse = makeHttpRequestCall(mContext, strURL, data);
            if (isRequireToSync) {
                updateSyncTable(mContext, intSync, strURL, objServerResponse);
                //update sync table with appropriate result code
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objServerResponse;
    }

    public ServerResponse makeHttpRequestCall(Context mContext, String strURL, String data) {
        Logger.debug("URL:" + strURL);
        Logger.debug("Data:" + data);
        ServerResponse objServerResponse = null;
        try {
            if (!isNetworkAvailable(mContext)) {
                objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.NO_INTERNET, ConstantVal.ServerResponseCode.NO_INTERNET);
            } else {
                URL url = new URL(strURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(10000);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();
                urlConnection.connect();
                Logger.debug("Response code from server:" + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());
                int resoponseCode = urlConnection.getResponseCode();
                //Logger.debug("Response message from server:" + urlConnection.getResponseMessage());
                if (resoponseCode >= 500 && resoponseCode <= 520) {
                    objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SERVER_ERROR, ConstantVal.ServerResponseCode.SERVER_ERROR);
                } else if (strURL.contains("verifyQRCode") && (resoponseCode >= 400 && resoponseCode <= 451)) {
                    objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.INVALID_QR_CODE, ConstantVal.ServerResponseCode.INVALID_QR_CODE);
                } else if (resoponseCode >= 400 && resoponseCode <= 451) {
                    objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.CLIENT_ERROR, ConstantVal.ServerResponseCode.CLIENT_ERROR);
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer res = new StringBuffer();
                    char[] chBuff = new char[1000];
                    int len = 0;
                    while ((len = in.read(chBuff)) > 0)
                        res.append(new String(chBuff, 0, len));
                    in.close();
                    String strResponse = res.toString();
                    Logger.debug("Response from server:" + strResponse);
                    if (strResponse == null || strResponse.equals("")) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.BLANK_RESPONSE, ConstantVal.ServerResponseCode.BLANK_RESPONSE);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.INVALID_QR_CODE)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.INVALID_QR_CODE, ConstantVal.ServerResponseCode.INVALID_QR_CODE);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.INVALID_LOGIN, ConstantVal.ServerResponseCode.INVALID_LOGIN);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.NOT_OFFICE_STAFF)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.NOT_OFFICE_STAFF, ConstantVal.ServerResponseCode.NOT_OFFICE_STAFF);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SESSION_EXPIRED, ConstantVal.ServerResponseCode.SESSION_EXPIRED);
                        Helper.logOutUser(mContext, true);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.SESSION_EXISTS)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SESSION_EXISTS, ConstantVal.ServerResponseCode.SESSION_EXISTS);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SUCCESS, ConstantVal.ServerResponseCode.SUCCESS);
                    } else {
                        boolean isValid = Helper.isValidJSON(strResponse);
                        if (!isValid) {
                            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.PARSE_ERROR, ConstantVal.ServerResponseCode.PARSE_ERROR);
                        } else {
                            objServerResponse = new ServerResponse(strResponse, ConstantVal.ServerResponseCode.SUCCESS);
                            //result=result;
                        }
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.INVALID_QR_CODE, ConstantVal.ServerResponseCode.INVALID_QR_CODE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.CLIENT_ERROR, ConstantVal.ServerResponseCode.CLIENT_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.REQUEST_TIMEOUT, ConstantVal.ServerResponseCode.REQUEST_TIMEOUT);
        } catch (Exception e) {
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.BLANK_RESPONSE, ConstantVal.ServerResponseCode.BLANK_RESPONSE);
            e.printStackTrace();
        }
        Logger.debug("Application response code" + objServerResponse.getResponseCode());
        String strEmail = Helper.getStringPreference(mContext, ClientAdminUser.Fields.USER_NAME, "");
        String message = "[Code:" + objServerResponse.getResponseCode() + "][Response:" + objServerResponse.getResponseString() + "]";
        CrashlyticsCore.getInstance().log(Log.ASSERT, strEmail, message);
        //Logger.debug("Application response string" + objServerResponse.getResponseString());
        return objServerResponse;
    }

    public long saveDataToSyncTable(Context ctx, String URL, String data, String isSync, String last_result_code) {
        String adminUserId = Helper.getStringPreference(ctx, ClientAdminUser.Fields.ADMINUSERID, "");
        String accountId = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        DataBase db = new DataBase(ctx);
        db.open();
        long result = db.insert(DataBase.device_to_db_sync_table, DataBase.device_to_db_sync_int, new String[]{URL, data, "0", "", adminUserId, accountId});
        db.close();
        return result;
    }

    public boolean updateSyncTable(Context ctx, long syncId, String url, ServerResponse objServerResponse) {
        String resultCode = objServerResponse.getResponseCode();
        ContentValues cv = new ContentValues();
        cv.put("isSync", 0);
        if (resultCode.equals(ConstantVal.ServerResponseCode.NO_INTERNET))
            Helper.displaySnackbar((AppCompatActivity) ctx, ctx.getString(R.string.msgSyncNoInternet));

        if (resultCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
            cv.put("isSync", 1);
        }

        cv.put("last_result_code", resultCode);
        DataBase db = new DataBase(ctx);
        db.open();
        boolean isUpdate = db.update(DataBase.device_to_db_sync_table, DataBase.device_to_db_sync_int, "_ID=" + syncId, cv);
        db.close();


        if (url.contains("managemessage/saveMessage")) {
            if (objServerResponse.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS))
                new asyncMessageList().updateLocalDabase(ctx, objServerResponse.getResponseString());
        }
        return isUpdate;
    }

    public boolean isNetworkAvailable(Context mContext) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(mContext, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            //Toast.makeText(mContext, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
