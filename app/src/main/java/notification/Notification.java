package notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.AsyncTask;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acAsset;
import com.lab360io.jobio.officeApp.acHome;
import com.lab360io.jobio.officeApp.acLogin;
import com.lab360io.jobio.officeApp.acMessageEmployeeList;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import asyncmanager.asyncAsset;
import asyncmanager.asyncMessageList;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


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
            Logger.writeToCrashlytics(e);
            return false;
        }
    }

    public static void initNotification(Context mContext) {
        OneSignal.startInit(mContext).init();
        //OneSignal.startInit(mContext).setNotificationReceivedHandler(new NotificationReceiveHandler()).init();
        //OneSignal.startInit(mContext).setNotificationOpenedHandler(new NotificationOpenHandler(mContext)).init();
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
                                new asyncAsset(mContext).getService();
                            } else if (action.equals(ConstantVal.NotificationType.ADD_INSPECTION_TRANSACTION)) {//data:account_id
                                new asyncAsset(mContext).getInspect();
                            } else if (action.equals(ConstantVal.NotificationType.ADD_EDIT_SERVICE)) {//data:account_id

                            } else if (action.equals(ConstantVal.NotificationType.ADD_EDIT_INSPECTION)) {//data:account_id

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
                        Logger.writeToCrashlytics(e);
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public static void showAndroidNotification(final JSONObject data, String title, String body, final Context mContext) {
        String action = data.optString("action", null);
        if (action != null) {
            boolean isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
            if (action.equals(ConstantVal.NotificationType.MESSAGE_RECEIVED)) {
                boolean is_primary_message_app = Helper.getBooleanPreference(mContext, ConstantVal.MessageHost.IS_OFFICE_APP_AS_PRIMARY_MESSAGE_APP, true);
                if (!is_primary_message_app)
                    return;

                String from_id = data.optString("from_id", null);
                Logger.debug("Notification->show notification" + from_id + " " + Helper.getStringPreference(mContext, ConstantVal.CURRENT_CHAT_FRIEND, ""));

                if (isSessionExists) {
                    if (!from_id.equals(Helper.getStringPreference(mContext, ConstantVal.CURRENT_CHAT_FRIEND, ""))) {
                        final boolean messageNotification = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_NOTIFICATION, true);
                        if (messageNotification) {
                            DataBase db = new DataBase(mContext);
                            db.open();
                            Cursor cur = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "auId='" + from_id + "'");
                            String name = "";
                            if (cur != null && cur.getCount() > 0) {
                                cur.moveToFirst();
                                name = cur.getString(3) + " " + cur.getString(4);
                            }
                            db.close();
                            showMessageNotification(from_id, name, title, body, mContext);
                        }
                    }
                } else {
                    showMessageNotificationAfterLogout(mContext.getString(R.string.msgNewMessageReceive), mContext);
                }
            } else if (action.equals(ConstantVal.NotificationType.ADD_INSPECTION_TRANSACTION)) {
                final boolean iTransactionNotification = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_NOTIFICATION, true);
                if (iTransactionNotification) {
                    final boolean sound = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_TONE, true);
                    if (isSessionExists)
                        showInspectNotification(title, body, mContext, sound);
                    else
                        showInspectNotificationAfterLogout(mContext.getString(R.string.msgNewInspectionReceive), mContext, sound);
                }
            } else if (action.equals(ConstantVal.NotificationType.ADD_SERVICE_TRANSACTION)) {
                final boolean sTransactionNotification = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_NOTIFICATION, true);
                if (sTransactionNotification) {
                    final boolean sound = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_TONE, true);
                    if (isSessionExists)
                        showServiceNotification(title, body, mContext, sound);
                    else
                        showServiceNotificationAfterLogout(mContext.getString(R.string.msgNewServiceReceive), mContext, sound);
                }
            } else if (action.equals(ConstantVal.NotificationType.ADD_EDIT_INSPECTION)) {
                final boolean addEditInspectionNotification = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_NOTIFICATION, true);
                if (addEditInspectionNotification) {
                    final boolean sound = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_TONE, true);
                    if (isSessionExists)
                        showInspectNotification(title, body, mContext, sound);
                    else
                        showInspectNotificationAfterLogout(mContext.getString(R.string.msgInspectionUpdateReceive), mContext, sound);
                }
            } else if (action.equals(ConstantVal.NotificationType.ADD_EDIT_SERVICE)) {
                final boolean addEditServiceNotification = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_NOTIFICATION, true);
                if (addEditServiceNotification) {
                    final boolean sound = Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_TONE, true);
                    if (isSessionExists)
                        showServiceNotification(title, body, mContext, sound);
                    else
                        showServiceNotificationAfterLogout(mContext.getString(R.string.msgServiceUpdateReceive), mContext, sound);
                }
            }
        }
    }

    private static void showMessageNotificationAfterLogout(String title, Context ctx) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_chat_white)
                .setAutoCancel(true);
        if (Helper.getBooleanPreference(ctx, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, true)) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentLogin = new Intent(ctx, acLogin.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acLogin.class);
        stackBuilder.addNextIntent(intentLogin);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }

    private static void showMessageNotification(String from_id, String name, String title, String content, Context ctx) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_chat_white)
                .setAutoCancel(true);
        if (Helper.getBooleanPreference(ctx, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, true)) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentHome = new Intent(ctx, acHome.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acHome.class);
        stackBuilder.addNextIntent(intentHome);

        Intent intentMessageEmp = new Intent(ctx, acMessageEmployeeList.class);
        stackBuilder.addNextIntent(intentMessageEmp);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }

    private static void showInspectNotification(String title, String content, Context ctx, boolean needToPlayTone) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_inspect_white)
                .setAutoCancel(true);
        if (needToPlayTone) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentHome = new Intent(ctx, acHome.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acHome.class);
        stackBuilder.addNextIntent(intentHome);

        Intent intentAsset = new Intent(ctx, acAsset.class);
        intentAsset.putExtra("tab", acAsset.INSPECT);
        stackBuilder.addNextIntent(intentAsset);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }

    private static void showInspectNotificationAfterLogout(String title, Context ctx, boolean needToPlayTone) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_inspect_white)
                .setAutoCancel(true);
        if (needToPlayTone) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentLogin = new Intent(ctx, acLogin.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acLogin.class);
        stackBuilder.addNextIntent(intentLogin);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }

    private static void showServiceNotification(String title, String content, Context ctx, boolean needToPlayTone) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_service_white)
                .setAutoCancel(true);
        if (needToPlayTone) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentHome = new Intent(ctx, acHome.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acHome.class);
        stackBuilder.addNextIntent(intentHome);

        Intent intentAsset = new Intent(ctx, acAsset.class);
        intentAsset.putExtra("tab", acAsset.SERVICE);
        stackBuilder.addNextIntent(intentAsset);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }

    private static void showServiceNotificationAfterLogout(String title, Context ctx, boolean needToPlayTone) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_service_white)
                .setAutoCancel(true);
        if (needToPlayTone) {
            mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        Intent intentLogin = new Intent(ctx, acLogin.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(acLogin.class);
        stackBuilder.addNextIntent(intentLogin);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0,
                mNotifyBuilder.build());
    }
}