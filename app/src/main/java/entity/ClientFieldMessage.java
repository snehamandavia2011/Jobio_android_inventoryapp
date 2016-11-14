package entity;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 6/18/2016.
 */
public class ClientFieldMessage {
    private long _ID;
    private String id, message, from_id, to_id, is_viewed;
    Date date;
    Date timeStamp;

    public ClientFieldMessage() {
    }

    public ClientFieldMessage(int _ID, String id, String message, String from_id, String to_id, String is_viewed, Date date, Date timeStamp) {
        this._ID = _ID;
        this.id = id;
        this.message = message;
        this.from_id = from_id;
        this.to_id = to_id;
        this.is_viewed = is_viewed;
        this.date = date;
        this.timeStamp = timeStamp;
    }

    public ClientFieldMessage(String id, String message, String from_id, String to_id, String is_viewed, Date date, Date timeStamp) {
        this.id = id;
        this.message = message;
        this.from_id = from_id;
        this.to_id = to_id;
        this.is_viewed = is_viewed;
        this.date = date;
        this.timeStamp = timeStamp;
    }

    public static ArrayList<ClientFieldMessage> getList(String JSONString) throws JSONException {
        ArrayList<ClientFieldMessage> arr = new ArrayList<ClientFieldMessage>();
        JSONArray jArray = new JSONArray(JSONString);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            //id, message, from_id, to_id, is_viewed, date, time
            String id = (obj.getString("id").equals("null") ? "0" : obj.getString("id"));
            String message = (obj.getString("message").equals("null") ? "" : obj.getString("message"));
            String from_id = (obj.getString("from_id").equals("null") ? "" : obj.getString("from_id"));
            String to_id = (obj.getString("to_id").equals("null") ? "" : obj.getString("to_id"));
            String is_viewed = (obj.getString("is_viewed").equals("null") ? "" : obj.getString("is_viewed"));
            String date = (obj.getString("date").equals("null") ? "" : obj.getString("date"));
            String time = (obj.getString("time").equals("null") ? "" : obj.getString("time"));
            Date dtDateTime = Helper.convertStringToDate(date + " " + time, ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
            Date dtTimeStamp = Helper.convertStringToDate(obj.getString("timestamp"), ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
            ClientFieldMessage obj1 = new ClientFieldMessage(id, message, from_id, to_id, is_viewed, dtDateTime, dtTimeStamp);
            arr.add(obj1);
        }
        return arr;
    }

    public ArrayList<ClientFieldMessage> getDataFromDatabase(Context ctx, String friendId, String selfId) {
        ArrayList<ClientFieldMessage> arr = null;
        DataBase db = new DataBase(ctx);
        db.open();
        String where = "from_id='" + friendId + "' and to_id='" + selfId + "' OR from_id='" + selfId + "' and to_id='" + friendId + "'";
        String orderby = "datetime ='',datetime ASC";
        Cursor cur = db.fetch(DataBase.field_message_table, where, orderby);
        //Logger.debug("count:" + cur.getCount());
        if (cur != null && cur.getCount() > 0) {
            arr = new ArrayList<>();
            cur.moveToFirst();
            do {
                arr.add(new ClientFieldMessage(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), new Date(cur.getLong(6)), new Date(cur.getLong(7))));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arr;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
