package asyncmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import org.json.JSONException;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientAsset;
import entity.ClientAssetInspect;
import entity.ClientAssetOwner;
import entity.ClientAssetService;
import entity.ClientEmployeeMaster;
import parser.parseAssetIOwn;
import parser.parseInspect;
import parser.parseService;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 9/13/2016.
 */
public class asyncAsset {
    Context ctx;
    public static String responseCode;

    public asyncAsset(Context ctx) {
        this.ctx = ctx;
    }

    public Thread getAllData() {
        Thread t = new Thread() {
            public void run() {
                getAsset();
                getService();
                getInspect();
            }
        };
        t.start();
        return t;
    }

    public void getService() {
        new Thread() {
            public void run() {
                ArrayList<ClientAssetService> arrServerdata = getServiceFromServer();
                if (arrServerdata == null)
                    return;
                DataBase db = new DataBase(ctx);
                db.open();
                db.cleanTable(DataBase.service_int);
                for (ClientAssetService obj : arrServerdata) {
                    String data[] = {obj.getAstId(), obj.getAstName(), obj.getAstAssetId(), obj.getAstAsset_name(),
                            obj.getAstAssetBarcode(), obj.getAstServiceFirmName(), obj.getAstAssignedTo(), String.valueOf(obj.getAstAssignedDate().getTime()), obj.getAstPerformedBy(),
                            String.valueOf(obj.getAstDateTime().getTime()), obj.getAstCost(), obj.getAstNote(), obj.getAstStatusId(), obj.getActmCategory_name(),
                            obj.getAmAsset_name(), obj.getAmDescription(), obj.getAmModel_name(), obj.getAmManufacturer_name(), obj.getAmSerial_no(), obj.getAmBarcode_no(),
                            String.valueOf(obj.getAmDate_acquired().getTime()), String.valueOf(obj.getAmDate_soon().getTime()), obj.getAmPurchase_cost(), obj.getAmPurchase_from(),
                            obj.getAmCurrent_value(), String.valueOf(obj.getAmDate_expired().getTime()),
                            obj.getAmAsset_location(), obj.getAmService_period(), obj.getAmIs_schedule_service_on(), obj.getAmService_aasigned_employee(),
                            obj.getAmInspection_period(), obj.getAmIs_schedule_inspection_on(), obj.getAmInspection_aasigned_employee(), String.valueOf(obj.getAmNext_service_date().getTime()),
                            String.valueOf(obj.getAmNext_inspection_date().getTime()), obj.getAmAsset_status(), obj.getAmCustom_field_1(), obj.getAmCustom_field_2(),
                            obj.getAmCustom_field_3(), obj.getAmCustom_field_4(), obj.getAmCustom_field_5()};
                    db.insert(DataBase.service_table, DataBase.service_int, data);

                    //if jobid is not available then save to table with false
                    String where = "astId=" + obj.getAstId() + "";
                    Cursor curIsVied = db.fetch(DataBase.service_view_table, DataBase.service_view_int, where);
                    if (curIsVied != null && curIsVied.getCount() == 0) {
                        String status = String.valueOf(ConstantVal.InspectionServiceStatus.NEW);
                        db.insert(DataBase.service_view_table, DataBase.service_view_int, new String[]{obj.getAstId(), status});
                    }
                    curIsVied.close();
                }
                db.close();
                Intent intent = new Intent();
                intent.setAction(ConstantVal.BroadcastAction.SERVICE_LIST);
                ctx.sendBroadcast(intent);
            }
        }.start();
    }

    public void getInspect() {
        new Thread() {
            public void run() {
                ArrayList<ClientAssetInspect> arrServerdata = getInspetFromServer();
                if (arrServerdata == null)
                    return;
                DataBase db = new DataBase(ctx);
                db.open();
                db.cleanTable(DataBase.inspect_int);
                for (ClientAssetInspect obj : arrServerdata) {
                    String data[] = {obj.getAitId(), obj.getAitName(), obj.getAitAssetId(), obj.getAitAsset_name(), obj.getAitAssetBarcode(), obj.getAitAssignedTo(),
                            String.valueOf(obj.getAitAssignedDate().getTime()), obj.getAitIsPresent(), String.valueOf(obj.getAitDateTime().getTime()),
                            obj.getAitNote(), obj.getAitStatusId(), obj.getActmCategory_name(),
                            obj.getAmAsset_name(), obj.getAmDescription(), obj.getAmModel_name(), obj.getAmManufacturer_name(), obj.getAmSerial_no(), obj.getAmBarcode_no(),
                            String.valueOf(obj.getAmDate_acquired().getTime()), String.valueOf(obj.getAmDate_soon().getTime()), obj.getAmPurchase_cost(), obj.getAmPurchase_from(), obj.getAmCurrent_value(), String.valueOf(obj.getAmDate_expired().getTime()),
                            obj.getAmAsset_location(), obj.getAmService_period(), obj.getAmIs_schedule_service_on(), obj.getAmService_aasigned_employee(),
                            obj.getAmInspection_period(), obj.getAmIs_schedule_inspection_on(), obj.getAmInspection_aasigned_employee(), String.valueOf(obj.getAmNext_service_date().getTime()),
                            String.valueOf(obj.getAmNext_inspection_date().getTime()), obj.getAmAsset_status(), obj.getAmCustom_field_1(), obj.getAmCustom_field_2(),
                            obj.getAmCustom_field_3(), obj.getAmCustom_field_4(), obj.getAmCustom_field_5()};
                    db.insert(DataBase.inspect_table, DataBase.inspect_int, data);

                    //if jobid is not available then save to table with false
                    String where = "aitId=" + obj.getAitId() + "";
                    Cursor curIsVied = db.fetch(DataBase.inspect_view_table, DataBase.inspect_view_int, where);
                    if (curIsVied != null && curIsVied.getCount() == 0) {
                        String status = String.valueOf(ConstantVal.InspectionServiceStatus.NEW);
                        db.insert(DataBase.inspect_view_table, DataBase.inspect_view_int, new String[]{obj.getAitId(), status});
                    }
                    curIsVied.close();
                }
                db.close();
                Intent intent = new Intent();
                intent.setAction(ConstantVal.BroadcastAction.INSPECT_LIST);
                ctx.sendBroadcast(intent);
            }
        }.start();
    }

