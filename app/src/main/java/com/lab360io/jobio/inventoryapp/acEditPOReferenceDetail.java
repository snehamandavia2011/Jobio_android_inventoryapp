package com.lab360io.jobio.inventoryapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.xwray.fontbinding.FontCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.Helper;

public class acEditPOReferenceDetail extends AppCompatActivity {
    Helper objHelper = new Helper();
    AppCompatActivity ac;
    Context mContext;
    TextView txtItemName, txtQuantity;
    MaterialEditText edCost, edPrice, edBarcode, edExpiry;
    ImageButton btnScanBarcode;
    Button btnCancel, btnSave;
    Calendar calExpiryDate = Calendar.getInstance();
    Date dtCurrentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;
    String itemName, cost, price, quantity;

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
                    itemName = getIntent().getStringExtra("itemName");
                    cost = getIntent().getStringExtra("cost");
                    price = getIntent().getStringExtra("price");
                    quantity = getIntent().getStringExtra("quantity");
                }
                txtItemName = (TextView) findViewById(R.id.txtItemName);
                txtQuantity = (TextView) findViewById(R.id.txtQuantity);
                edCost = (MaterialEditText) findViewById(R.id.edCost);
                edPrice = (MaterialEditText) findViewById(R.id.edPrice);
                edBarcode = (MaterialEditText) findViewById(R.id.edBarCode);
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
        } else if (requestCode == ConstantVal.ZBAR_QR_SCANNER_REQUEST && resultCode == RESULT_OK) {
            edBarcode.setText(ZBarConstants.SCAN_RESULT);
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

                    break;
            }
        }
    };


}
