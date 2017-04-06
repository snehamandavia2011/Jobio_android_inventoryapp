package com.lab360io.jobio.officeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import entity.BusinessAccountMaster;
import notification.Notification;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;

public class acSplash extends Activity {
    ImageView imgLogo;
    TextView txtAccountName;
    Context mContext;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.splash);
        mContext = this;
        Notification.initNotification(getApplicationContext());
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        txtAccountName = (TextView) findViewById(R.id.txtAccountName);
        final boolean isConfigure = Helper.getBooleanPreference(mContext, ConstantVal.IS_QRCODE_CONFIGURE, false);
        Thread t1 = new Thread() {
            public void run() {
                if (!isConfigure) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imgLogo.setVisibility(View.VISIBLE);
                            txtAccountName.setVisibility(View.GONE);
                            imgLogo.setImageResource(R.drawable.ic_jobio_logo_white);
                        }
                    });
                } else {
                    //get the image value from server
                    final String strBase64 = Helper.getStringPreference(mContext, BusinessAccountMaster.Fields.ACCOUNT_LOGO, "");
                    final String imageDataBytes = strBase64.substring(strBase64.indexOf(",") + 1);
                    final String strAccountName = Helper.getStringPreference(mContext, BusinessAccountMaster.Fields.ACCOUNT_NAME, "");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!strBase64.equals("")) {
                                try {
                                    imgLogo.setVisibility(View.VISIBLE);
                                    txtAccountName.setVisibility(View.GONE);
                                    imgLogo.setImageBitmap(Helper.convertBase64ImageToBitmap(imageDataBytes));
                                } catch (Exception e) {
                                    //Logger.debug(e.getMessage());
                                    txtAccountName.setVisibility(View.VISIBLE);
                                    imgLogo.setVisibility(View.GONE);
                                    txtAccountName.setText(strAccountName);
                                }
                            } else {
                                txtAccountName.setVisibility(View.VISIBLE);
                                imgLogo.setVisibility(View.GONE);
                                txtAccountName.setText(strAccountName);
                            }
                        }
                    });
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                if (!isConfigure) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(mContext, acManualQRCode.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    boolean isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
                    Logger.debug("tokenId:" + tokenId + " isSessionExists:" + isSessionExists);
                    if (tokenId.equals("") && !isSessionExists) {//User never logged in
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(mContext, acLogin.class);
                        startActivity(i);
                        finish();
                    } else if (!tokenId.equals("")) {//User logged in atleast one time.
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isSessionExists)
                            Helper.startBackgroundService(mContext);
                        Intent i = new Intent(mContext, acHome.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        };

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}


