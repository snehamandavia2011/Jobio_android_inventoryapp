package com.lab360io.jobio.officeApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.xwray.fontbinding.FontCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientPORefDetail;
import entity.JobPOInvoiceTransactionResult;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.InputFilterMinMax;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acEditPOReferenceDetail extends AppCompatActivity {
    DotProgressBar dot_progress_bar;
    RelativeLayout lyMainContent;
    Helper objHelper = new Helper();
    AppCompatActivity ac;
    Context mContext;
    TextView txtItemName, txtQuantity;
    MaterialEditText edCost, edPrice, edBarcode, edExpiry, edNote;
    ImageButton btnScanBarcode;
    Button btnCancel, btnSave;
    Calendar calExpiryDate = Calendar.getInstance();
    Date dtCurrentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;

    ClientPORefDetail objClientPORefDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.edit_po_reference_detail);
        ac = this;
        mContext = this;
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (getIntent().getExtras() != null) {
                    selStockTransactionStatus = getIntent().getIntExtra("selStockTransactionStatus", 0);
                    selStockTransactionReason = getIntent().getIntExtra("selStockTransactionReason", 0);
                    referenceType = getIntent().getStringExtra("referenceType");
                    refId = getIntent().getStringExtra("refId");
                    fromId = getIntent().getStringExtra("fromId");
                    toId = getIntent().getStringExtra("toId");
                    fromType = getIntent().getStringExtra("fromType");
                    toType = getIntent().getStringExtra("toType");
                    objClientPORefDetail = (ClientPORefDetail) getIntent().getSerializableExtra("objClientPORefDetail");
                    //Logger.debug(selStockTransactionStatus + " " + selStockTransactionReason + " " + referenceType + " " + refId + " " + fromId + " " + toId + " " + fromType + " " + toType + objClientPORefDetail.display());
                }
                lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                InputFilterMinMax filter = new InputFilterMinMax((double) 0, Double.MAX_VALUE);
                txtItemName = (TextView) findViewById(R.id.txtItemName);
                txtItemName.setText(objClientPORefDetail.getImItem_name());
                txtQuantity = (TextView) findViewById(R.id.txtQuantity);
                txtQuantity.setText(objClientPORefDetail.getPodQty());
                edNote = (MaterialEditText) findViewById(R.id.edNote);
                edCost = (MaterialEditText) findViewById(R.id.edCost);
                edCost.setText(objClientPORefDetail.getPodCost());
                edCost.setFilters(new InputFilter[]{filter});
                edPrice = (MaterialEditText) findViewById(R.id.edPrice);
                edPrice.setText(objClientPORefDetail.getPodPrice());
                edPrice.setFilters(new InputFilter[]{filter});
                edBarcode = (MaterialEditText) findViewById(R.id.edBarcode);
                edExpiry = (MaterialEditText) findViewById(R.id.edExpiry);
                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnSave = (Button) findViewById(R.id.btnSave);
                btnScanBarcode = (ImageButton) findViewById(R.id.btnScanBarcode);
                edExpiry.setOnClickListener(onClick);
                btnCancel.setOnClickListener(onClick);
                btnSave.setOnClickListener(onClick);
                btnScanBarcode.setOnClickListener(onClick);
                objHelper.setActionBar(ac, mContext.getString(R.string.strTransaction));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        } else if (requestCode == ConstantVal.ZBAR_SCANNER_REQUEST && resultCode == RESULT_OK) {
            edBarcode.setText(data.getStringExtra(ZBarConstants.SCAN_RESULT));
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edExpiry:
                    final Dialog dp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            view.setMinDate(dtCurrentDate.getTime());
                            calExpiryDate.set(Calendar.YEAR, year);
                            calExpiryDate.set(Calendar.MONTH, monthOfYear);
                            calExpiryDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            edExpiry.setText(dateFormat.format(calExpiryDate.getTime()));
                        }
                    }, calExpiryDate.get(Calendar.YEAR), calExpiryDate.get(Calendar.MONTH), calExpiryDate.get(Calendar.DAY_OF_MONTH));
                    dp.show();
                    break;
                case R.id.btnScanBarcode:
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        Intent intent = new Intent(mContext, ZBarScannerActivity.class);
                        startActivityForResult(intent, ConstantVal.ZBAR_SCANNER_REQUEST);
                    }
                    break;
                case R.id.btnCancel:
                    finish();
                    break;
                case R.id.btnSave:
                    if (new HttpEngine().isNetworkAvailable(mContext))
                        saveData();
                    else {
                        Helper.displaySnackbar(ac, mContext.getString(R.string.strInternetNotAvaiable));
                    }
                    break;
            }
        }
    };

    private void saveData() {
        new AsyncTask() {
            boolean isDataEntered = true;
            ServerResponse sr;
            String strCost, strPrice, strBarcode, strnNote, strExpiry;
            JobPOInvoiceTransactionResult objJobPOInvoiceTransactionResult;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyMainContent.setVisibility(View.GONE);
                dot_progress_bar.setVisibility(View.VISIBLE);
                strCost = edCost.getText().toString();
                strPrice = edPrice.getText().toString();
                strBarcode = edBarcode.getText().toString();
                strnNote = edNote.getText().toString();
                if (!Helper.isFieldBlank(edExpiry.getText().toString())) {
                    strExpiry = Helper.convertDateToString(calExpiryDate.getTime(), ConstantVal.DATE_FORMAT);
                } else {
                    strExpiry = "0000-00-00";
                }
                if (Helper.isFieldBlank(edCost.getText().toString())) {
                    edCost.setError(mContext.getString(R.string.msgEnterCost));
                    requestFocus(edCost);
                    isDataEntered = false;
                }
                if (Helper.isFieldBlank(edPrice.getText().toString())) {
                    edPrice.setError(mContext.getString(R.string.msgEnterPrice));
                    requestFocus(edPrice);
                    isDataEntered = false;
                }
                if (Helper.isFieldBlank(edBarcode.getText().toString())) {
                    edBarcode.setError(mContext.getString(R.string.strEnterBarCode));
                    requestFocus(edBarcode);
                    isDataEntered = false;
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataEntered) {
                    Date dt = new Date();
                    String date = Helper.convertDateToString(dt, ConstantVal.DATE_FORMAT);
                    String time = Helper.convertDateToString(dt, ConstantVal.TIME_FORMAT);
                    final HttpEngine objHttpEngine = new HttpEngine();
                    String admin_user_id = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                    final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    final URLMapping um = ConstantVal.savePOTransaction(mContext);
                    String data[] = {String.valueOf(selStockTransactionStatus), String.valueOf(selStockTransactionReason), referenceType, refId,
                            objClientPORefDetail.getPodItem_id(), fromId, toId, fromType, toType, objClientPORefDetail.getPodQty(),
                            strCost, strPrice, strExpiry, strnNote, strBarcode, account_id, admin_user_id, date, time, tokenId};
                    sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                    String result = sr.getResponseString();
                    objJobPOInvoiceTransactionResult = JobPOInvoiceTransactionResult.parseResult(result);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                lyMainContent.setVisibility(View.VISIBLE);
                dot_progress_bar.setVisibility(View.GONE);
                if (isDataEntered) {
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.NO_INTERNET)) {
                        Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgSyncNoInternet)).setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                ac.setResult(ConstantVal.EDIT_PO_REFERENCE_RESPONSE);
                                finish();
                            }
                        });
                    } else if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        ac.setResult(ConstantVal.EDIT_PO_REFERENCE_RESPONSE);
                        finish();
                    } else if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                        Helper.displaySnackbar(ac, sr.getResponseCode()).setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                finish();
                            }
                        });
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
