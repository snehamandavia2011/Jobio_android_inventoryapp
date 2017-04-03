package coaching;

import android.content.Context;

import utility.Helper;

/**
 * Created by SAI on 3/14/2017.
 */
public class CoachingPreference {
    public static final String IS_APP_LOAD_FIRST_TIME="is_app_load_first_time";
    public static final String MANUAL_QR_CODE_SCREEN = "manual_qr_code_screen";
    public static final String MANUAL_QR_CODE_SCAN_QR_CODE_BUTTON = "manual_qr_code_scan_QR_code_button";
    public static final String SCAN_QR_CODE_SCREEN = "scan_QR_Code_screen";
    public static final String SYNC_SCREEN = "sync_screen";
    public static final String HOME_SCREEN = "home_screen";
    public static final String MESSAGE_EMPLOYEE_LIST_SCREEN = "message_emp_list_Screen";
    public static final String SEND_MESSAGE_SCREEN = "send_message_Screen";
    public static final String SETTING_SCREEN = "setting_screen";
    public static final String ASSET_SCREEN = "asset_screen";

    public static void updatePreference(Context ctx, String preferenceName) {
        Helper.setBooleanPreference(ctx, preferenceName, false);
    }

    public static boolean needToShowPrompt(Context ctx, String preferenceName) {
        return Helper.getBooleanPreference(ctx, preferenceName, true);
    }

    public static void clearCache(Context ctx) {
        Helper.clearPreference(ctx, IS_APP_LOAD_FIRST_TIME);
        Helper.clearPreference(ctx, MANUAL_QR_CODE_SCREEN);
        Helper.clearPreference(ctx, MANUAL_QR_CODE_SCAN_QR_CODE_BUTTON);
        Helper.clearPreference(ctx, SCAN_QR_CODE_SCREEN);
        Helper.clearPreference(ctx, SYNC_SCREEN);
        Helper.clearPreference(ctx, HOME_SCREEN);
        Helper.clearPreference(ctx, MESSAGE_EMPLOYEE_LIST_SCREEN);
        Helper.clearPreference(ctx, SEND_MESSAGE_SCREEN);
        Helper.clearPreference(ctx, ASSET_SCREEN);
        Helper.clearPreference(ctx, SETTING_SCREEN);
    }
}
