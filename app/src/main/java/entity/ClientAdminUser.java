package entity;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 12/14/2015.
 */
public class ClientAdminUser {
    private String userName, password, id;//,password,date,time,timeStamp,status;

    public ClientAdminUser() {
    }

    public class Fields {
        public static final String USER_NAME = "userName";
        public static final String PASSWORD = "password";
        public static final String ADMINUSERID = "adminUserid";
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.USER_NAME);
        Helper.clearPreference(c, Fields.PASSWORD);
        Helper.clearPreference(c, Fields.ADMINUSERID);
    }

    public void saveFiledsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.USER_NAME, this.getUserName());
        Helper.setStringPreference(c, Fields.PASSWORD, this.getPassword());
        Helper.setStringPreference(c, Fields.ADMINUSERID, this.getId());
    }

    public void parseJSON(JSONObject objJSON) throws JSONException {
        this.setUserName(objJSON.getString("user_name"));
        //this.setPassword(objJSON.getString("password")); No need to set password value as it in encoded format
        this.setId(objJSON.getString("uuid").equals("null") ? "" : objJSON.getString("uuid"));
        //this.display();
    }

    public void display() {
        Logger.debug(".....................ClientAdminUser...........................");
        Logger.debug("userName:" + userName);
        Logger.debug("Password:" + password);
        Logger.debug("id:" + this.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
