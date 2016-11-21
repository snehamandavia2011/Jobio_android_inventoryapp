package asyncmanager;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUserEmployee;
import parser.parseAdminUserEmployeeList;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 6/17/2016.
 */
public class asyncEmployeeList extends Thread {
    Context ctx;
    public static String responseCode;

    public asyncEmployeeList(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        getData();
    }

    public void getData() {
        ArrayList<ClientAdminUserEmployee> arrServerdata = this.getDataFromServer();
        if (arrServerdata == null)
            return;
        DataBase db = new DataBase(ctx);
        db.open();
        db.cleanTable(DataBase.adminuser_employee_int);
        for (ClientAdminUserEmployee obj : arrServerdata) {
            String data[] = {String.valueOf(obj.getAuId()), String.valueOf(obj.getEmpId()), obj.getFirst_name(),
                    obj.getLast_name(), obj.getEmployee_status(), obj.getContact_no(), obj.getUser_type_name(),
                    obj.getIsOnLine()};
            db.insert(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, data);
        }
        db.close();
    }

    private ArrayList<ClientAdminUserEmployee> getDataFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientAdminUserEmployee> arrServerdata = null;
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        URLMapping um = ConstantVal.getClientAdminUserEmployeeList(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("")) {
            try {
                arrServerdata = parseAdminUserEmployeeList.getList(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrServerdata;
    }

    public static boolean isDataLoadSuccessfully() {
        if (responseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
            return true;
        } else {
            return false;
        }
    }
}
