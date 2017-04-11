package entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.DataBase;
import utility.Logger;

/**
 * Created by SAI on 12/4/2016.
 */
public class ClientUIFormStatus {
    int id;
    String statusName;

    public ClientUIFormStatus(int id, String statusName) {
        this.id = id;
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public static ArrayList<ClientUIFormStatus> parseData(String strJSON) {
        try {
            JSONArray arr = new JSONArray(strJSON);
            ArrayList<ClientUIFormStatus> arrClientUIFormStatus = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject objJSON = arr.getJSONObject(i);
                int id = (objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id"));
                String name = (objJSON.getString("status_name").equals("null") ? "" : objJSON.getString("status_name"));
                arrClientUIFormStatus.add(new ClientUIFormStatus(id, name));
            }
            return arrClientUIFormStatus;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static void saveDataToDatabase(Context ctx, ArrayList<ClientUIFormStatus> arrClientUIFormStatus) {
        DataBase db = new DataBase(ctx);
        db.open();
        db.cleanTable(DataBase.UI_form_status_int);
        for (ClientUIFormStatus obj : arrClientUIFormStatus) {
            db.insert(DataBase.UI_form_status, DataBase.UI_form_status_int, new String[]{String.valueOf(obj.getId()), obj.getStatusName()});
        }
        db.close();
    }
}
