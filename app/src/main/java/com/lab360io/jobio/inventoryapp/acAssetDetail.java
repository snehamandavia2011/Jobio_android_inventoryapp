package com.lab360io.jobio.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import entity.ClientAsset;
import entity.ClientAssetOwner;
import entity.ClientFieldMessage;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;
import utility.Helper;
import utility.Logger;

public class acAssetDetail extends AppCompatActivity {
    DotProgressBar dot_progress_bar;
    ScrollView scrlMailContent;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    TextView txtAssetNameCategory, txtDesc, txtModel, txtManufacturer, txtSerialNumber, txtAssetLocation, txtCost, txtCurrentValue, txtPurchaseFrom, txtExpireDate, txtStatus;
    RelativeLayout lyBarcode;
    LinearLayout lyassetOwner;
    String assetId;
    ClientAsset objClientAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.asset_detail);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                dot_progress_bar.setVisibility(View.VISIBLE);
                scrlMailContent = (ScrollView) findViewById(R.id.scrlMailContent);
                txtAssetNameCategory = (TextView) findViewById(R.id.txtAssetNameCategory);
                txtDesc = (TextView) findViewById(R.id.txtDesc);
                txtModel = (TextView) findViewById(R.id.txtModel);
                txtManufacturer = (TextView) findViewById(R.id.txtManufacturer);
                txtSerialNumber = (TextView) findViewById(R.id.txtSerialNumber);
                txtAssetLocation = (TextView) findViewById(R.id.txtAssetLocation);
                txtCost = (TextView) findViewById(R.id.txtCost);
                txtCurrentValue = (TextView) findViewById(R.id.txtCurrentValue);
                txtPurchaseFrom = (TextView) findViewById(R.id.txtPurchaseFrom);
                txtExpireDate = (TextView) findViewById(R.id.txtExpireDate);
                txtStatus = (TextView) findViewById(R.id.txtStatus);
                lyBarcode = (RelativeLayout) findViewById(R.id.lyBarcode);
                lyassetOwner = (LinearLayout) findViewById(R.id.lyassetOwner);
                if (ac.getIntent().getExtras() != null) {
                    assetId = ac.getIntent().getStringExtra("AssetId");
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                objClientAsset = ClientAsset.getDataFromDatabase(mContext, assetId).get(0);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                txtAssetNameCategory.setText(objClientAsset.getAmAsset_name() + "[" + objClientAsset.getActmCategory_name() + "]");
                txtDesc.setText(objClientAsset.getAmDescription());
                txtModel.setText(objClientAsset.getAmModel_name());
                txtManufacturer.setText(objClientAsset.getAmManufacturer_name());
                txtSerialNumber.setText(objClientAsset.getAmSerial_no());
                txtAssetLocation.setText(objClientAsset.getAmAsset_location());
                txtCost.setText(objClientAsset.getAmPurchase_cost());
                txtCurrentValue.setText(objClientAsset.getAmCurrent_value());
                txtPurchaseFrom.setText(objClientAsset.getAmPurchase_from());
                txtExpireDate.setText(Helper.convertDateToString(objClientAsset.getAmDate_expired(), ConstantVal.DATE_FORMAT));
                txtStatus.setText(objClientAsset.getAmAsset_status());
                Helper.setBarcodeToView(mContext, objClientAsset.getAmBarcode_no(), lyBarcode);
                for (ClientAssetOwner obj : objClientAsset.getArrOwner()) {
                    View v = addItemToLayout(obj);
                    lyassetOwner.addView(v);
                }
                dot_progress_bar.setVisibility(View.GONE);
                scrlMailContent.setVisibility(View.VISIBLE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private View addItemToLayout(ClientAssetOwner obj) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = DataBindingUtil.inflate(mInflater, R.layout.asset_owner_item, null, true).getRoot();
        TextView txtEmployeeName = (TextView) v.findViewById(R.id.txtEmployeeName);
        TextView txtStartDate = (TextView) v.findViewById(R.id.txtStartDate);
        TextView txtEndDate = (TextView) v.findViewById(R.id.txtEndDate);
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor curEmp = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "empId='" + obj.getAoEmployee_id() + "'");
        Logger.debug(obj.getAoEmployee_id() + " " + curEmp.getCount());
        if (curEmp != null && curEmp.getCount() > 0) {
            curEmp.moveToFirst();
            txtEmployeeName.setText(curEmp.getString(3) + " " + curEmp.getString(4) + "[" + curEmp.getString(7) + "]");
            txtStartDate.setText(Helper.convertDateToString(Helper.convertStringToDate(obj.getAoStart_date(), ConstantVal.DATE_FORMAT), ConstantVal.DATE_FORMAT));
            txtEndDate.setText(Helper.convertDateToString(Helper.convertStringToDate(obj.getAoEnd_date(), ConstantVal.DATE_FORMAT), ConstantVal.DATE_FORMAT));
        }
        curEmp.close();
        db.close();
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
}
