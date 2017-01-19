package com.lab360io.jobio.officeApp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import entity.ClientAdminUser;
import me.zhanghai.android.materialedittext.MaterialEditText;
import me.zhanghai.android.materialedittext.MaterialTextInputLayout;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.Logger;
import utility.OptionMenu;

public class acReportIssue extends AppCompatActivity {
    File database = null;
    int requestType;
    public static final String REQUEST_TYPE = "request_type";
    public static final int ISSUE = 1;
    public static final int SUGGESTION = 2;
    Button btnCancel, btnSend;
    TextView txtTitle, txtSubject;
    LinearLayout lyLogReady;
    DotProgressBar dot_progress_bar;
    LinearLayout lyOnlyForIssue;
    MaterialEditText edInquiryDesc;
    MaterialTextInputLayout lyInquiryDesc;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.report_issue);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strSettings));
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnSend = (Button) findViewById(R.id.btnSend);
                txtTitle = (TextView) findViewById(R.id.txtTitle);
                txtSubject = (TextView) findViewById(R.id.txtSubject);
                lyLogReady = (LinearLayout) findViewById(R.id.lyLogReady);
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                lyOnlyForIssue = (LinearLayout) findViewById(R.id.lyOnlyForIssue);
                edInquiryDesc = (MaterialEditText) findViewById(R.id.edInquiryDesc);
                lyInquiryDesc = (MaterialTextInputLayout) findViewById(R.id.lyInquiryDesc);
                if (ac.getIntent().getExtras() != null) {
                    requestType = ac.getIntent().getIntExtra(REQUEST_TYPE, 0);
                }
                String userName = Helper.getStringPreference(mContext, ClientAdminUser.Fields.USER_NAME, "");
                if (requestType == ISSUE) {
                    txtTitle.setText(getString(R.string.strReportIssue));
                    txtSubject.setText("#" + getString(R.string.strIssue) + "-" + userName + "-#V-" + BuildConfig.VERSION_NAME);
                    lyInquiryDesc.setHint(getString(R.string.strIssueDescription));
                    lyOnlyForIssue.setVisibility(View.VISIBLE);
                    dot_progress_bar.setVisibility(View.VISIBLE);
                    lyLogReady.setVisibility(View.GONE);
                } else if (requestType == SUGGESTION) {
                    txtTitle.setText(getString(R.string.strReportSuggestion));
                    txtSubject.setText("#" + getString(R.string.strSuggestion) + "-" + userName + "-#V-" + BuildConfig.VERSION_NAME);
                    lyInquiryDesc.setHint(getString(R.string.strSuggestionDescription));
                }
                btnSend.setOnClickListener(btnClick);
                btnCancel.setOnClickListener(btnClick);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (requestType == ISSUE) {
                    database = dumpDbToSdcard();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.setVisibility(View.GONE);
                ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                if (requestType == ISSUE && database != null) {
                    lyLogReady.setVisibility(View.VISIBLE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSend:
                    new AsyncTask() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            if (Helper.isFieldBlank(edInquiryDesc.getText().toString())) {
                                if (requestType == ISSUE)
                                    edInquiryDesc.setError(getString(R.string.strIssueDescription));
                                else if (requestType == SUGGESTION)
                                    edInquiryDesc.setError(getString(R.string.strSuggestionDescription));
                                requestFocus(edInquiryDesc);
                            } else {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"appdev@360io.co"});
                                i.putExtra(Intent.EXTRA_SUBJECT, txtSubject.getText().toString());
                                i.putExtra(Intent.EXTRA_TEXT, edInquiryDesc.getText().toString());
                                if (database != null) {
                                    i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(database));
                                }
                                i.setType("text/plain");
                                startActivity(Intent.createChooser(i, null));
                                finish();
                            }
                        }

                        @Override
                        protected Object doInBackground(Object[] params) {
                            return null;
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
                case R.id.btnCancel:
                    finish();
                    break;
            }
        }
    };

    private File dumpDbToSdcard() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + BuildConfig.APPLICATION_ID + "//databases//dbJobioAIM";
                String backupDBPath = "dbJobioAIM.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    return backupDB;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
