package asyncmanager;

import android.content.Context;
import android.text.Html;

import org.json.JSONException;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ModuleFlag;
import parser.parsModuleFlag;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 12/19/2015.
 */
public class asyncModuleFlag extends Thread {
    Context ctx;
    public static String responseCode;

    public asyncModuleFlag(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        getData();
    }

    public void getData() {
        ArrayList<ModuleFlag> arrServerdata = this.getDataFromServer();
        if (arrServerdata == null)
            return;
        DataBase db = new DataBase(ctx);
        db.open();
        db.cleanTable(DataBase.module_Flag_int);
        for (ModuleFlag sObj : arrServerdata) {
            db.insert(DataBase.module_flag_table, DataBase.module_Flag_int, new String[]{String.valueOf(sObj.getServerPKModuleId()),
                    sObj.getModuleName(), String.valueOf(sObj.getAccess())});
        }
        db.close();
    }

    private ArrayList<ModuleFlag> getDataFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ModuleFlag> arrServerdata = new ArrayList<>();
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        URLMapping um = ConstantVal.getModuleFlagUrl(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{Helper.getStringPreference(ctx, ClientAdminUser.Fields.ADMINUSERID, ""), String.valueOf(ConstantVal.AdminUserType.AIM_APP_USERS),
                        String.valueOf(tokenId), account_id}, um.getParamNames(), um.isNeedToSync());
        responseCode = objServerRespose.getResponseCode();
        String resultModuleFlag = Html.fromHtml(objServerRespose.getResponseString()).toString();
        if (resultModuleFlag != null && !resultModuleFlag.equals("")) {
            try {
                arrServerdata = parsModuleFlag.getList(resultModuleFlag);
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
