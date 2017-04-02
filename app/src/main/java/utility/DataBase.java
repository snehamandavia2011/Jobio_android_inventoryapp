package utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DataBase {

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_0_CREATE);
            db.execSQL(TABLE_1_CREATE);
            db.execSQL(TABLE_2_CREATE);
            db.execSQL(TABLE_3_CREATE);
            db.execSQL(TABLE_4_CREATE);
            db.execSQL(TABLE_5_CREATE);
            db.execSQL(TABLE_6_CREATE);
            db.execSQL(TABLE_7_CREATE);
            db.execSQL(TABLE_8_CREATE);
            db.execSQL(TABLE_9_CREATE);
            db.execSQL(TABLE_10_CREATE);
            db.execSQL(TABLE_11_CREATE);
            db.execSQL(TABLE_12_CREATE);
            db.execSQL(TABLE_13_CREATE);
            db.execSQL(TABLE_14_CREATE);
            db.execSQL(TABLE_15_CREATE);
            db.execSQL(TABLE_16_CREATE);
            db.execSQL(TABLE_17_CREATE);
            db.execSQL(TABLE_18_CREATE);
            db.execSQL(TABLE_19_CREATE);
            db.execSQL(TABLE_20_CREATE);
            db.execSQL(TABLE_21_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + device_to_db_sync_table);
            db.execSQL("DROP TABLE IF EXISTS " + field_message_table);
            db.execSQL("DROP TABLE IF EXISTS " + adminuser_employee_table);
            db.execSQL("DROP TABLE IF EXISTS " + asset_table);
            db.execSQL("DROP TABLE IF EXISTS " + asset_owner_table);
            db.execSQL("DROP TABLE IF EXISTS " + inspect_table);
            db.execSQL("DROP TABLE IF EXISTS " + inspect_view_table);
            db.execSQL("DROP TABLE IF EXISTS " + item_master_table);
            db.execSQL("DROP TABLE IF EXISTS " + item_transaction_table);
            db.execSQL("DROP TABLE IF EXISTS " + stock_transaction_status_table);
            db.execSQL("DROP TABLE IF EXISTS " + stock_transaction_reason_table);
            db.execSQL("DROP TABLE IF EXISTS " + module_flag_table);
            db.execSQL("DROP TABLE IF EXISTS " + UI_form_status);
            db.execSQL("DROP TABLE IF EXISTS " + UI_Control);
            db.execSQL("DROP TABLE IF EXISTS " + UI_Control_Type);
            db.execSQL("DROP TABLE IF EXISTS " + custom_form_table);
            db.execSQL("DROP TABLE IF EXISTS " + custom_form_fields_table);
            db.execSQL("DROP TABLE IF EXISTS " + custom_form_view_table);
            db.execSQL("DROP TABLE IF EXISTS " + custom_form_fields_data_table);
            db.execSQL("DROP TABLE IF EXISTS " + custom_form_photo_data_table);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDb;
    private Context HCtx = null;
    private static final String DATABASE_NAME = "dbJobioAIM";
    private static final int DATABASE_VERSION = 1;

    public static final String device_to_db_sync_table = "DeviceToDbSync";
    public static final int device_to_db_sync_int = 0;
    public static final String field_message_table = "fieldMessage";
    public static final int field_message_int = 1;
    public static final String adminuser_employee_table = "adminuserEmployeeTable";
    public static final int adminuser_employee_int = 2;
    public static final String asset_table = "asset";
    public static final int asset_int = 3;//field of asset and inspect are same. Table contain the data for asset I own.
    public static final String asset_owner_table = "assetOwner";
    public static final int asset_owner_int = 4;
    public static final String inspect_table = "inspect";
    public static final int inspect_int = 5;
    public static final String inspect_view_table = "inspectView";
    public static final int inspect_view_int = 6;
    public static final String service_table = "service";
    public static final int service_int = 7;
    public static final String service_view_table = "serviceView";
    public static final int service_view_int = 8;
    public static final String item_master_table = "itemMaster";
    public static final int item_master_int = 9;
    public static final String item_transaction_table = "itemTransaction";
    public static final int item_transaction_int = 10;
    public static final String stock_transaction_status_table = "stockTransactionSatus";
    public static final int stock_transaction_status_int = 11;
    public static final String stock_transaction_reason_table = "stockTransactionReason";
    public static final int stock_transaction_reason_int = 12;
    public static final String module_flag_table = "ModuleFlag";
    public static final int module_Flag_int = 13;
    public static final String UI_form_status = "UIFormStatus";
    public static final int UI_form_status_int = 14;
    public static final String UI_Control = "UIControl";
    public static final int UI_control_int = 15;
    public static final String UI_Control_Type = "UIControlType";
    public static final int UI_control_type_int = 16;
    public static final String custom_form_table = "CustomForm";
    public static final int custom_form_int = 17;
    public static final String custom_form_fields_table = "CustomFormFields";
    public static final int custom_form_fields_int = 18;
    public static final String custom_form_view_table = "CustomFormView";
    public static final int custom_form_view_int = 19;
    public static final String custom_form_fields_data_table = "CustomFormFieldsData";
    public static final int custom_form_fields_data_int = 20;
    public static final String custom_form_photo_data_table = "CustomFormPhotoData";
    public static final int custom_form_photo_data_int = 21;


    String[][] tables = new String[][]{{"_ID", "URL", "data", "isSync", "last_result_code", "admin_user_id", "account_id"},
            {"_ID", "id", "message", "from_id", "to_id", "is_viewed", "datetime", "timestamp"},
            {"_ID", "auId", "empId", "first_name", "last_name", "employee_status", "contact_no", "user_type_name", "isOnLine", "designation_name"},
            {"_ID", "aoAsset_id", "actmCategory_name",
                    "amAsset_name", "amDescription", "amModel_name", "amManufacturer_name", "amSerial_no", "amBarcode_no",
                    "amDate_acquired", "amDate_soon", "amPurchase_cost", "amPurchase_from", "amCurrent_value", "amDate_expired"
                    , "amAsset_location", "amService_period", "amIs_schedule_service_on", "amService_aasigned_employee",
                    "amInspection_period", "amIs_schedule_inspection_on", "amInspection_aasigned_employee", "amNext_service_date",
                    "amNext_inspection_date", "amAsset_status", "amCustom_field_1", "amCustom_field_2", "amCustom_field_3", "amCustom_field_4", "amCustom_field_5"},
            {"_ID", "aoAsset_id", "aoEmployee_id", "aoInOut", "aoDate"},
            {"_ID", "aitId", "aitName", "aitAssetId", "aitAsset_name", "aitAssetBarcode", "aitAssignedTo", "aitAssignedDate", "aitIsPresent", "aitDateTime",
                    "aitNote", "aitStatusId", "actmCategory_name",
                    "amAsset_name", "amDescription", "amModel_name", "amManufacturer_name", "amSerial_no", "amBarcode_no",
                    "amDate_acquired", "amDate_soon", "amPurchase_cost", "amPurchase_from", "amCurrent_value", "amDate_expired"
                    , "amAsset_location", "amService_period", "amIs_schedule_service_on", "amService_aasigned_employee",
                    "amInspection_period", "amIs_schedule_inspection_on", "amInspection_aasigned_employee", "amNext_service_date",
                    "amNext_inspection_date", "amAsset_status", "amCustom_field_1", "amCustom_field_2", "amCustom_field_3", "amCustom_field_4", "amCustom_field_5"},
            {"_ID", "aitId", "localViewStatus"},
            {"_ID", "astId", "astName", "astAssetId", "astAsset_name", "astAssetBarcode", "astServiceFirmName", "astAssignedTo",
                    "astAssignedDate", "astPerformedBy", "astDateTime", "astCost",
                    "astNote", "astStatusId", "actmCategory_name",
                    "amAsset_name", "amDescription", "amModel_name", "amManufacturer_name", "amSerial_no", "amBarcode_no",
                    "amDate_acquired", "amDate_soon", "amPurchase_cost", "amPurchase_from", "amCurrent_value", "amDate_expired"
                    , "amAsset_location", "amService_period", "amIs_schedule_service_on", "amService_aasigned_employee",
                    "amInspection_period", "amIs_schedule_inspection_on", "amInspection_aasigned_employee", "amNext_service_date",
                    "amNext_inspection_date", "amAsset_status", "amCustom_field_1", "amCustom_field_2", "amCustom_field_3", "amCustom_field_4", "amCustom_field_5"},
            {"_ID", "astId", "localViewStatus"},
            {"_ID", "uuid", "item_name", "short_code", "specification", "category_name", "uom_name",
                    "package_type_name", "location_name", "item_status", "manufacturer", "model", "photo", "monthly_demand",
                    "min_qty_for_restock", "display_status", "custom_field_1", "custom_field_2", "custom_field_3",
                    "custom_field_4", "custom_field_5", "last_update_date_time"},
            {"_ID", "imUUID", "itUUID", "available_qty", "cost", "price", "barcode", "spplierName"},
            {"_ID", "id", "action_name"}, {"_ID", "id", "stock_transaction_status_id", "reason"},
            {"localPKModuleId", "serverPKModuleId", "moduleName", "access"}, {"_ID", "id", "status_name"},
            {"_ID", "id", "control_name", "parent"}, {"_ID", "id", "ui_control_id", "type_name", "ui_size", "ui_data_default_input"},
            {"_ID", "ftForm_transaction_uuid", "ftForm_id", "ftIs_mandatory", "ftRef_id", "ftIs_submitted",
                    "ftIs_showing_to_cust", "fpForm_name", "fpForm_description", "fpForm_category", "fbBiz_name", "fpForm_status", "ftRefType"},
            {"_ID", "ftForm_transaction_uuid", "ffpForm_id", "ffpUI_control_id", "ffpUI_control_type", "ffpUI_control_validation", "ffpUI_control_style", "ffpUI_control_given_name",
                    "ffpUI_control_default_data_1", "ffpUI_control_default_data_2", "ffpPosition", "ffpScreen_no", "form_prototype_uuid"},
            {"_ID", "ftForm_transaction_uuid", "ffpForm_id", "status"},
            {"_ID", "ftForm_transaction_uuid", "ffpForm_id", "ffpUI_control_id", "ffpUI_control_type", "ffpPosition", "ffpScreen_no", "fftData", "form_prototype_uuid"},
            {"_ID", "ftForm_transaction_uuid", "ffpForm_id", "ffpUI_control_id", "ffpUI_control_type", "ffpPosition", "ffpScreen_no", "photoUUID", "localPath"}};
    private static final String TABLE_0_CREATE = "create table "
            + device_to_db_sync_table
            + "(_ID integer primary key autoincrement,URL text not null,data text not null,isSync text not null,last_result_code text not null,admin_user_id text not null, account_id text not null);";

    private static final String TABLE_1_CREATE = "create table "
            + field_message_table
            + "(_ID integer primary key autoincrement,id text not null,message text not null," +
            "from_id text not null,to_id text not null,is_viewed text not null," +
            "datetime text not null,timestamp text not null);";

    private static final String TABLE_2_CREATE = "create table "
            + adminuser_employee_table
            + "(_ID integer primary key autoincrement,auId text not null,empId text not null," +
            "first_name text not null,last_name text not null,employee_status text not null," +
            "contact_no text not null,photo text null,user_type_name text not null,isOnLine text not null," +
            "designation_name text not null);";

    private static final String TABLE_3_CREATE = "create table "
            + asset_table
            + "(_ID integer primary key autoincrement,aoAsset_id text not null,actmCategory_name text not null," +
            "amAsset_name text not null,amDescription text null,amModel_name text not null,amManufacturer_name text not null," +
            "amSerial_no text not null,amBarcode_no text null,amDate_acquired text not null,amDate_soon text not null," +
            "amPurchase_cost text not null,amPurchase_from text null,amCurrent_value text not null,amDate_expired text not null," +
            "amAsset_location text not null,amService_period text null,amIs_schedule_service_on text not null," +
            "amService_aasigned_employee text not null,amInspection_period text not null,amIs_schedule_inspection_on text null," +
            "amInspection_aasigned_employee text not null,amNext_service_date text not null,amNext_inspection_date text not null," +
            "amAsset_status text not null,amCustom_field_1 text not null,amCustom_field_2 text not null,amCustom_field_3 text not null," +
            "amCustom_field_4 text not null,amCustom_field_5 text not null,photo text null);";

    private static final String TABLE_4_CREATE = "create table "
            + asset_owner_table
            + "(_ID integer primary key autoincrement,aoAsset_id text not null,aoEmployee_id text not null," +
            "aoInOut text not null,aoDate text not null);";
    private static final String TABLE_5_CREATE = "create table "
            + inspect_table
            + "(_ID integer primary key autoincrement,aitId text not null,aitName text not null,aitAssetId text not null,aitAsset_name text not null," +
            "aitAssetBarcode text not null,aitAssignedTo text not null,aitAssignedDate text not null,aitIsPresent text not null," +
            "aitDateTime text not null,aitNote text not null,aitPhoto text null,aitStatusId text not null, actmCategory_name text not null," +
            "amAsset_name text not null,amDescription text null,amModel_name text not null,amManufacturer_name text not null," +
            "amSerial_no text not null,amBarcode_no text null,amDate_acquired text not null,amDate_soon text not null," +
            "amPurchase_cost text not null,amPurchase_from text null,amCurrent_value text not null,amDate_expired text not null," +
            "amAsset_location text not null,amService_period text null,amIs_schedule_service_on text not null," +
            "amService_aasigned_employee text not null,amInspection_period text not null,amIs_schedule_inspection_on text null," +
            "amInspection_aasigned_employee text not null,amNext_service_date text not null,amNext_inspection_date text not null," +
            "amAsset_status text not null,amCustom_field_1 text not null,amCustom_field_2 text not null,amCustom_field_3 text not null," +
            "amCustom_field_4 text not null,amCustom_field_5 text not null);";

    private static final String TABLE_6_CREATE = "create table "
            + inspect_view_table
            + "(_ID integer primary key autoincrement,aitId text not null,localViewStatus text not null);";


    private static final String TABLE_7_CREATE = "create table "
            + service_table
            + "(_ID integer primary key autoincrement,astId text not null,astName text not null,astAssetId text not null,astAsset_name text not null," +
            "astAssetBarcode text not null,astServiceFirmName text not null,astAssignedTo text not null,astAssignedDate text not null,astPerformedBy text not null," +
            "astDateTime text not null,astCost text not null,astNote text not null,astPhoto text null,astInvoicePicture text null,astStatusId text not null, actmCategory_name text not null," +
            "amAsset_name text not null,amDescription text null,amModel_name text not null,amManufacturer_name text not null," +
            "amSerial_no text not null,amBarcode_no text null,amDate_acquired text not null,amDate_soon text not null," +
            "amPurchase_cost text not null,amPurchase_from text null,amCurrent_value text not null,amDate_expired text not null," +
            "amAsset_location text not null,amService_period text null,amIs_schedule_service_on text not null," +
            "amService_aasigned_employee text not null,amInspection_period text not null,amIs_schedule_inspection_on text null," +
            "amInspection_aasigned_employee text not null,amNext_service_date text not null,amNext_inspection_date text not null," +
            "amAsset_status text not null,amCustom_field_1 text not null,amCustom_field_2 text not null,amCustom_field_3 text not null," +
            "amCustom_field_4 text not null,amCustom_field_5 text not null);";

    private static final String TABLE_8_CREATE = "create table "
            + service_view_table
            + "(_ID integer primary key autoincrement,astId text not null,localViewStatus text not null);";

    private static final String TABLE_9_CREATE = "create table "
            + item_master_table
            + "(_ID integer primary key autoincrement,uuid text not null,item_name text not null,short_code text not null," +
            "specification text not null,category_name text not null,uom_name text not null,package_type_name text not null," +
            "location_name text not null,item_status text not null,manufacturer text not null,model text not null,photo text not null," +
            "monthly_demand text not null,min_qty_for_restock text not null,display_status text not null,custom_field_1 text not null," +
            "custom_field_2 text not null,custom_field_3 text not null,custom_field_4 text not null,custom_field_5 text not null," +
            "last_update_date_time text not null);";
    private static final String TABLE_10_CREATE = "create table "
            + item_transaction_table
            + "(_ID integer primary key autoincrement,imUUID text not null,itUUID text not null,available_qty text not null," +
            "cost text not null,price text not null,barcode text not null,spplierName text not null );";

    private static final String TABLE_11_CREATE = "create table "
            + stock_transaction_status_table
            + "(_ID integer primary key autoincrement,id text not null,action_name text not null);";

    private static final String TABLE_12_CREATE = "create table "
            + stock_transaction_reason_table
            + "(_ID integer primary key autoincrement,id text not null,stock_transaction_status_id text not null,reason text not null);";

    private static final String TABLE_13_CREATE = "create table "
            + module_flag_table
            + "(localPKModuleId integer primary key autoincrement,"
            + "serverPKModuleId text not null,moduleName text not null, access text not null);";

    private static final String TABLE_14_CREATE = "create table "
            + UI_form_status
            + "(_ID integer primary key autoincrement,id text not null,status_name text not null);";

    private static final String TABLE_15_CREATE = "create table "
            + UI_Control
            + "(_ID integer primary key autoincrement,id text not null,control_name text not null,parent text not null);";

    private static final String TABLE_16_CREATE = "create table "
            + UI_Control_Type
            + "(_ID integer primary key autoincrement,id text not null,ui_control_id text not null," +
            "type_name text not null,ui_size text not null,ui_data_default_input text not null);";

    private static final String TABLE_17_CREATE = "create table "
            + custom_form_table
            + "(_ID integer primary key autoincrement,ftForm_transaction_uuid text not null,ftForm_id text not null,ftIs_mandatory text not null" +
            ",ftRef_id text not null,ftIs_submitted text not null,ftIs_showing_to_cust text not null,fpForm_name text not null,fpForm_description text not null," +
            "fpForm_category text not null,fbBiz_name text not null,fpForm_status text not null,ftRefType text not null);";

    private static final String TABLE_18_CREATE = "create table "
            + custom_form_fields_table
            + "(_ID integer primary key autoincrement,ftForm_transaction_uuid text not null,ffpForm_id text not null,ffpUI_control_id text not null" +
            ",ffpUI_control_type text not null,ffpUI_control_validation text not null,ffpUI_control_style text not null,ffpUI_control_given_name text not null," +
            "ffpUI_control_default_data_1 text not null," +
            "ffpUI_control_default_data_2 text not null,ffpPosition text not null,ffpScreen_no text not null,form_prototype_uuid text not null);";

    private static final String TABLE_19_CREATE = "create table "
            + custom_form_view_table
            + "(_ID integer primary key autoincrement,ftForm_transaction_uuid text not null," +
            "ffpForm_id text not null,status text not null);";

    private static final String TABLE_20_CREATE = "create table "
            + custom_form_fields_data_table
            + "(_ID integer primary key autoincrement,ftForm_transaction_uuid text not null," +
            "ffpForm_id text not null,ffpUI_control_id text not null,ffpUI_control_type text not null," +
            " ffpPosition text not null,ffpScreen_no text not null,fftData text not null,form_prototype_uuid text not null);";

    private static final String TABLE_21_CREATE = "create table "
            + custom_form_photo_data_table
            + "(_ID integer primary key autoincrement,ftForm_transaction_uuid text not null," +
            "ffpForm_id text not null,ffpUI_control_id text not null,ffpUI_control_type text not null,ffpPosition text not null," +
            "ffpScreen_no text not null,photoUUID text not null,localPath text not null);";


    public DataBase(Context ctx) {
        HCtx = ctx;
    }

    public DataBase open() throws SQLException {
        dbHelper = new DatabaseHelper(HCtx);
        sqLiteDb = dbHelper.getWritableDatabase();
        return this;
    }

    public void cleanAll() {
        sqLiteDb.delete(device_to_db_sync_table, null, null);
        sqLiteDb.delete(field_message_table, null, null);
        sqLiteDb.delete(adminuser_employee_table, null, null);
        sqLiteDb.delete(asset_table, null, null);
        sqLiteDb.delete(asset_owner_table, null, null);
        sqLiteDb.delete(inspect_table, null, null);
        sqLiteDb.delete(inspect_view_table, null, null);
        sqLiteDb.delete(service_table, null, null);
        sqLiteDb.delete(service_view_table, null, null);
        sqLiteDb.delete(item_master_table, null, null);
        sqLiteDb.delete(item_transaction_table, null, null);
        sqLiteDb.delete(stock_transaction_status_table, null, null);
        sqLiteDb.delete(stock_transaction_reason_table, null, null);
        sqLiteDb.delete(module_flag_table, null, null);
        sqLiteDb.delete(UI_form_status, null, null);
        sqLiteDb.delete(UI_Control, null, null);
        sqLiteDb.delete(UI_Control_Type, null, null);
        sqLiteDb.delete(custom_form_table, null, null);
        sqLiteDb.delete(custom_form_fields_table, null, null);
        sqLiteDb.delete(custom_form_view_table, null, null);
        sqLiteDb.delete(custom_form_fields_data_table, null, null);
        sqLiteDb.delete(custom_form_photo_data_table, null, null);
        cleanDataFromImageFolder();

    }

    public void cleanNotUserTable() {
        sqLiteDb.delete(inspect_view_table, null, null);
        sqLiteDb.delete(service_view_table, null, null);
        sqLiteDb.delete(custom_form_view_table, null, null);
        sqLiteDb.delete(custom_form_fields_data_table, null, null);
        sqLiteDb.delete(custom_form_photo_data_table, null, null);
        cleanDataFromImageFolder();
    }

    public void cleanDataFromImageFolder() {
        Cursor cur = fetchAll(DataBase.custom_form_photo_data_table, DataBase.custom_form_photo_data_int);
        if (cur != null && cur.getCount() > 0) {
            String localPath = cur.getString(8);
            File file = new File(localPath);
            if (file.exists()) {
                file.delete();
            }
        }
        cur.close();
    }

    public void cleanLogoutTable() {
        sqLiteDb.delete(device_to_db_sync_table, "isSync=?", new String[]{"1"});
        sqLiteDb.delete(field_message_table, null, null);
        sqLiteDb.delete(adminuser_employee_table, null, null);
        sqLiteDb.delete(asset_table, null, null);
        sqLiteDb.delete(asset_owner_table, null, null);
        sqLiteDb.delete(inspect_table, null, null);
        sqLiteDb.delete(service_table, null, null);
        sqLiteDb.delete(stock_transaction_status_table, null, null);
        sqLiteDb.delete(stock_transaction_reason_table, null, null);
        sqLiteDb.delete(module_flag_table, null, null);
        sqLiteDb.delete(UI_form_status, null, null);
        sqLiteDb.delete(UI_Control, null, null);
        sqLiteDb.delete(UI_Control_Type, null, null);
        sqLiteDb.delete(custom_form_table, null, null);
        sqLiteDb.delete(custom_form_fields_table, null, null);
    }

    public void cleanTable(int tableNo, String where) {
        switch (tableNo) {
            case custom_form_int:
                sqLiteDb.delete(custom_form_table, where, null);
                break;
            case custom_form_fields_int:
                sqLiteDb.delete(custom_form_fields_table, where, null);
                break;
        }
    }

    public void cleanTable(int tableNo) {
        switch (tableNo) {
            case device_to_db_sync_int:
                sqLiteDb.delete(device_to_db_sync_table, null, null);
                break;
            case field_message_int:
                sqLiteDb.delete(field_message_table, null, null);
                break;
            case adminuser_employee_int:
                sqLiteDb.delete(adminuser_employee_table, null, null);
                break;
            case asset_int:
                sqLiteDb.delete(asset_table, null, null);
                break;
            case asset_owner_int:
                sqLiteDb.delete(asset_owner_table, null, null);
                break;
            case inspect_int:
                sqLiteDb.delete(inspect_table, null, null);
                break;
            case inspect_view_int:
                sqLiteDb.delete(inspect_view_table, null, null);
                break;
            case service_int:
                sqLiteDb.delete(service_table, null, null);
                break;
            case service_view_int:
                sqLiteDb.delete(service_view_table, null, null);
                break;
            case item_master_int:
                sqLiteDb.delete(item_master_table, null, null);
                break;
            case item_transaction_int:
                sqLiteDb.delete(item_transaction_table, null, null);
                break;
            case module_Flag_int:
                sqLiteDb.delete(module_flag_table, null, null);
                break;
            case UI_form_status_int:
                sqLiteDb.delete(UI_form_status, null, null);
                break;
            case UI_control_int:
                sqLiteDb.delete(UI_Control, null, null);
                break;
            case UI_control_type_int:
                sqLiteDb.delete(UI_Control_Type, null, null);
                break;
            case custom_form_int:
                sqLiteDb.delete(custom_form_table, null, null);
                break;
            case custom_form_fields_int:
                sqLiteDb.delete(custom_form_fields_table, null, null);
                break;
            case custom_form_view_int:
                sqLiteDb.delete(custom_form_view_table, null, null);
                break;
            case custom_form_fields_data_int:
                sqLiteDb.delete(custom_form_fields_data_table, null, null);
                break;
            case custom_form_photo_data_int:
                sqLiteDb.delete(custom_form_photo_data_table, null, null);
                break;
            default:
                break;
        }
    }

    public void close() {
        dbHelper.close();
    }

    public synchronized long insert(String DATABASE_TABLE, int tableNo,
                                    String[] values) {
        ContentValues vals = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            vals.put(tables[tableNo][i + 1], values[i]);
        }
        return sqLiteDb.insert(DATABASE_TABLE, null, vals);
    }

    public synchronized long insert(String DATABASE_TABLE, ContentValues cv) {
        return sqLiteDb.insert(DATABASE_TABLE, null, cv);
    }

    public synchronized long insertWithSR_NO(String DATABASE_TABLE,
                                             int tableNo, String[] values, String srno) {
        ContentValues vals = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            vals.put(tables[tableNo][i + 1], values[i]);
        }
        vals.put(tables[tableNo][0], srno);
        return sqLiteDb.insert(DATABASE_TABLE, null, vals);
    }

    public boolean delete(String DATABASE_TABLE, int tableNo, long rowId) {
        return sqLiteDb.delete(DATABASE_TABLE,
                tables[tableNo][0] + "=" + rowId, null) > 0;
    }

    public synchronized boolean delete(String DATABASE_TABLE, int tableNo,
                                       String whereCause) {
        return sqLiteDb.delete(DATABASE_TABLE, whereCause, null) > 0;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     long rowId) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][0] + "=" + rowId, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized int getCounts(String DATABASE_TABLE, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where,
                null, null, null, null);
        if (ret != null) {
            return ret.getCount();
        }
        ret.close();
        return 0;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int[] colindex, String where) throws SQLException {

        String[] cols = new String[colindex.length];
        for (int i = 0; i < colindex.length; i++)
            cols[i] = tables[tableNo][colindex[i]];

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, cols, where, null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     String[] cols, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, cols, where, null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "'", null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval, int colIndex2, String colval2)
            throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "' and "
                        + tables[tableNo][colIndex2] + "='" + colval2 + "'",
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        strSelection = strSelection.substring(0, strSelection.length() - 5);
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchQ(String DATABASE_TABLE, int tableNo,
                                      int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        strSelection = strSelection
                + "stackid in (select stack_id from stacks where isarchieve='No')";
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchFollowUp(String DATABASE_TABLE,
                                             int tableNo, int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        // strSelection = strSelection.substring(0,strSelection.length() - 5);
        strSelection = strSelection
                + "stackid in (select stack_id from stacks where isarchieve='No')";
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval, String orderByval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "'", null, null,
                null, orderByval);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, String where, String orderByval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where, null, null,
                null, orderByval);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], null, null,
                    null, null, null);

        } catch (Exception e) {
            Log.e("yo", e.getMessage());
            return null;
        }
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo,
                                        String orderByval) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], null, null,
                    null, null, orderByval);

        } catch (Exception e) {
            Log.e("yo", e.getMessage());
            return null;
        }
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo,
                                        String orderByval, String where) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where, null,
                    null, null, orderByval);

        } catch (Exception e) {
            Log.e("yo", e.getMessage());
            return null;
        }
    }

    public synchronized Cursor fetchDistinctSupplier(String DATABASE_TABLE) {
        try {
            return sqLiteDb.query(DATABASE_TABLE,
                    new String[]{"DISTINCT categoryID", "categoryName"}, null, null, null,
                    null, null);
        } catch (Exception e) {
            Log.e("yo", e.getMessage());
            return null;
        }
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          ContentValues vc) {
        return sqLiteDb.update(DATABASE_TABLE, vc, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, String where,
                          ContentValues cv) {
        return sqLiteDb.update(DATABASE_TABLE, cv, where, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, String where, String[] args,
                          ContentValues cv) {
        return sqLiteDb.update(DATABASE_TABLE, cv, where, args) > 0;
    }

    public boolean updatePage(String DATABASE_TABLE, int tableNo,
                              int flashCardID, int pageno, String val) {
        ContentValues vals = new ContentValues();
        // for (int i = 0; i < values.length; i++)
        // vals.put(tables[tableNo][i + 1], values[i]);
        vals.put(tables[tableNo][3], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, " flashcardid='"
                + flashCardID + "' and pageno='" + pageno + "'", null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          int colIndex, int val) {
        ContentValues vals = new ContentValues();
        vals.put(tables[tableNo][colIndex], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          int colIndex, String val) {
        ContentValues vals = new ContentValues();
        vals.put(tables[tableNo][colIndex], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public Cursor fetch(String DATABASE_TABLE, int tableNo, int i,
                        String string, int j) {
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][i] + "='" + string + "' and mediatypeid<>" + j,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

}


