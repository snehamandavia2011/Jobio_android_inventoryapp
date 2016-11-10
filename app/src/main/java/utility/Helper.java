package utility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.TypefaceSpan;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acAsset;
import com.lab360io.jobio.officeApp.acHome;
import com.lab360io.jobio.officeApp.acItemDetail;
import com.lab360io.jobio.officeApp.acLogin;
import com.lab360io.jobio.officeApp.acMessageEmployeeList;
import com.lab360io.jobio.officeApp.acSetting;
import com.lab360io.jobio.officeApp.acStock;
import com.lab360io.jobio.officeApp.acSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.BusinessAccountMaster;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientAdminUserAppsRel;
import entity.ClientEmployeeMaster;
import entity.ClientItemMaster;
import entity.ClientStockSelection;
import io.fabric.sdk.android.Fabric;
import parser.parseItemMaster;
import service.serDeviceToServerSync;
import service.serLocationTracker;
import service.serServerToDeviceSync;

/**
 * Created by SAI on 8/5/2016.
 */
public class Helper {
    public static void startFabric(Context mContext) {
        Fabric.with(mContext, new Crashlytics());
        String strEmail = Helper.getStringPreference(mContext, ClientAdminUser.Fields.USER_NAME, "");
        CrashlyticsCore.getInstance().setString("Email Address", strEmail);
        CrashlyticsCore.getInstance().setUserIdentifier(strEmail);
        CrashlyticsCore.getInstance().setUserEmail(strEmail);
        CrashlyticsCore.getInstance().setUserName(strEmail);
    }


    public static String getCurrencySymbol() {
        Locale defaultLocale = Locale.getDefault();
        Currency currency = Currency.getInstance(defaultLocale);
        return currency.getSymbol();
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

    public static float getFlaotPreference(Context context, String pref,
                                           float def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }

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

    public static Bitmap convertBase64ImageToBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        Logger.debug(imgString);
        return imgString;
    }

    public DotProgressBar dtDialog;