    public void getAsset() {
        new Thread() {
            public void run() {
                ArrayList<ClientAsset> arrServerdata = getAssetDataFromServer();
                if (arrServerdata == null)
                    return;
                DataBase db = new DataBase(ctx);
                db.open();
                db.cleanTable(DataBase.asset_int);
                db.cleanTable(DataBase.asset_owner_int);
                for (ClientAsset obj : arrServerdata) {
                    String data[] = {obj.getAoAsset_id(), obj.getActmCategory_name(),
                            obj.getAmAsset_name(), obj.getAmDescription(), obj.getAmModel_name(), obj.getAmManufacturer_name(), obj.getAmSerial_no(), obj.getAmBarcode_no(),
                            String.valueOf(obj.getAmDate_acquired().getTime()), String.valueOf(obj.getAmDate_soon().getTime()), obj.getAmPurchase_cost(), obj.getAmPurchase_from(), obj.getAmCurrent_value(), String.valueOf(obj.getAmDate_expired().getTime()),
                            obj.getAmAsset_location(), obj.getAmService_period(), obj.getAmIs_schedule_service_on(), obj.getAmService_aasigned_employee(),
                            obj.getAmInspection_period(), obj.getAmIs_schedule_inspection_on(), obj.getAmInspection_aasigned_employee(), String.valueOf(obj.getAmNext_service_date().getTime()),
                            String.valueOf(obj.getAmNext_inspection_date().getTime()), obj.getAmAsset_status(), obj.getAmCustom_field_1(), obj.getAmCustom_field_2(),
                            obj.getAmCustom_field_3(), obj.getAmCustom_field_4(), obj.getAmCustom_field_5()};
                    db.insert(DataBase.asset_table, DataBase.asset_int, data);
                    for (ClientAssetOwner objOwner : obj.getArrOwner()) {
                        String[] dataOwner = {obj.getAoAsset_id(), objOwner.getAoEmployee_id(), objOwner.getAoInOut(), objOwner.getAoDate()};
                        db.insert(DataBase.asset_owner_table, DataBase.asset_owner_int, dataOwner);
                    }
                }
                db.close();
                Intent intent = new Intent();
                intent.setAction(ConstantVal.BroadcastAction.ASSET_LIST);
                ctx.sendBroadcast(intent);
            }
        }.start();
    }

    private ArrayList<ClientAsset> getAssetDataFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientAsset> arrServerdata = null;
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        String employee_id = Helper.getStringPreference(ctx, ClientEmployeeMaster.Fields.EMPLOYEE_ID, "");
        URLMapping um = ConstantVal.fetchOwnAsset(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{account_id, employee_id, String.valueOf(tokenId)}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && responseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            try {
                arrServerdata = parseAssetIOwn.getList(result);
                //Logger.debug("size:" + arrServerdata.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrServerdata;
    }

    private ArrayList<ClientAssetInspect> getInspetFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientAssetInspect> arrServerdata = null;
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        String employee_id = Helper.getStringPreference(ctx, ClientEmployeeMaster.Fields.EMPLOYEE_ID, "");
        URLMapping um = ConstantVal.fetchAssignedInspection(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{account_id, employee_id, String.valueOf(tokenId)}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && responseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            try {
                arrServerdata = parseInspect.getList(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrServerdata;
    }

    private ArrayList<ClientAssetService> getServiceFromServer() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientAssetService> arrServerdata = null;
        final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        String employee_id = Helper.getStringPreference(ctx, ClientEmployeeMaster.Fields.EMPLOYEE_ID, "");
        URLMapping um = ConstantVal.fetchAssignedService(ctx);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                new String[]{account_id, employee_id, String.valueOf(tokenId)}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && responseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            try {
                arrServerdata = parseService.getList(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrServerdata;

    }
}
