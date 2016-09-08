package parser;

import org.json.JSONObject;


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
        }
        return photo;
    }
}
