package utility;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Date;

import entity.ClientAdminUser;
import io.fabric.sdk.android.services.common.Crash;

public class Logger {
    public static void debug(String msg) {
        Log.d("JobioAIM", msg);
    }

    public static void writeToCrashlytics(String msg) {
        Crashlytics.getInstance().core.logException(new RuntimeException("Device date: " + new Date() + msg));
        //CrashlyticsCore.getInstance().log(Log.ERROR, strEmail, "Device date: " + new Date() + msg);
    }

    public static void writeToCrashlytics(Exception e) {
        Logger.writeToCrashlytics("\n\n\nMessage: " + e.getMessage() + "\n\n\nCause:" + e.getCause() + "\n\n\nStackTrace:" + e.getStackTrace().toString());
    }

    public static void writeToCrashlytics(Error e) {
        Logger.writeToCrashlytics("\n\n\nMessage: " + e.getMessage() + "\n\n\nCause:" + e.getCause() + "\n\n\nStackTrance:" + e.getStackTrace().toString());
    }

    public static boolean sendExceptionToCrashlytics(String strMessage, String strErrorStackTrace) {
        Crashlytics.logException(new RuntimeException("#ERROR_MESSAGE - " + strMessage + " \n \n" + "#ERROR_STACK_TRACE - " + strErrorStackTrace));
        return false;
    }
}
