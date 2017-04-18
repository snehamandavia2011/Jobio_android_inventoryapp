package com.lab360io.jobio.officeApp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.thomsonreuters.rippledecoratorview.RippleDecoratorView;
import com.xwray.fontbinding.FontCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import asyncmanager.asyncAsset;
import asyncmanager.asyncDashboardData;
import asyncmanager.asyncEmployeeList;
import asyncmanager.asyncLoadCommonData;
import asyncmanager.asyncLocationTrackingInterval;
import asyncmanager.asyncMessageList;
import asyncmanager.asyncModuleFlag;
import asyncmanager.asyncUserData;
import entity.BusinessAccountMaster;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientAdminUserAppsRel;
import entity.ClientEmployeeMaster;
import entity.ClientLoginUser;
import entity.MyLocation;
import me.zhanghai.android.materialedittext.MaterialEditText;
import parser.parsQRCodeAndLoginDetail;
import permission.LocationPermission;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.GPSTracker;
import utility.Helper;
import utility.HttpEngine;
import notification.Notification;
import utility.Logger;
import utility.OptionMenu;
import utility.ServerResponse;
import utility.URLMapping;


//https://github.com/snehamandavia1988/ElectraSync2
public class acLogin extends AppCompatActivity {
    RippleDecoratorView rippleNotUserName, rippleForgotPassword;
    MaterialEditText edUserName, edPassword;
    Button btnLogin;
    String QRCode;
    Handler handler = new Handler();
    Context mContext;
    Button btnNotUser, btnForgotPassword;
    AppCompatActivity ac;
    ProgressDialog pd;
    Helper objHelper = new Helper();
    DotProgressBar dotProgressBar;
    TextView txtCopyRight;
    LocationPermission objLocationPermission;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.login);
        dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        ac = this;
        mContext = this;
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        rippleNotUserName = (RippleDecoratorView) findViewById(R.id.rippleNotUserName);
        rippleForgotPassword = (RippleDecoratorView) findViewById(R.id.rippleForgotPassword);
        txtCopyRight = (TextView) findViewById(R.id.txtCopyRight);
        txtCopyRight.setText(txtCopyRight.getText() + " [" + getString(R.string.strVersion) + ":" + BuildConfig.VERSION_NAME + "]");
        objLocationPermission = new LocationPermission(ac);
        if (!objLocationPermission.isHavePermission()) {
            objLocationPermission.askForPermission();
        }
        new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final String strBase64 = Helper.getStringPreference(mContext, BusinessAccountMaster.Fields.ACCOUNT_LOGO, "");
                        final String imageDataBytes = strBase64.substring(strBase64.indexOf(",") + 1);
                        final Bitmap bmp = Helper.convertBase64ImageToBitmap(imageDataBytes);
                        if (bmp != null) {
                            int pixel = Helper.getPixelByPercentageOfScreenHeight(15, mContext);
                            imgLogo.setImageBitmap(Bitmap.createScaledBitmap(bmp, pixel, pixel, false));
                        } else {
                            imgLogo.setImageResource(R.drawable.ic_jobio_logo_white);
                        }
                        objHelper.setActionBar(ac, getString(R.string.strLogin), bmp, false);
                    }
                });
            }
        }.start();

        QRCode = Helper.getStringPreference(getApplicationContext(), ConstantVal.QRCODE_VALUE, "");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edUserName = (MaterialEditText) findViewById(R.id.edUserName);
        edPassword = (MaterialEditText) findViewById(R.id.edPassword);

        //set underline to clickable textview
        btnNotUser = (Button) findViewById(R.id.btnNotUser);
        btnNotUser.setPaintFlags(btnNotUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setPaintFlags(btnNotUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnNotUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear cache and clear edittext and hide NotUserName
                edUserName.setEnabled(true);
                Helper.notUserName(mContext);
                rippleNotUserName.setVisibility(View.GONE);
                edUserName.setText("");
                edPassword.setText("");
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater infalInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final Dialog dialog = new Dialog(mContext);
                View view1 = DataBindingUtil.inflate(infalInflater, R.layout.dlg_forgot_password, null, true).getRoot();
                final LinearLayout lyInput = (LinearLayout) view1.findViewById(R.id.lyInput);
                lyInput.setVisibility(View.VISIBLE);
                final LinearLayout lyConfirmation = (LinearLayout) view1.findViewById(R.id.lyConfirmation);
                ImageButton btnClose = (ImageButton) view1.findViewById(R.id.btnClose);
                Button btnDone = (Button) view1.findViewById(R.id.btnDone);
                Button btnSendNewPassword = (Button) view1.findViewById(R.id.btnSendNewPassword);
                final DotProgressBar dot_progress_bar = (DotProgressBar) view1.findViewById(R.id.dot_progress_bar);
                final MaterialEditText edUserName = (MaterialEditText) view1.findViewById(R.id.edUserName);
                final TextView msgError = (TextView) view1.findViewById(R.id.msgError);
                btnSendNewPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPassoword(dialog, msgError, edUserName, lyInput, lyConfirmation, dot_progress_bar);
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(mContext, acHome.class);
                startActivity(i);
                finish();*/
                if (!objLocationPermission.isHavePermission()) {
                    objLocationPermission.askForPermission();
                    return;
                }
                boolean isDataEntedProperly = true;
                if (Helper.isFieldBlank(edUserName.getText().toString())) {
                    edUserName.setError(getString(R.string.strEnterEmailId));
                    requestFocus(edUserName);
                    isDataEntedProperly = false;
                } else if (!Helper.isValidEmailId(edUserName.getText().toString())) {
                    edUserName.setError(getString(R.string.strEnterValidEmail));
                    requestFocus(edUserName);
                    isDataEntedProperly = false;
                } else if (Helper.isFieldBlank(edPassword.getText().toString())) {
                    edPassword.setError(getString(R.string.strEnterPassword));
                    requestFocus(edPassword);
                    isDataEntedProperly = false;
                }
                if (isDataEntedProperly) {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.darkgrey)));
                    final HttpEngine objHttpEngine = new HttpEngine();
                    double lat = 0, lon = 0;
                    GPSTracker gps = new GPSTracker(mContext, new Handler());
                    MyLocation loc = gps.getLocation();
                    if (loc != null) {
                        lon = loc.getLongitude();
                        lat = loc.getLatitude();
                    }
                    final String deviceName = Build.DEVICE + " " + Build.MODEL;
                    final String deviceVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
                    final String strUserName = edUserName.getText().toString();
                    final String strPassword = edPassword.getText().toString();
                    final String strLocation = lat + "," + lon;
                    final String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    //pd = new ProgressDialog(mContext, ProgressDialog.STYLE_HORIZONTAL);
                    //Logger.debug(strUserName + " " + strPassword + " " + QRCode + " " + strLocation + " " + deviceName + " " + deviceVersion);
                    //pd.setMessage(getString(R.string.strVerifying));
                    //pd.show();
                    dotProgressBar.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            //verify login detail
                            String result = "";
                            try {
                                Date dt = new Date();
                                String date = Helper.convertDateToString(dt, ConstantVal.DATE_FORMAT);
                                String time = Helper.convertDateToString(dt, ConstantVal.TIME_FORMAT);
                                String QRCode = Helper.getStringPreference(mContext, ConstantVal.QRCODE_VALUE, "");
                                URLMapping um = ConstantVal.getLoginCredentialsUrl(mContext);
                                ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                                        new String[]{QRCode, strUserName, strPassword, strLocation, deviceName, deviceVersion, account_id, date, time}, um.getParamNames(), um.isNeedToSync());
                                result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                                //Logger.debug("After login Server Response:" + result);
                                if (result != null && !result.equals("")) {
                                    //result = result.substring(1, result.length() - 1).replace("\\", "");
                                    try {
                                        ClientLoginUser objLoginUser = parsQRCodeAndLoginDetail.parseLogin(result);
                                        objLoginUser.setUserName(strUserName);
                                        objLoginUser.setPassword(strPassword);
                                        //objLoginUser.setQRCode(QRCode);
                                        if (objLoginUser != null && !objLoginUser.getToken().equals("")) {
                                            //save verified data to shared preferences
                                            objLoginUser.saveDatatoPreference(mContext);
                                            try {
                                                new asyncUserData(mContext).join();
                                                new Notification().savePlayerId(mContext).join();
                                                new asyncAsset(mContext).getAllData().join();
                                                new asyncDashboardData(mContext).join();
                                                new asyncLocationTrackingInterval(mContext).join();
                                                new asyncModuleFlag(mContext).join();
                                                new asyncEmployeeList(mContext).join();
                                                new asyncLoadCommonData(mContext).startSync().join();
                                                new asyncMessageList(mContext);
                                                Helper.setLongPreference(mContext, ConstantVal.LAST_SERVER_TO_DEVICE_SYNC_TIME, new Date().getTime());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Logger.writeToCrashlytics(e);
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        btnLogin.setEnabled(true);
                                                        btnLogin.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                                    }
                                                });
                                            }
                                            Logger.debug((asyncUserData.isDataLoadSuccessfully() + " " + Notification.isDataLoadSuccessfully() + " " +
                                                    asyncAsset.isDataLoadSuccessfully() + " " + asyncDashboardData.isDataLoadSuccessfully() + " " +
                                                    asyncLocationTrackingInterval.isDataLoadSuccessfully() + " " + asyncModuleFlag.isDataLoadSuccessfully() + " " +
                                                    asyncEmployeeList.isDataLoadSuccessfully()));
                                            if (asyncUserData.isDataLoadSuccessfully() && Notification.isDataLoadSuccessfully() &&
                                                    asyncAsset.isDataLoadSuccessfully() && asyncDashboardData.isDataLoadSuccessfully() &&
                                                    asyncLocationTrackingInterval.isDataLoadSuccessfully() && asyncModuleFlag.isDataLoadSuccessfully() &&
                                                    asyncEmployeeList.isDataLoadSuccessfully()) {
                                                Helper.startBackgroundService(mContext);
                                                Intent i = new Intent(mContext, acHome.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Helper.displaySnackbar(ac, getString(R.string.strUnableToLoadData), ConstantVal.ToastBGColor.DANGER).setCallback(new TSnackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(TSnackbar snackbar, @DismissEvent int event) {
                                                        super.onDismissed(snackbar, event);
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                btnLogin.setEnabled(true);
                                                                btnLogin.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                                                Helper.logOutUser(mContext, true);
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Logger.debug("in:" + e.getMessage());
                                        Logger.writeToCrashlytics(e);
                                        Helper.displaySnackbar(ac, result, ConstantVal.ToastBGColor.DANGER);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                btnLogin.setEnabled(true);
                                                btnLogin.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                            }
                                        });
                                    }
                                }
                            } catch (Exception e) {
                                Logger.debug("out:" + e.getMessage());
                                e.printStackTrace();
                                Logger.writeToCrashlytics(e);
                                Helper.displaySnackbar(ac, result, ConstantVal.ToastBGColor.DANGER);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnLogin.setEnabled(true);
                                        btnLogin.setBackgroundDrawable(new ColorDrawable(ac.getResources().getColor(R.color.tilt)));
                                    }
                                });
                            } finally {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //pd.dismiss();
                                        dotProgressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }
        });
    }

    private void sendPassoword(final Dialog d, final TextView msgError, final MaterialEditText edUserName, final LinearLayout lyInput, final LinearLayout lyConfirmation, final DotProgressBar dot_progress_bar) {
        new AsyncTask() {
            boolean isDataEnteredProper = true;
            String email, newPassword;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                email = edUserName.getText().toString();
                if (Helper.isFieldBlank(email)) {
                    isDataEnteredProper = false;
                    edUserName.setError(getString(R.string.strEnterEmailId));
                    requestFocus(edUserName);
                } else if (!Helper.isValidEmailId(email)) {
                    isDataEnteredProper = false;
                    edUserName.setError(getString(R.string.strEnterValidEmail));
                    requestFocus(edUserName);
                }
                if (isDataEnteredProper) {
                    dot_progress_bar.setVisibility(View.VISIBLE);
                    dot_progress_bar.bringToFront();
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataEnteredProper) {
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    URLMapping umVerifyUser = ConstantVal.verifyUserName(mContext);
                    URLMapping umForgotPassword = ConstantVal.forgotPassword(mContext);
                    HttpEngine objHttpEngine = new HttpEngine();
                    String[] dataVerifyUser = {email, account_id};
                    final String strVerifyUserCode = objHttpEngine.getDataFromWebAPI(mContext, umVerifyUser.getUrl(), dataVerifyUser, umVerifyUser.getParamNames(), umVerifyUser.isNeedToSync()).getResponseCode();
                    if (strVerifyUserCode.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        Date dt = new Date();
                        String date = Helper.convertDateToString(dt, ConstantVal.DATE_FORMAT);
                        String time = Helper.convertDateToString(dt, ConstantVal.TIME_FORMAT);
                        String[] data = {account_id, email, date, time, ConstantVal.APP_REF_TYPE};
                        final String result = objHttpEngine.getDataFromWebAPI(mContext, umForgotPassword.getUrl(), data, umForgotPassword.getParamNames(), umForgotPassword.isNeedToSync()).getResponseString();
                        if (result != null && result.length() > 0) {
                            try {
                                JSONObject objJSON = new JSONObject(result);
                                newPassword = objJSON.getString("password");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Logger.writeToCrashlytics(e);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        msgError.setVisibility(View.VISIBLE);
                                        msgError.setText(ConstantVal.ServerResponseCode.getMessage(ac, result));
                                    }
                                });
                            }
                        }
                    } else {
                        if (strVerifyUserCode.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgError.setVisibility(View.VISIBLE);
                                    msgError.setText(mContext.getString(R.string.strInvalidUserName));
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgError.setVisibility(View.VISIBLE);
                                    msgError.setText(ConstantVal.ServerResponseCode.getMessage(ac, strVerifyUserCode));
                                }
                            });
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.setVisibility(View.GONE);
                if (newPassword != null && newPassword.length() > 0) {
                    lyConfirmation.setVisibility(View.VISIBLE);
                    lyInput.setVisibility(View.GONE);
                    Helper.setStringPreference(mContext, ClientAdminUser.Fields.PASSWORD, newPassword);
                    Helper.setStringPreference(mContext, ClientAdminUser.Fields.ISPASSWORDSET, "N");
                }
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get user name value from preferences and set to edit text.
        String strUserName = Helper.getStringPreference(mContext, ClientAdminUser.Fields.USER_NAME, "");
        if (!strUserName.equals("")) {
            edUserName.setEnabled(false);
            btnNotUser.setText(getString(R.string.strNot) + " " + Helper.getStringPreference(mContext, ClientEmployeeMaster.Fields.FIRST_NAME, "") + "?");
            edUserName.setText(strUserName);
            rippleNotUserName.setVisibility(View.VISIBLE);
        } else {
            rippleNotUserName.setVisibility(View.GONE);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
        return false;
    }

    OptionMenu objOptionMenu = new OptionMenu();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        objOptionMenu.getCommonMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        objOptionMenu.handleMenuItemClick(this, item);
        return super.onOptionsItemSelected(item);
    }
}

