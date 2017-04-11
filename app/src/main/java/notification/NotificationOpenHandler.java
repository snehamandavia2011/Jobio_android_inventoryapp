package notification;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;

import com.lab360io.jobio.officeApp.acAsset;
import com.lab360io.jobio.officeApp.acHome;
import com.lab360io.jobio.officeApp.acLogin;
import com.lab360io.jobio.officeApp.acMessageEmployeeList;
import com.lab360io.jobio.officeApp.acMessageList;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 11/23/2016.
 */
public class NotificationOpenHandler implements OneSignal.NotificationOpenedHandler {
    Context mContext;

    public NotificationOpenHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void notificationOpened(final OSNotificationOpenResult result) {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    OSNotificationAction.ActionType actionType = result.action.type;
                    JSONObject data = result.notification.payload.additionalData;
                    boolean isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
                    if (!isSessionExists) {
                        Intent i = new Intent(mContext, acLogin.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        ((MultiDexApplication) mContext).startActivity(i);
                    } else if (data != null && isSessionExists) {
                        String action;
                        Logger.debug("in notification open handler " + data.toString());
                        action = data.optString("action", null);
                        if (action != null) {
                            if (action.equals(ConstantVal.NotificationType.ADD_INSPECTION_TRANSACTION)) {//do not require to perform any action

                                Intent i1 = new Intent(mContext, acHome.class);
                                i1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                                ((MultiDexApplication) mContext).startActivity(i1);

                                Intent i2 = new Intent(mContext, acAsset.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i2.putExtra("tab", acAsset.INSPECT);
                                try {
                                    ((AppCompatActivity) mContext).startActivityForResult(i2, ConstantVal.EXIT_REQUEST_CODE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.writeToCrashlytics(e);
                                    ((MultiDexApplication) mContext).startActivity(i2);
                                }

                            } else if (action.equals(ConstantVal.NotificationType.ADD_SERVICE_TRANSACTION)) {//do not require to perform any action

                                Intent i1 = new Intent(mContext, acHome.class);
                                i1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                                ((MultiDexApplication) mContext).startActivity(i1);

                                Intent i2 = new Intent(mContext, acAsset.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i2.putExtra("tab", acAsset.SERVICE);
                                try {
                                    ((AppCompatActivity) mContext).startActivityForResult(i2, ConstantVal.EXIT_REQUEST_CODE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.writeToCrashlytics(e);
                                    ((MultiDexApplication) mContext).startActivity(i2);
                                }

                            } else if (action.equals(ConstantVal.NotificationType.MESSAGE_RECEIVED)) {
                                String to_id = data.optString("to_id", null);
                                String from_id = data.optString("from_id", null);
                                DataBase db = new DataBase(mContext);
                                db.open();
                                Cursor cur = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "auId='" + from_id + "'");
                                String name = "";
                                if (cur != null && cur.getCount() > 0) {
                                    cur.moveToFirst();
                                    name = cur.getString(3) + " " + cur.getString(4);
                                }
                                db.close();

                                Intent i1 = new Intent(mContext, acHome.class);
                                i1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                                ((MultiDexApplication) mContext).startActivity(i1);

                                Intent i2 = new Intent(mContext, acMessageEmployeeList.class);
                                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    ((AppCompatActivity) mContext).startActivityForResult(i2, ConstantVal.EXIT_REQUEST_CODE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.writeToCrashlytics(e);
                                    ((MultiDexApplication) mContext).startActivity(i2);
                                }

                                Intent i3 = new Intent(mContext, acMessageList.class);
                                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i3.putExtra("friendAdminUserId", from_id);
                                i3.putExtra("name", name);
                                try {
                                    ((AppCompatActivity) mContext).startActivityForResult(i3, ConstantVal.EXIT_REQUEST_CODE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.writeToCrashlytics(e);
                                    ((MultiDexApplication) mContext).startActivity(i3);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
        /*if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Logger.debug("Button pressed with id: " + result.action.actionID);*/

            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

}
