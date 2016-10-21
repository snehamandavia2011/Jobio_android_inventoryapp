package utility;

import android.content.Context;

import com.lab360io.jobio.inventoryapp.R;

import java.util.ArrayList;

import entity.ClientAssetInspectServiceStatus;

/**
 * Created by SAI on 9/1/2016.
 */
public class ConstantVal {
    public static final String APP_REF_TYPE = "S";
    public static final String IS_QRCODE_CONFIGURE = "isQrConfigure";
    public static final String QRCODE_VALUE = "qrCodeValue";
    public static final String TOKEN = "token";
    public static final String IS_SESSION_EXISTS = "is_session_exists";
    public static final String LAST_ITEM_MASTER_SYNC_TIME = "last_item_master_sync_time";
    public static final String LAST_SERVER_TO_DEVICE_SYNC_TIME = "last_server_to_device_sync_time";
    public static final String LAST_MESSAGE_SYNC_TIME = "last_message_sync_time";
    public static final String COUNT_TO_OPEN_FEEDBACK_ONDASHBOARD = "count_to_open_feedback_ondashboard";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String WELCOME_MESSAGE = "welcome_message";

    public static final int REQUEST_TO_START_CAMERA_ACTIVITY = 100;
    public static final int REQUEST_TO_START_CAMERA_ACTIVITY1 = 101;
    public static final int EXIT_REQUEST_CODE = 1;
    public static final int EXIT_RESPONSE_CODE = 2;
    public static final int INSPECTION_TRANSACTION_REQUEST_CODE = 3;
    public static final int INSPECTION_TRANSACTION_RESPONSE_CODE = 4;
    public static final int SERVICE_TRANSACTION_REQUEST_CODE = 5;
    public static final int SERVICE_TRANSACTION_RESPONSE_CODE = 6;
    public static final int SEARCH_ITEM_BY_BARCODE_REQUEST_CODE = 7;
    public static final int SEARCH_ITEM_BY_BARCODE__RESPONSE_CODE = 8;
    public static final int ZBAR_SCANNER_REQUEST = 9;
    public static final int ZBAR_QR_SCANNER_REQUEST = 10;

    public static class BroadcastAction {
        public static final String CHANGED_MESSAGE_STATUS = "jobio.io.MESSAGE_STATUS";
        public static final String CHANGED_MESSAGE_LIST = "jobio.io.MESSAGE_LIST";
        public static final String SESSION_EXPIRE = "jobio.io.SESSION_EXPIRE";
        public static final String ASSET_LIST = "jobio.io.CHANGED_ASSET_LIST";
        public static final String INSPECT_LIST = "jobio.io.CHANGED_INSPECT_LIST";
        public static final String SERVICE_LIST = "jobio.io.CHANGED_SERVICE_LIST";
        public static final String ITEM_DETAIL = "jobio.io.CHANGED_ITEM_DETAIL";
    }

    public static class SettingFlags {
        public static void clearCache(Context ctx) {
            Helper.clearPreference(ctx, MESSAGE_CONVERSATION_TONE);
            Helper.clearPreference(ctx, MESSAGE_CONVERSATION_NOTIFICATION);
        }

        public static final String MESSAGE_CONVERSATION_TONE = "messaage_conversation_tone";
        public static final String MESSAGE_CONVERSATION_NOTIFICATION = "job_incoming_tone";
    }

    public static class MessageChatStatus {
        public static final int WAITING = 0;
        public static final int SENT = 1;
        public static final int VIEW = 2;
    }

    public static class assetServiceInspectionStatus {
        public static final int NOT_ATTENDED_YET = 1;
        public static final int DONE = 2;
        public static final int POSTPONE = 3;
        public static final int NO_NEED_WORKING_FINE = 4;

        public static String getStatusName(Context ctx, int status) {
            switch (status) {
                case NOT_ATTENDED_YET:
                    return ctx.getString(R.string.strNotAttendedYet);
                case DONE:
                    return ctx.getString(R.string.strDone);
                case POSTPONE:
                    return ctx.getString(R.string.strPostpone);
                case NO_NEED_WORKING_FINE:
                    return ctx.getString(R.string.strNoNeedWorkingFine);
            }
            return "";
        }

        public static ArrayList<ClientAssetInspectServiceStatus> getStatusArr(Context ctx) {
            ArrayList<ClientAssetInspectServiceStatus> arr = new ArrayList<>();
            arr.add(new ClientAssetInspectServiceStatus(1, ctx.getString(R.string.strNotAttendedYet)));
            arr.add(new ClientAssetInspectServiceStatus(2, ctx.getString(R.string.strDone)));
            arr.add(new ClientAssetInspectServiceStatus(3, ctx.getString(R.string.strPostpone)));
            arr.add(new ClientAssetInspectServiceStatus(4, ctx.getString(R.string.strNoNeedWorkingFine)));
            return arr;
        }
    }

