package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import entity.ClientAdminUserEmployee;
import entity.ClientAsset;
import entity.ClientAssetOwner;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 9/13/2016.
 */
public class parseAssetIOwn {
    public static ArrayList<ClientAsset> getList(String JSONString) throws JSONException {
        ArrayList<ClientAsset> arr = new ArrayList<ClientAsset>();
        JSONArray jArray = new JSONArray(JSONString);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String asset_id = (obj.getString("asset_id").equals("null") ? "" : obj.getString("asset_id"));
            String category_name = (obj.getString("category_name").equals("null") ? "" : obj.getString("category_name"));
            String asset_name = (obj.getString("asset_name").equals("null") ? "" : obj.getString("asset_name"));
            String description = (obj.getString("description").equals("null") ? "" : obj.getString("description"));
            String model_name = (obj.getString("model_name").equals("null") ? "" : obj.getString("model_name"));
            String manufacturer_name = (obj.getString("manufacturer_name").equals("null") ? "" : obj.getString("manufacturer_name"));
            String serial_no = (obj.getString("serial_no").equals("null") ? "" : obj.getString("serial_no"));
            String barcode_no = (obj.getString("barcode_no").equals("null") ? "" : obj.getString("barcode_no"));
            Date date_acquired = Helper.convertStringToDate(obj.getString("date_acquired"), ConstantVal.DATE_FORMAT);
            Date date_soon = Helper.convertStringToDate(obj.getString("date_soon"), ConstantVal.DATE_FORMAT);
            String purchase_cost = (obj.getString("purchase_cost").equals("null") ? "" : obj.getString("purchase_cost"));
            String purchase_from = (obj.getString("purchase_from").equals("null") ? "" : obj.getString("purchase_from"));
            String current_value = (obj.getString("current_value").equals("null") ? "" : obj.getString("current_value"));
            Date date_expired = Helper.convertStringToDate(obj.getString("date_expired"), ConstantVal.DATE_FORMAT);
            String asset_location = (obj.getString("asset_location").equals("null") ? "" : obj.getString("asset_location"));
            String service_period = (obj.getString("service_period").equals("null") ? "" : obj.getString("service_period"));
            String is_schedule_service_on = (obj.getString("is_schedule_service_on").equals("null") ? "" : obj.getString("is_schedule_service_on"));
            String service_aasigned_employee = (obj.getString("service_aasigned_employee").equals("null") ? "" : obj.getString("service_aasigned_employee"));
            String inspection_period = (obj.getString("inspection_period").equals("null") ? "" : obj.getString("inspection_period"));
            String is_schedule_inspection_on = (obj.getString("is_schedule_inspection_on").equals("null") ? "" : obj.getString("is_schedule_inspection_on"));
            String inspection_aasigned_employee = (obj.getString("inspection_aasigned_employee").equals("null") ? "" : obj.getString("inspection_aasigned_employee"));
            Date next_service_date = Helper.convertStringToDate(obj.getString("next_service_date"), ConstantVal.DATE_FORMAT);
            Date next_inspection_date = Helper.convertStringToDate(obj.getString("next_inspection_date"), ConstantVal.DATE_FORMAT);
            String asset_status = (obj.getString("status_name").equals("null") ? "" : obj.getString("status_name"));
            String custom_field_1 = (obj.getString("custom_field_1").equals("null") ? "" : obj.getString("custom_field_1"));
            String custom_field_2 = (obj.getString("custom_field_2").equals("null") ? "" : obj.getString("custom_field_2"));
            String custom_field_3 = (obj.getString("custom_field_3").equals("null") ? "" : obj.getString("custom_field_3"));
            String custom_field_4 = (obj.getString("custom_field_4").equals("null") ? "" : obj.getString("custom_field_4"));
            String custom_field_5 = (obj.getString("custom_field_5").equals("null") ? "" : obj.getString("custom_field_5"));
            ArrayList<ClientAssetOwner> arrClientAssetOwner = null;
            if (obj.has("Owner")) {
                JSONArray arrOwner = obj.getJSONArray("Owner");
                arrClientAssetOwner = new ArrayList<>();
                for (int j = 0; j < arrOwner.length(); j++) {
                    JSONObject objO = arrOwner.getJSONObject(j);
                    String employee_id = (objO.getString("employee_id").equals("null") ? "" : objO.getString("employee_id"));
                    String start_date = (objO.getString("start_date").equals("null") ? "" : objO.getString("start_date"));
                    String end_date = (objO.getString("end_date").equals("null") ? "" : objO.getString("end_date"));
                    arrClientAssetOwner.add(new ClientAssetOwner(employee_id, start_date, end_date));
                }
            }
            ClientAsset objClientAsset = new ClientAsset(asset_id, category_name, asset_name, description, model_name, manufacturer_name, serial_no, barcode_no, date_acquired, date_soon, purchase_cost, purchase_from, current_value, date_expired, asset_location, service_period, is_schedule_service_on, service_aasigned_employee, inspection_period, is_schedule_inspection_on, inspection_aasigned_employee, next_service_date, next_inspection_date, asset_status, custom_field_1, custom_field_2, custom_field_3, custom_field_4, custom_field_5, arrClientAssetOwner);
            arr.add(objClientAsset);
        }
        return arr;
    }
}
