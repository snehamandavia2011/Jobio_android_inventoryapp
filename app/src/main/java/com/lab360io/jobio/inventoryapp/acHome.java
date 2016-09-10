package com.lab360io.jobio.inventoryapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import asyncmanager.asyncLoadCommonData;
import entity.BusinessAccountMaster;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientAdminUserAppsRel;
import entity.ClientAdminUserEmployee;
import entity.ClientEmployeeMaster;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.OptionMenu;
import utility.URLMapping;


public class acHome extends AppCompatActivity {
    Context mContext;
    Helper objHelper = new Helper();
    AppCompatActivity ac;
    CircleImageView profile_image;
    TextView txtUserName, txtDesc, inspectDay, inspectCount, inspectStatus, serviceDay, serviceCount, serviceStatus, messageDay, messageCount, messageStatus, txtWelcomeText;
    String[] arrFeedback;
    boolean isFeedbackCountIncrease = false;
    LinearLayout lyInspect, lyMessage, lyService;
    public static LinearLayout lyUserDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.home_main);
        mContext = this;
        ac = this;
        String name = Helper.getStringPreference(mContext, ClientEmployeeMaster.Fields.FIRST_NAME, "");
        if (!name.equals("")) {
            name = getString(R.string.strHiya) + ", " + name;
        }
        objHelper.setActionBar(ac, getString(R.string.strHome),name);
                initilizeData();
    }

    private void initilizeData() {
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        Helper.setViewLayoutParmas(profile_image, 30, mContext);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        inspectDay = (TextView) findViewById(R.id.inspectDay);
        inspectCount = (TextView) findViewById(R.id.inspectCount);
        inspectStatus = (TextView) findViewById(R.id.inspectStatus);
        serviceDay = (TextView) findViewById(R.id.serviceDay);
        serviceCount = (TextView) findViewById(R.id.serviceCount);
        serviceStatus = (TextView) findViewById(R.id.serviceStatus);
        messageDay = (TextView) findViewById(R.id.messageDay);
        messageCount = (TextView) findViewById(R.id.messageCount);
        messageStatus = (TextView) findViewById(R.id.messageStatus);
        txtWelcomeText = (TextView) findViewById(R.id.txtWelcomeText);
        lyService = (LinearLayout) findViewById(R.id.lyService);
        lyInspect = (LinearLayout) findViewById(R.id.lyInspect);
        lyMessage = (LinearLayout) findViewById(R.id.lyMessage);
        lyUserDetail = (LinearLayout) findViewById(R.id.lyUserDetail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTokenExistance();
    }

    private void openFeedback() {
        if (!isFeedbackCountIncrease) {
            new AsyncTask() {
                int count;

                @Override
                protected Object doInBackground(Object[] params) {
                    count = Helper.getIntPreference(mContext, ConstantVal.COUNT_TO_OPEN_FEEDBACK_ONDASHBOARD, 0);
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (count >= 50) {
                        count = 1;
                        openFeedbackDialog();
                    } else {
                        count++;
                    }
                    Helper.setIntPreference(mContext, ConstantVal.COUNT_TO_OPEN_FEEDBACK_ONDASHBOARD, count);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            isFeedbackCountIncrease = true;
        } else {
            Logger.debug("not going to increase counter");
        }
    }


    private void openResetPasswordDialog() {
        String isPasswordReset = Helper.getStringPreference(mContext, ClientAdminUserAppsRel.Fields.IS_PASSWORD_RESETED, "N");
        Logger.debug("in openResetPasswordDialog:" + isPasswordReset);
        if (isPasswordReset.equals("N")) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Dialog dialog = new Dialog(mContext);
            View view1 = DataBindingUtil.inflate(infalInflater, R.layout.dlg_reset_password, null, true).getRoot();
            final MaterialEditText edOldPassword = (MaterialEditText) view1.findViewById(R.id.edOldPassword);
            final MaterialEditText edNewPassword = (MaterialEditText) view1.findViewById(R.id.edNewPassword);
            final MaterialEditText edConfirmPassword = (MaterialEditText) view1.findViewById(R.id.edConfirmPassword);
            final TextView txtMessage = (TextView) view1.findViewById(R.id.txtMessage);
            Button btnSave = (Button) view1.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveResetPassword(dialog, edOldPassword, edNewPassword, edConfirmPassword, txtMessage);
                }
            });
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setContentView(view1);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            dialog.show();
        } else {
            openFeedback();
        }
    }

    private void saveResetPassword(final Dialog dialog, final MaterialEditText edOldPassword, final MaterialEditText edNewPassword, final MaterialEditText edConfirmPassword, final TextView txtMessage) {
        new AsyncTask() {
            boolean isDataVerified = true;
            boolean isAllFieldsEntered = true;
            String userName, oldPassword, newPassword;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (Helper.isFieldBlank(edOldPassword.getText().toString())) {
                    edOldPassword.setError(getString(R.string.strOldPassword));
                    requestFocus(edOldPassword);
                    isAllFieldsEntered = false;
                } else if (Helper.isFieldBlank(edNewPassword.getText().toString())) {
                    edNewPassword.setError(getString(R.string.strNewPassword));
                    requestFocus(edNewPassword);
                    isAllFieldsEntered = false;
                } else if (Helper.isFieldBlank(edConfirmPassword.getText().toString())) {
                    edConfirmPassword.setError(getString(R.string.strConfirmPassword));
                    requestFocus(edConfirmPassword);
                    isAllFieldsEntered = false;
                }
                if (isAllFieldsEntered) {
                    if (!edNewPassword.getText().toString().equals(edConfirmPassword.getText().toString())) {
                        isDataVerified = false;
                        txtMessage.setVisibility(View.VISIBLE);
                        txtMessage.setText(getString(R.string.msgNewandConfirmPassword));
                    } else {
//check whether old passowrd is exists on server or not, if exists then replace the password with new.
                        userName = Helper.getStringPreference(mContext, ClientAdminUser.Fields.USER_NAME, "");
                        oldPassword = Helper.getStringPreference(mContext, ClientAdminUser.Fields.PASSWORD, "");
                        newPassword = edNewPassword.getText().toString();
                        if (!oldPassword.equals(edOldPassword.getText().toString())) {
                            isDataVerified = false;
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText(getString(R.string.msgInvalidOldPassword));
                        } else if (oldPassword.equals(newPassword)) {
                            isDataVerified = false;
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText(getString(R.string.msgNewPasswordShouldDiffentThanOld));
                        } else {
                            dialog.dismiss();
                            openFeedback();
                        }
                    }
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataVerified && isAllFieldsEntered) {
                    Helper.setStringPreference(mContext, ClientAdminUserAppsRel.Fields.IS_PASSWORD_RESETED, "Y");
                    Helper.setStringPreference(mContext, ClientAdminUser.Fields.PASSWORD, newPassword);
                    int tokenId = Helper.getIntPreference(mContext, ConstantVal.TOKEN_ID, 0);
                    String adminUserId = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    URLMapping um = ConstantVal.changePassword(mContext);
                    HttpEngine objHttpEngine = new HttpEngine();
                    String[] data = {String.valueOf(tokenId), String.valueOf(adminUserId), userName, newPassword, account_id};
                    objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }.execute();
    }

    private void openFeedbackDialog() {
        arrFeedback = mContext.getResources().getStringArray(R.array.arrFeedback);
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view1 = DataBindingUtil.inflate(infalInflater, R.layout.dlg_dashboard_feedback, null, true).getRoot();
        ImageButton btnClose = (ImageButton) view1.findViewById(R.id.btnClose);
        ListView lvl = (ListView) view1.findViewById(R.id.lvlFeedback);
        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                        //go to play store
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                    case 3:
                    case 4:
                        dialog.dismiss();
                        Intent i = new Intent(mContext, acReportIssue.class);
                        i.putExtra(acReportIssue.REQUEST_TYPE, acReportIssue.SUGGESTION);
                        startActivity(i);
                        break;
                    case 5:
                        dialog.dismiss();
                        break;
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lvl.setAdapter(new ArrayAdapter<>(mContext, R.layout.spinner_item, arrFeedback));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view1);
        dialog.show();
    }

    private void setDashboardData() {
        new AsyncTask() {
            ClientAdminUserEmployee objClientAdminUserEmployee = null;
            String strWelcome, business_name;
            float cust_feedback;
            String adminUserId;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                strWelcome = Helper.getStringPreference(mContext, ConstantVal.WELCOME_MESSAGE, "");
                adminUserId = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                business_name = Helper.getStringPreference(mContext, BusinessAccountMaster.Fields.ACCOUNT_NAME, "");
                DataBase db = new DataBase(mContext);
                db.open();
                Cursor cur = db.fetch(DataBase.adminuser_employee_table, "auId='" + adminUserId + "'");
                if (cur != null && cur.getCount() > 0) {
                    objClientAdminUserEmployee = new ClientAdminUserEmployee(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                            cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9));
                }
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (strWelcome.equals("")) {
                    txtWelcomeText.setVisibility(View.GONE);
                } else {
                    txtWelcomeText.setText(strWelcome);
                }
                if (objClientAdminUserEmployee != null) {
                    txtUserName.setText(objClientAdminUserEmployee.getFirst_name() + " " + objClientAdminUserEmployee.getLast_name());
                    txtDesc.setText(objClientAdminUserEmployee.getUser_type_name() + " " + mContext.getString(R.string.strAt) + " " + business_name);
                    new asyncLoadCommonData(mContext).loadAdminUserEmployeePhotoById(profile_image, objClientAdminUserEmployee, null, R.drawable.ic_field_engineer);
                }
                DataBase db = new DataBase(mContext);
                db.open();
                serviceCount.setText(String.valueOf(0));
                serviceStatus.setText(mContext.getString(R.string.strService));
                serviceDay.setText(mContext.getString(R.string.strToday));
                int msgCount = db.getCounts(DataBase.field_message_table, "to_id='" + adminUserId + "' and is_viewed='N'");
                messageCount.setText(String.valueOf(msgCount));
                messageStatus.setText(mContext.getString(R.string.strUnread));
                messageDay.setText(mContext.getString(R.string.strToday));
                inspectCount.setText(String.valueOf(0));
                inspectStatus.setText(mContext.getString(R.string.strInspect));
                inspectDay.setText(mContext.getString(R.string.strToday));
                db.close();
                ((ScrollView) findViewById(R.id.scrollView)).setVisibility(View.VISIBLE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private BroadcastReceiver objEmployeeDetailBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String name = intent.getStringExtra("name");
                if (!name.equals("")) {
                    name = getString(R.string.strHiya) + ", " + name;
                }
                objHelper.setActionBar(ac, getString(R.string.strHome), name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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


    private boolean checkTokenExistance() {
        boolean isSessionExists = Helper.getBooleanPreference(mContext, ConstantVal.IS_SESSION_EXISTS, false);
        if (!isSessionExists) {
            Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.SESSION_EXPIRED);
            return false;
        } else {
            setDashboardData();
            openResetPasswordDialog();
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.EXIT_REQUEST_CODE && resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
