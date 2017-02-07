package entity;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 12/14/2015.
 */
public class ClientEmployeeMaster {
    private String firstName, lastName,employeeId;

    public ClientEmployeeMaster() {
    }

    public class Fields {
        public static final String EMPLOYEE_ID = "employeeId";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.EMPLOYEE_ID);
        Helper.clearPreference(c, Fields.FIRST_NAME);
        Helper.clearPreference(c, Fields.LAST_NAME);
    }

    public void saveFiledsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.FIRST_NAME, this.getFirstName());
        Helper.setStringPreference(c, Fields.LAST_NAME, this.getLastName());
        Helper.setStringPreference(c, Fields.EMPLOYEE_ID, this.getEmployeeId());
        //following broadcast is send to acHome.java
        Intent intent = new Intent();
        intent.setAction("jobio.io.EMPLOYEE_DETAIL");
        intent.putExtra("name", getFirstName());
        c.sendBroadcast(intent);
    }

    public void parseJSON(JSONObject objJSON) throws JSONException {
        this.setEmployeeId(objJSON.getString("employeeuuId").equals("null") ? "" : objJSON.getString("employeeuuId"));
        this.setFirstName(objJSON.getString("first_name").equals("null") ? "" : objJSON.getString("first_name"));
        this.setLastName(objJSON.getString("last_name").equals("null") ? "" : objJSON.getString("last_name"));
//        this.display();
    }

    public void display() {
        Logger.debug(".....................ClientEmployeeMaster...........................");
        Logger.debug("employee id:" + employeeId);
        Logger.debug("first_name:" + firstName);
        Logger.debug("last name:" + lastName);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
