package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import entity.ModuleFlag;

/**
 * Created by SAI on 12/17/2015.
 */
public class parsModuleFlag {
    public static ArrayList<ModuleFlag> getList(String JSONString) throws JSONException {
        ArrayList<ModuleFlag> arr = new ArrayList<ModuleFlag>();
        JSONArray jArray = new JSONArray(JSONString);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String id = obj.getString("id");
            String name = obj.getString("module_name");
            String access = obj.getString("access");
            ModuleFlag obj1 = new ModuleFlag(id, name, access);
            //obj1.display();
            arr.add(obj1);
        }
        return arr;
    }
}
