package com.lab360io.jobio.officeApp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import permission.WriteSDPermission;
import utility.ConstantVal;
import utility.Helper;
import utility.OptionMenu;

public class acSetting extends AppCompatActivity {
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    CheckBox chkInspectionTransactionTone, chkInspectionTransactionNotification, chkServiceTransactionTone, chkServiceTransactionNotification;
    CheckBox chkAddEditInspectionTone, chkAddEditInspectionNotification, chkAddEditServiceTone, chkAddEditServiceNotification;
    CheckBox chkConversationTone, chkMessageNotification;
    Button btnReportIssue, btnSendFeedback;
    TextView strAppVersion;
    LinearLayout lyInspectionTransactionSetting, lyServiceTransactionSetting, lyAddEditInspectionSetting, lyAddEditServiceSetting, lyMessageSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.setting);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, getString(R.string.strSettings), getString(R.string.strSettings));
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyInspectionTransactionSetting = (LinearLayout) findViewById(R.id.lyInspectionTransactionSetting);
                lyServiceTransactionSetting = (LinearLayout) findViewById(R.id.lyServiceTransactionSetting);
                lyAddEditInspectionSetting = (LinearLayout) findViewById(R.id.lyAddEditInspectionSetting);
                lyAddEditServiceSetting = (LinearLayout) findViewById(R.id.lyAddEditServiceSetting);
                lyMessageSetting = (LinearLayout) findViewById(R.id.lyMessageSetting);
                chkConversationTone = (CheckBox) findViewById(R.id.chkConversationTone);
                chkMessageNotification = (CheckBox) findViewById(R.id.chkMessageNotification);
                chkInspectionTransactionTone = (CheckBox) findViewById(R.id.chkInspectionTransactionTone);
                chkInspectionTransactionNotification = (CheckBox) findViewById(R.id.chkInspectionTransactionNotification);
                chkServiceTransactionTone = (CheckBox) findViewById(R.id.chkServiceTransactionTone);
                chkServiceTransactionNotification = (CheckBox) findViewById(R.id.chkServiceTransactionNotification);
                chkAddEditInspectionTone = (CheckBox) findViewById(R.id.chkAddEditInspectionTone);
                chkAddEditInspectionNotification = (CheckBox) findViewById(R.id.chkAddEditInspectionNotification);
                chkAddEditServiceTone = (CheckBox) findViewById(R.id.chkAddEditServiceTone);
                chkAddEditServiceNotification = (CheckBox) findViewById(R.id.chkAddEditServiceNotification);
                btnReportIssue = (Button) findViewById(R.id.btnReportIssue);
                btnSendFeedback = (Button) findViewById(R.id.btnSendFeedback);
                strAppVersion = (TextView) findViewById(R.id.strAppVersion);
                strAppVersion.setText(getString(R.string.strVersion) + ":" + BuildConfig.VERSION_NAME);
                btnReportIssue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteSDPermission objWriteSDPermission = new WriteSDPermission((AppCompatActivity) mContext);
                        if (!objWriteSDPermission.isHavePermission()) {
                            objWriteSDPermission.askForPermission();
                            return;
                        }
                        Intent i = new Intent(mContext, acReportIssue.class);
                        i.putExtra(acReportIssue.REQUEST_TYPE, acReportIssue.ISSUE);
                        startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });

                btnSendFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, acReportIssue.class);
                        i.putExtra(acReportIssue.REQUEST_TYPE, acReportIssue.SUGGESTION);
                        startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                    }
                });
                chkConversationTone.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, true));
                chkMessageNotification.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_NOTIFICATION, true));

                chkInspectionTransactionTone.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_TONE, true));
                chkInspectionTransactionNotification.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_NOTIFICATION, true));

                chkServiceTransactionTone.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_TONE, true));
                chkServiceTransactionNotification.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_NOTIFICATION, true));

                chkAddEditInspectionTone.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_TONE, true));
                chkAddEditInspectionNotification.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_NOTIFICATION, true));

                chkAddEditServiceTone.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_TONE, true));
                chkAddEditServiceNotification.setChecked(Helper.getBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_NOTIFICATION, true));

                chkConversationTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_TONE, isChecked);
                        if (isChecked)
                            chkMessageNotification.setChecked(true);
                    }
                });

                chkMessageNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.MESSAGE_CONVERSATION_NOTIFICATION, isChecked);
                        if (!isChecked)
                            chkConversationTone.setChecked(false);
                    }
                });

                chkServiceTransactionTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_TONE, isChecked);
                        if (isChecked)
                            chkServiceTransactionNotification.setChecked(true);
                    }
                });

                chkServiceTransactionNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.SERVICE_TRANSACTION_NOTIFICATION, isChecked);
                        if (!isChecked)
                            chkServiceTransactionTone.setChecked(false);
                    }
                });

                chkInspectionTransactionTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_TONE, isChecked);
                        if (isChecked)
                            chkInspectionTransactionNotification.setChecked(true);
                    }
                });

                chkInspectionTransactionNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.INSPECTION_TRANSACTION_NOTIFICATION, isChecked);
                        if (!isChecked)
                            chkInspectionTransactionTone.setChecked(false);
                    }
                });

                chkAddEditInspectionTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_TONE, isChecked);
                        if (isChecked)
                            chkAddEditInspectionNotification.setChecked(true);
                    }
                });

                chkAddEditInspectionNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_INSPECTION_NOTIFICATION, isChecked);
                        if (!isChecked)
                            chkAddEditInspectionTone.setChecked(false);
                    }
                });

                chkAddEditServiceTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_TONE, isChecked);
                        if (isChecked)
                            chkAddEditServiceNotification.setChecked(true);
                    }
                });

                chkAddEditServiceNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Helper.setBooleanPreference(mContext, ConstantVal.SettingFlags.ADD_EDIT_SERVICE_NOTIFICATION, isChecked);
                        if (!isChecked)
                            chkAddEditServiceTone.setChecked(false);
                    }
                });

                if (Helper.isModuleAccessAllow(mContext, ConstantVal.ModuleAccess.ASSET) == false) {
                    lyAddEditInspectionSetting.setVisibility(View.GONE);
                    lyAddEditServiceSetting.setVisibility(View.GONE);
                    lyInspectionTransactionSetting.setVisibility(View.GONE);
                    lyServiceTransactionSetting.setVisibility(View.GONE);
                } else {
                    lyAddEditInspectionSetting.setVisibility(View.VISIBLE);
                    lyAddEditServiceSetting.setVisibility(View.VISIBLE);
                    lyInspectionTransactionSetting.setVisibility(View.VISIBLE);
                    lyServiceTransactionSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    OptionMenu objOptionMenu = new OptionMenu();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast(ac);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
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

