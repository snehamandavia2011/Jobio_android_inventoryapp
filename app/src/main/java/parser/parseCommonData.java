package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entity.ClientAssetInspectServiceStatus;
import utility.Logger;


/**
 * Created by SAI on 4/25/2016.
 */
public class parseCommonData {
    public String parsePhoto(String JSONString) {
        String photo = "";
        try {
            JSONObject obj = new JSONObject(JSONString);
            photo = obj.getString("photo").equals("null") ? "" : obj.getString("photo");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        return photo;
    }

    public ArrayList<ClientAssetInspectServiceStatus> parseJobInspectionStatusMaster(String JSONString) throws JSONException {
        ArrayList<ClientAssetInspectServiceStatus> arr = new ArrayList<ClientAssetInspectServiceStatus>();
        JSONArray arrJson = new JSONArray(JSONString);
        for (int i = 0; i < arrJson.length(); i++) {
            JSONObject o = arrJson.getJSONObject(i);
            ClientAssetInspectServiceStatus objClientJobInspectionStatusMaster = new ClientAssetInspectServiceStatus();
            objClientJobInspectionStatusMaster.parseData(o);
            arr.add(objClientJobInspectionStatusMaster);
        }
        return arr;
    }
}
