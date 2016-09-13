package asyncmanager;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientAsset;
import entity.ClientAssetOwner;
import entity.ClientEmployeeMaster;
import parser.parseAssetIOwn;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 9/13/2016.
 */
public class asyncAsset extends Thread {
    Context ctx;
    public static String responseCode;

    public asyncAsset(Context ctx) {
        this.ctx = ctx;
        start();
    }

    public void run() {
        getDataAssetOwnData();
        getService();
        getInspect();
    }

    public void getService() {

    }

    public void getInspect() {

    }

    public void getDataAssetOwnData() {
        ArrayList<ClientAsset> arrServerdata = this.getDataFromServer();
        if (arrServerdata == null)
            return;
        DataBase db = new DataBase(ctx);
        db.open();
        db.cleanTable(DataBase.asset_int);
        db.cleanTable(DataBase.asset_owner_int);
        for (ClientAsset obj : arrServerdata) {
            String data[] = {obj.getAoAsset_id(), obj.getActmCategory_name(),
                    obj.getAmAsset_name(), obj.getAmDescription(), obj.getAmModel_name(), obj.getAmManufacturer_name(), obj.getAmSerial_no(), obj.getAmBarcode_no(),
                    obj.getAmDate_acquired(), obj.getAmDate_soon(), obj.getAmPurchase_cost(), obj.getAmPurchase_from(), obj.getAmCurrent_value(), obj.getAmDate_expired(),
                    obj.getAmAsset_location(), obj.getAmService_period(), obj.getAmIs_schedule_service_on(), obj.getAmService_aasigned_employee(),
                    obj.getAmInspection_period(), obj.getAmIs_schedule_inspection_on(), obj.getAmInspection_aasigned_employee(), obj.getAmNext_service_date(),
                    obj.getAmNext_inspection_date(), obj.getAmAsset_status(), obj.getAmCustom_field_1(), obj.getAmCustom_field_2(),
                    obj.getAmCustom_field_3(), obj.getAmCustom_field_4(), obj.getAmCustom_field_5()};
            db.insert(DataBase.asset_table, DataBase.asset_owner_int, data);
            for (ClientAssetOwner objOwner : obj.getArrOwner()) {
                String[] dataOwner = {obj.getAoAsset_id(), objOwner.getAoEmployee_id(), objOwner.getAoStart_date(), objOwner.getAoEnd_date()};
                db.insert(DataBase.asset_owner_table, DataBase.asset_owner_int, data);
            }
        }
        db.close();

    }

    private ArrayList<ClientAsset> getDataFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientAsset> arrServerdata = null;
        final int tokenId = Helper.getIntPreference(ctx, ConstantVal.TOKEN_ID, 0);
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        String employee_id = Helper.getStringPreference(ctx, ClientEmployeeMaster.Fields.EMPLOYEE_ID, "");
        URLMapping um = ConstantVal.fetchOwnAsset(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{account_id, employee_id, String.valueOf(tokenId)}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("")) {
            try {
                arrServerdata = parseAssetIOwn.getList(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrServerdata;
    }
}
