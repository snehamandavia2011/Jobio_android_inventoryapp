package utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by SAI on 8/5/2016.
 */
public class Helper {
    public static void startFabric(Context mContext) {
        Fabric.with(mContext, new Crashlytics());
        CrashlyticsCore.getInstance().setString("Email Address", Helper.getStringPreference(mContext, "", ""));
    }

    public static void clearPreference(Context c, String key) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(key);
        e.commit();
    }

    public static void setStringPreference(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getStringPreference(Context context, String pref,
                                             String def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(pref, def);
    }

    public static int getIntPreference(Context context, String pref, int def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                pref, def);
    }

    public static void setIntPreference(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();
    }

    public static float getFloatPreference(Context context, String pref, float def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }

    public static void setFloatPreference(Context c, String pref, float val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putFloat(pref, val);
        e.commit();
    }

    public static boolean getBooleanPreference(Context context, String pref,
                                               boolean def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(pref, def);
    }

    public static void setBooleanPreference(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    /*public static void setFloatPreference(Context c, String pref, float val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putFloat(pref, val);
        e.commit();
    }

    public static float getFlaotPreference(Context context, String pref,
                                           float def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }*/

    public static void setLongPreference(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getLongPreference(Context context, String pref,
                                         long def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
                pref, def);
    }

}
