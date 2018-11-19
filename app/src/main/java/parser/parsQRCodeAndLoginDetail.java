package parser;

import org.json.JSONException;
import org.json.JSONObject;

import entity.ClientLoginUser;

public class parsQRCodeAndLoginDetail {
    public static ClientLoginUser parseLogin(String JSONString) throws JSONException {
        // Logger.debug(JSONString);
        ClientLoginUser objLoginUSer = new ClientLoginUser();
        JSONObject objJSON = new JSONObject(JSONString);
        objLoginUSer.setToken((objJSON.has("token")) ? objJSON.getString("token") : "");
        objLoginUSer.setTokenId(objJSON.has("token_id") ? objJSON.getInt("token_id") : 0);
        objLoginUSer.setDeviceName((objJSON.has("device_name")) ? objJSON.getString("device_name") : "");
        objLoginUSer.setOsName((objJSON.has("os_name")) ? objJSON.getString("os_name") : "");
        return objLoginUSer;
    }

    public static String parseQRCodeResult(String JSONString) throws JSONException {
        JSONObject objJSON = new JSONObject(JSONString);
        return objJSON.getString("account_logo");
    }
}
