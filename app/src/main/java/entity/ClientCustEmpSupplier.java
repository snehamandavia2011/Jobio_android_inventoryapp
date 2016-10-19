package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SAI on 10/19/2016.
 */
public class ClientCustEmpSupplier {
    String uuid, name, type;

    public ClientCustEmpSupplier(String uuid, String name, String type) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<ClientCustEmpSupplier> parseData(String JSONString) {
        try {
            ArrayList<ClientCustEmpSupplier> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(JSONString);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                arr.add(new ClientCustEmpSupplier(objJSON.getString("uuid"), objJSON.getString("first_name") + " " + objJSON.getString("last_name"), objJSON.getString("type")));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return name + "-" + type;
    }
}
