package entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.DataBase;

/**
 * Created by SAI on 12/4/2016.
 */
public class ClientUIControl {
    int id, parent;
    String control_name;

    public ClientUIControl(int id, String control_name, int parent) {
        this.id = id;
        this.control_name = control_name;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getControl_name() {
        return control_name;
    }

    public void setControl_name(String control_name) {
        this.control_name = control_name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public static ArrayList<ClientUIControl> parseData(String strJSON) {
        try {
            JSONArray arr = new JSONArray(strJSON);
            ArrayList<ClientUIControl> arrClientUIControl = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject objJSON = arr.getJSONObject(i);
                int id = (objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id"));
                String control_name = (objJSON.getString("control_name").equals("null") ? "" : objJSON.getString("control_name"));
                int parent = (objJSON.getString("parent").equals("null") ? 0 : objJSON.getInt("parent"));
                arrClientUIControl.add(new ClientUIControl(id, control_name, parent));
            }
            return arrClientUIControl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDataToDatabase(Context ctx, ArrayList<ClientUIControl> arrClientUIFormStatus) {
        DataBase db = new DataBase(ctx);
        db.open();
        db.cleanTable(DataBase.UI_control_int);
        for (ClientUIControl obj : arrClientUIFormStatus) {
            db.insert(DataBase.UI_Control, DataBase.UI_control_int, new String[]{String.valueOf(obj.getId()),
                    obj.getControl_name(), String.valueOf(obj.getParent())});
        }
        db.close();
    }
}
