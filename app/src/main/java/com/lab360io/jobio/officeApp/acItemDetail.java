package com.lab360io.jobio.officeApp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import asyncmanager.asyncLoadCommonData;
import entity.BusinessAccountdbDetail;
import entity.ClientItemMaster;
import entity.ClientItemTransaction;
import parser.parseItemMaster;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acItemDetail extends AppCompatActivity {
    String barcode;
    DotProgressBar dot_progress_bar;
    CircleImageView item_image;
    ScrollView scrlMainContent;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    TextView txtItemNameCategory, txtDesc, txtShortCode, txtUnitOfMeasure, txtPackage, txtMonthyDemand, txtMinQtyForRestock, txtModel, txtManufacturer, txtLocation, txtLastUpdated;
    LinearLayout lyItemTransactionDetail;
    public static LinearLayout lyItemDetail;
    String itemUUId;
    ClientItemMaster objClientItemMaster;
    boolean needToSyncFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.item_detail);
        ac = this;
        mContext = this;
//        objHelper.setActionBar(ac, mContext.getString(R.string.strItemDetail));
        mContext.registerReceiver(objItemDetailBroadcast, new IntentFilter(ConstantVal.BroadcastAction.ITEM_DETAIL));
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                objHelper.setActionBar(ac, mContext.getString(R.string.strItemDetail));
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                dot_progress_bar.setVisibility(View.VISIBLE);
                scrlMainContent = (ScrollView) findViewById(R.id.scrlMainContent);
                txtItemNameCategory = (TextView) findViewById(R.id.txtItemNameCategory);
                txtDesc = (TextView) findViewById(R.id.txtDesc);
                txtShortCode = (TextView) findViewById(R.id.txtShortCode);
                txtUnitOfMeasure = (TextView) findViewById(R.id.txtUnitOfMeasure);
                txtPackage = (TextView) findViewById(R.id.txtPackage);
                txtMonthyDemand = (TextView) findViewById(R.id.txtMonthyDemand);
                txtMinQtyForRestock = (TextView) findViewById(R.id.txtMinQtyForRestock);
                txtModel = (TextView) findViewById(R.id.txtModel);
                txtManufacturer = (TextView) findViewById(R.id.txtManufacturer);
                txtLocation = (TextView) findViewById(R.id.txtLocation);
                txtLastUpdated = (TextView) findViewById(R.id.txtLastUpdated);
                lyItemTransactionDetail = (LinearLayout) findViewById(R.id.lyItemTransactionDetail);
                lyItemDetail = (LinearLayout) findViewById(R.id.lyItemDetail);
                item_image = (CircleImageView) findViewById(R.id.item_image);
                Helper.setViewLayoutParmas(item_image, 30, mContext);
                if (ac.getIntent().getExtras() != null) {
                    itemUUId = ac.getIntent().getStringExtra("itemUUId");
                    barcode = ac.getIntent().getStringExtra("barcode");
                    needToSyncFromServer = ac.getIntent().getBooleanExtra("needToSyncFromServer", false);//if activivity open by frStockitem then flag is true, if open by barcode scanner then flag is false
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                fetchDataFromDatabase();
                if (needToSyncFromServer) {
                    fetchDataFromServer();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fetchDataFromDatabase() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                objClientItemMaster = ClientItemMaster.getDataFromDatabase(mContext, itemUUId, barcode).get(0);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                assignDataToView();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fetchDataFromServer() {
        new AsyncTask() {
            ServerResponse objServerResponse;

            @Override
            protected Object doInBackground(Object[] params) {
                final HttpEngine objHttpEngine = new HttpEngine();
                String strToken = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String accountId = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                URLMapping um = ConstantVal.getSupplierListByItemId(mContext);
                objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                        new String[]{strToken, accountId, itemUUId}, um.getParamNames(), um.isNeedToSync());
                final String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                if (result != null && !result.equals("")) {
                    try {
                        objClientItemMaster = parseItemMaster.parse(result);
                        if (objServerResponse.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS))
                            objClientItemMaster.saveUpdateItemData(mContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.writeToCrashlytics(e);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (objServerResponse.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    fetchDataFromDatabase();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void assignDataToView() {
        if (objClientItemMaster != null) {
            asyncLoadCommonData.setPreExecutionPhotoToImageView(mContext, objClientItemMaster.getPhoto(), item_image, null, R.drawable.ic_nopic);
        }
        txtItemNameCategory.setText(objClientItemMaster.getItem_name() + " [" + objClientItemMaster.getCategory_name() + "]");
        txtDesc.setText(objClientItemMaster.getSpecification().equals("") ? mContext.getString(R.string.msgDescNotAvail) : objClientItemMaster.getSpecification());
        txtShortCode.setText(objClientItemMaster.getShort_code());
        txtUnitOfMeasure.setText(objClientItemMaster.getUom_name());
        txtPackage.setText(objClientItemMaster.getPackage_type_name());
        txtMonthyDemand.setText(objClientItemMaster.getMonthly_demand());
        txtMinQtyForRestock.setText(objClientItemMaster.getMin_qty_for_restock());
        txtModel.setText(objClientItemMaster.getModel());
        txtManufacturer.setText(objClientItemMaster.getManufacturer());
        txtLocation.setText(objClientItemMaster.getLocation_name());
        try {
            txtLastUpdated.setText(getString(R.string.strLastUpdate) + ": " + Helper.convertDateToAbbrevString(Long.parseLong(objClientItemMaster.getLast_update_date_time())));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            txtLastUpdated.setText(getString(R.string.strLastUpdate) + ": " + getString(R.string.strNA));
        }
        lyItemTransactionDetail.removeAllViews();
        if (objClientItemMaster.getArrItemTransaction() != null) {
            for (ClientItemTransaction obj : objClientItemMaster.getArrItemTransaction()) {
                View v = addItemToLayout(obj);
                lyItemTransactionDetail.addView(v);
            }
        }
        //txtBarcode.setText(objClientAsset.getAmBarcode_no());
        dot_progress_bar.setVisibility(View.GONE);
        scrlMainContent.setVisibility(View.VISIBLE);
    }

    private View addItemToLayout(final ClientItemTransaction obj) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = DataBindingUtil.inflate(mInflater, R.layout.item_transaction_item, null, true).getRoot();
        TextView txtSupplierName = (TextView) v.findViewById(R.id.txtSupplierName);
        TextView txtCost = (TextView) v.findViewById(R.id.txtCost);
        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        TextView txtAvailQty = (TextView) v.findViewById(R.id.txtAvailQty);
        TextView txtBarcode = (TextView) v.findViewById(R.id.txtBarcode);
        txtSupplierName.setText(obj.getSpplierName());
        txtCost.setText(": " + obj.getCost());
        txtPrice.setText(": " + obj.getPrice());
        txtAvailQty.setText(": " + obj.getAvailable_qty());
        txtBarcode.setText(": " + obj.getBarcode());
        txtBarcode.setPaintFlags(txtBarcode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.openBarcodeDialog(mContext, obj.getBarcode());
            }
        });
        return v;
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
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(objItemDetailBroadcast);
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

    private BroadcastReceiver objItemDetailBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            itemUUId = intent.getStringExtra("itemUUId");
            fetchDataFromDatabase();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
