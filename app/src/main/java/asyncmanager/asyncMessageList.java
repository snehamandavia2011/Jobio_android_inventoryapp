package asyncmanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acMessageList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientFieldMessage;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 6/17/2016.
 */
public class asyncMessageList {
    Context ctx;
    Date dtCurrentTime;
    long lngLastMessageSyncTime;

    public asyncMessageList() {
    }

    public asyncMessageList(Context ctx) {
        this.ctx = ctx;
        this.dtCurrentTime = new Date();
        updateStatus();//This function shall update the is_viewed status of sender (login user) local database to "Y", if receiver view the message
    }

    private void updateStatus() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(ctx);
                db.open();
                Cursor cur = db.fetch(DataBase.field_message_table, DataBase.field_message_int, "is_viewed='N'");
                if (cur != null && cur.getCount() > 0) {
                    String ids = "";
                    cur.moveToFirst();
                    do {
                        ids += cur.getInt(1) + ",";
                    } while (cur.moveToNext());
                    if (ids.length() > 0) {
                        ids = ids.substring(0, ids.length() - 1);
                        String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                        HttpEngine objHttpEngine = new HttpEngine();
                        URLMapping um = ConstantVal.getMessageStatus(ctx);
                        String[] data = {String.valueOf(tokenId), ids, account_id};
                        ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                        parseAndUpdateData(objServerResponse.getResponseString());
                    }
                    cur.close();
                    db.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                getData();//This function fetch not viewed message by login user from server and save to local database.
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void parseAndUpdateData(String JSONString) {
        //This function is use to  check whether peer has view the message or not.
        //So it take the iview value from server, if isView="Y", then update the login user local database with is_view=Y where is view="N"
        //And also send broadcast on acMessage list as message status is changed for particular message
        try {
            JSONArray jArray = new JSONArray(JSONString);
            for (int i = 0; i < jArray.length(); i++) {
                //Take peer message view status from server
                JSONObject obj = jArray.getJSONObject(i);
                int id = obj.getInt("id");
                String is_viewed = obj.getString("is_viewed");
                if (is_viewed.equals("Y")) {
                    //update local database, if is_view ="y"
                    DataBase db = new DataBase(ctx);
                    db.open();
                    ContentValues cv = new ContentValues();
                    cv.put("is_viewed", is_viewed);
                    db.update(DataBase.field_message_table, DataBase.field_message_int, "id=" + id, cv);

                    //send broadcast to acMessageList that particular message is viewed.
                    Cursor cur = db.fetch(DataBase.field_message_table, DataBase.field_message_int, "id=" + id);
                    if (cur != null && cur.getCount() > 0) {
                        cur.moveToFirst();
                        int localPK = cur.getInt(0);
                        //Need to send broadcast, as messages are viewed on acMessageList (show double tick)
                        Intent intent = new Intent();
                        intent.putExtra(acMessageList.LOCAL_PK, localPK);
                        intent.putExtra(acMessageList.STATUS, ConstantVal.MessageChatStatus.VIEW);
                        intent.setAction(ConstantVal.BroadcastAction.CHANGED_MESSAGE_STATUS);//Broadcast to asMessageList
                        ctx.sendBroadcast(intent);

                    }
                    cur.close();
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        new AsyncTask() {
            HashMap<String, MessageNotification> map = new HashMap<String, MessageNotification>();

            @Override
            protected Object doInBackground(Object[] params) {
                String responseCode = "";
                final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String userId = Helper.getStringPreference(ctx, ClientAdminUser.Fields.ADMINUSERID, "");
                String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                String isDataLoadFirstTime = "";
                HttpEngine objHttpEngine = new HttpEngine();
                URLMapping um = ConstantVal.getEmployeeMessage(ctx);
                String[] data = null;
                lngLastMessageSyncTime = Helper.getLongPreference(ctx, ConstantVal.LAST_MESSAGE_SYNC_TIME, 0);
                if (lngLastMessageSyncTime <= 0) {
//means first time loading the message, so load read/unread since last 30days, and save to local database.
                    isDataLoadFirstTime = "Y";
                } else {
                    //means next time loading the message, so load unread since last 30days, and save to local database.
                    isDataLoadFirstTime = "N";
                }
                data = new String[]{String.valueOf(tokenId), userId, isDataLoadFirstTime, account_id};
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                responseCode = objServerResponse.getResponseCode();
                String result = objServerResponse.getResponseString();
                if (result != null && !result.equals("")) {
                    try {
                        //parse data
                        ArrayList<ClientFieldMessage> arrClientFieldMessage = ClientFieldMessage.getList(result);
                        if (arrClientFieldMessage.size() > 0) {
                            //save data
                            DataBase db = new DataBase(ctx);
                            db.open();

                            for (ClientFieldMessage objClientFieldMessage : arrClientFieldMessage) {
                                //if id is exists then no need to do any thing otherwise insert the record.
                                //String where = "from_id=" + objClientFieldMessage.getFrom_id() + " and to_id=" + objClientFieldMessage.getTo_id() + " and datetime=" + objClientFieldMessage.getDate().getTime();
                                String where = "id=" + objClientFieldMessage.getId();
                                Cursor cur = db.fetch(DataBase.field_message_table, DataBase.field_message_int, where);
                                if (cur != null && cur.getCount() > 0) {
                                } else {//insert
                                    db.insert(DataBase.field_message_table, DataBase.field_message_int, new String[]{objClientFieldMessage.getId(),
                                            objClientFieldMessage.getMessage(), objClientFieldMessage.getFrom_id(), objClientFieldMessage.getTo_id(),
                                            objClientFieldMessage.getIs_viewed(), String.valueOf(objClientFieldMessage.getDate().getTime()),
                                            String.valueOf(objClientFieldMessage.getTimeStamp().getTime())});
                                    if (objClientFieldMessage.getTo_id().equals(String.valueOf(userId)) && objClientFieldMessage.getIs_viewed().equals("N")) {
                                        //generate hashtable for Notification
                                        if (map.containsKey(objClientFieldMessage.getFrom_id())) {
                                            MessageNotification objMessageNotification = map.get(objClientFieldMessage.getFrom_id());
                                            objMessageNotification.setCount(objMessageNotification.getCount() + 1);
                                            map.put(objClientFieldMessage.getFrom_id(), objMessageNotification);
                                        } else {
                                            Cursor curEmployee = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "auId='" + objClientFieldMessage.getFrom_id() + "'");
                                            if (curEmployee != null) {
                                                cur.moveToFirst();
                                                String name = curEmployee.getString(3) + " " + curEmployee.getString(4);
                                                map.put(objClientFieldMessage.getFrom_id(), new MessageNotification(name, 1));
                                            }
                                            curEmployee.close();
                                        }
                                    }
                                }
                                cur.close();
                            }
                            db.close();
                        }
                    } catch (Exception e) {
                        //Logger.debug("Error:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) || responseCode.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) {
                    if (!responseCode.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                        Helper.setLongPreference(ctx, ConstantVal.LAST_MESSAGE_SYNC_TIME, new Date().getTime());
                        Intent intent = new Intent();
                        intent.setAction(ConstantVal.BroadcastAction.CHANGED_MESSAGE_LIST);//Broadcast to asMessageList
                        ctx.sendBroadcast(intent);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (Helper.getBooleanPreference(ctx, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_NOTIFICATION, true)) {
                    showNotification(map);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateLocalDabase(Context c, String JSONString) {
        try {
            JSONObject obj = new JSONObject(JSONString);
            String local_pk = obj.getString("local_pk");
            String server_pk = obj.getString("server_pk");
            String timestamp = obj.getString("timestamp");
            DataBase db = new DataBase(c);
            db.open();
            ContentValues cv = new ContentValues();
            cv.put("id", server_pk);
            cv.put("timestamp", Helper.convertStringToDate(timestamp, ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT).getTime());
            db.update(DataBase.field_message_table, DataBase.field_message_int, "_ID=" + local_pk, cv);
            db.close();
            //Need to send broadcast, as messages are saved on server, and as acknowledge server returns PK and timestamp acMessageList (show single tick)
            Intent intent = new Intent();
            intent.putExtra(acMessageList.LOCAL_PK, Integer.parseInt(local_pk));
            intent.putExtra(acMessageList.STATUS, ConstantVal.MessageChatStatus.SENT);
            intent.setAction(ConstantVal.BroadcastAction.CHANGED_MESSAGE_STATUS);//Broadcast to asMessageList
            c.sendBroadcast(intent);

            //play sound for out going message
            if (Helper.getBooleanPreference(ctx, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, true)) {
                Helper.playSound(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MessageNotification {
        private String name;
        private int count;

        public MessageNotification(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    private void showNotification(HashMap<String, MessageNotification> map) {
        for (Map.Entry<String, MessageNotification> entry : map.entrySet()) {
            String key = entry.getKey();
            MessageNotification objMessageNotification = entry.getValue();
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
// Sets an ID for the Notification, so it can be updated
            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(ctx)
                    .setContentTitle(ctx.getString(R.string.strJobioMessage))
                    .setContentText(objMessageNotification.getName())
                    .setSmallIcon(R.drawable.ic_chat_white)
                    .setNumber(objMessageNotification.getCount())
                    .setAutoCancel(true);
            if (Helper.getBooleanPreference(ctx, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, true)) {
                mNotifyBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            }
            Intent resultIntent = new Intent(ctx, acMessageList.class);
            resultIntent.putExtra("friendAdminUserId", key);
            resultIntent.putExtra("name", objMessageNotification.getName());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(acMessageList.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            mNotificationManager.notify(
                    key, 0,
                    mNotifyBuilder.build());
        }
    }

}