    public static class InspectionServiceStatus {
        public static final int NEW = 0;
        public static final int PENDING = 1;
        public static final int DONE = 2;

        public static String getStatusName(Context ctx, int status) {
            switch (status) {
                case NEW:
                    return ctx.getString(R.string.strNew);
                case PENDING:
                    return ctx.getString(R.string.strPending);
                case DONE:
                    return ctx.getString(R.string.strDone);
            }
            return "";
        }
    }

    public static class ServerResponseCode {
        public static final String SESSION_EXISTS = "1";//value receive from server as response
        public static final String NO_INTERNET = "001";
        public static final String PARSE_ERROR = "002";
        public static String SERVER_NOT_RESPONDING = "003";
        public static String REQUEST_TIMEOUT = "004";//30 seconds
        public static String SESSION_EXPIRED = "005";//value receive from server as response
        public static String INVALID_LOGIN = "006";//value receive from server as response
        public static String SERVER_ERROR = "007";
        public static String SUCCESS = "008";
        public static String INVALID_QR_CODE = "009";//value receive from server as response
        public static String CLIENT_ERROR = "010";
        public static String BLANK_RESPONSE = "011";
        public static String NOT_OFFICE_STAFF = "012";

        public static String getMessage(Context ctx, String strCode) {
            try {
                int intCode = Integer.parseInt(strCode);
                if (intCode == Integer.parseInt(NO_INTERNET)) {
                    return ctx.getString(R.string.strInternetNotAvaiable);
                } else if (intCode == Integer.parseInt(PARSE_ERROR)) {
                    return ctx.getString(R.string.strUnableToParseData);
                } else if (intCode == Integer.parseInt(SERVER_NOT_RESPONDING)) {
                    return ctx.getString(R.string.strServerNotResponding);
                } else if (intCode == Integer.parseInt(REQUEST_TIMEOUT)) {
                    return ctx.getString(R.string.strRequestTimeout);
                } else if (intCode == Integer.parseInt(SESSION_EXPIRED)) {
                    return ctx.getString(R.string.strSessionExpire);
                } else if (intCode == Integer.parseInt(INVALID_LOGIN)) {
                    return ctx.getString(R.string.strInvalidUserNameAndPassword);
                } else if (intCode == Integer.parseInt(SERVER_ERROR)) {
                    return ctx.getString(R.string.strServerError);
                } else if (intCode == Integer.parseInt(SUCCESS)) {
//
                } else if (intCode == Integer.parseInt(INVALID_QR_CODE)) {
                    return ctx.getString(R.string.strQRNotExist);
                } else if (intCode == Integer.parseInt(CLIENT_ERROR)) {
                    return ctx.getString(R.string.strClientError);
                } else if (intCode == Integer.parseInt(BLANK_RESPONSE)) {
                    return ctx.getString(R.string.strDatacannotReceive);
                } else if (intCode == Integer.parseInt(NOT_OFFICE_STAFF)) {
                    return ctx.getString(R.string.msgInvalidCreditialsAsNotStaffMember);
                }
                return strCode;
            } catch (NumberFormatException e) {
                return strCode;
            }
        }
    }

    private static String getWebURLPrefix(String QRCode) {
        return "http://" + QRCode + ".jobio.io/mWebApi/v1/";
        //return "http://10.0.2.2:80/Electrasync_API_new/index.php/";//for emulator
        //return "http://10.0.3.2/jobio_mobile_webapi/index.php/";//for genymotion
    }


    public static URLMapping getQRCodeVerificationUrl(Context c, String QRCode) {
        String[] paramName = {"qrcode"};
        String URL = getWebURLPrefix(QRCode) + "verifycredentials/verifyQRCode";
        return new URLMapping(paramName, URL, false);
    }

