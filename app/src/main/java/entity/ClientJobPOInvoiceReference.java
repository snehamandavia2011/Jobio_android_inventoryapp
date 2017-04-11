package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 10/20/2016.
 */
public class ClientJobPOInvoiceReference {
    String uuid, name;

    public ClientJobPOInvoiceReference(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
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

    public static ArrayList<ClientJobPOInvoiceReference> parseList(String strJSON) {
        try {
            ArrayList<ClientJobPOInvoiceReference> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(strJSON);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                arr.add(new ClientJobPOInvoiceReference(objJSON.getString("uuid"), objJSON.getString("name")));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