    public void getItemDetailByBarcode(final AppCompatActivity ac, final Handler mHandler, final String strCode, final View[] view, final Fragment fr) {
        final Context ctx = ac;
        final HttpEngine objHttpEngine = new HttpEngine();
        dtDialog = (DotProgressBar) ac.findViewById(R.id.dot_progress_bar);
        dtDialog.setVisibility(View.VISIBLE);
        new AsyncTask() {
            boolean needToSendBroadcast = false, isActivityRunning = true;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                for (View v : view) {
                    v.setEnabled(false);
                    v.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.darkgrey)));
                }
                DataBase db = new DataBase(ac.getApplicationContext());
                db.open();
                Cursor cur = db.fetch(DataBase.item_transaction_table, DataBase.item_transaction_int, "barcode='" + strCode + "'");
                if (cur != null && cur.getCount() > 0) {
                    needToSendBroadcast = true;
                    isActivityRunning = false;
                    Intent i = new Intent(ac.getApplicationContext(), acItemDetail.class);
                    i.putExtra("needToSyncFromServer", false);
                    i.putExtra("itemUUId", cur.getString(1));
                    i.putExtra("barcode", strCode);
                    ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    ac.finish();
                }
                cur.close();
                db.close();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String strToken = Helper.getStringPreference(ctx, ConstantVal.TOKEN, "");
                String accountId = Helper.getStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getItemDetailByBarcode(ctx);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                        new String[]{strToken, accountId, strCode}, um.getParamNames(), um.isNeedToSync());
                final String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    try {
                        ClientItemMaster objClientItemMaster = parseItemMaster.parse(result);
                        if (objClientItemMaster != null) {
                            objClientItemMaster.saveUpdateItemData(ctx);
                            if (needToSendBroadcast) {
                                Intent intent = new Intent();
                                intent.putExtra("itemUUId", objClientItemMaster.getUuid());
                                intent.putExtra("barcode", strCode);
                                intent.setAction(ConstantVal.BroadcastAction.ITEM_DETAIL);
                                ac.sendBroadcast(intent);
                            } else {
                                isActivityRunning = false;
                                Intent i = new Intent(ac.getApplicationContext(), acItemDetail.class);
                                i.putExtra("needToSyncFromServer", false);
                                i.putExtra("itemUUId", objClientItemMaster.getUuid());
                                i.putExtra("barcode", strCode);
                                ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                                ac.finish();
                            }
                            //send broadcast to detail activity
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dtDialog.setVisibility(View.GONE);
                                if (result.equals(ConstantVal.ServerResponseCode.BLANK_RESPONSE)) {
                                    displaySnackbar(ac, ac.getString(R.string.msgItemDetailNotAvailAtServer));
                                    if (fr != null && isActivityRunning) {
                                        FragmentTransaction ft = fr.getActivity().getFragmentManager().beginTransaction();
                                        ft.detach(fr).attach(fr).commit();
                                    }
                                } else if (!result.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                                    displaySnackbar(ac, result);
                                    if (fr != null && isActivityRunning) {
                                        FragmentTransaction ft = fr.getActivity().getFragmentManager().beginTransaction();
                                        ft.detach(fr).attach(fr).commit();
                                    }
                                }
                                for (View v : view) {
                                    v.setEnabled(true);
                                    v.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                }
                            }
                        });
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new Thread() {
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });


            }
        }.start();
    }

    public void verifyingQRcode(final AppCompatActivity ac, final Handler mHandler, final String strCode, final View[] view) {
        final Context ctx = ac;
        final HttpEngine objHttpEngine = new HttpEngine();
        dtDialog = (DotProgressBar) ac.findViewById(R.id.dot_progress_bar);
        dtDialog.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (View v : view) {
                            v.setEnabled(false);
                            v.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.darkgrey)));
                        }
                    }
                });
                URLMapping um = ConstantVal.getQRCodeVerificationUrl(ctx, strCode);
                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(ctx, um.getUrl(),
                        new String[]{strCode}, um.getParamNames(), um.isNeedToSync());
                final String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                //Logger.debug("After login Server Response:" + result);
                if (result != null && !result.equals("")) {
                    try {
                        JSONObject objJSON = new JSONObject(result);
                        String photo = objJSON.getString("account_logo");
                        String account_id = objJSON.getString("account_id");
                        Helper.setStringPreference(ctx, ConstantVal.QRCODE_VALUE, strCode);
                        Helper.setBooleanPreference(ctx, ConstantVal.IS_QRCODE_CONFIGURE, true);
                        Helper.setStringPreference(ctx, BusinessAccountMaster.Fields.ACCOUNT_LOGO, photo);
                        Helper.setStringPreference(ctx, BusinessAccountdbDetail.Fields.ACCOUNT_ID, account_id);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(ac.getApplicationContext(), acLogin.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
                                ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                                ac.finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dtDialog.setVisibility(View.GONE);
                                displaySnackbar(ac, result);
                                for (View v : view) {
                                    v.setEnabled(true);
                                    v.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                }
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public static Snackbar displaySnackbar(final AppCompatActivity ac, final String result) {
        final Context ctx = ac;
        if (result.equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
            Snackbar snackbar = Snackbar
                    .make(ac.findViewById(android.R.id.content), ConstantVal.ServerResponseCode.getMessage(ctx, result), Snackbar.LENGTH_LONG);
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    Intent i = new Intent(ac, acLogin.class);
                    ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
                    ac.startActivity(i);
                    ac.finish();
                }
            });
            snackbar.show();
            return snackbar;
        } else {
            Snackbar snackbar = Snackbar
                    .make(ac.findViewById(android.R.id.content), ConstantVal.ServerResponseCode.getMessage(ctx, result), Snackbar.LENGTH_LONG);
            TextView txt = ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text));
            txt.setMaxLines(3);
            txt.setTypeface(Helper.getUbuntuL(ctx));
            snackbar.show();
            return snackbar;
        }
    }

    private AppCompatActivity objAppCompatActivity;
    private BroadcastReceiver objSessionTimeoutBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.debug("brdSessionTimeOut1:" + objAppCompatActivity.getLocalClassName());
            Helper.displaySnackbar(objAppCompatActivity, ConstantVal.ServerResponseCode.SESSION_EXPIRED);
        }
    };

    public void registerSessionTimeoutBroadcast(final AppCompatActivity ac) {
        objAppCompatActivity = ac;
        objAppCompatActivity.registerReceiver(objSessionTimeoutBroadcast, new IntentFilter(ConstantVal.BroadcastAction.SESSION_EXPIRE));
    }

    public void unRegisterSesionTimeOutBroadcast(final AppCompatActivity ac) {
        objAppCompatActivity = ac;
        objAppCompatActivity.unregisterReceiver(objSessionTimeoutBroadcast);
    }

    public static boolean isValidJSON(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            //ex.printStackTrace();
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                //ex1.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean isValidEmailId(String emailID) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailID;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isFieldBlank(String val) {
        if (val == null) {
            return true;
        } else if (val.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isSdPresent() {
        return android.os.Environment.MEDIA_MOUNTED.equals("mounted");
    }


    public static void masterClear(Context ctx) {
        try {
            DataBase db = new DataBase(ctx);
            db.open();
            db.cleanAll();
            db.close();
            BusinessAccountdbDetail.clearCache(ctx);
            BusinessAccountMaster.clearCache(ctx);
            ClientStockSelection.clearCache(ctx);
            ClientAdminUser.clearCache(ctx);
            ClientAdminUserAppsRel.clearCache(ctx);
            ClientEmployeeMaster.clearCache(ctx);
            ConstantVal.SettingFlags.clearCache(ctx);
            Helper.clearPreference(ctx, ConstantVal.TOKEN);
            Helper.clearPreference(ctx, ConstantVal.IS_QRCODE_CONFIGURE);
            Helper.clearPreference(ctx, ConstantVal.QRCODE_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug("Error while wipe:" + e.getMessage());
        }
    }

    public static void logOutUser(Context ctx, boolean isNeedTosendBroadcast) {
        boolean isSessionExists = Helper.getBooleanPreference(ctx, ConstantVal.IS_SESSION_EXISTS, false);
        if (isSessionExists) {
            try {
                DataBase db = new DataBase(ctx);
                db.open();
                db.cleanLogoutTable();
                db.close();
                Helper.clearPreference(ctx, ConstantVal.TOKEN);
                Helper.clearPreference(ctx, ConstantVal.IS_SESSION_EXISTS);
                Helper.clearPreference(ctx, ConstantVal.LAST_SERVER_TO_DEVICE_SYNC_TIME);
                Helper.clearPreference(ctx, ConstantVal.LAST_ITEM_MASTER_SYNC_TIME);
                Helper.clearPreference(ctx, ConstantVal.LAST_MESSAGE_SYNC_TIME);
                Helper.clearPreference(ctx, ConstantVal.WELCOME_MESSAGE);
                serServerToDeviceSync.isServiceRunning = false;
                serLocationTracker.isServiceRunning = false;
                serDeviceToServerSync.isServiceRunning = false;
                Logger.debug("serServerToDeviceSync.isServiceRunning:" + serServerToDeviceSync.isServiceRunning);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.debug("Error while wipe:" + e.getMessage());
            }
            if (isNeedTosendBroadcast) {
                Intent intent = new Intent();
                intent.setAction(ConstantVal.BroadcastAction.SESSION_EXPIRE);
                ctx.sendBroadcast(intent);
            } else {
                AppCompatActivity ac = (AppCompatActivity) ctx;
                Intent i = new Intent(ac, acLogin.class);
                ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
                ac.startActivity(i);
                ac.finish();
            }
        }
    }

    public static void notUserName(Context c) {
        try {
            ClientStockSelection.clearCache(c);
            ClientAdminUser.clearCache(c);
            ClientAdminUserAppsRel.clearCache(c);
            ClientEmployeeMaster.clearCache(c);
            ConstantVal.SettingFlags.clearCache(c);
            DataBase db = new DataBase(c);
            db.open();
            db.cleanNotUserTable();
            db.close();
        } catch (Exception e) {
            Logger.debug("Error while wipe:" + e.getMessage());
        }
    }

    public static String convertDateToString(Date dt, String strFormate) {
        if (dt != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(strFormate);
                return df.format(dt.getTime());
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static Date convertStringToDate(String strDate, String strFormate) {
        if (!strDate.equals("null") || !strDate.equals("")) {
            DateFormat df = new SimpleDateFormat(strFormate);
            try {
                return df.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date(0);
            }
        } else {
            return new Date(0);
        }
    }


    public static void playSound(Context ctx) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(ctx, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertDateToAbbrevString(String strDateTime) {
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat(ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
            Date convertedDate = dateFormat.parse(strDateTime);

            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    0L, DateUtils.FORMAT_ABBREV_ALL);
            return relativeTime.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDateToAbbrevString(Long lngDateTime) {
        try {
            long now = System.currentTimeMillis();
            Date convertedDate = new Date(lngDateTime);

            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    0L, DateUtils.FORMAT_ABBREV_ALL);
            return relativeTime.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Typeface getUbuntuL(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Ubuntu-L.ttf");
    }

    public static Typeface getUbuntuC(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Ubuntu-C.ttf");
    }

    public static Typeface getUbuntuM(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Ubuntu-M.ttf");
    }

    public static void openImageZoomDialog(Context mContext, Bitmap bmp) {
        int width = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        Logger.debug("width:" + width);
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view1 = DataBindingUtil.inflate(infalInflater, R.layout.dlg_zoom_image, null, true).getRoot();
        ImageView img = (ImageView) view1.findViewById(R.id.img);
        img.invalidate();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width - 10);
        img.setLayoutParams(param);
        //img.setImageBitmap(bmp);
        img.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), bmp));
        ImageButton btnClose = (ImageButton) view1.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view1);
        dialog.show();
    }

    public static int getDifferentInMinute(long time1, long time2) {
        long different = 0;
        int minute = 0;
        different = time1 - time2;
        minute = (int) (different / (60 * 1000));
        return minute;
    }

    public static int getPixelByPercentageOfScreenWidth(int percentage, Context mContext) {
        int pixel = (int) (((Display) ((Activity) mContext).getWindowManager()
                .getDefaultDisplay()).getWidth() * (percentage / 100.0f));
        return pixel;
    }

    public static int getPixelByPercentageOfScreenHeight(int percentage, Context mContext) {
        int pixel = (int) (((Display) ((Activity) mContext).getWindowManager()
                .getDefaultDisplay()).getHeight() * (percentage / 100.0f));
        return pixel;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static void setViewLayoutParmas(View v, int percentage, Context mContext) {
        RelativeLayout.LayoutParams sublParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (((Display) ((Activity) mContext).getWindowManager()
                        .getDefaultDisplay()).getHeight() * (percentage / 100.0f)));

        v.setLayoutParams(sublParam);
        v.requestLayout();
    }

    public static void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public static void setRatingBarColor(RatingBar rt, Context mContext) {
        LayerDrawable layerDrawable = null;
        if (rt.getProgressDrawable() instanceof LayerDrawable) {
            layerDrawable = (LayerDrawable) rt.getProgressDrawable();
        } else if (rt.getProgressDrawable() instanceof DrawableWrapper) {
            DrawableWrapper wrapper = (DrawableWrapper) rt.getProgressDrawable();
            if (wrapper.getWrappedDrawable() instanceof LayerDrawable) {
                layerDrawable = (LayerDrawable) wrapper.getWrappedDrawable();
            }
        }

        if (layerDrawable != null) {
            // Filled stars
            Helper.setRatingStarColor(layerDrawable.getDrawable(2), ContextCompat.getColor(mContext, R.color.tilt));
            // Half filled stars
            Helper.setRatingStarColor(layerDrawable.getDrawable(1), ContextCompat.getColor(mContext, R.color.tilt));
            // Empty stars
            Helper.setRatingStarColor(layerDrawable.getDrawable(0), ContextCompat.getColor(mContext, R.color.tilt));
        }
    }

    public static String getTextFromDate(Context ct, Date dt) {
        //returns date in Today, tomorrow,yesterday, 20 March, 2016 formate
        String strText = "";
        Calendar realTime = Calendar.getInstance();
        realTime.setTimeInMillis(dt.getTime());
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        /*Logger.debug("Real time:" + realTime.get(Calendar.DATE) + "-" + realTime.get(Calendar.MONTH) + "-" + realTime.get(Calendar.YEAR));
        Logger.debug("Today:" + today.get(Calendar.DATE) + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.YEAR));
        Logger.debug("Tomorrow:" + tomorrow.get(Calendar.DATE) + "-" + tomorrow.get(Calendar.MONTH) + "-" + tomorrow.get(Calendar.YEAR));
        Logger.debug("Yesterday:" + yesterday.get(Calendar.DAY_OF_MONTH) + "-" + yesterday.get(Calendar.MONTH) + "-" + yesterday.get(Calendar.YEAR));*/
        if (today.get(Calendar.DATE) == realTime.get(Calendar.DATE) && today.get(Calendar.MONTH) == realTime.get(Calendar.MONTH) && today.get(Calendar.YEAR) == realTime.get(Calendar.YEAR)) {
            strText = ct.getString(R.string.strToday);
        } else if (tomorrow.get(Calendar.DATE) == realTime.get(Calendar.DATE) && tomorrow.get(Calendar.MONTH) == realTime.get(Calendar.MONTH) && tomorrow.get(Calendar.YEAR) == realTime.get(Calendar.YEAR)) {
            strText = ct.getString(R.string.strTomorrow);
        } else if (yesterday.get(Calendar.DATE) == realTime.get(Calendar.DATE) && yesterday.get(Calendar.MONTH) == realTime.get(Calendar.MONTH) && yesterday.get(Calendar.YEAR) == realTime.get(Calendar.YEAR)) {
            strText = ct.getString(R.string.strYesterday);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
            strText = df.format(realTime.getTime());
            //strText = realTime.get(Calendar.DATE) + "-" + (realTime.get(Calendar.MONTH) + 1) + "-" + realTime.get(Calendar.YEAR);
        }
        return strText;
    }

    public void setActionBar(final AppCompatActivity ac, final String text) {//Back button navigation
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar;
                actionBar = ac.getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
                SpannableString s = new SpannableString(text);
                s.setSpan(new TypefaceSpan("Ubuntu-M.ttf"), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(s);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setActionBar(final AppCompatActivity ac, final String text, final Bitmap bmp, final boolean isRequireToShowBitmap) {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar;
                actionBar = ac.getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
                View v = DataBindingUtil.inflate(ac.getLayoutInflater(), R.layout.home_action, null, true).getRoot();
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ImageButton btnDropDownMenu = (ImageButton) v.findViewById(R.id.btnDropDownMenu);
                TextView txtName = (TextView) v.findViewById(R.id.txtName);
                try {
                    if (isRequireToShowBitmap) {
                        btnDropDownMenu.setImageBitmap(Bitmap.createScaledBitmap(bmp, (int) ac.getResources().getDimension(R.dimen.actionBarImageSize), (int) ac.getResources().getDimension(R.dimen.actionBarImageSize), false));
                        btnDropDownMenu.setBackgroundColor(ac.getResources().getColor(R.color.transperant));
                        btnDropDownMenu.setVisibility(View.VISIBLE);
                    } else {
                        btnDropDownMenu.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
                txtName.setText(text);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean isTopMenuVisible = false;

    public void setActionBar(final AppCompatActivity ac, final String strOpenText, final String strCloseText) {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar;
                actionBar = ac.getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
                View v = DataBindingUtil.inflate(ac.getLayoutInflater(), R.layout.home_action, null, true).getRoot();
                //View v = ac.getLayoutInflater().inflate(R.layout.home_action, null);
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ImageButton btnDropDownMenu = (ImageButton) v.findViewById(R.id.btnDropDownMenu);
                //ImageButton btnHome = (ImageButton) v.findViewById(R.id.btnHome);
                TextView txtName = (TextView) v.findViewById(R.id.txtName);
                final RelativeLayout rlMenu = (RelativeLayout) ac.findViewById(R.id.rlMenu);
                setMenuBackground(ac, rlMenu);
                //final ImageButton btnCloseMenu = (ImageButton) rlMenu.findViewById(R.id.btnCloseMenu);
                ImageButton btnStock = (ImageButton) rlMenu.findViewById(R.id.btnStock);
                ImageButton btnAsset = (ImageButton) rlMenu.findViewById(R.id.btnAsset);
                ImageButton btnMessage = (ImageButton) rlMenu.findViewById(R.id.btnMessage);
                ImageButton btnSettings = (ImageButton) rlMenu.findViewById(R.id.btnSettings);
                ImageButton btnSync = (ImageButton) rlMenu.findViewById(R.id.btnSync);
                ImageButton btnLogout = (ImageButton) rlMenu.findViewById(R.id.btnLogout);
                if (ac.getClass() != acHome.class) {
                    if (isTopMenuVisible) {//rlMenu.setVisibility(View.VISIBLE);
                        //btnHome.setVisibility(View.VISIBLE);
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    } else {
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        //btnHome.setVisibility(View.GONE);
                    }
                }
                if (isTopMenuVisible) {
                    btnDropDownMenu.setVisibility(View.GONE);
                    txtName.setText(strOpenText);
                } else {
                    btnDropDownMenu.setVisibility(View.VISIBLE);
                    txtName.setText(strCloseText);
                }
                btnDropDownMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.debug("Click on drop down:" + rlMenu.getVisibility());
                        Animation slideDown = AnimationUtils.loadAnimation(ac.getApplicationContext(), R.anim.slide_down);
                        if (rlMenu.getVisibility() == View.GONE) {
                            rlMenu.startAnimation(slideDown);
                            rlMenu.setVisibility(View.VISIBLE);
                            isTopMenuVisible = true;
                            try {
                                Thread.sleep(250);
                                setActionBar(ac, strOpenText, strCloseText);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                });
                rlMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation slideUp = AnimationUtils.loadAnimation(ac.getApplicationContext(), R.anim.slide_up);
                        rlMenu.startAnimation(slideUp);
                        isTopMenuVisible = false;
                        rlMenu.setVisibility(View.GONE);
                        try {
                            Thread.sleep(250);
                            setActionBar(ac, strOpenText, strCloseText);
                        } catch (InterruptedException e) {
                        }
                    }
                });
                btnStock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ac.getClass() != acHome.class)
                            ac.finish();
                        rlMenu.performClick();
                        Intent i = new Intent(ac.getApplicationContext(), acStock.class);
                        ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });
                btnAsset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ac.getClass() != acHome.class)
                            ac.finish();
                        rlMenu.performClick();
                        Intent i = new Intent(ac.getApplicationContext(), acAsset.class);
                        ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });
                btnMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ac.getClass() != acHome.class)
                            ac.finish();
                        rlMenu.performClick();
                        Intent i = new Intent(ac.getApplicationContext(), acMessageEmployeeList.class);
                        ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });

                btnSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ac.getClass() != acHome.class)
                            ac.finish();
                        rlMenu.performClick();
                        Intent i = new Intent(ac.getApplicationContext(), acSetting.class);
                        ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });

                btnSync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ac.getClass() != acHome.class)
                            ac.finish();
                        rlMenu.performClick();
                        Intent i = new Intent(ac.getApplicationContext(), acSync.class);
                        ac.startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });
                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ConfimationSnackbar snackbar = new ConfimationSnackbar(ac);
                        snackbar.showSnackBar(ac.getString(R.string.msgLogoutConfirmation), ac.getString(R.string.strLogout), ac.getString(R.string.strCancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //expire the token of server
                                new AsyncTask() {
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        snackbar.dismissSnackBar();
                                    }

                                    @Override
                                    protected Object doInBackground(Object[] params) {
                                        final String tokenId = Helper.getStringPreference(ac, ConstantVal.TOKEN, "");
                                        final String accountID = Helper.getStringPreference(ac, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                                        URLMapping objURLMapping = ConstantVal.logoutUser(ac);
                                        HttpEngine objHttpEngine = new HttpEngine();
                                        objHttpEngine.getDataFromWebAPI(ac, objURLMapping.getUrl(), new String[]{String.valueOf(tokenId), accountID}, objURLMapping.getParamNames(), objURLMapping.isNeedToSync());
                                        logOutUser(ac, false);
                                        return null;
                                    }
                                }.execute();
                            }
                        }, null);
                    }
                });
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setMenuBackground(final AppCompatActivity ac, RelativeLayout rlMenu) {
        LinearLayout lyStock = (LinearLayout) rlMenu.findViewById(R.id.lyStock);
        TextView txtStock = (TextView) rlMenu.findViewById(R.id.txtStock);
        ImageButton btnStock = (ImageButton) rlMenu.findViewById(R.id.btnStock);
        if (ac.getClass() == acStock.class) {
            btnStock.setEnabled(false);
            setDisableBg(ac, btnStock, lyStock, txtStock, R.drawable.ic_stock_grey);
        } else {
            setBackgroundonTouch(ac, btnStock, lyStock, txtStock, R.drawable.ic_stock_tilt, R.drawable.ic_stock_white);
        }

        LinearLayout lyAssets = (LinearLayout) rlMenu.findViewById(R.id.lyAsset);
        TextView txtAsset = (TextView) rlMenu.findViewById(R.id.txtAsset);
        ImageButton btnAsset = (ImageButton) rlMenu.findViewById(R.id.btnAsset);
        if (ac.getClass() == acAsset.class) {
            btnAsset.setEnabled(false);
            setDisableBg(ac, btnAsset, lyAssets, txtAsset, R.drawable.ic_asset_grey);
        } else {
            setBackgroundonTouch(ac, btnAsset, lyAssets, txtAsset, R.drawable.ic_asset_tilt, R.drawable.ic_asset_white);
        }

        LinearLayout lyMessage = (LinearLayout) rlMenu.findViewById(R.id.lyMessage);
        TextView txtMessage = (TextView) rlMenu.findViewById(R.id.txtMessage);
        ImageButton btnMessage = (ImageButton) rlMenu.findViewById(R.id.btnMessage);
        if (ac.getClass() == acMessageEmployeeList.class) {
            btnMessage.setEnabled(false);
            setDisableBg(ac, btnMessage, lyMessage, txtMessage, R.drawable.ic_message_grey);
        } else {
            setBackgroundonTouch(ac, btnMessage, lyMessage, txtMessage, R.drawable.ic_message_tilt, R.drawable.ic_message_white);
        }

        LinearLayout lySync = (LinearLayout) rlMenu.findViewById(R.id.lySync);
        TextView txtSync = (TextView) rlMenu.findViewById(R.id.txtSync);
        ImageButton btnSync = (ImageButton) rlMenu.findViewById(R.id.btnSync);
        if (ac.getClass() == acSync.class) {
            btnSync.setEnabled(false);
            setDisableBg(ac, btnSync, lySync, txtSync, R.drawable.ic_sync_grey);
        } else {
            setBackgroundonTouch(ac, btnSync, lySync, txtSync, R.drawable.ic_sync_tilt, R.drawable.ic_sync_white);
        }

        LinearLayout lySetting = (LinearLayout) rlMenu.findViewById(R.id.lySetting);
        TextView txtSetting = (TextView) rlMenu.findViewById(R.id.txtSetting);
        ImageButton btnSetting = (ImageButton) rlMenu.findViewById(R.id.btnSettings);
        if (ac.getClass() == acSetting.class) {
            btnSetting.setEnabled(false);
            setDisableBg(ac, btnSetting, lySetting, txtSetting, R.drawable.ic_settings_grey);
        } else {
            setBackgroundonTouch(ac, btnSetting, lySetting, txtSetting, R.drawable.ic_settings_tilt, R.drawable.ic_setting_white);
        }

        LinearLayout lyExit = (LinearLayout) rlMenu.findViewById(R.id.lyLogout);
        TextView txtExit = (TextView) rlMenu.findViewById(R.id.txtLogout);
        ImageButton btnExit = (ImageButton) rlMenu.findViewById(R.id.btnLogout);
        setBackgroundonTouch(ac, btnExit, lyExit, txtExit, R.drawable.ic_logout_tilt, R.drawable.ic_logout_white);
    }

    private void setBackgroundonTouch(final AppCompatActivity ac, final ImageButton btn, final LinearLayout ly, final TextView txt, final int img, final int imgSelected) {
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setActionDownBg(ac, btn, ly, txt, imgSelected);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    setActionUpBg(ac, btn, ly, txt, img);
                }
                return false;
            }
        });
    }

    private void setDisableBg(final AppCompatActivity ac, final ImageButton btn, final LinearLayout ly, final TextView txt, final int imgDisable) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.white)));
        txt.setTextColor(ac.getResources().getColor(R.color.darkgrey));
        btn.setBackgroundColor(ac.getResources()
                .getColor(R.color.white));
        btn.setImageResource(imgDisable);
    }

    private void setActionDownBg(final AppCompatActivity ac, final ImageButton btn, final LinearLayout ly, final TextView txt, final int imgSelected) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.tilt)));
        txt.setTextColor(ac.getResources().getColor(R.color.white));
        btn.setBackgroundColor(ac.getResources()
                .getColor(R.color.tilt));
        btn.setImageResource(imgSelected);
    }


    private void setActionUpBg(final AppCompatActivity ac, final ImageButton btn, final LinearLayout ly, final TextView txt, final int img) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.white)));
        txt.setTextColor(ac.getResources().getColor(R.color.tilt));
        btn.setBackgroundColor(ac.getResources()
                .getColor(R.color.white));
        btn.setImageResource(img);
    }

    public static void scheduleLocationService(Context mContext, int intervalTime) {
        //Convert minute to millisecond
        intervalTime = 1 * 1000 * 60 * intervalTime;
        if (!serLocationTracker.isServiceRunning) {
            AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
            Intent i = new Intent(mContext, serLocationTracker.class);
            PendingIntent pi = PendingIntent.getService(mContext, 1, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, 0, intervalTime, pi);
            serLocationTracker.isServiceRunning = true;
        }
    }

    public static void startBackgroundService(Context mContext) {
        if (!serServerToDeviceSync.isServiceRunning) {
            AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
            Intent i = new Intent(mContext, serServerToDeviceSync.class);
            PendingIntent pi = PendingIntent.getService(mContext, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 5 * 60 * 1000, pi);//poll time 5 minutes
            serServerToDeviceSync.isServiceRunning = true;
        }
        if (!serDeviceToServerSync.isServiceRunning) {
            AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
            Intent i = new Intent(mContext, serDeviceToServerSync.class);
            PendingIntent pi = PendingIntent.getService(mContext, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 3 * 60 * 1000, pi);//poll time 3 minutes
            serDeviceToServerSync.isServiceRunning = true;
        }
    }


    public static void setBarcodeToView(Context mContext, String strBarcode, RelativeLayout ly) {
        zXingBarcodeGenerator obj = new zXingBarcodeGenerator(mContext, strBarcode);
        View bar = obj.getBarcode();
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(getDPFromPixel(mContext, 180), getDPFromPixel(mContext, 90));
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.CENTER_VERTICAL);
        bar.setLayoutParams(param);
        ly.addView(bar);
    }

    public static int getDPFromPixel(Context mContext, int pixel) {
        return (int) (pixel / mContext.getResources().getDisplayMetrics().density);
    }

    public static void openBarcodeDialog(Context mContext, String strBarcode) {
        zXingBarcodeGenerator obj = new zXingBarcodeGenerator(mContext, strBarcode);
        View bar = obj.getBarcode();
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(300, 300);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        bar.setLayoutParams(param);
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view1 = DataBindingUtil.inflate(infalInflater, R.layout.dlg_barcode, null, true).getRoot();
        RelativeLayout ly = (RelativeLayout) view1.findViewById(R.id.lyBarcode);
        ly.addView(bar);
        ImageButton btnClose = (ImageButton) view1.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view1);
        dialog.show();
    }

    public static boolean isValidPassword(String pass) {
        if (pass.length() >= 6) {
            for (char c : pass.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public static boolean isModuleAccessAllow(Context mContext, String moduleName) {
        boolean isAllow = false;
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur = db.fetch(DataBase.module_flag_table, DataBase.module_Flag_int, "moduleName='" + moduleName + "'");
        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                if (cur.getInt(3) == 1) {
                    isAllow = true;
                    return isAllow;
                }
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return isAllow;
    }
}
