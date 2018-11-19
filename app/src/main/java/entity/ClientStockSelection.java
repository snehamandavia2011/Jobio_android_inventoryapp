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
public class ClientStockSelection {
    private String optionName;
    private int id;

    public ClientStockSelection() {
    }

    public class Fields {
        public static final String ID = "ssID";
        public static final String OPTION_NAME = "ssOptionName";
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.ID);
        Helper.clearPreference(c, Fields.OPTION_NAME);
    }

    public void saveFiledsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.OPTION_NAME, this.getOptionName());
        Helper.setIntPreference(c, Fields.ID, this.getId());
    }

    public void parseJSON(JSONObject objJSON) throws JSONException {
        this.setId(objJSON.getString("SSid").equals("null") ? 0 : objJSON.getInt("SSid"));
        this.setOptionName(objJSON.getString("SSoption_name").equals("null") ? "" : objJSON.getString("SSoption_name"));
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
