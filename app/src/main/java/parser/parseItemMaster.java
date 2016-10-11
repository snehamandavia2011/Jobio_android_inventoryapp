package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import entity.ClientAsset;
import entity.ClientAssetOwner;
import entity.ClientItemMaster;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 9/13/2016.
 */
public class parseItemMaster {
    public static ClientItemMaster parse(String JSONString) throws JSONException {
        JSONObject obj = new JSONObject(JSONString);
        String uuid = (obj.getString("uuid").equals("null") ? "" : obj.getString("uuid"));
        String item_name = (obj.getString("item_name").equals("null") ? "" : obj.getString("item_name"));
        String short_code = (obj.getString("short_code").equals("null") ? "" : obj.getString("short_code"));
        String specification = (obj.getString("specification").equals("null") ? "" : obj.getString("specification"));
        String category_name = (obj.getString("category_name").equals("null") ? "" : obj.getString("category_name"));
        String uom_id = (obj.getString("uom_id").equals("null") ? "" : obj.getString("uom_id"));
        String package_type_name = (obj.getString("package_type_name").equals("null") ? "" : obj.getString("package_type_name"));
        String location_name = (obj.getString("location_name").equals("null") ? "" : obj.getString("location_name"));
        String item_status = (obj.getString("item_status").equals("null") ? "" : obj.getString("item_status"));
        String manufacturer = (obj.getString("manufacturer").equals("null") ? "" : obj.getString("manufacturer"));
        String model = (obj.getString("model").equals("null") ? "" : obj.getString("model"));
        String monthly_demand = (obj.getString("monthly_demand").equals("null") ? "" : obj.getString("monthly_demand"));
        String min_qty_for_restock = (obj.getString("min_qty_for_restock").equals("null") ? "" : obj.getString("min_qty_for_restock"));
        String display_status = (obj.getString("display_status").equals("null") ? "" : obj.getString("display_status"));
        String custom_field_1 = (obj.getString("custom_field_1").equals("null") ? "" : obj.getString("custom_field_1"));
        String custom_field_2 = (obj.getString("custom_field_2").equals("null") ? "" : obj.getString("custom_field_2"));
        String custom_field_3 = (obj.getString("custom_field_3").equals("null") ? "" : obj.getString("custom_field_3"));
        String custom_field_4 = (obj.getString("custom_field_4").equals("null") ? "" : obj.getString("custom_field_4"));
        String custom_field_5 = (obj.getString("custom_field_5").equals("null") ? "" : obj.getString("custom_field_5"));
        ClientItemMaster objClientItemMaster = new ClientItemMaster(uuid, item_name, short_code, specification, category_name, uom_id, package_type_name, location_name, item_status, manufacturer, model, monthly_demand, min_qty_for_restock, display_status, custom_field_1, custom_field_2, custom_field_3, custom_field_4, custom_field_5);
        return objClientItemMaster;
    }
}
