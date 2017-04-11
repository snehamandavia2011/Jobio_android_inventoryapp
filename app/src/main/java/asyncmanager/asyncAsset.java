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
import entity.ClientCustomForm;
import entity.ClientCustomFormField;
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
    public static String assetResponseCode, serviceResponseCode, inspectResponseCode;

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
                        String status;
                        if (Integer.parseInt(obj.getAstStatusId()) == ConstantVal.assetServiceInspectionStatus.DONE) {
                            status = String.valueOf(ConstantVal.InspectionServiceStatus.DONE);
                        } else {
                            status = String.valueOf(ConstantVal.InspectionServiceStatus.NEW);
                        }
                        db.insert(DataBase.service_view_table, DataBase.service_view_int, new String[]{obj.getAstId(), status});
                    }
                    curIsVied.close();
                    insertClientJobForm(obj.getArrClientCustomForm(), "S", ctx);
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
                        String status;
                        if (Integer.parseInt(obj.getAitStatusId()) == ConstantVal.assetServiceInspectionStatus.DONE) {
                            status = String.valueOf(ConstantVal.InspectionServiceStatus.DONE);
                        } else {
                            status = String.valueOf(ConstantVal.InspectionServiceStatus.NEW);
                        }
                        db.insert(DataBase.inspect_view_table, DataBase.inspect_view_int, new String[]{obj.getAitId(), status});
                    }
                    curIsVied.close();
                    insertClientJobForm(obj.getArrClientCustomForm(), "I", ctx);
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
        assetResponseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && assetResponseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            try {
                arrServerdata = parseAssetIOwn.getList(result);
                //Logger.debug("size:" + arrServerdata.size());
            } catch (JSONException e) {
                e.printStackTrace();
                Logger.writeToCrashlytics(e);
            }
        }
        return arrServerdata;
    }

    private ArrayList<ClientAssetInspect> getInspetFromServer() {
        try {
            HttpEngine objHttpEngine = new HttpEngine();
            ArrayList<ClientAssetInspect> arrServerdata = null;
            final String tokenId = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
            String account_id = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
            String employee_id = Helper.getStringPreference(ctx, ClientEmployeeMaster.Fields.EMPLOYEE_ID, "");
            URLMapping um = ConstantVal.fetchAssignedInspection(ctx);
            ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                    new String[]{account_id, employee_id, String.valueOf(tokenId)}, um.getParamNames(), um.isNeedToSync());
            String result = objServerRespose.getResponseString();
            inspectResponseCode = objServerRespose.getResponseCode();
            if (result != null && !result.equals("") && inspectResponseCode == ConstantVal.ServerResponseCode.SUCCESS) {
                try {
                    arrServerdata = parseInspect.getList(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
            }

            return arrServerdata;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
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
        serviceResponseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && serviceResponseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            try {
                arrServerdata = parseService.getList(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Logger.writeToCrashlytics(e);
            }
        }
        return arrServerdata;
    }

    public static boolean isDataLoadSuccessfully() {
        try {
            if (assetResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    inspectResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS) &&
                    serviceResponseCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return false;
        }
    }

    private void insertClientJobForm(ArrayList<ClientCustomForm> arr, String refType, Context ctx) {
        DataBase db = new DataBase(ctx);
        db.open();
        Cursor curForm = db.fetch(DataBase.custom_form_table, DataBase.custom_form_int, "ftRefType='" + refType + "'");
        if (curForm != null && curForm.getCount() > 0) {
            curForm.moveToFirst();
            do {
                String ftForm_transaction_uuid = curForm.getString(1);
                String ftForm_id = curForm.getString(2);
                db.cleanTable(DataBase.custom_form_int, "ftRefType='" + refType + "'");
                db.cleanTable(DataBase.custom_form_fields_int, "ftForm_transaction_uuid='" + ftForm_transaction_uuid + "' and ffpForm_id='" + ftForm_id + "'");
            } while (curForm.moveToNext());
        }
        curForm.close();
        for (ClientCustomForm obj1 : arr) {
            String[] arrClientJobForm = {obj1.getForm_transaction_uuid(), obj1.getFtForm_id(), obj1.getFtIs_mandatory(), obj1.getFtRef_id(),
                    obj1.getFtIs_submitted(), obj1.getFtIs_showing_to_cust(), obj1.getFpForm_name(), obj1.getFpForm_description(),
                    obj1.getFpForm_category(), obj1.getFbBiz_name(), obj1.getFpForm_status(), obj1.getFpRef_type()};
            db.insert(DataBase.custom_form_table, DataBase.custom_form_int, arrClientJobForm);

            String whereFormView = "ftForm_transaction_uuid='" + obj1.getForm_transaction_uuid() + "' and ffpForm_id='" + obj1.getFtForm_id() + "'";
            Cursor curIsFormView = db.fetch(DataBase.custom_form_view_table, DataBase.custom_form_view_int, whereFormView);
            if (curIsFormView != null && curIsFormView.getCount() == 0) {
                db.insert(DataBase.custom_form_view_table, DataBase.custom_form_view_int, new String[]{obj1.getForm_transaction_uuid(), obj1.getFtForm_id(), String.valueOf(ConstantVal.FormStatus.PENGING)});
            }
            curIsFormView.close();

            ArrayList<ClientCustomFormField> arrClientCustomFormField = obj1.getArrClientCustomFormField();
            for (ClientCustomFormField objClientCustomFormField : arrClientCustomFormField) {
                String[] fields = {obj1.getForm_transaction_uuid(), objClientCustomFormField.getFrom_Id(),
                        String.valueOf(objClientCustomFormField.getUi_control_id()),
                        String.valueOf(objClientCustomFormField.getUi_control_type()), objClientCustomFormField.getUi_control_validation(),
                        objClientCustomFormField.getUi_control_style(), objClientCustomFormField.getUi_control_given_name(),
                        objClientCustomFormField.getUi_control_default_data_1(), objClientCustomFormField.getUi_control_default_data_2(),
                        String.valueOf(objClientCustomFormField.getPosition()), String.valueOf(objClientCustomFormField.getScreen_no()), objClientCustomFormField.getForm_prototype_id()};
                db.insert(DataBase.custom_form_fields_table, DataBase.custom_form_fields_int, fields);
                String whereForm = "ftForm_transaction_uuid='" + obj1.getForm_transaction_uuid() + "' and ffpForm_id='" + objClientCustomFormField.getFrom_Id() + "' and ffpPosition=" + objClientCustomFormField.getPosition() + " and ffpScreen_no=" + objClientCustomFormField.getScreen_no();
                Cursor curData = db.fetch(DataBase.custom_form_fields_data_table, DataBase.custom_form_fields_data_int, whereForm);
                if (curData != null && curData.getCount() > 0) {
                } else {
                    db.insert(DataBase.custom_form_fields_data_table, DataBase.custom_form_fields_data_int, new String[]{obj1.getForm_transaction_uuid(),
                            objClientCustomFormField.getFrom_Id(), String.valueOf(objClientCustomFormField.getUi_control_id()),
                            String.valueOf(objClientCustomFormField.getUi_control_type()), String.valueOf(objClientCustomFormField.getPosition()), String.valueOf(objClientCustomFormField.getScreen_no()),
                            "", objClientCustomFormField.getForm_prototype_id()});
                }
            }
        }
        db.close();
    }
}
