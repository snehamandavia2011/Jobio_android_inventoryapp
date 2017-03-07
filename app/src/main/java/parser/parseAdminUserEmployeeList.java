package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import entity.ClientAdminUserEmployee;
import utility.Helper;

/**
 * Created by SAI on 12/17/2015.
 */
public class parseAdminUserEmployeeList {
    public static ArrayList<ClientAdminUserEmployee> getList(String JSONString) throws JSONException {
        ArrayList<ClientAdminUserEmployee> arr = new ArrayList<ClientAdminUserEmployee>();
        JSONArray jArray = new JSONArray(JSONString);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String adminUserId = (obj.getString("auId").equals("null") ? "" : obj.getString("auId"));
            String empId = (obj.getString("empId").equals("null") ? "" : obj.getString("empId"));
            String firstName = (obj.getString("first_name").equals("null") ? "" : obj.getString("first_name"));
            String lastName = (obj.getString("last_name").equals("null") ? "" : obj.getString("last_name"));
            String employee_status = (obj.getString("employee_status").equals("null") ? "" : obj.getString("employee_status"));
            String contact_no = (obj.getString("contact_no").equals("null") ? "" : obj.getString("contact_no"));
            String user_type_name = (obj.getString("user_type_name").equals("null") ? "" : obj.getString("user_type_name"));
            String isOnLine = (obj.getString("isOnLine").equals("null") ? "" : obj.getString("isOnLine"));
            String designation_name = (obj.getString("designation_name").equals("null") ? "" : obj.getString("designation_name"));
            //Date start_datetime = Helper.convertStringToDate(obj.getString("start_datetime"), "yyyy-MM-dd HH:mm:ss");
            //Date end_datetime = Helper.convertStringToDate(obj.getString("end_datetime"), "yyyy-MM-dd HH:mm:ss");
            ClientAdminUserEmployee obj1 = new ClientAdminUserEmployee(adminUserId, empId, firstName, lastName, employee_status, contact_no, null, user_type_name, isOnLine, designation_name);
            arr.add(obj1);
        }
        return arr;
    }
}
