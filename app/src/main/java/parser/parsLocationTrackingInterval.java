package parser;

import org.json.JSONException;
import org.json.JSONObject;

import entity.ClientLocationTrackingInterval;


public class parsLocationTrackingInterval {
    public static ClientLocationTrackingInterval parseJSON(String JSONString) throws JSONException {
        JSONObject objJSON = new JSONObject(JSONString);
        int val = objJSON.getString("period").equals("null") ? 0 : objJSON.getInt("period");
        ClientLocationTrackingInterval obj = new ClientLocationTrackingInterval(val);
        return obj;
    }
}
