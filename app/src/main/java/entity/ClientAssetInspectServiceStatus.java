package entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SAI on 9/19/2016.
 */
public class ClientAssetInspectServiceStatus {
    private int id;
    private String name;

    public ClientAssetInspectServiceStatus(){}
    public ClientAssetInspectServiceStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void parseData(JSONObject objJSON) throws JSONException {
        this.setId(objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id"));
        this.setName(objJSON.getString("status_name").equals("null") ? "" : objJSON.getString("status_name"));
    }

    @Override
    public String toString() {
        return name;
    }
}