    public static URLMapping getMessageStatus(Context c) {
        String[] paramNames = {"token_id", "ids", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "managemessage/getMessageStatus";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getEmployeeMessage(Context c) {
        String[] paramNames = {"token_id", "login_admin_user_id", "isMessage_load_first_time", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "managemessage/getEmployeeMessage";
        return new URLMapping(paramNames, URL, false);
    }


    public static URLMapping updateMessageStatus(Context c) {
        String[] paramNames = {"token_id", "notViewedIds", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "managemessage/updateMessageStatus";
        return new URLMapping(paramNames, URL, true);
    }

    public static URLMapping saveMessage(Context c) {
        String[] paramNames = {"token_id", "local_pk", "message", "from_id", "to_id", "is_viewed", "date", "time", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "managemessage/saveMessage";
        return new URLMapping(paramNames, URL, true);
    }

    public static URLMapping loadPhoto(Context c) {
        String[] paramNames = {"token_id", "id", "table_index", "account_id"};//3:employee_master,4:asset photo
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "getcommondata/loadPhoto";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping changePassword(Context c) {////different in AIM
        String[] paramNames = {"token_id", "adminUserId", "user_name", "new_password", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "verifycredentialsAIM/changePassword";
        return new URLMapping(paramNames, URL, true);
    }

    public static URLMapping logoutUser(Context c) {
        String[] paramNames = {"token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "verifycredentials/logoutUser";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getUserDataUrl(Context c) {
        String[] paramName = {"user_name", "password", "qrcode", "token_id", "account_id", "app_type"};
        String QRCode = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(QRCode) + "getuserdata/loaddata";
        return new URLMapping(paramName, URL, false);
    }

    public static URLMapping getWelcomeText(Context c) {
        String[] paramNames = {"token_id", "account_id", "app_ref_type"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "getcommondata/getWelcomeText";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getSaveUserLocationURL(Context c) {
        String[] paramName = {"latitude", "longitude", "gps_type", "reverse_geo_code_name", "admin_user_id", "app_type", "job_id", "token_id", "date", "time", "account_id"};
        String QRCode = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(QRCode) + "userlocation/saveUserLocation";
        return new URLMapping(paramName, URL, true);
    }

    public static URLMapping getLocationTrackingIntervalURL(Context c) {
        String[] paramName = {"token_id", "account_id"};
        String QRCode = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(QRCode) + "userlocation/getLocationTrackingInterval";
        return new URLMapping(paramName, URL, false);
    }

    public static URLMapping getLoginCredentialsUrl(Context c) {//different in AIM
        String[] paramName = {"user_name", "password", "location", "device_version", "os_name", "account_id", "date", "time"};
        String QRCode = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(QRCode) + "verifycredentialsAIM/verifyUserNamePassword";
        return new URLMapping(paramName, URL, false);
    }

    public static URLMapping getClientAdminUserEmployeeList(Context c) {
        String[] paramNames = {"token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "managemessage/getEmployeeList";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping verifyUserName(Context c) {////different in AIM
        String[] paramNames = {"user_name", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "verifycredentialsAIM/verifyUserName";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping forgotPassword(Context c) {
        String[] paramNames = {"account_id", "to_email", "date", "time", "app_ref_type"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "email/forgotPassword";
        return new URLMapping(paramNames, URL, true);
    }


    public static URLMapping fetchOwnAsset(Context c) {
        String[] paramNames = {"account_id", "emp_id", "token_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "assetAIM/fetchOwnAsset";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping fetchAssignedInspection(Context c) {
        String[] paramNames = {"account_id", "emp_id", "token_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "assetAIM/fetchAssignedInspection";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping fetchAssignedService(Context c) {
        String[] paramNames = {"account_id", "emp_id", "token_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "assetAIM/fetchAssignedService";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getAssetServiceInspectionStatus(Context c) {
        String[] paramNames = {"token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "getcommondata/getAssetServiceInspectionStatus";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping updateInspectTransaction(Context c) {
        String[] paramNames = {"token_id", "account_id", "iTransactionID", "name", "date_time", "note", "photo", "is_present", "status_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "assetAIM/updateInspectTransaction";
        return new URLMapping(paramNames, URL, true);
    }

    public static URLMapping updateServiceTransaction(Context c) {
        String[] paramNames = {"token_id", "account_id", "sTransactionID", "name", "perform_by", "service_firm_name", "cost", "date_time", "note", "photo", "invoice", "status_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "assetAIM/updateServiceTransaction";
        return new URLMapping(paramNames, URL, true);
    }

    public static URLMapping getItemDetailByBarcode(Context c) {
        String[] paramNames = {"token_id", "account_id", "barcode"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getItemDetailByBarcode";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getSupplierListByItemId(Context c) {
        String[] paramNames = {"token_id", "account_id", "item_uuid"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getSupplierListByItemId";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getItemTransactionList(Context c) {
        String[] paramNames = {"token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getItemTransactionList";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getStockTransactionStatus(Context c) {
        String[] paramNames = {"token_id", "account_id", "barcode"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "getcommondata/getStockTransactionStatus";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getStockTransactionReason(Context c) {
        String[] paramNames = {"token_id", "account_id", "item_uuid"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "getcommondata/getStockTransactionReason";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getCustomerEmployeeSupplierList(Context c) {
        String[] paramNames = {"token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getCustomerEmployeeSupplierList";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getTransactionalReferenceList(Context c) {
        String[] paramNames = {"status", "type", "token_id", "account_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getTransactionalReferenceList";
        return new URLMapping(paramNames, URL, false);
    }

    public static URLMapping getReferenceDetail(Context c) {
        String[] paramNames = {"refId", "refType", "statusId", "account_id","token_id"};
        String subDomain = Helper.getStringPreference(c, ConstantVal.QRCODE_VALUE, "");
        String URL = getWebURLPrefix(subDomain) + "stockAIM/getReferenceDetail";
        return new URLMapping(paramNames, URL, false);
    }
}
